package Services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import Definitions.Frame;

public class FrameService {

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
		long crc=0;
		for(int i=0;i<data.size();i++){
			type="i";
			num=i%8;
			crc=this.createCrc(""+type+num+data.get(i));
			frames.add(new Frame(type,num,data.get(i),crc));
		}
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
					this.bitStuffing(this.bitConverter(""+frame.getCrc()))+
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
	public long createCrc(String data){
		String poly="10001000000100001";
		String polyToHexa=Integer.toHexString(Integer.parseInt(poly,2)); 
		int WIDTH = (8 * 2);
		int TOPBIT = (1 << (WIDTH - 1));
		int POLYNOMIAL = Integer.decode("0x"+polyToHexa);

        final byte message[] = data.getBytes();
        int nBytes = message.length;
        if(nBytes<1) return 0;
        long rem = 0; 
        int b;
        for(b=0;b<nBytes;++b)
        {
            rem ^= (message[b] << (WIDTH - 8));
            byte bit;
            for(bit=8;bit>0;--bit)
            {
                if ((rem & TOPBIT)>0)
                {
                    rem = (rem<< 1) ^ POLYNOMIAL;
                }
                else
                {
                    rem = (rem << 1);
                }
            }
        }

        return (rem);
		}
	
	public String bitConverter(String data){
		return new BigInteger(data.getBytes()).toString(2);
		
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
	
}
