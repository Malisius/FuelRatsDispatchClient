package com.cmdrsforhire;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.List;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Case {
	private String cmdr;
	private String platform;
	private String system;
	private boolean cr;
	private String language;
	private int casenum;
	private double caseCoords[];
	private ArrayList<String> assignedRats;
	private List caseChatList;
	private String signalText;

	public Case(String ratsignal) {
		caseCoords = new double[3];
		setCmdr(ratsignal.substring(ratsignal.indexOf("CMDR")+5, ratsignal.indexOf("- System")));
		setSystem(ratsignal.substring(ratsignal.indexOf("System:") + 8, ratsignal.indexOf('(')));
		setPlatform(ratsignal.substring(ratsignal.indexOf("Platform:") + 10, ratsignal.indexOf("- O2")));
		if(ratsignal.substring(ratsignal.indexOf("O2:") + 4, ratsignal.indexOf("O2:") + 7).equalsIgnoreCase("OK")) {
			setCr(true);
		} else setCr(false);
		setLanguage(ratsignal.substring(ratsignal.indexOf("Language:")+10, ratsignal.indexOf("(Case")));
		setCasenum(Integer.parseInt(ratsignal.substring(ratsignal.indexOf("(Case")+7, ratsignal.indexOf("(Case")+8)));
		setSignalText(ratsignal);
		assignedRats = new ArrayList<String>();
	}
	
	public Case clone() {
		return new Case(getSignalText());
	}
	
	public void setSignalText(String signalText) {
		this.signalText = signalText;
	}
	
	public String getSignalText() {
		return signalText;
	}

	public String getCmdr() {
		return cmdr;
	}

	public void setCmdr(String cmdr) {
		this.cmdr = cmdr;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system.trim();
	}

	public boolean isCr() {
		return cr;
	}

	public void setCr(boolean cr) {
		this.cr = cr;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language.trim();
	}

	public int getCasenum() {
		return casenum;
	}

	public void setCasenum(int casenum) {
		this.casenum = casenum;
	}

	@Override
	public String toString() {
		return "Case Number: #" + getCasenum() + "\nCMDR: " + getCmdr() + "\nSystem: " + getSystem()
		+ "\nCODE RED: " + isCr() + "\nPlatform: " + getPlatform() + "\nLanguage: " + getLanguage(); 
	}

	public String getJsonString() {
		String returnString = "{\"id\": \"test1\", \"text\": \"" + toString() + "\", \"size\", \"normal\", color\": \"red\", \"x\": 200, \"y\": 100, \"ttl\": 15}";

		return returnString;
	}

	public void displayTray(String cmdrName) throws AWTException, IOException {
		//Obtain only one instance of the SystemTray object
		SystemTray tray = SystemTray.getSystemTray();

		//If the icon is a file
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		//Alternative (if the icon is on the classpath):
		//Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		//Let the system resize the image if needed
		trayIcon.setImageAutoSize(true);
		//Set tooltip text for the tray icon
		trayIcon.setToolTip("Fuel Rats RATSIGNAL!");
		tray.add(trayIcon);

		trayIcon.displayMessage("RATSIGNAL!", this.toString(), MessageType.WARNING);
	}
	
	/**
	 * Check to see if the passed irc message is relevant to this case.
	 * Many things could make a message relevant. Perhaps it was sent by a client.
	 * Maybe it was sent by an assigned rat. Maybe it contains the case number.
	 * Who knows.
	 * @param message The message recieved in IRC
	 * @param sender 
	 * @return True if relevant, false otherwise.
	 */
	public boolean isRelevant(String message, String sender) {
		if(message.contains(cmdr) || message.contains("#"+casenum) || assignedRats.contains(sender)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Add a message to this Cases caseChatList
	 * @param message
	 */
	public void addMessage(String message, String sender) {
		caseChatList.add(sender + ": " + message);
	}

	/**
	 * Query the EDSM for the distance between the current location of
	 * cmdrName and this {@link Case}'s {@link system}.
	 * @param cmdrName The fuel rat we're checking, probably you.
	 * @return The distance to the {@link Case} in Ly.
	 * @throws IOException 
	 */
	public double getTravelDistance(String cmdrName) throws IOException {
		double distance = -1;
		String currentLocation;
		JSONObject currentLocationJson;
		JSONObject locationInfo;
		double currentCoords[] = new double[3];
		JSONArray jsonCoords;
		
		StringBuilder result = new StringBuilder();
		URL url = new URL("http://www.edsm.net/api-logs-v1/get-position?commanderName=" + cmdrName.replaceAll(" ", "%20"));
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		currentLocationJson = new JSONObject(result.toString());
		currentLocation = currentLocationJson.getString("System");
		
		url = new URL("http://www.edsm.net/api-v1/system?showCoordinates=1&systemName=" + currentLocation);
		result = new StringBuilder();
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		locationInfo = new JSONObject(result.toString());
		jsonCoords = locationInfo.getJSONArray("coords");
		currentCoords[0] = jsonCoords.getDouble(0);
		currentCoords[1] = jsonCoords.getDouble(1);
		currentCoords[2] = jsonCoords.getDouble(2);
		
		url = new URL("https://www.edsm.net/api-v1/system?showCoordinates=1&systemName=" + this.getSystem());
		result = new StringBuilder();
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		
		locationInfo = new JSONObject(result.toString());
		jsonCoords = locationInfo.getJSONArray("coords");
		caseCoords[0] = jsonCoords.getDouble(0);
		caseCoords[1] = jsonCoords.getDouble(1);
		caseCoords[2] = jsonCoords.getDouble(2);
		
		return distance;
	}
}
