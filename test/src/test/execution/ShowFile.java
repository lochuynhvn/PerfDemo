import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.performance.example.FileFinder;
import com.performance.example.File_Interact;
import com.performance.example.Gdrive;

import common.CONSTANT;

public class ShowFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		List<Path> listOfFile = new ArrayList<Path>();    
		File file;
        // get the matched paths
    	System.out.println("Name of Main Class: "+ ShowFile.class.getName());
    	String xpath = System.getProperty("user.dir");
    	Path path = Paths.get(xpath);
    	xpath = path.getParent().toString()+ CONSTANT.resultPath;
    	
    	FileFinder finder = new FileFinder("*.csv");
    	listOfFile = finder.getFileName(xpath.toString(), finder);        
        for(Path p : listOfFile)
        {
        	String url = xpath + "/"+ p.toString();
        	file = new File(url);
   		String[] lines = File_Interact.getContentOfFile(file);
   		File_Interact.writeFile(file, lines, "ID", ","+ System.getProperty("env"));
   		Gdrive.uploadFileToGooleFolder("Performance Result", xpath + "/"+ p.toString());
        } 
	}
	
	public void testPostMerge(){
		
	}

}
