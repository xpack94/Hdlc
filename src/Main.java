import java.util.List;

import Services.FileService;
import Services.FrameService;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FileService fileService = new FileService();
		List<String> data=fileService.readFile("/home/xpack/Desktop/test");

		FrameService frameService=new FrameService();
		
		List<String> toBinary=frameService.dataToBinary(frameService.createFrames(data));
		
		
		for (int i=0;i<toBinary.size();i++){
			System.out.println(toBinary.get(i));
		}
		 
		
	}

}
