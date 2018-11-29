package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Services.FrameService;

public class TestFrameService {

	
	/***
	 * tester la methode qui fait le bit stuffing
	 * 
	 * */
	@Test
	public void testBitStuffing() {
		
		String normalString="11100111101111111";
		String emptyString="";
		String shouldStuffAtTheEnd ="1110011111";
		FrameService fService = new FrameService();
		String stuffedNormal=fService.bitStuffing(normalString);
		String stuffedEmpty=fService.bitStuffing(emptyString);
		String stuffedAtTheEnd=fService.bitStuffing(shouldStuffAtTheEnd);
		
		
		assertEquals(stuffedNormal, "111001111011111011");
		assertEquals(stuffedEmpty, "");
		assertEquals(stuffedAtTheEnd, "11100111110");
		
	}
	/**
	 * tester la methode qui supprime le bit stuffing 
	 * 
	 * 
	 * **/
	@Test
	public void testRemoveBitStuffing() {
		
		String normalString="111001111011111011";
		String emptyString="";
		String shouldStuffAtTheEnd ="11100111110";
		FrameService fService = new FrameService();
		String unStuffedNormal=fService.removeBitStuffing(normalString);
		String unStuffedEmpty=fService.removeBitStuffing(emptyString);
		String unStuffedAtTheEnd=fService.removeBitStuffing(shouldStuffAtTheEnd);
		
		
		assertEquals(unStuffedNormal, "11100111101111111");
		assertEquals(unStuffedEmpty, "");
		assertEquals(unStuffedAtTheEnd, "1110011111");
		
	}
	
	
	@Test
	public void testCreateCrc(){
		String type ="i";
		int num=0;
		String data="hello";
		String empty=""; //  test de chaine vide 
		FrameService fService = new FrameService();
		String crc= fService.createCrc(
								fService.bitConverter(type)+
								fService.bitConverter(""+num)+
								fService.bitConverter(data)
								);
		
		String emptyCrc=fService.createCrc(
								fService.bitConverter(type)+
								fService.bitConverter(""+num)+
								fService.bitConverter(empty)
								);
		assertEquals(crc, "0001101110000111");
		assertEquals(crc.length(), 16); // s'assuer que la taile du crc est de 16 
		assertEquals(emptyCrc, "1001001000101111");
		assertEquals(emptyCrc.length(), 16);
		
	}
	
	@Test
	public void testCheckErros(){
		
		String type ="i";
		int num=0;
		String data="hello";
		FrameService fService = new FrameService();		
		String crc= fService.createCrc(
				fService.bitConverter(type)+
				fService.bitConverter(""+num)+
				fService.bitConverter(data)
				);	
		
		
		String code=fService.bitConverter(type)+
				fService.bitConverter(""+num)+
				fService.bitConverter(data)+
				crc;
		String code2=code+"1" ; // rajouter un bit au code 
		
		assertEquals(fService.checkErrors(code2), true); // puisuqe on a rajouté un 1 il doit y avoir une erreur
		code=code.substring(0,code2.length()-2); // enlever un caractaire du code 
		assertEquals(fService.checkErrors(code2), true);
		
		String code3=fService.bitStuffing(code); //faire le bitstuffing 
		assertEquals(fService.checkErrors(code), true);
		
		}	
		
	
	
	

}
