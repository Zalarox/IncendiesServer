package main.game.players.content.clanchat.load;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import main.Data;
import main.GameEngine;

/**
 *
 * @authors PJNoMore, Thelife
 *
 */

public class Clans {

	Properties p = new Properties();
	int saveCount = 0;

	public void initialize() {
		loadClans();
	}

	public void process() {
		if (saveCount == 100) {
			Calendar cal = new GregorianCalendar();
			for (int i = 0; i < GameEngine.clanChat.clans.length; i++) {
				if (GameEngine.clanChat.clans[i] != null) {
					if (cal.get(Calendar.MINUTE) == 0) {
						GameEngine.clanChat.clans[i].Bans.clear();
					}
					if (GameEngine.clanChat.clans[i].changesMade == true) {
						String owner = GameEngine.clanChat.clans[i].owner;
						saveClan(owner, GameEngine.clanChat.clans[i].name, GameEngine.clanChat.clans[i].lootshare);
						owner = GameEngine.clanChat.clans[i].owner;
						Write(owner, "Kick", i);
						Write(owner, "Talk", i);
						Write(owner, "Enter", i);
						GameEngine.clanChat.updateClanChat(i);
						GameEngine.clanChat.clans[i].changesMade = false;
					}
				}
			}
			saveCount = 0;
		}
		saveCount++;
	}

	public void saveClans() {
		for (int i = 0; i < GameEngine.clanChat.clans.length; i++) {
			if (GameEngine.clanChat.clans[i] != null) {
				String owner = GameEngine.clanChat.clans[i].owner;
				String name = GameEngine.clanChat.clans[i].name;

				int lootshare = GameEngine.clanChat.clans[i].lootshare;

				Write(owner, "Kick", i);
				Write(owner, "Talk", i);
				Write(owner, "Enter", i);

				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(Data.CLAN_DATA_CONFIG_DIRECTORY + owner + ".ini"));
					try {
						out.write("owner=" + owner);
						out.newLine();
						out.write("name=" + name);
						out.newLine();
						out.write("lootshare=" + lootshare);
					} finally {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// System.out.println("Clan saved! Owner: "+owner+" Name:
				// "+name+" Password: "+password+" Lootshare: "+loot);
			}
		}
	}

	public void saveClan(String owner, String name, int lootshare) {
		try {
			File parentDir = new File(Data.CLAN_DATA_CONFIG_DIRECTORY);
			final String fileName = owner + ".ini";
			File file = new File(parentDir, fileName);
			file.delete();
			file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(Data.CLAN_DATA_CONFIG_DIRECTORY + owner + ".ini"));
			try {
				out.write("owner=" + owner);
				out.newLine();
				out.write("name=" + name);
				out.newLine();
				out.write("lootshare=" + lootshare);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("Clan created! Owner: "+owner+" Name: "+name+"
		// Password: "+password+" Lootshare: "+loot);
	}

	@SuppressWarnings("unused")
	public void loadClans() {
		File clanDir = new File(Data.CLAN_DATA_CONFIG_DIRECTORY);
		File[] files = clanDir.listFiles();
		int numClans = files.length;
		int loot = 0;
		boolean pass = false;

		for (int i = 0; i < numClans; i++) {
			String clanFile = files[i].toString();
			try {
				p.load(new FileInputStream(clanFile));
				if (p.getProperty("lootshare").equals(1))
					loot = 1;
				if (p.getProperty("lootshare").equals(2))
					loot = 2;
				GameEngine.clanChat.loadClan(p.getProperty("owner"), p.getProperty("name"), loot);
				// System.out.println("Clan loaded! Owner:
				// "+p.getProperty("owner")+" Name: "+p.getProperty("name")+"
				// Password: "+p.getProperty("password")+" Lootshare: "+loot);
			} catch (Exception e) {
			}
		}
	}

	public void loadOptions() {
		for (int i = 0; i < GameEngine.clanChat.clans.length; i++) {
			if (GameEngine.clanChat.clans[i] != null) {
				GameEngine.clanChat.clans[i].whoCanKickOnChat = Load(GameEngine.clanChat.clans[i].owner, "Kick", i);
				GameEngine.clanChat.clans[i].whoCanTalkOnChat = Load(GameEngine.clanChat.clans[i].owner, "Talk", i);
				GameEngine.clanChat.clans[i].whoCanEnterChat = Load(GameEngine.clanChat.clans[i].owner, "Enter", i);
			}
		}
	}

	@SuppressWarnings("unused")
	public void Write(String name, String file, int i) {
		BufferedWriter cc = null;
		BufferedReader File = null;
		BufferedWriter fileW = null;
		try {
			File = new BufferedReader(new FileReader(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
		} catch (FileNotFoundException e) {
			try {
				fileW = new BufferedWriter(new FileWriter(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
			} catch (IOException a) {

			}
		}
		try {
			cc = new BufferedWriter(new FileWriter(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
		} catch (IOException ioexception) {
		}

		try {
			cc = new BufferedWriter(new FileWriter(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
			if (file == "Kick") {
				cc.write(Integer.toString(GameEngine.clanChat.clans[i].whoCanKickOnChat), 0,
						Integer.toString(GameEngine.clanChat.clans[i].whoCanKickOnChat).length());
			}
			if (file == "Talk") {
				cc.write(Integer.toString(GameEngine.clanChat.clans[i].whoCanTalkOnChat), 0,
						Integer.toString(GameEngine.clanChat.clans[i].whoCanTalkOnChat).length());
			}
			if (file == "Enter") {
				cc.write(Integer.toString(GameEngine.clanChat.clans[i].whoCanEnterChat), 0,
						Integer.toString(GameEngine.clanChat.clans[i].whoCanEnterChat).length());
			}
			cc.close();
		} catch (IOException ioexception) {
		}
	}

	@SuppressWarnings("unused")
	public int Load(String name, String file, int a) {
		int cc = 0;
		String s = "";
		int i = 1;
		try {
			BufferedReader bufferedreader = null;
			BufferedReader File = null;
			BufferedWriter fileW = null;
			try {
				File = new BufferedReader(new FileReader(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
			} catch (FileNotFoundException e) {
				fileW = new BufferedWriter(new FileWriter(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
				Write(name, file, a);
			}
			bufferedreader = new BufferedReader(new FileReader(Data.CLAN_CHAT_CONFIG_DIRECTORY + name + "/" + file + ".txt"));
			for (String s1 = bufferedreader.readLine(); s1 != null; s1 = bufferedreader.readLine()) {
				s1 = s1.trim();
				cc = Integer.parseInt(s1);
				i++;
			}

			bufferedreader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return cc;
	}

}