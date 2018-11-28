import java.math.BigInteger;
import java.util.List;

import Entities.Sender;
import Services.FileService;
import Services.FrameService;


public class SenderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			int port=args.length>1 && args[1]!=null?Integer.parseInt(args[1]):6868; // a changé pour un autre port si voulue 
			String host =args.length>0 && args[0]!=null?args[0]:"localhost";  // a changer pour un autre domaine si voulue
			String fileName=args.length>2 && args[2]!=null?args[2]:"/home/xpack/Desktop/test"; // a changer pour votre fichier
			int useBackn=args.length>3 && args[3]!=null?Integer.parseInt(args[3]):0;
			Sender sender =new Sender();
			sender.messageSenderControler(fileName,port,host);
		


		
	}

}
