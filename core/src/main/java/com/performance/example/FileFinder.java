package com.performance.example;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.nio.file.FileVisitResult.CONTINUE;

public class FileFinder extends SimpleFileVisitor<Path> {
	 
    private final PathMatcher matcher;
    private List<Path> matchedPaths = new ArrayList<Path>();
    
    public FileFinder(String pattern) {
        matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + pattern);
    }
 
    // Compares the glob pattern against
    // the file or directory name.
    void match(Path file) {
        Path name = file.getFileName();
 
        if (name != null && matcher.matches(name)) {
            matchedPaths.add(name);
        }
    }
 
    // Invoke the pattern matching
    // method on each file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        match(file);
        return CONTINUE;
    }
 
    // Invoke the pattern matching
    // method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs) {
        match(dir);
        return CONTINUE;
    }
 
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
 
    public int getTotalMatches() {
        return matchedPaths.size();
    }
 
    public Collection<Path> getMatchedPaths() {
        return matchedPaths;
    }
    
    public List<Path> getFileName(String path, FileFinder finder) throws IOException{
    	List<Path> listOfFile = new ArrayList<Path>();
    	Files.walkFileTree(Paths.get(path), finder);
    	Collection<Path> matchedFiles = getMatchedPaths();
		
		for (Path p: matchedFiles)
		{
			listOfFile.add(p);
		}
		
		return listOfFile;
    }
}
