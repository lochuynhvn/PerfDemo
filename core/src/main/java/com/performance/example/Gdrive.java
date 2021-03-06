package com.performance.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;

import common.CONSTANT;

import com.google.api.services.drive.Drive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Gdrive {
	/**
	 * Service of GDrive
	 */

	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/drive-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/drive-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		String path = System.getProperty("user.dir");
		InputStream in = new FileInputStream(path + "/src/main/resource/gdrive_client_secret.json");

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static boolean uploadFileToGoole(String fileAddress) throws IOException {
		Drive service = getDriveService();
		File fileMetadata = new File();
		fileMetadata.setName("My Report");
		fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");

		java.io.File filePath = new java.io.File(fileAddress);
		FileContent mediaContent = new FileContent("text/csv", filePath);
		File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
		System.out.println("File ID: " + file.getId());
		return true;
	}

	public static boolean uploadFileToGooleFolder(String folderName, String fileAddress) throws IOException {
		Drive service = getDriveService();
		String folderId = null;
		FileList result = service.files().list()
				.setQ("mimeType='application/vnd.google-apps.folder' and name = '" + folderName + "'").execute();

		for (File f : result.getFiles()) {
			folderId = f.getId();
			break;
		}

		File fileMetadata = new File();
		fileMetadata.setName("My Report");
		fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
		fileMetadata.setParents(Collections.singletonList(folderId));

		java.io.File filePath = new java.io.File(fileAddress);
		FileContent mediaContent = new FileContent("text/csv", filePath);
		File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
		System.out.println("File ID: " + file.getId());
		return true;
	}

	public static void showFile(Drive service) throws IOException {

		// Print the names and IDs for up to 10 files.
		FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files:");
			for (File file : files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
			}
		}

	}

	public static void main(String[] args) throws IOException {
		List<Path> listOfFile = new ArrayList<Path>();

		// get the matched paths

		String xpath = System.getProperty("user.dir");
		Path path = Paths.get(xpath);
		xpath = path.getParent().toString() + CONSTANT.resultPath;

		FileFinder finder = new FileFinder("*.csv");
		listOfFile = finder.getFileName(xpath.toString(), finder);		

		for (Path p : listOfFile) {
			uploadFileToGoole(xpath + "/" + p.toString());
		}
	}

}
