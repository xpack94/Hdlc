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

		int port=6868;
		String host ="localhost";
		System.out.println(new FrameService().bitConverter("h")); 
		Sender sender =new Sender();
		sender.messageSenderControler("/home/xpack/Desktop/test",port,host);

		
	}

}
