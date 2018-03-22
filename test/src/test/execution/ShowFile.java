import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.performance.example.FileFinder;
import com.performance.example.Gdrive;

import common.CONSTANT;

public class ShowFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		List<Path> listOfFile = new ArrayList<Path>();    
		 
        // get the matched paths
    	System.out.println("Name of Main Class: "+ ShowFile.class.getName());
    	String xpath = System.getProperty("user.dir");
    	Path path = Paths.get(xpath);
    	xpath = path.getParent().toString()+ CONSTANT.resultPath;
    	
    	FileFinder finder = new FileFinder("*.csv");
    	listOfFile = finder.getFileName(xpath.toString(), finder);
    	
    	Gdrive gdrive = new Gdrive();       
        
        for(Path p : listOfFile)
        {
        	gdrive.uploadFileToGooleFolder("Performance Result", gdrive.getDriveService(), xpath + "/"+ p.toString());
        } 
	}

}
