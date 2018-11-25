import java.math.BigInteger;
import java.util.List;

import Entities.Sender;
import Services.FileService;
import Services.FrameService;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*FileService fileService = new FileService();
		List<String> data=fileService.readFile("/home/xpack/Desktop/test");

		FrameService frameService=new FrameService();
		
		List<String> toBinary=frameService.dataToBinary(frameService.createFrames(data));
		*/
		int port=6868;
		String host ="localhost";
		System.out.println(new FrameService().bitConverter("h")); 
		Sender sender =new Sender();
		sender.messageSenderControler("/home/xpack/Desktop/test",port,host);
		
		
		
	  
	  
	 // System.out.println(f.checkErrors(data));
		
	}

}
