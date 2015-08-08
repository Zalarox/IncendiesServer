package main.game.players;

/**
 * A class tracking various equipment-related fields, along 
 * with helper methods for getting/setting equipment IDs or 
 * sets of IDs.
 * 
 * @author Branon McClellan (KeepBotting)
 *
 */

public class Equipment {

	Player c;

	public Equipment(Player c) {
		this.c = c;
	}
	
	/**
	 * Some integer constants representing various equipment slots.
	 * 
	 * These are all out of order and occasionally skip numbers, 
	 * because they've got to respect the indices of  PI's 
	 * playerEquipment[] array -- which is all out of order and 
	 * occasionally skips numbers.
	 * 
	 * The declarative order of these constants conforms to the 
	 * left-to-right order of the equipment slots, in-game.
	 * 
	 * The index order of these constants conforms to PI's 
	 * playerEquipment[] array.
	 */
	public final static int 
	EQUIPMENT_HEAD   = 0,
	EQUIPMENT_BACK   = 1,
    EQUIPMENT_NECK   = 2,
    EQUIPMENT_AMMO   = 13,
    EQUIPMENT_WEAPON = 3,
    EQUIPMENT_CHEST  = 4,
    EQUIPMENT_SHIELD = 5,
    EQUIPMENT_LEGS   = 7,
    EQUIPMENT_HANDS  = 9,
    EQUIPMENT_FEET   = 10,
    EQUIPMENT_RING   = 12;
	
	/**
	 * Sends the equipment data to the client. Called on login,
	 * or whenever an equipment slot's ID is altered.
	 * 
	 * @param id The ID.
	 * @param amount The amount.
	 * @param slot The slot.
	 */
	public void loadEquipment(int targetSlot, int wearID, int amount) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			
			c.getInstance().playerEquipment[targetSlot] = wearID;
			c.getInstance().playerEquipmentN[targetSlot] = amount;
			
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	/**
	 * Sets the ID of the item in an equipment slot by reloading the slot with the new ID in place.
	 * 
	 * @param slot The slot of the equipment we're setting.
	 * @param id The ID for the item we set.
	 */
	public void setEquipment(int id, int slot) {
		c.getEquipment().loadEquipment(slot, id, c.getEquipment().getAmount(slot));
	}
	
	/**
	 * Returns the ID of the item in an equipment slot by reading directly from the array. 
	 * 
	 * @param slot The slot of the equipment we're getting.
	 * @returns The ID of the item in the slot.
	 */
	public int getID(int slot) {
		return c.getInstance().playerEquipment[slot];
	}
	
	/**
	 * Returns the amount of the item in an equipment slot by reading directly from the array. 
	 * 
	 * @param slot The slot of the equipment we're getting.
	 * @returns The amount of the item in the slot.
	 */
	public int getAmount(int slot) {
		return c.getInstance().playerEquipmentN[slot];
	}
	
	/**
	 * Returns the name of the item in an equipment slot by calling ItemAssistant.
	 * 
	 * @param slot The slot.
	 * @return The name of the item in the slot.
	 */
	public String getName(int slot) {
		return c.getItems().getItemName(c.getEquipment().getID(slot));
	}
	
