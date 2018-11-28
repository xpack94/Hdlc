package Tests;

import java.util.List;
import java.util.Random;

import Definitions.Frame;

public class Test {

		
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
	 * efface une trame au hasard 
	 * @param la liste des trames 
	 * @return la meme liste de trame mais avec une trame effacé 
	 * 
	 * 
	 * **/
	public static List<String> removeFrame(List<String> data){
		List<String> frames = data;
		int randomFrameIndex=new Random().nextInt(data.size()-1); // generer un nombre au hasard 
		frames.remove(randomFrameIndex);
		return frames;
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
