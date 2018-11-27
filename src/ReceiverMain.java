import Entities.Receiver;


public class ReceiverMain {

public static void main(String []args ){
		
		Receiver receiver=new Receiver();
		int port =6868;
		receiver.receiverController(port);
	}

}