	/**
	 * Sets the ID of the item on the player's head.
	 * @param id The ID of the item.
	 */
	public void setHead(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_HEAD, id);
	}
	
	/**
	 * Returns the ID of the item on the player's head.
	 * @return The ID of the item.
	 */
	public int getHead() { //lol, from who?
		return c.getEquipment().getID(EQUIPMENT_HEAD);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's head.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's head, false if not.
	 */
	public boolean hasHead(int id) {
		return c.getEquipment().getHead() == id;
	}
	
	
	/**
	 * Sets the ID of the item on the player's back.
	 * @param id The ID of the item.
	 */
	public void setBack(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_BACK, id);
	}
	
	/**
	 * Returns the ID of the item on the player's back.
	 * @return The ID of the item.
	 */
	public int getBack() { //never gonna corner me!
		return c.getEquipment().getID(EQUIPMENT_BACK);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's back.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's back, false if not.
	 */
	public boolean hasBack(int id) {
		return c.getEquipment().getBack() == id;
	}
	
	/**
	 * Sets the ID of the item on the player's neck.
	 * @param id The ID of the item.
	 */
	public void setNeck(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_NECK, id);
	}
	
	/**
	 * Returns the ID of the item on the player's neck.
	 * @return The ID of the item.
	 */
	public int getNeck(){
		return c.getEquipment().getID(EQUIPMENT_NECK);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's neck.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's neck, false if not.
	 */
	public boolean hasNeck(int id) {
		return c.getEquipment().getNeck() == id;
	}
	
	/**
	 * Sets the ID of the item in the player's ammo slot.
	 * @param id The ID of the item.
	 */
	public void setAmmo(int id, int amount) {
		c.getEquipment().setEquipment(EQUIPMENT_AMMO, id);
	}
	
	/**
	 * Returns the ID of the item in the player's ammo slot.
	 * @return The ID of the item.
	 */
	public int getAmmo() {
		return c.getEquipment().getID(EQUIPMENT_AMMO);
	}
	
	/**
	 * Returns true if the item with the specified ID is in the player's ammo slot.
	 * @param id The ID of the item.
	 * @return True if the item is in the player's ammo slot, false if not.
	 */
	public boolean hasAmmo(int id) {
		return c.getEquipment().getAmmo() == id;
	}
	
	/**
	 * Sets the ID of the item in the player's weapon slot.
	 * @param id The ID of the item.
	 */
	public void setWeapon(int id, int amount) {
		c.getEquipment().setEquipment(EQUIPMENT_WEAPON, id);
	}
	
	/**
	 * Returns the ID of the item in the player's weapon slot.
	 * @return The ID of the item.
	 */
	public int getWeapon() {
		return c.getEquipment().getID(EQUIPMENT_WEAPON);
	}
	
	/**
	 * Returns true if the item with the specified ID is in the player's weapon slot.
	 * @param id The ID of the item.
	 * @return True if the item is in the player's weapon slot, false if not.
	 */
	public boolean hasWeapon(int id) {
		return c.getEquipment().getWeapon() == id;
	}
	
	
	/**
	 * Sets the ID of the item on the player's chest.
	 * @param id The ID of the item.
	 */
	public void setChest(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_CHEST, id);
	}
	
	/**
	 * Returns the ID of the item on the player's chest.
	 * @return The ID of the item.
	 */
	public int getChest() {
		return c.getEquipment().getID(EQUIPMENT_CHEST);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's chest.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's chest, false if not.
	 */
	public boolean hasChest(int id) {
		return c.getEquipment().getChest() == id;
	}
	
	/**
	 * Sets the ID of the player's shield.
	 * @param id The ID of the item.
	 */
	public void setShield(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_SHIELD, id);
	}
	
	/**
	 * Returns the ID of the player's shield.
	 * @return The ID of the item.
	 */
	public int getShield() {
		return c.getEquipment().getID(EQUIPMENT_SHIELD);
	}
	
	/**
	 * Returns true if the item with the specified ID is in the player's shield slot.
	 * @param id The ID of the item.
	 * @return True if the item is in the player's shield slot, false if not.
	 */
	public boolean hasShield(int id) {
		return c.getEquipment().getShield() == id;
	}
	
	/**
	 * Sets the ID of the item on the player's legs.
	 * @param id The ID of the item.
	 */
	public void setLegs(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_LEGS, id);
	}
	
	/**
	 * Returns the ID of the item on the player's legs.
	 * @return The ID of the item.
	 */
	public int getLegs() {
		return c.getEquipment().getID(EQUIPMENT_LEGS);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's legs.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's legs, false if not.
	 */
	public boolean hasLegs(int id) {
		return c.getEquipment().getLegs() == id;
	}
	
	/**
	 * Sets the ID of the item on the player's hands.
	 * @param id The ID of the item.
	 */
	public void setHands(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_HANDS, id);
	}
	
	/**
	 * Returns the ID of the item on the player's hands.
	 * @return The ID of the item.
	 */
	public int getHands() {
		return c.getEquipment().getID(EQUIPMENT_HANDS);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's hands.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's hands, false if not.
	 */
	public boolean hasHands(int id) {
		return c.getEquipment().getHands() == id;
	}
	
	/**
	 * Sets the ID of the item on the player's feet.
	 * @param id The ID of the item.
	 */
	public void setFeet(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_FEET, id);
	}
	
	/**
	 * Returns the ID of the item on the player's feet.
	 * @return The ID of the item.
	 */
	public int getFeet() {
		return c.getEquipment().getID(EQUIPMENT_FEET);
	}
	
	/**
	 * Returns true if the item with the specified ID is on the player's feet.
	 * @param id The ID of the item.
	 * @return True if the item is on the player's feet, false if not.
	 */
	public boolean hasFeet(int id) {
		return c.getEquipment().getFeet() == id;
	}
	
	/**
	 * Sets the ID of the player's ring.
	 * @param id The ID of the item.
	 */
	public void setRing(int id) {
		c.getEquipment().setEquipment(EQUIPMENT_RING, id);
	}
	
	/**
	 * Returns the ID of the player's ring.
	 * @return The ID of the item.
	 */
	public int getRing() {
		return c.getEquipment().getID(EQUIPMENT_RING);
	}
	
	/**
	 * Returns true if the item with the specified ID is in the player's ring slot..
	 * @param id The ID of the item.
	 * @return True if the item is in the player's ring slot, false if not.
	 */
	public boolean hasRing(int id) {
		return c.getEquipment().getRing() == id;
	}
	
	/**
	 * An enumerated type containing information about the 
	 * various components of the various barrows sets.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	private enum BarrowsSet {
		/* <brother name>, <head id>, <chest id>, <legs id>, <weapon id> */
		
	    DHAROK ("Dharok", 4716, 4720, 4722, 4718),
		GUTHAN ("Guthan", 4724, 4728, 4730, 4726),
		VERAC  ("Verac",  4753, 4757, 4759, 4755),
		KARIL  ("Karil",  4732, 4736, 4738, 4734),
		TORAG  ("Torag",  4745, 4749, 4751, 4747),
		AHRIM  ("Ahrim",  4708, 4712, 4714, 4710);

		private String brother;
		private int head;
		private int chest;
		private int legs;
		private int weapon;
		
	    BarrowsSet(String brother, int head, int chest, int legs, int weapon) {
	        this.brother = brother;
	        this.head    = head;
	        this.chest   = chest;
	        this.legs    = legs;
	        this.weapon  = weapon;
	    }
	    
		@SuppressWarnings("unused")
		String brother() {
	    	return this.brother;
	    }
	    
	    int headID() {
	    	return this.head;
	    }
	    
	    int chestID() {
	    	return this.chest;
	    }
	    
	    int legsID() {
	    	return this.legs;
	    }
	    
	    int weaponID() {
	    	return this.weapon;
	    }
	    
	    }
	
	/**
	 * Some integer constants representing the various barrows sets.
	 */
	public final static int
	BARROWS_SET_DHAROK = 0,
	BARROWS_SET_GUTHAN = 1,
	BARROWS_SET_VERAC = 2,
	BARROWS_SET_KARIL = 3,
	BARROWS_SET_TORAG = 4,
	BARROWS_SET_AHRIM = 5;
	
	/**
	 * Returns true if the player has all four components of a specific barrows set equipped.
	 * 
	 * @param setID The ID of the barrows set.
	 * @return True if all four components are equipped, false otherwise.
	 */
	private boolean hasBarrowsSet(int setID) {
		BarrowsSet set = null;
		
		switch (setID) {
		case BARROWS_SET_DHAROK:
			set = BarrowsSet.DHAROK;
			break;
		case BARROWS_SET_GUTHAN:
			set = BarrowsSet.GUTHAN;
			break;
		case BARROWS_SET_VERAC:
			set = BarrowsSet.VERAC;
			break;
		case BARROWS_SET_KARIL:
			set = BarrowsSet.KARIL;
			break;
		case BARROWS_SET_TORAG:
			set = BarrowsSet.TORAG;
			break;
		case BARROWS_SET_AHRIM:
			set = BarrowsSet.AHRIM;
			break;
		}
		
		if (c.getEquipment().hasHead  (set.headID())
		 && c.getEquipment().hasChest (set.chestID())
		 && c.getEquipment().hasLegs  (set.legsID())
		 && c.getEquipment().hasWeapon(set.weaponID())) {
				return true;
			} else {
				return false;
			}	
	}
	
	/**
	 * Determines whether or not the player has all four components of Dharok's set equipped.
	 * @return True if all four components are equipped, false if not.
	 */
	public boolean hasDharok() {
		return c.getEquipment().hasBarrowsSet(BARROWS_SET_DHAROK);
	}
	
	/**
	 * Determines whether or not the player has all four components of Guthan's set equipped.
	 * @return True if all four components are equipped, false if not.
	 */
	public boolean hasGuthan() {
		return c.getEquipment().hasBarrowsSet(BARROWS_SET_GUTHAN);
	}
	
	
	/**
	 * Determines whether or not the player has all four components of Verac's set equipped.
	 * @return True if all four components are equipped, false if not.
	 */
	public boolean hasVerac() {
		return c.getEquipment().hasBarrowsSet(BARROWS_SET_VERAC);
	}
	
	/**
	 * Determines whether or not the player has all four components of Karil's set equipped.
	 * @return True if all four components are equipped, false if not.
	 */
	public boolean hasKaril() {
		return c.getEquipment().hasBarrowsSet(BARROWS_SET_KARIL);
	}
	
	/**
	 * Determines whether or not the player has all four components of Torag's set equipped.
	 * @return True if all four components are equipped, false if not.
	 */
	public boolean hasTorag() {
		return c.getEquipment().hasBarrowsSet(BARROWS_SET_TORAG);
	}
	
	/**
	 * Determines whether or not the player has all four components of Ahrim's set equipped.
	 * @return True if all four components are equipped, false if not.
	 */
	public boolean hasAhrim() {
		return c.getEquipment().hasBarrowsSet(BARROWS_SET_AHRIM);
	}
	
	/**
	 * Some integer constants which represent item IDs that are 
	 * always used in all void knight sets.
	 */
	private final static int
	VOID_KNIGHT_CHEST = 8839,
	VOID_KNIGHT_LEGS  = 8840,
	VOID_KNIGHT_HANDS = 8842;
	
	/**
	 * An enumerated type containing information about the 
	 * various components of the various void knight sets.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 */
	private enum VoidKnightSet {
		/* <type>, <head id>, <chest id>, <legs id>, <hands id> */
		
	    MELEE ("Melee", 11665, VOID_KNIGHT_CHEST, VOID_KNIGHT_LEGS, VOID_KNIGHT_HANDS),
		MAGIC ("Magic", 11663, VOID_KNIGHT_CHEST, VOID_KNIGHT_LEGS, VOID_KNIGHT_HANDS),
		RANGE ("Range", 11664, VOID_KNIGHT_CHEST, VOID_KNIGHT_LEGS, VOID_KNIGHT_HANDS);

		private String type;
		private int head;
		private int chest;
		private int legs;
		private int hands;
		
	    VoidKnightSet(String type, int head, int chest, int legs, int hands) {
	        this.type  = type;
	        this.head  = head;
	        this.chest = chest;
	        this.legs  = legs;
	        this.hands = hands;
	    }
	    
		@SuppressWarnings("unused")
		String type() {
	    	return this.type;
	    }
	    
	    int headID() {
	    	return this.head;
	    }
	    
	    int chestID() {
	    	return this.chest;
	    }
	    
	    int legsID() {
	    	return this.legs;
	    }
	    
	    int handsID() {
	    	return this.hands;
	    }
	    
	    }
	
	/**
	 * Some integer constants representing the various void knight sets.
	 */
	private final int
	VOID_KNIGHT_SET_MELEE = 0,
	VOID_KNIGHT_SET_MAGIC = 1,
	VOID_KNIGHT_SET_RANGE = 2;
	
	/**
	 * Returns true if the player has all four components of a specific void knight set equipped.
	 * 
	 * @param setID The ID of the void knight set.
	 * @return True if all four components are equipped, false otherwise.
	 */
	private boolean hasVoidKnightSet(int setID) {
		VoidKnightSet set = null;
		
		switch (setID) {
		case VOID_KNIGHT_SET_MELEE:
			set = VoidKnightSet.MELEE;
			break;
		case VOID_KNIGHT_SET_MAGIC:
			set = VoidKnightSet.MAGIC;
			break;
		case VOID_KNIGHT_SET_RANGE:
			set = VoidKnightSet.RANGE;
			break;
		}
				
		if (c.getEquipment().hasHead (set.headID())
		 && c.getEquipment().hasChest(set.chestID())
		 && c.getEquipment().hasLegs (set.legsID())
		 && c.getEquipment().hasHands(set.handsID())) {
				return true;
			} else {
				return false;
			}	
	}
	
	public boolean hasVoidKnightMelee() {
		return c.getEquipment().hasVoidKnightSet(VOID_KNIGHT_SET_MELEE);
	}
	
	public boolean hasVoidKnightMagic() {
		return c.getEquipment().hasVoidKnightSet(VOID_KNIGHT_SET_MAGIC);
	}
	
	public boolean hasVoidKnightRange() {
		return c.getEquipment().hasVoidKnightSet(VOID_KNIGHT_SET_RANGE);
	}
	
	/**
	 * An enumerated type containing the three types of Nex armor:
	 * Torva,
	 * Pernix,
	 * Virtus
	 * 
	 * that can exist in one of three equipment slots:
	 * EQUIPMENT_HEAD,
	 * EQUIPMENT_CHEST,
	 * EQUIPMENT_LEGS
	 * 
	 * This enum is used a bit different in that its enumerations correspond 
	 * to equipment slots rather than equipment sets, and the values contained 
	 * within said enumeration only correspond to items that reside in that 
	 * particular equipment slot.
	 * 
	 * @author Branon McClellan (KeepBotting)
	 *
	 */
	private enum NexArmor {
		/* <type>, <slot>, <torva id>, <pernix id>, <virtus id>*/
		
	    HEAD  ("Head",  EQUIPMENT_HEAD,  20135, 20147, 20159),
		CHEST ("Chest", EQUIPMENT_CHEST, 20139, 20151, 20163),
		LEGS  ("Legs",  EQUIPMENT_LEGS,  20143, 20155, 20167);

		private String type;
		private int slot;
		private int torva;
		private int pernix;
		private int virtus;
		
	    NexArmor(String type, int slot, int torva, int pernix, int virtus) {
	        this.type  = type;
	        this.slot  = slot;
	        this.torva  = torva;
	        this.pernix = pernix;
	        this.virtus  = virtus;
	    }
	    
		@SuppressWarnings("unused")
		String type() {
	    	return this.type;
	    }
		
		int slot() {
	    	return this.slot;
	    }
	    
	    int torvaID() {
	    	return this.torva;
	    }
	    
	    int pernixID() {
	    	return this.pernix;
	    }
	    
	    int virtusID() {
	    	return this.virtus;
	    }
	    
	    }
	
	/**
	 * Determines whether or not a player is wearing (one of the) three pieces of Nex armor in the given slot.
	 * @param slot The slot.
	 * 
	 * @return True if the player is wearing (one of the) three pieces of Nex armor in the given slot, false otherwise.
	 */
	public boolean hasNexArmor(int slot) {
		NexArmor n = null;
		
		switch (slot) {
		case EQUIPMENT_HEAD:
			n = NexArmor.HEAD;
			break;
		case EQUIPMENT_CHEST:
			n = NexArmor.CHEST;
			break;
		case EQUIPMENT_LEGS:
			n = NexArmor.LEGS;
			break;
		}
		
		if (c.getEquipment().getID(n.slot()) == n.torvaID()
		 || c.getEquipment().getID(n.slot()) == n.pernixID()
		 || c.getEquipment().getID(n.slot()) == n.virtusID()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determines whether or not a player is wearing one of each piece of Nex armor.
	 * @return True if the player is wearing one of each piece of Nex armor false otherwise.
	 */
	public boolean hasNexArmor() {
		return c.getEquipment().hasNexArmor(EQUIPMENT_HEAD) 
			&& c.getEquipment().hasNexArmor(EQUIPMENT_CHEST) 
			&& c.getEquipment().hasNexArmor(EQUIPMENT_LEGS);
	}
}
	
