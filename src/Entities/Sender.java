package Entities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import Definitions.Frame;
import Services.FileService;
import Services.FrameService;
import Tests.Test;

public class Sender {

	
	private  PrintWriter writer;
	private BufferedReader reader;
	private FrameService frameService;
	private FileService fileService;
	private int WINDOW_WIDTH=8; // la taille d'une fenetre a envoyer est de 8 trame 
	// mettre a true pour simuler des erreurs 
	private Socket socket;
	private DataOutputStream dOut;
	private boolean FLIPBITS=true;
	private boolean REMOVEBITS=false;
	private List<Frame> frames;
	public Sender(){
		this.frameService=new FrameService();
		this.fileService=new FileService();
	}
	
	/**
	 * le controlleur de cette classe 
	 * @param le lien du fichier a lire et envoyer 
	 * @param le numero du port 
	 * @param le nom de la machine serveur 
	 * 
	 * 
	 * **/
	public void messageSenderControler(String filePath,int port,String host){
		// on prépare les données a envoyer 
		this.frames=this.prepareSending(filePath);
		
		// on fait la conversion des trames en bits et on retourne le resultat
		List<String> dataToSend=frameService.dataToBinary(this.frames);

		// on envoie les données 
		this.send(dataToSend,port,host);
		
	}
	
	/**
	 * prepare tout les donnée a envoyer au receveur 
	 * @param le lien du fichier a lire et envoyer 
	 * 
	 * 
	 * **/
	public List<Frame> prepareSending(String filePath){
		
		// on li le fichier a envoyér 
		List<String> data=this.fileService.readFile(filePath);
		
		// on cree une liste de trames
		List<Frame>frames=this.frameService.createFrames(data);
		
	
		return frames;
	}
	
	
	/**
	 * @param data : les données en bits a envoyer
	 * @param port : le port sur lequel se fait la connexion  
	 * @param host : le nom de la machine avec laquelle on se connecte 
	 * 
	 * 
	 * methode qui permet d'établir un connexion avec une autre machine et d'envoyer 
	 * des données a cette derniere
	 * 
	 * **/
	
	@SuppressWarnings("deprecation")
	public void send(List<String> data,int port ,String host){
		
		try {
			 this.socket =new Socket(host, port);
			 this.dOut = new DataOutputStream(socket.getOutputStream());
			
			 InputStream input = socket.getInputStream();
             this.reader = new BufferedReader(new InputStreamReader(input));
			 
			 OutputStream output = socket.getOutputStream();
	         this.writer = new PrintWriter(output, true);
	         
	         int windowCounter=0;
	         // simulation d'erreur
	         if(this.FLIPBITS){
	        	 //inverser un des bits d'une des trames 
	        	 data=new Test().flipBits(data);
	         }
	         if(this.REMOVEBITS){
	        	 //supprimer une des trames au hasard 
	        	 data=new Test().removeFrame(data);
	         }
	       
	        
            for(int i=0;i<data.size();i++){
            	System.out.println("sending "+data.get(i));
            	writer.println(data.get(i));
            	if((i+1)%this.WINDOW_WIDTH==0 && i!=0){
            		// changement de fenetre 
            		
            		this.readResponses(data,windowCounter,0);
            		windowCounter+=this.WINDOW_WIDTH;
            	}
            	
            	
            }
            if(data.size()%this.WINDOW_WIDTH!=0){
            	this.readResponses(data,windowCounter,0);
            	
            }
           
            System.out.println("fin de la transmission!");
            dOut.close();
            socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	synchronized
	private void resend(List<String> data,int from,int to){
		String reEncodedFrame=this.reEncode(data,from,to);
		data.set(from,reEncodedFrame);
		for(int i=from;i<to;i++){
			writer.println(data.get(i));
		}
		this.readResponses(data, to-this.WINDOW_WIDTH,to-from);
		
	}
	
	private String reEncode(List<String> frames,int index,int num){
		Frame frameToReEncode =this.frames.get(index);
		String reEncodedFrame=frameService.handleOneFrame(frameToReEncode);		
		String f=reEncodedFrame.substring(8,reEncodedFrame.length()-8);
		
		return reEncodedFrame;
	}
	
	/**
	 * @param la liste des trames 
	 * @param le compteur indiquant le numero de la fenetre courante
	 * methode qui gere les réponses envoyés du receveur pour chaque fenetre 
	 * 
	 * 
	 * **/
	private void readResponses(List<String> data,int windowCounter,int stopAt){
		int count;
		boolean didResponseMakeIt=true;
		if(stopAt!=0){
			count=stopAt;
		}else{
			count=this.WINDOW_WIDTH;
		}
		for(int i=0;i<count;i++){
			String response=null;
			try {
				if(this.reader!=null) response =this.reader.readLine();
				if(response!=null)
		    		didResponseMakeIt=this.handleResponse(response,data,windowCounter);
				if(!didResponseMakeIt) break; // une des trames n'est pas arrivé au bout 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // lire la réponse du receveur 
			
		}
		
	}
	
	/**
	 * @param une suite de bits qui correspond a la reponse du receveur
	 * @param l'ensemble des donnée envoyé au receveur  
	 * @param l'indice qui represente le numero de la fenetre sur laquelle on est presentement (une fenetre est de taille 8 trames)
	 * 
	 * la methode qui permet de filtrer la reponse du receveur qui peut etre 
	 * soit de type A dans ce cas c'est un accusé de réception 
	 * soit de type R dans ce cas c'est une demande de retransmission
	 * **/
	private boolean handleResponse(String response,List<String> data,int current){
		String type=this.frameService.fromBinaryToString(response.substring(0,7)); // extraire et convertir le type d'une suite  bits en suite de charactaire
		int frameIndexToResend=Integer.parseInt(response.substring(9,12),2); // extraire le num de la trame erroné 
		
		if(type.toUpperCase().equals("R")){
			// faire une retransmission de la trame erronée
			if(frameIndexToResend+current<=data.size()){
				System.out.println("retransmission de "+data.get(frameIndexToResend+current));
				this.resend(data,frameIndexToResend+current,current+this.WINDOW_WIDTH);
				return false;
			}	
		}else if(type.toUpperCase().equals("A")){
			//message bien reçu
			System.out.println("accusé de réception");
		}
		return true;
	}
	
	
}
