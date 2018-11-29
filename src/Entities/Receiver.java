package Entities;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Services.FrameService;

public class Receiver {

	
	private PrintWriter writer;
	private Socket socket;
	private FrameService frameService;
	private int WINDOW_WIDTH=8; // la taille d'une fenetre a envoyer est de 8 trame
	
	public Receiver(){
		this.frameService=new FrameService();
	}
	
	/**
	 * le conrolleur de la classe 
	 * @param le numero du port 
	 * 
	 * **/
	public void receiverController(int port){
		this.createServer(port);
		this.receive();
	}
	
	/**
	 * methode qui permet de recevoir les trames envoyé par l'emetteur
	 * et de faire les verification necessaire sur ces dernieres
	 * 
	 * **/
	public void receive(){
		
		InputStream input;
		try {
			input = this.socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			List<String> receivedFrames=new ArrayList<String>();
			int counter=0;
			String receivedMsg="";
			do{
				receivedMsg=reader.readLine();
				counter++;
				if(counter%this.WINDOW_WIDTH==0){
					receivedFrames.add(receivedMsg); // ajout de la derniere trame du message courant
					this.handleReceivedWindowMessages(receivedFrames); // traité toutes les trames de la fenetre currente
					receivedFrames.clear(); // vider la liste pour les prochaine fenetres 
					counter=0;
				}else{
					// sauvgarder les trames reçu dans la liste 
					receivedFrames.add(receivedMsg);
				}
			}while(!isEnd(receivedMsg));
			
			if(receivedFrames.size()>0) this.handleReceivedWindowMessages(receivedFrames); 

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
		
	}
	
	/**
	 * @param le nombre de trames a renvoyer 
	 * 
	 * cette méthode écoute les retransmissions de l'emetteur de la trame mal reçu 
	 * ainsi que les trames de la meme fenetre qui suivent  
	 * 
	 * **/
	synchronized
	private void listenForRetransmission(int numberOfResentFrames){
		InputStream input;
		try {
			input = this.socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			List<String> receivedFrames=new ArrayList<String>();
			String receivedMsg="";
			int counter=0;
			while(counter<numberOfResentFrames){
				receivedMsg=reader.readLine(); // lire la trame 
				receivedFrames.add(receivedMsg);
				counter++;
			}
			this.handleReceivedWindowMessages(receivedFrames); // gerer les trames de retransmission
		
		
		
		
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * methode qui s'occupe gerer tout les trames reçu 
	 * 
	 * @param la liste des trames reçu 
	 * **/
	private void handleReceivedWindowMessages(List<String> receivedMessages){
		int index=0;
		int length=receivedMessages.size();
		for(String receivedMessage:receivedMessages){ //boucler sur tout les messages reçu de la fenetre courrante 
			if(!isEnd(receivedMessage)){
				index++;
				// vérification de la trame reçu 
				if(!this.handleInput(receivedMessage)){
					// la trame n'est pas reçu , demande de retransmission
					this.listenForRetransmission(length-index+1);
					break; // sortir de la boucle car une trame n'est pas bien reçu 
				}
				
			}
		}
	}
	/**
	 * 
	 * @param la trame reçu 
	 * @return un boolean qui est a true si c'est la fin de la transmission et false sinon
	 * verifie si c'est la fin de la transmission 
	 * 
	 * **/
	private boolean isEnd(String msg){
		if(msg!=null){
			String type = msg.substring(8,15); //extraire le type de la trame 
			
			if(this.frameService.fromBinaryToString(type).equals("f")){
				//fin de la transmission
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param la trame en bits reçu par l'emetteur
	 * @return un boolean qui est a true si la trame est bien reçu et false sinon 
	 * cette methode s'occupe de verifié la validé 
	 * 
	 * **/
	private boolean handleInput(String msg){
		
		msg=msg.substring(8,msg.length()-8);  //enlever les flags de debut et de fin 
		msg=this.frameService.removeBitStuffing(msg); // supprimer le bit stuffing 
		//verifier si le message reçu contient des erreurs
		if(this.frameService.checkErrors(msg)){
			this.sendRej(msg); // demande de retransmission 
			return false; // la trame n'est pas reçu 
		}else{
			// aucune erreur n'est detéctée 
			//on affiche alors le message envoyé 
			this.printSentMessage(msg);
			this.sendAck(msg); // envoi de l'acuser de réception
			
		}	
		return true;
	}
	/**
	 * @param le message reçu sous forme de bits 
	 * 
	 * la methode qui fait la convertion en string du message reçu et le print
	 * 
	 * **/
	private void printSentMessage(String msg){
		//enlever les bits du type ainsi que le num et le crc
		String extractedMessage=msg.substring(13,msg.length()-16);
		//convertion de bits en string
		String receivedMessage=this.frameService.fromBinaryToString(extractedMessage);
		System.out.println("reçu "+receivedMessage);
		
	}
	
	/**
	 * @param la trame reçu
	 * 
	 * cette methode envoie la trame contenant le code d'erreur Rej a l'emmeteur 
	 * pour que ce dernier renvoi la trame erronée
	 * 
	 * **/
	private void sendRej(String msg){
	
		// creer la trame contenant le type Rej
		String rejFrame=this.frameService.bitConverter("R")+msg.substring(8,msg.length());
		this.writer.println(rejFrame);
		
	}
	
	/**
	 * @param la trame reçu 
	 * cette methode envoi la trame contenant le type RR qui correspond a l'acuser de réception
	 * 
	 * **/
	private void sendAck(String msg){
		// creer la trame contenant le type RR 
		String ackFrame=this.frameService.bitConverter("A")+msg.substring(8,msg.length());
		this.writer.println(ackFrame);
	}
	
	
	/**
	 * @param le port sur lequel le serveur doit écouté 
	 * 
	 * methode qui crée un serveur écoutant sur un port donnée 
	 * 
	 * **/
	public void createServer(int port){
		
		try{
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("le serveur écoute sur le port " + port);
			  
			this.socket = serverSocket.accept();
            OutputStream output = socket.getOutputStream();
            this.writer = new PrintWriter(output, true);
            
			
		}catch (UnknownHostException ex) {
			 
            System.out.println("serveur n'est pas trouvé  " + ex.getMessage());
 
        }catch(IOException e){
			System.out.println("erreur "+e);
			e.printStackTrace();
		}
	}
	
	
	
}
