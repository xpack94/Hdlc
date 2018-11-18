package Services;

import java.util.ArrayList;
import java.util.List;

import Definitions.Frame;

public class FrameService {

	
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
	
	
	public long createCrc(String data){
		String poly="10001000000100001";
		String polyToHexa=Integer.toHexString(Integer.parseInt(poly,2)); 
		int WIDTH = (8 * 2);
		int TOPBIT = (1 << (WIDTH - 1));
		System.out.println();
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
	
	
	
	
}
