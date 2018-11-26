package Entities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Services.FrameService;

public class Receiver {

	
	private PrintWriter writer;
	private Socket socket;
	
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
			
			String receivedMsg=reader.readLine();
			while(receivedMsg!=null){
				this.handleInput(receivedMsg);
				receivedMsg=reader.readLine();
			}
			this.socket.close(); // fermer la socket 
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
	}
	
	/**
	 * @param la trame en bits reçu par l'emetteur
	 * 
	 * cette methode s'occupe de verifié la validé 
	 * 
	 * **/
	private void handleInput(String msg){
		FrameService fService=new FrameService();
		msg=msg.substring(8,msg.length()-8);  //enlever les flags de debut et de fin 
		msg=fService.removeBitStuffing(msg); // supprimer le bit stuffing 
		//verifier si le message reçu contient des erreurs
		if(fService.checkErrors(msg)){
			this.sendRej(msg); // demande de retransmission 
		}else{
			// aucune erreur n'est detéctée 
			//on affiche alors le message envoyé 
			this.printSentMessage(msg);
			this.sendAck(msg); // envoi de l'acuser de réception
			
		}	
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
		String receivedMessage= new String(new BigInteger(extractedMessage, 2).toByteArray());
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
		String rejFrame=msg.substring(0,8)+"Rej"+msg.substring(13,msg.length()-16)+msg.substring(msg.length()-16,msg.length());
		this.writer.println(rejFrame);
		
	}
	
	/**
	 * @param la trame reçu 
	 * cette methode envoi la trame contenant le type RR qui correspond a l'acuser de réception
	 * 
	 * **/
	private void sendAck(String msg){
		// creer la trame contenant le type RR 
		String ackFrame=msg.substring(0,8)+"RR"+msg.substring(13,msg.length()-16)+msg.substring(msg.length()-16,msg.length());
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
	

	
	
	public static void main(String []args ){
		
		Receiver receiver=new Receiver();
		int port =6868;
		receiver.receiverController(port);
		
		
	}
	
	
}
