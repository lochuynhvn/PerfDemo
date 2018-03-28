package com.performance.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class File_Interact {
	static int numLines;

	/***
	 * Read a line in a file based on line number
	 * 
	 * @param lineNumber
	 * @param aFile
	 * @return
	 */
	public static String readLine(int lineNumber, File aFile) {
		String lineText = "";

		try {

			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				for (int count = 0; count < lineNumber; count++) {
					input.readLine(); // ReadLine returns the contents of the next line, and returns null at the end
										// of the file.
				}
				lineText = input.readLine();
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return lineText;
	}
	
	/***
	 * Return number of lines in a file
	 * @param aFile
	 * @return
	 */
	public static int getNumberLines(File aFile) {
		int numLines = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				while ((input.readLine()) != null) { // ReadLine returns the contents of the next line, and returns null
														// at the end of the file.
					numLines++;
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return numLines;
	}

	/***
	 * Return whole content of file in array of String
	 * @param file
	 * @return
	 */
	public static String[] getContentOfFile(File file) {
		String[] lines;
		int numLines = getNumberLines(file);

		lines = new String[numLines];

		for (int i = 0; i < numLines; i++) {
			lines[i] = readLine(i, file);
		}
		return lines;
	}
	
	/***
	 * Rewrite file with new content
	 * @param aFile
	 * @param oldContent
	 * @param key
	 * @param value
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeFile(File aFile, String[] oldContent, String key, String value)
			throws FileNotFoundException, IOException {
		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: " + aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: " + aFile);
		}
		System.out.println("Gia tri cua key la: " + key);
		System.out.println("Gia tri cua value la: " + value);
		BufferedWriter output = new BufferedWriter(new FileWriter(aFile));
		try {
			for (int count = 0; count < oldContent.length; count++) {
				if (count == 0) {
					oldContent[count] = oldContent[count].concat("," + key);
				} else {
					oldContent[count] = oldContent[count].concat(value);
				}
				output.write(oldContent[count]);
				if (count != oldContent.length - 1) {// This makes sure that an extra new line is not inserted at the
														// end of the file
					output.newLine();
				}

			}

		} finally {
			output.close();
		}
	}

	public static void main(String[] args) throws IOException {

		File myFile = new File(
				"F:\\Sym Framework\\PerfProject\\PerfDemo\\test\\target\\jmeter\\results\\20180326-Blaze_Demo.csv");
		numLines = getNumberLines(myFile);
		String[] lines = getContentOfFile(myFile);
		writeFile(myFile, lines, "ID", ",1");

	}
}
