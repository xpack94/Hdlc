package Services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import Definitions.Frame;

public class FrameService {
	public String POLYNOM="10001000000100001";
	/**
	 * @param les données qui correspondent au lignes du fichier lu en entrée
	 * @return une liste ou chaque element est une classe de type Frame contenant tout les headers necessaire
	 * 
	 * 
	 * 
	 * **/
	public List<Frame> createFrames(List<String>data){
		List<Frame> frames=new ArrayList<Frame>();
		String type="";
		int num=0;
		String crc="";
		for(int i=0;i<data.size();i++){
			type="i";
			num=i%8;
			crc=this.createCrc(""+this.bitConverter(type)+this.bitConverter(""+num)+this.bitConverter(data.get(i)));
			frames.add(new Frame(type,num,data.get(i),crc));
		}
		
		frames.add(new Frame("f", 00, "00", "00")); // creation de la trame de fin 
		
		return frames;
		
	}
	
	
	/**
	 * @param une liste d'element de type Frame 
	 * @return une liste ou chaque element est encodé en binaire
	 * 
	 * methode qui fait la conversion d'une liste de trames en une liste de bits 
	 * 
	 * **/
	public List<String> dataToBinary(List<Frame> data){
		List<String> toBinary=new ArrayList<String>();
		for(Frame frame : data){
			toBinary.add(
					frame.getFLAG()+
					this.bitStuffing(this.bitConverter(frame.getType()))+
					this.bitStuffing(this.bitConverter(""+frame.getNum()))+
					this.bitStuffing(this.bitConverter(frame.getData()))+
					this.bitStuffing(""+frame.getCrc())+
					frame.getFLAG()
					);
		}
		return toBinary;
	}
	
	
	/**
	 * @param data de type string qui represente les données de la trame 
	 * pour laquelle on calcule le crc 
	 * les données correspondent au type + num+information de la trame
	 * 
	 * cette methode calcule le crc checksum et le retourne 
	 * 
	 * **/
	public String createCrc(String data){
		  String code = data;
		  while(code.length() < (data.length() + this.POLYNOM.length() - 1))
		   code = code + "0";
		  code =this.div(code,this.POLYNOM);
		  return code;
	}
	
	/**
	 * @param num1 correspond au donnée representé comme une suite de bits
	 * @param polynom correspond au polynome generateur
	 * @return String qui correspond au resultat de la division
	 * 
	 * cette methode fait la division euclidienne des données sur le polynome générateur
	 * et retourne le résultat
	 * 
	 * **/
	public String div(String num1,String polynom)
	 {
	  int pointer = polynom.length();
	  String result = num1.substring(0, pointer);
	  String remainder = "";
	  for(int i = 0; i < polynom.length(); i++)
	  {
	   if(result.charAt(i) == polynom.charAt(i))
	    remainder += "0";
	   else
	    remainder += "1";
	  }
	  while(pointer < num1.length())
	  {
	   if(remainder.charAt(0) == '0')
	   {
	    remainder = remainder.substring(1, remainder.length());
	    remainder = remainder + String.valueOf(num1.charAt(pointer));
	    pointer++;
	   }
	   result = remainder;
	   remainder = "";
	   for(int i = 0; i < polynom.length(); i++)
	   {
	    if(result.charAt(i) == polynom.charAt(i))
	     remainder += "0";
	    else
	     remainder += "1";
	   }
	  }
	  return remainder.substring(1,remainder.length());
	 }
	
	/**
	 * @param une suite de bits qui correspondent au bits reçu
	 * @return boolean
	 * 
	 * cette methode verifie si on erreur c'est produite lors de l'envoi 
	 * retourne false si aucune erreur est detectée 
	 * et true sinon
	 * 
	 * **/
	public boolean checkErrors(String data){
		try{
			return !(Integer.parseInt(this.div(data, this.POLYNOM))==0);
		}catch(Exception e){
			return true; 
		}
		
	}
	/**
	 * @param une chaine de caractaire 
	 * @return une String qui correspond a une suite de bits 
	 * 
	 * cette methode fait la transformation d'une chaine de charactaire en une 
	 * suite de bits 
	 * 
	 * **/
	public String bitConverter(String data){
		return new BigInteger(data.getBytes()).toString(2);
		
	}
	
	/**
	 * permet de faire la conversion d'une trame de bits en une suite de caractaires
	 * 
	 * @param la trame en bits 
	 * **/
	public String fromBinaryToString(String data){
		return new String(new BigInteger(data, 2).toByteArray());
	}
	
	
	
	/**
	 * @param les donnée pour lequel on fait le bit stuffing
	 * 
	 * @return une string contenant les données en entrée ainsi que des bits rajouté d'ou le nom bit stuffing
	 * **/
	public String bitStuffing(String data){
		 
		int cnt = 0; 
	        String s = ""; 
	        for (int i = 0; i < data.length(); i++) { 
	            char ch = data.charAt(i); 
	            if (ch == '1') { 
	                cnt++; 
	                if (cnt < 5) 
	                    s += ch; 
	                else { 
	                    s = s + ch + '0'; 
	                    cnt = 0; 
	                } 
	            } 
	            else { 
	                s += ch; 
	                cnt = 0; 
	            }
	        }
	        return s;
		
		
	}
	
	/**
	 * @param une suite de bits ayant eu du bit stuffing
	 * @return la stuite de bit sans bit stuffing
	 * 
	 * la methode qui permet d'enlever les bit rajouté lors du bit stuffing
	 * 
	 * **/
	public String removeBitStuffing(String stuffedFrame){
		
		int counter=0;
		String out="";
		 for(int i=0;i<stuffedFrame.length();i++){
             if(stuffedFrame.charAt(i) == '1'){
                 counter++;
                 out = out + stuffedFrame.charAt(i);           
              }else{
                  out = out + stuffedFrame.charAt(i);
                  counter = 0;
          		}
            if(counter == 5){
               if((i+2)!=stuffedFrame.length())
               out = out + stuffedFrame.charAt(i+2);
               else
               out=out + '1';
               i=i+2;
               counter = 1;
                 }
		 }
		 return out;
	}
	
	
	
	 
	
}
