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

public class Sender {

	
	private  PrintWriter writer;
	private BufferedReader reader;
	private FrameService frameService;
	private FileService fileService;
	
	public Sender(){
		this.frameService=new FrameService();
		this.fileService=new FileService();
	}
	
	public void messageSenderControler(String filePath,int port,String host){
		// on prépare les données a envoyer 
		List<String> dataToSend=this.prepareSending(filePath);
		// on envoie les données 
		this.send(dataToSend,port,host);
		
	}
	
	
	public List<String> prepareSending(String filePath){
		
		// on li le fichier a envoyér 
		List<String> data=this.fileService.readFile(filePath);
		
		// on cree une liste de trames
		List<Frame>frames=this.frameService.createFrames(data);
		
		// on fait la conversion des trames en bits et on retourne le resultat
		return frameService.dataToBinary(frames);
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
			 Socket socket =new Socket(host, port);
			 DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			
			 InputStream input = socket.getInputStream();
             this.reader = new BufferedReader(new InputStreamReader(input));
			 
			 OutputStream output = socket.getOutputStream();
	         this.writer = new PrintWriter(output, true);
	        
	         
            for(int i=0;i<data.size();i++){
            	System.out.println("sending "+data.get(i));
            	writer.println(data.get(i));
            	String response=reader.readLine(); // lire la réponse du receveur 
            	this.handleResponse(response,data,i);
            }
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
	private void handleResponse(String response,List<String> data,int index){
		if(response.contains("Rej")){
			// faire une retransmission de la trame erronéé
			this.writer.println(data.get(index));
			
		}else if(response.contains("RR")){
			//message bien reçu
			System.out.println("accusé de réception");
		}
	}
	
	
}
