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
			String fileName=args.length>2 && args[2]!=null?args[2]:"/home/xpack/Desktop/test"; // a changer pour votre fichier
			int useBackn=args.length>3 && args[3]!=null?Integer.parseInt(args[3]):0;
			Sender sender =new Sender();
			sender.messageSenderControler(fileName,port,host);
			
		
		
		/*FrameService fService=new FrameService();
		String w="class ";
		String type="i";
		String num="6";
		String bitType=fService.bitConverter(type);
		String bitNum=fService.bitConverter(num);
		String bitW=fService.bitConverter(w);
		String crc =fService.createCrc(bitType+bitNum+bitW);
		String conv=bitType+bitNum+bitW+crc;
		System.out.println("before stuffing "+fService.checkErrors(conv));
		String stuffed=fService.bitStuffing(conv);
		String unStuffed=fService.removeBitStuffing(stuffed);
		System.out.println(stuffed);
		
		System.out.println("after un-stuffing "+fService.removeBitStuffing(stuffed));
		System.out.println("before stuffing "+fService.checkErrors(conv));
		System.out.println("after stuffing "+fService.checkErrors(stuffed));
		System.out.println("affter unstuffing "+fService.checkErrors(unStuffed));
		System.out.println(stuffed);
		System.out.println(unStuffed);
		*/
		/*String s ="11010011101101100011011011000110000101110011011100110010000010010000100111110";
		String afterStuffing=new FrameService().bitStuffing(s);
		System.out.println("after stuffing "+afterStuffing);
		String afterUnstuffing=new FrameService().removeBitStuffing(s);
		System.out.println("cheking errros before stufing "+new FrameService().checkErrors(s));
		System.out.println("cheking errros after stuffing "+new FrameService().checkErrors(afterStuffing));
		System.out.println("after unstuffing "+afterUnstuffing);
		System.out.println("cheking errors after un-stuffing "+new FrameService().checkErrors(afterUnstuffing));
		System.out.println(s.equals(afterUnstuffing));
		*/
		
	}

}
