import Entities.Receiver;


public class ReceiverMain {

public static void main(String []args ){
		
		
		int port =args.length>0 && args[0]!=null?Integer.parseInt(args[0]):6868; 
	
		Receiver receiver=new Receiver();
		
		receiver.receiverController(port);
	}

}
