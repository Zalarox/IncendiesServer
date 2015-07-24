package main.game.players.content.clanchat.load;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.game.players.Player;

/**
 * @author Sanity, Thelife
 */

public class ClanDataWriter {

	public ClanDataWriter(String owner, String name, int lootshare) {
		this.owner = owner;
		this.name = name;
		this.lootshare = lootshare;
		this.loadFriends(owner);
		this.loadRecruits(owner);
		this.loadCorporals(owner);
		this.loadSergeants(owner);
		this.loadLieutenants(owner);
		this.loadCaptains(owner);
		this.loadGenerals(owner);
		this.changesMade = false;
	}

	public ArrayList<String> Friends = new ArrayList<String>();
	public ArrayList<String> Recruits = new ArrayList<String>();
	public ArrayList<String> Corporals = new ArrayList<String>();
	public ArrayList<String> Sergeants = new ArrayList<String>();
	public ArrayList<String> Lieutenants = new ArrayList<String>();
	public ArrayList<String> Captains = new ArrayList<String>();
	public ArrayList<String> Generals = new ArrayList<String>();
	public ArrayList<String> Bans = new ArrayList<String>();

	public boolean isFriend(String name) {
		return (Friends.contains(name.toLowerCase()));
	}

	public boolean isRecruit(String name) {
		return (Recruits.contains(name.toLowerCase()));
	}

	public boolean isCorporal(String name) {
		return (Corporals.contains(name.toLowerCase()));
	}

	public boolean isSergeant(String name) {
		return (Sergeants.contains(name.toLowerCase()));
	}

	public boolean isLieutenant(String name) {
		return (Lieutenants.contains(name.toLowerCase()));
	}

	public boolean isCaptain(String name) {
		return (Captains.contains(name.toLowerCase()));
	}

	public boolean isGeneral(String name) {
		return (Generals.contains(name.toLowerCase()));
	}

	public boolean isBanned(String name) {
		return (Bans.contains(name.toLowerCase()));
	}

	public void removeFriend(String name, String owner) {
		Friends.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Friends.txt", name);
	}

	public void removeRecruit(String name, String owner) {
		Recruits.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Recruits.txt", name);
	}

	public void removeCorporal(String name, String owner) {
		Corporals.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Corporals.txt", name);
	}

	public void removeSergeant(String name, String owner) {
		Sergeants.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Sergeants.txt", name);
	}

	public void removeLieutenant(String name, String owner) {
		Lieutenants.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Lieutenants.txt", name);
	}

	public void removeCaptain(String name, String owner) {
		Captains.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Captains.txt", name);
	}

	public void removeGeneral(String name, String owner) {
		Generals.remove(name.toLowerCase());
		deleteFromFile("./Data/ClanData/" + owner + "/Generals.txt", name);
	}

	@SuppressWarnings("unused")
	public void loadFriends(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Friends.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Friends.txt"));
			}

			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Friends.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 7);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadRecruits(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Recruits.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Recruits.txt"));
			}

			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Recruits.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 1);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadCorporals(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Corporals.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Corporals.txt"));
			}
			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Corporals.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 2);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadSergeants(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Sergeants.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Sergeants.txt"));
			}
			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Sergeants.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 3);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadLieutenants(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Lieutenants.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Lieutenants.txt"));
			}
			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Lieutenants.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 4);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadCaptains(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Captains.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Captains.txt"));
			}
			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Captains.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 5);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadGenerals(String name) {
		try {
			boolean folderName;
			folderName = (new File("./Data/ClanData/" + name + "")).mkdirs();
			BufferedWriter file = null;
			BufferedReader File = null;
			try {
				File = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Generals.txt"));
			} catch (FileNotFoundException e) {
				file = new BufferedWriter(new FileWriter("./Data/ClanData/" + name + "/Generals.txt"));
			}
			BufferedReader in = new BufferedReader(new FileReader("./Data/ClanData/" + name + "/Generals.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addName(data, 6);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeRank(Player c, String name, String rank, int Rank) {
		addName(name, Rank);
		addNameToFile(c, name, rank);
	}

	public void addName(String name, int rank) {
		if (rank == -1) {
			Bans.add(name.toLowerCase());
		}
		if (rank == 1) {
			Recruits.add(name.toLowerCase());
		}
		if (rank == 2) {
			Corporals.add(name.toLowerCase());
		}
		if (rank == 3) {
			Sergeants.add(name.toLowerCase());
		}
		if (rank == 4) {
			Lieutenants.add(name.toLowerCase());
		}
		if (rank == 5) {
			Captains.add(name.toLowerCase());
		}
		if (rank == 6) {
			Generals.add(name.toLowerCase());
		}
		if (rank == 7) {
			Friends.add(name.toLowerCase());
		}
	}

	public static void deleteFromFile(String file, String name) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			ArrayList<String> contents = new ArrayList<String>();
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (!line.equalsIgnoreCase(name)) {
					contents.add(line);
				}
			}
			r.close();
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (String line : contents) {
				w.write(line, 0, line.length());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
		}
	}

	public void addNameToFile(Player c, String Name, String rank) {
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter("./Data/ClanData/" + c.playerName + "/" + rank + ".txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int whoCanKickOnChat = 8;
	public int whoCanTalkOnChat = 0;
	public int whoCanEnterChat = 0;
	public boolean changesMade = false;
	public int[] members = new int[100];
	public int[] mutedMembers = new int[10];
	public String name;
	public String owner;
	public int lootshare;
	public int CSLS = 0;
	public int membersNumber;
}