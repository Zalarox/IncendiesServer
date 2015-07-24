package main.game.players.content.skills.construction;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;

/**
 * 
 * @author TheLife
 *
 */

public class Process {

	public boolean stopEvent = false;

	public void registerEvent(final Player c) {
		stopEvent = false;
		CycleEventHandler.getInstance().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (c != null) {
					// if (c != null || c.disconnected) {
					if (c.CD.INTERFACE_TIMER_V1 != 0) {
						c.CD.INTERFACE_TIMER_V1--;
						if (c.CD.INTERFACE_TIMER_V1 == 0 && c.CD.LOADED == false) {
							c.OL.loadObjects(c);
							c.CD.LOADED = true;
							stopEvent = true;
						}
					}
					if (c.CD.INTERFACE_TIMER_V1 != 0) {
						c.CD.INTERFACE_TIMER_V1--;
					}
					if (c.CD.INTERFACE_TIMER_V3 != 0) {
						c.CD.INTERFACE_TIMER_V3--;
					}
					if (c.CD.INTERFACE_TIMER_V2 == 0 && c.CD.INTERFACE_LOADED_V2 == false) {
						c.CD.INTERFACE_LOADED_V2 = true;
						c.EH.EnterTheHouse(c, "BUILDING_MODE");
						stopEvent = true;
					}
					if (c.CD.INTERFACE_TIMER_V1 == 0 && c.CD.INTERFACE_LOADED_V1 == false) {
						c.getPA().closeAllWindows();
						c.CD.INTERFACE_LOADED_V1 = true;
						stopEvent = true;
					}
					if (c.CD.INTERFACE_TIMER_V2 != 0) {
						c.CD.INTERFACE_TIMER_V2--;
						if (c.CD.INTERFACE_TIMER_V2 == 0 && c.CD.LOADED == false) {
							c.OL.loadObjectsOther(c);
							c.CD.LOADED = true;
							if (c.CD.IN_BUILDING_MODE == true) {
								c.KP.kickFromHouse(c, "REGULAR");
							}
							stopEvent = true;
						}
					}
				} else {
					container.stop();
				}
			}

			@Override
			public void stop() {

			}
		}, 1);
	}

}
