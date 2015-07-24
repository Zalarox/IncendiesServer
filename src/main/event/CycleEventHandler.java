package main.event;

import java.util.ArrayList;
import java.util.List;

import main.GameEngine;

/**
 * Handles all of our cycle based events
 * 
 * @author Stuart <RogueX>
 * 
 */
public class CycleEventHandler {

	/**
	 * The instance of this class
	 */
	private static CycleEventHandler instance;

	/**
	 * Returns the instance of this class
	 * 
	 * @return
	 */
	public static CycleEventHandler getInstance() {
		if (instance == null) {
			instance = new CycleEventHandler();
		}
		return instance;
	}

	/**
	 * Thock321: Added this so I didn't need to change getSingleton to
	 * getInstance when I copy and pasted
	 */
	public static CycleEventHandler getSingleton() {
		return getInstance();
	}

	/**
	 * Holds all of our events currently being ran
	 */
	private List<CycleEventContainer> events;

	/**
	 * Creates a new instance of this class
	 */
	public CycleEventHandler() {
		this.events = new ArrayList<CycleEventContainer>();
	}

	/**
	 * Add an event to the list
	 * 
	 * @param owner
	 * @param event
	 * @param cycles
	 */
	public void addEvent(Object owner, CycleEvent event, int cycles) {
		this.events.add(new CycleEventContainer(owner, event, cycles));
	}

	/**
	 * Add an event with no specific owner (uses Server as the owner)
	 */
	public void addEvent(CycleEvent event, int cycles) {
		addEvent(GameEngine.class, event, cycles);
	}

	/**
	 * Execute and remove events
	 */
	public void process() {
		List<CycleEventContainer> eventsCopy = new ArrayList<CycleEventContainer>(events);
		List<CycleEventContainer> remove = new ArrayList<CycleEventContainer>();
		for (CycleEventContainer c : eventsCopy) {
			if (c != null) {
				if (c.needsExecution() && c.isRunning())
					c.execute();
				if (!c.isRunning()) {
					remove.add(c);
				}
			}
		}
		for (CycleEventContainer c : remove) {
			events.remove(c);
		}
	}

	/**
	 * Stops all events which are being ran by the given owner
	 * 
	 * @param owner
	 */
	public void stopEvents(Object owner) {
		for (CycleEventContainer c : events) {
			if (c.getOwner() == owner) {
				c.forceStop();
			}
		}
	}

	public void stopEvent(CycleEvent e) {
		for (CycleEventContainer c : events) {
			if (c.getEvent() == e) {
				c.forceStop();
			}
		}
	}

}