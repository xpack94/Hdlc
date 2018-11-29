import Definitions.Frame;
import Entities.Sender;
import Services.FrameService;


public class SenderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			int port=args.length>1 && args[1]!=null?Integer.parseInt(args[1]):6868; // a changé pour un autre port si voulue 
			String host =args.length>0 && args[0]!=null?args[0]:"localhost";  // a changer pour un autre domaine si voulue
			String fileName=args.length>2 && args[2]!=null?args[2]:System.getProperty("user.dir")+"/fileToread"; // a changer pour votre fichier				
			int useBackn=args.length>3 && args[3]!=null?Integer.parseInt(args[3]):0;
			int simulateErros=args.length>4 && args[4]!=null?Integer.parseInt(args[4]):0 ; // simulation d'erreur 0= faux 1=flip un bit , 2= suppression d'un bit
			
			Sender sender =new Sender();

			if(simulateErros==1){
				// interchangement d'un des bits d'une trame au hasard 
				sender.setFLIPBITS(true);
			}else if(simulateErros==2){
				// suppression d'un des bits d'une trame au hasard 
				sender.setREMOVEBITS(true);
			}else if(simulateErros==3){
				//simulation des deux a la fois 
				// suppression d'un bit et interchangement d'un autres
				sender.setFLIPBITS(true);
				sender.setREMOVEBITS(true);
			}
			
			
			sender.messageSenderControler(fileName,port,host);
		
		
	}

}
