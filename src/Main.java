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
		List<String> data=fileService.readFile("/home/xpack/Desktop/question3-1.ipynb");
		
		
		
		FrameService frameService=new FrameService();
		frameService.createFrames(data);
	}

}
