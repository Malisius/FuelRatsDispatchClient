package com.cmdrsforhire;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class RatBot extends PircBot {
	/**
	 * The main application window
	 */
	SpatchWindow window;
	/**
	 * The name of the fuel rats bot that we should be listening to for ratsignals
	 */
	private String channelBotNick;
	/**
	 * The text that will alert us to a ratsignal, usually just the word RATSIGNAL
	 */
	private String ratsignalFlagText;
	/**
	 * An array of nicknames that we will try to use when joining, in order
	 */
	private String[] pNick;
	/**
	 * Our nickserv password
	 */
	private String password;
	/**
	 * Our cmdr name, which will be used when contacting EDSM
	 */
	private String cmdrName;
	/**
	 * The cases that we know about right now, and which are active 
	 */
	private CaseList cases;
	/**
	 * True if we want to receive windows notifications, false otherwise
	 */
	private boolean popNotifications;

	public void setCmdrName(String cmdrName) {
		this.cmdrName = cmdrName;
	}

	public String getCmdrName() {
		return cmdrName;
	}

	public String getChannelBotNick() {
		return channelBotNick;
	}

	public void setChannelBotNick(String channelBotNick) {
		this.channelBotNick = channelBotNick;
	}

	public String getRatsignalFlagText() {
		return ratsignalFlagText;
	}

	public void setRatsignalFlagText(String ratsignalFlagText) {
		this.ratsignalFlagText = ratsignalFlagText;
	}
	
	public void setPopNotifications(boolean popNotifications) {
		this.popNotifications = popNotifications;
	}
	
	public boolean isPopNotifications() {
		return popNotifications;
	}

	public RatBot() {
		super();
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		System.out.println(sender + "@" + channel+ ": " + message);

		//Check for a new case, and add it to the list if it is one.
		if(message.contains(ratsignalFlagText) && sender.equalsIgnoreCase(channelBotNick)) {
			System.out.println("NEW CASE INCOMING!!!");
			Case newcase = new Case(message);
			System.out.println(newcase.toString());
			if(!cases.add(newcase.clone())) {
				System.out.println("Failed to add case to the caselist.");
			}
			//Check to make sure that case is actually there
			if(!cases.contains(newcase)) {
				System.out.println("Failed to add case to the caselist.");
			}
			
			//Send a windows notification if enabled
			if(popNotifications) {
				try {
					newcase.displayTray(getCmdrName());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Couldn't pop notification!");
				}
			}
			
			//Copy system to the clipboard if enabled
			//TODO Un-hardcode the boolean
			boolean copyToClipboard = true;
			if(copyToClipboard) {
				StringSelection selection = new StringSelection(newcase.getSystem());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		}
		
		//Check to see if this message is relevant to any existing cases
		//If so, add it to the relevant list.
		/*for(Case c : cases) {
			if(c.isRelevant(message, sender)) {
				c.addMessage(message, sender);
			}
		}*/
		
		//Check to see if this message is coming from either channel.
		//If it is, add it to that list.
		if(channel.equalsIgnoreCase("#fuelrats")) {
			this.window.fuelRatsList.add(sender + ": " +message);
			this.window.fuelRatsList.update();
		} else if(channel.equalsIgnoreCase("#ratchat")) {
			this.window.ratChatList.add(sender + ": " + message);
			this.window.ratChatList.update();
		}
		
		System.out.print("Breakpoint");
	}

	public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
		RatBot testBot = new RatBot();

		//Don't use this till I get it working.
		//Just hardcode that shit in.
		//testBot.getSettings();

		testBot.cases = new CaseList();
		testBot.setCmdrName("Isaac Sinclair");
		testBot.setName("SinclairBot");
		testBot.setLogin("SinclairBot");
		testBot.setPopNotifications(true);
		testBot.setVerbose(false);

		testBot.connect("irc.fuelrats.com");
		testBot.identify("1nRd^*7%x*");
		testBot.setRatsignalFlagText("RATSIGNAL - CMDR");
		testBot.setChannelBotNick("MechaSqueak[BOT]");
		testBot.joinChannel("#fuelrats");
		
		Thread windowThread = new Thread() {
			public void run() {
					try {
					testBot.window = new SpatchWindow();
					testBot.window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		windowThread.run();
	}

}
