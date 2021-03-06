package com.performance.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class Gsheet {
	/** Application name. */
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final String dir = ".credentials/sheets.googleapis.com-java-quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), dir);

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;
	private static final String spreadsheetId = "13fu1RoQ-SI3hrxkBbJOdLPyl0UspXJMCy2URy8a4jmA";

	public static Sheets service;

	/**
	 * 
	 * 
	 * 
	 */
	public Gsheet(String applicationName) {
		try {
			Gsheet.service = Gsheet.getSheetsService(applicationName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

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
		InputStream in = new FileInputStream(path + "/src/main/resource/client_secret.json");
		// InputStream in = Gsheet.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * 
	 * @return an authorized Sheets API client service
	 * @throws IOException
	 */
	public static Sheets getSheetsService(String applicationName) throws IOException {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName).build();
	}

	public static List<List<Object>> getData(String spreadsheetId, String sheetName, String range) throws IOException {

		range = sheetName + "!" + range;
		List<List<Object>> data = new ArrayList<List<Object>>();
		// service.spreadsheets().values().append(spreadsheetId, range,
		// value).setValueInputOption("RAW").execute();

		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();

		data = response.getValues();
		if (data == null || data.size() == 0) {
			System.out.println("No data found.");
		} else {
			// System.out.println(values.size());
			// for (List row : values) {
			// // Print columns A and E, which correspond to indices 0 and 4.
			// //System.out.printf("%s, %s\n", row.get(0), row.get(0));
			// System.out.printf("%s", row.get(0));
			// }
			return data;
		}
		return null;
	}
	
	/***
	 * Set value of data following range
	 * @param spreadsheetId
	 * @param sheetName
	 * @param range
	 * @param data
	 * @throws IOException
	 */
	public void setData(String spreadsheetId, String sheetName, String range, List<List<Object>> data)
			throws IOException {

		range = sheetName + "!" + range;
		// service.spreadsheets().values().append(spreadsheetId, range,
		// value).setValueInputOption("RAW").execute();
		ValueRange value = new ValueRange();
		value.setValues(data);

		service.spreadsheets().values().update(spreadsheetId, range, value).setValueInputOption("RAW").execute();
	}
	
	/***
	 * Show data(in range) of sheet based on spread sheet id
	 * @param spreadsheetId
	 * @param sheetName
	 * @param range
	 */
	public void showData(String spreadsheetId, String sheetName, String range) {
		List<List<Object>> data = null;
		try {
			data = getData(spreadsheetId, sheetName, range);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (data == null || data.size() == 0) {
			System.out.println("No data found.");
		} else {
			// int count = 1;
			System.out.println("Size of data is: " + data.size());
			for (List<?> row : data) {
				// Print columns A and E, which correspond to indices 0 and 4.
				// System.out.printf("%s, %s\n", row.get(0), row.get(0));
				System.out.printf("%s - %s \n", row.get(0), row.get(1));
			}
		}
	}

	public static List<List<Object>> toArraryOfArray(String content) {
		List<Object> data1 = new ArrayList<Object>();
		data1.add(content);
		List<List<Object>> data = new ArrayList<List<Object>>();
		data.add(data1);
		return data;
	}

	public void uploadData(String spreadsheetId, String sheetName, String range) {
		List<List<Object>> data = null;
		try {
			data = getData(spreadsheetId, sheetName, range);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (data == null || data.size() == 0) {
			System.out.println("No data found.");
		} else {
			// int count = 1;
			System.out.println("Size of data is: " + data.size());
			for (List<?> row : data) {
				// Print columns A and E, which correspond to indices 0 and 4.
				// System.out.printf("%s, %s\n", row.get(0), row.get(0));
				System.out.printf("%s - %s \n", row.get(0), row.get(1));
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Gsheet obj = new Gsheet(APPLICATION_NAME);
		obj.showData(spreadsheetId, "Sheet1", "A1:D");

		// gg.setData(spreadsheetId, "Example", "A2:A2",GoogleAPI.toArraryOfArray("Chỉnh
		// sửa"));

	}
}
