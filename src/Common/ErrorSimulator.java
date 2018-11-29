package Common;

import java.util.List;
import java.util.Random;

import Definitions.Frame;

public class ErrorSimulator {

		
	/**
	 * la methode qui flip un bit dans une trame au hasard 
	 *
	 *@param la liste des trames
	 * 
	 * 
	 * **/
	public List<String> flipBits(List<String>data ){
		
		int randomFrameIndex=new Random().nextInt(data.size()-1); // generer un nombre au hasard 
		int upperBound=data.get(randomFrameIndex).length()-16;
		int lowerBound=8;
		int randomBitToFlip=new Random().nextInt(upperBound-lowerBound)+lowerBound;
		data.set(randomFrameIndex,flipBit(randomBitToFlip, data.get(randomFrameIndex)));
		return data;
		
	}
	
	
	/**
	 * efface un bit d'une trame au hasard au hasard 
	 * @param la liste des trames 
	 * @return la meme liste de trame mais avec une trame effacé 
	 * 
	 * 
	 * **/
	public static List<String> removeFrame(List<String> data){
	
		int randomFrameIndex=new Random().nextInt(data.size()-1); // generer un nombre au hasard 
		String chosenString=data.get(randomFrameIndex);
		String extractedData=chosenString.substring(13,chosenString.length()); 
		int upperBound=extractedData.length()-16;
		int lowerBound=8;
		int randomBitToRemove=new Random().nextInt(upperBound-lowerBound)+lowerBound;
		String afterRemove=extractedData.substring(0,randomBitToRemove)+
				extractedData.substring(randomBitToRemove+1,extractedData.length());
		
		String dataToPut=chosenString.substring(0,13)+afterRemove; // la nouvelle trame 
		data.set(randomFrameIndex,dataToPut);
		return data;
	}
	
	
	/**
	 * la methode qui supprime un bit d'une trame au hasard
	 * 
	 * @param la liste des trame
	 * @return la meme liste de trame avec un bit supprimé d'une de ses trames
	 * 
	 * 
	 * **/
	public List<Frame> removeBits(List<Frame> data){
		
		
		
		return data;
		
	}
	/**
	 * methode qui permet de changer un bit de 0 a 1 et de 1 a 0 
	 * 
	 * @param l'index du bit a changer 
	 * @param les données pour lesquels on change le bit
	 * @return les nouveau donnée avec le bit changé 
	 * 
	 * **/
	public String flipBit(int bitToFlip,String data){
		
		return data.substring(0,bitToFlip)+(data.charAt(bitToFlip)=='0'?'1':'0')+data.substring(bitToFlip+1,data.length());
	}
	
	

}
