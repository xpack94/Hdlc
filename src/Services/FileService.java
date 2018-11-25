package Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * classe qui donne des services relatif au fichiers 
 * comme la lecture d'un fichier ainsi que l'écriture
 * dans notre cas on aura seulement besoin de la lecutre du fichier 
 * a envoyer 
 * 
 * **/
public class FileService {

	
	/**
	 * @param filePath de type string qui correspond au line du fichier a lire 
	 * @return une liste de type string qui correspond au lignes du fichier a lire 
	 * 
	 * fonction qui permet de lire un fichier et sauvegarde ses lignes dans une liste
	 * 
	 * **/
	public List<String> readFile(String filePath){
		List<String> data=new ArrayList<String>();
		try{
			
			File fileToRead=new File(filePath);
			FileReader reader=new FileReader(fileToRead);
			BufferedReader buffer=new BufferedReader(reader);
			String line;
			try {
				line = buffer.readLine();
				while( line!=null){
					if(line.length()>0)
						data.add(line);
					line=buffer.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch(FileNotFoundException e){
			System.out.println("le fichier données n'est pas trouvé ");
		}
		
		
		return data;
		
	}
	
	public void printFile(List<String> file){
		for(String line:file){
			System.out.println(line);
		}
	}
	
	
	
}
