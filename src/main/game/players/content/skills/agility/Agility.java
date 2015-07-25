package main.game.players.content.skills.agility;

import main.event.CycleEvent;
import main.event.CycleEventContainer;
import main.event.CycleEventHandler;
import main.game.players.Player;
import main.handlers.SkillHandler;

public class Agility {

	/**
	 ** @author Ochroid | Scott
	 **/

	public void obstacle(final Player p, int Emote, int reqAgility, int newX, int newY, final int agilityTimer, int amtEXP,
			String message) {
		if (p.getVariables().playerLevel[16] >= reqAgility) {
			p.getVariables().agilityEmote = true;
			p.walk(newX, newY, Emote);
			p.getVariables();
			p.getPA().addSkillXP(amtEXP, p.getVariables().playerAgility);
			CycleEventHandler.getSingleton().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer c) {
					p.stopEmote();
					stop();
				}

				@Override
				public void stop() {

				}
			}, agilityTimer);
		} else {
			p.sendMessage("You need " + reqAgility + " agility to attempt this obstacle.");
		}
	}

	public static void agilityDelay(final Player p, int Emote, final int X, final int Y, final int H, int reqAgility,
			int amtEXP, String message) {
		if (p.getVariables().playerLevel[16] >= reqAgility) {
			p.startAnimation(Emote);
			p.getVariables().agilityEmote = true;
			p.getVariables();
			p.getPA().addSkillXP(amtEXP, p.getVariables().playerAgility);
			CycleEventHandler.getSingleton().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer c) {
					p.getPA().movePlayer(X, Y, H);
					p.getVariables().agilityEmote = false;
					stop();
				}

				@Override
				public void stop() {

				}
			}, 1);
		} else {
			p.sendMessage("You need " + reqAgility + " agility to attempt this obstacle.");
		}
	}

	private static void agilityWalk(final Player c, final int walkAnimation, final int x, final int y) {
		c.getVariables().isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.getVariables().playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo(x, y);
		c.sendMessage("lewl agy");
	}

	private static void resetAgilityWalk(final Player c) {
		c.getVariables().isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.getVariables().playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
		c.getVariables().doingAgility = false;
	}

	private static final int[] agilityObject = { 2295, 2285, 2286, 2313, 2312, 2314, 2315, 154, 4058, 2282, 2284, 2294,
			2302, 3205, 1948 };

	public static void swing(final Player c) {
		if (c.stopPlayerPacket) {
			return;
		}
		c.stopPlayerPacket = true;

		if (c.absY != 3953) {
			c.getPA().walkTo(c.absX - 3953, 0);
		}

		c.turnPlayerTo(3005, 3958);
		c.startAnimation(751);

		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				c.getPA().walkTo(0, 1);
				container.stop();
			}

			@Override
			public void stop() {
				c.getVariables().barbObstacle = 1;
				c.getVariables();
				c.getPA().addSkillXP(450, c.getVariables().playerAgility);
			}
		}, 2);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				c.teleportToX = 2551;// end
				c.teleportToY = 3549;
				container.stop();
			}

			@Override
			public void stop() {
				resetAgilityWalk(c);
				c.stopPlayerPacket = false;
			}
		}, 3);
	}

	public static boolean agilityObstacle(final Player c, final int objectType) {
		for (int i = 0; i < agilityObject.length; i++) {
			if (objectType == agilityObject[i]) {
				return true;
			}
		}
		return false;
	}

	public static void agilityCourse(final Player c, final int objectType) {
		if (c.getVariables().doingAgility) {
			return;
		}
		switch (objectType) {
		/**
		 * Barbarian Course
		 */

		case 1948: // crumbling wall
			if (c.getVariables().objectX == 2536 && c.absX < 2537) {
				if (c.getVariables().barbObstacle == 5 && c.getVariables().playerLevel[16] >= 30) {
					c.getPA().movePlayer(2537, 3553, 0);
					c.getVariables();
					c.getPA().addSkillXP(175, c.getVariables().playerAgility);
					c.getVariables().barbObstacle = 6;
				} else if (c.getVariables().playerLevel[16] >= 30) {
					c.getPA().movePlayer(2537, 3553, 0);
					c.getVariables();
					c.getPA().addSkillXP(175, c.getVariables().playerAgility);
				} else {
					c.sendMessage("You need 30 Agility to attempt this obstacle");
				}
			} else if (c.getVariables().objectX == 2539 && c.absX < 2540) {
				if (c.getVariables().barbObstacle == 6 && c.getVariables().playerLevel[16] >= 30) {
					c.getPA().movePlayer(2540, 3553, 0);
					c.getVariables();
					c.getPA().addSkillXP(175, c.getVariables().playerAgility);
					c.getVariables().barbObstacle = 7;
				} else if (c.getVariables().playerLevel[16] >= 30) {
					c.getPA().movePlayer(2540, 3553, 0);
					c.getVariables();
					c.getPA().addSkillXP(175, c.getVariables().playerAgility);
				} else {
					c.sendMessage("You need 30 Agility to attempt this obstacle");
				}
			} else if (c.getVariables().objectX == 2542 && c.absX < 2543) {
				if (c.getVariables().barbObstacle == 7 && c.getVariables().playerLevel[16] >= 30) {
					c.getPA().movePlayer(2543, 3553, 0);
					c.getVariables();
					c.getPA().addSkillXP(175, c.getVariables().playerAgility);
					c.getVariables();
					c.getPA().addSkillXP(10000, c.getVariables().playerAgility);
					c.getVariables().barbObstacle = 0;
					return;
				} else if (c.getVariables().playerLevel[16] >= 30) {
					c.getPA().movePlayer(2543, 3553, 0);
					c.getVariables();
					c.getPA().addSkillXP(175, c.getVariables().playerAgility);
				} else {
					c.sendMessage("You need 30 Agility to attempt this obstacle");
				}
			}
			break;

		// Log Balance
		case 2294:
			if (c.absY != 3546 && c.absX != 2551) {
				c.getPA().movePlayer(2551, 3546, 0);
				c.absY = 3546;
				return;
			}
			c.getVariables().doingAgility = true;
			agilityWalk(c, 762, -10, 0);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int cycle = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (c.absX == 2551 && cycle == 2) {
						container.stop();
					}
					if (c.absY != 3546) {
						c.getPA().movePlayer(2551, 3546, 0);
					}
					if (c.absX == 2541 && c.absY == 3546) {
						container.stop();
					}
					cycle++;
				}

				@Override
				public void stop() {
					resetAgilityWalk(c);
					c.getVariables();
					c.getPA().addSkillXP(47 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
					// c.getVariables().playerAgility);
					if (c.getVariables().barbObstacle == 1) {
						c.getVariables().barbObstacle = 2;
					}
					c.getVariables().doingAgility = false;
				}
			}, 1);
			break;

		// Net
		case 2284: // Barbarian Obstacle net
			agilityDelay(c, 828, 2537, 3546, 1, 40, 275, "You climbed the nets succesfully.");
			resetAgilityWalk(c);
			c.getVariables().doingAgility = false;
			if (c.getVariables().barbObstacle == 2) {
				c.getVariables().barbObstacle = 3;
			}
			break;

		// Log Climb Thing XD
		case 2302:
			if (c.absY != 3547 && c.absX != 2536) {
				c.getPA().movePlayer(2536, 3547, 1);
				c.absY = 3547;
				return;
			}
			c.getVariables().doingAgility = true;
			agilityWalk(c, 756, -4, 0);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int cycle = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (c.absX == 2536 && cycle == 2) {
						container.stop();
						return;
					}
					if (c.absY != 3547) {
						c.getPA().movePlayer(2536, 3547, 1);
					}
					if (c.absX == 2532 && c.absY == 3547) {
						container.stop();
					}
					cycle++;
				}

				@Override
				public void stop() {
					resetAgilityWalk(c);
					c.getPA().addSkillXP(48 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
					c.getVariables().doingAgility = false;
					if (c.getVariables().barbObstacle == 3) {
						c.getVariables().barbObstacle = 4;
					}
				}
			}, 1);
			break;

		case 3205: // Ladder
			agilityDelay(c, 827, 2532, 3546, 0, 40, 180, "You climb down.");
			if (c.getVariables().barbObstacle == 4) {
				c.getVariables().barbObstacle = 5;
			}
			break;

		/**
		 * Gnome Course
		 */
		case 2282:
			c.getVariables().objectDistance = 5;
			if (c.absY < 3554) {
				return;
			}
			if (c.absX != 2551) {
				c.getPA().movePlayer(2551, 3554, 0);
				return;
			}
			swing(c);
			break;
		case 2295:
			if (c.absX != 2474) {
				c.getPA().movePlayer(2474, 3436, 0);
				return;
			}
			c.getVariables().doingAgility = true;
			agilityWalk(c, 762, 0, -7);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int cycle = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (c.absY == 3436 && cycle == 2) {
						container.stop();
						return;
					}
					if (c.absX != 2474) {
						c.getPA().movePlayer(2474, 3436, 0);
					}
					if (c.absX == 2474 && c.absY == 3429) {
						container.stop();
					}
					cycle++;
				}

				@Override
				public void stop() {
					resetAgilityWalk(c);
					c.getPA().addSkillXP(11 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
					c.getVariables().logBalance = true;
					c.getVariables().doingAgility = false;
				}
			}, 1);
			break;
		case 2285:
			c.startAnimation(828);
			c.getPA().movePlayer(c.absX, 3424, 1);
			c.getPA().addSkillXP(13 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
			c.getVariables().obstacleNetUp = true;
			break;
		case 2313:
			c.startAnimation(828);
			c.getPA().movePlayer(2473, 3420, 2);
			c.getPA().addSkillXP(12 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
			c.getVariables().treeBranchUp = true;
			break;
		case 2312:
			c.getVariables().doingAgility = true;
			if (c.absX != 2477 || c.absY != 3420) {
				c.getPA().movePlayer(2477, 3420, 2);
				return;
			}
			agilityWalk(c, 762, 6, 0);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int cycle = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (c.absX == 2477 && cycle == 2) {
						container.stop();
						return;
					}
					if (c.absY != 3420) {
						c.getPA().movePlayer(2477, 3420, 2);
					}
					if (c.absX == 2483 && c.absY == 3420) {
						container.stop();
						return;
					}
					cycle++;
				}

				@Override
				public void stop() {
					resetAgilityWalk(c);
					c.getPA().addSkillXP(12 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
					c.getVariables().balanceRope = true;
					c.getVariables().doingAgility = false;
				}
			}, 1);
			break;
		case 2314:
		case 2315:
			c.startAnimation(828);
			c.getPA().movePlayer(c.absX, c.absY, 0);
			c.getPA().addSkillXP(11 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
			c.getVariables().treeBranchDown = true;
			break;
		case 2286:
			if (c.absY > 3426) {
				return;
			}
			c.getVariables().doingAgility = true;
			c.startAnimation(828);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.getPA().movePlayer(c.absX, 3427, 0);
					container.stop();
				}

				@Override
				public void stop() {
					c.turnPlayerTo(c.absX, 3426);
					c.getPA().addSkillXP(13 * SkillHandler.XPRates.AGILITY.getXPRate(), c.getVariables().playerAgility);
					c.getVariables().obstacleNetOver = true;
					c.getVariables().doingAgility = false;
				}
			}, 1);
			break;
		case 154:
			if (c.absY > 3432) {
				return;
			}
			c.getVariables().doingAgility = true;
			if (c.absX != 2484 && c.absY != 3430) {
				c.getPA().movePlayer(2484, 3430, 0);
				return;
			}
			c.startAnimation(749);
			agilityWalk(c, 844, 0, 7);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int cycle = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (c.absY == 3430 && cycle == 2) {
						container.stop();
						return;
					}
					if (c.absY == 3437) {
						container.stop();
					}
					cycle++;
				}

				@Override
				public void stop() {
					c.startAnimation(748);
					resetAgilityWalk(c);
					if (c.absY != 3430) {
						c.getVariables().logBalance = false;
						c.getVariables().obstacleNetUp = false;
						c.getVariables().treeBranchUp = false;
						c.getVariables().balanceRope = false;
						c.getVariables().treeBranchDown = false;
						c.getVariables().obstacleNetOver = false;
						c.getVariables().doingAgility = false;
						if (c.getVariables().logBalance && c.getVariables().obstacleNetUp
								&& c.getVariables().treeBranchUp && c.getVariables().balanceRope
								&& c.getVariables().treeBranchDown && c.getVariables().obstacleNetOver) {
							c.getPA().addSkillXP(50 * SkillHandler.XPRates.AGILITY.getXPRate(),
									c.getVariables().playerAgility);
							c.sendMessage("You have completed the full gnome agility course.");
							return;
						} else {
							c.getPA().addSkillXP(22 * SkillHandler.XPRates.AGILITY.getXPRate(),
									c.getVariables().playerAgility);
						}
					}
				}
			}, 1);
			break;
		case 4058:
			if (c.absY > 3432) {
				return;
			}
			if (c.absX != 2487 && c.absY != 3430) {
				c.getPA().movePlayer(2487, 3430, 0);
				return;
			}
			c.startAnimation(749);
			agilityWalk(c, 844, 0, 7);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int cycle = 0;

				@Override
				public void execute(CycleEventContainer container) {
					if (c.absY == 3430 && cycle == 2) {
						container.stop();
						return;
					}
					if (c.absY == 3437) {
						container.stop();
					}
					cycle++;
				}

				@Override
				public void stop() {
					c.startAnimation(748);
					resetAgilityWalk(c);
					if (c.absY != 3430) {
						c.getVariables().logBalance = false;
						c.getVariables().obstacleNetUp = false;
						c.getVariables().treeBranchUp = false;
						c.getVariables().balanceRope = false;
						c.getVariables().treeBranchDown = false;
						c.getVariables().obstacleNetOver = false;
						if (c.getVariables().logBalance && c.getVariables().obstacleNetUp
								&& c.getVariables().treeBranchUp && c.getVariables().balanceRope
								&& c.getVariables().treeBranchDown && c.getVariables().obstacleNetOver) {
							c.getPA().addSkillXP(37 * SkillHandler.XPRates.AGILITY.getXPRate(),
									c.getVariables().playerAgility);
							c.sendMessage("You have completed the full gnome agility course.");
							return;
						} else {
							c.getPA().addSkillXP(22 * SkillHandler.XPRates.AGILITY.getXPRate(),
									c.getVariables().playerAgility);
						}
					}
				}
			}, 1);
			break;
		}
	}
}