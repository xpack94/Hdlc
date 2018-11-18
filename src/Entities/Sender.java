package Entities;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import Definitions.Frame;
import Services.FileService;
import Services.FrameService;

public class Sender {

	
	public void messageSenderControler(String filePath,int port,String host){
		// on prépare les données a envoyer 
		List<String> dataToSend=this.prepareSending(filePath);
		// on envoie les données 
		this.send(dataToSend,port,host);
		
	}
	
	
	public List<String> prepareSending(String filePath){
		FileService fileService = new FileService();
		// on li le fichier a envoyér 
		List<String> data=fileService.readFile(filePath);
		
		FrameService frameService=new FrameService();
		// on cree une liste de trames
		List<Frame>frames=frameService.createFrames(data);
		
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
            
            for(int i=0;i<data.size();i++){
            	dOut.writeByte(i);
            	dOut.writeUTF(data.get(i));
            	dOut.flush(); 
            }
            dOut.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
