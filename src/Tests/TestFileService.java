package Tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import Services.FileService;

public class TestFileService {

	/**
	 * tester la lécture des fichier 
	 * 
	 * **/
	@Test
	public void testReadFile(){
		
		FileService fService = new FileService();
		List<String>file=fService.readFile("testReadFile");
		assertEquals(file.size(), 3);
		assertEquals(file.get(0),"test"); // 1ere ligne contient le mot test
		assertEquals(file.get(1),"read"); // 2eme ligne contient le mot read
		assertEquals(file.get(2),"file"); // 3eme ligne contient le mot file
	}

}
