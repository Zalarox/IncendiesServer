package main.game.players.content.skills.summoning;

import java.util.HashMap;

import main.GameEngine;
import main.game.npcs.NPC;
import main.game.npcs.NPCHandler;
import main.game.npcs.data.SummoningData;
import main.game.players.Player;
import main.game.players.PlayerHandler;
import main.handlers.ItemHandler;
import main.world.map.Region;

/**
 * The Summoning Handler class.
 * 
 * @Author: Mr-Nick
 */
public class Summoning {

	/**
	 * Owner of the summoning instance.
	 */
	public final Player c;

	/**
	 * The owner's summoned familiar.
	 */
	public Familiar summonedFamiliar = null;

	/**
	 * Instances the summoning class.
	 */
	public Summoning(Player p) {
		this.c = p;
	}

	/**
	 * Represents different FamiliarType's.
	 * 
	 * @Author: Mr Nick
	 */
	public static enum FamiliarType {
		NORMAL, COMBAT, BOB;
	}

	/**
	 * Familiar special interface.
	 */
	public static interface FamiliarSpecial {
		public abstract void execute(Object... arguements);
	}

	/**
	 * Represents different Familiar's.
	 * 
	 * @Author: Mr-Nick
	 */
	public static enum Familiar {
		Spirit_Wolf(6829, 12047, 12425, 15, FamiliarType.NORMAL, 0, 40, false, "Howl", "", new FamiliarSpecial() {
			@Override
			public void execute(Object... arguments) {
				if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 3) {
					((Player) arguments[0]).sendMessage("This special attack is currently unvailable.");
				} else {
					((Player) arguments[0]).sendMessage(
							"Your familiar has " + ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
									+ " special energy left and needs 3..");
				}
			}
		}), Spirit_Spider(6841, 12059, 12428, 15, FamiliarType.NORMAL, 0, 40, false, "Egg spawn", "",
				new FamiliarSpecial() {
					@Override
					public void execute(Object... arguments) {
						if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 5) {
							((Player) arguments[0]).sendMessage("This special attack is currently unvailable.");
						} else {
							((Player) arguments[0]).sendMessage(
									"Your familiar has " + ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
											+ " special energy left and needs 5..");
						}
					}
				}), Thorny_Snail(6806, 12019, 12459, 15, FamiliarType.BOB, 3, 40, false, "Slime Spray", "",
						new FamiliarSpecial() {
							@Override
							public void execute(Object... arguments) {
								if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 4) {
									((Player) arguments[0]).sendMessage("This special attack is currently unvailable.");
								} else {
									((Player) arguments[0]).sendMessage("Your familiar has "
											+ ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
											+ " special energy left and needs 4..");
								}
							}
						}),

		/**
		 * HAS IDS ANIMS ++ ABOVE
		 */

		Granite_Crab(6796, 12009, 12533, 15, FamiliarType.NORMAL, 0, 40, false, "Stony Shell", "",
				new FamiliarSpecial() {
					@Override
					public void execute(Object... arguments) {
						if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 2) {
							((Player) arguments[0]).sendMessage("This special attack is currently unvailable.");
						} else {
							((Player) arguments[0]).sendMessage(
									"Your familiar has " + ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
											+ " special energy left and needs 2..");
						}
					}
				}), Spirit_Mosquito(7331, 12778, 12838, 15, FamiliarType.NORMAL, 0, 40, false, "Pester", "",
						new FamiliarSpecial() {
							@Override
							public void execute(Object... arguments) {
								if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 5) {
									((Player) arguments[0]).sendMessage("This special attack is currently unvailable.");
								} else {
									((Player) arguments[0]).sendMessage("Your familiar has "
											+ ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
											+ " special energy left and needs 5..");
								}
							}
						}), Desert_Wyrm(6831, 12049, 12460, 15, FamiliarType.NORMAL, 0, 40, false, "Electric lash", "",
								new FamiliarSpecial() {
									@Override
									public void execute(Object... arguments) {
										if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 6) {
											((Player) arguments[0])
													.sendMessage("This special attack is currently unvailable.");
										} else {
											((Player) arguments[0])
													.sendMessage("Your familiar has "
															+ ((Player) arguments[0])
																	.getSummoning().familiarSpecialEnergy
															+ " special energy left and needs 6..");
										}
									}
								}), Spirit_Scorpion(6837, 12055, 12432, 15, FamiliarType.NORMAL, 0, 40, false,
										"Venom shot", "", new FamiliarSpecial() {
											@Override
											public void execute(Object... arguments) {
												if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 6) {
													((Player) arguments[0]).sendMessage(
															"This special attack is currently unvailable.");
												} else {
													((Player) arguments[0]).sendMessage("Your familiar has "
															+ ((Player) arguments[0])
																	.getSummoning().familiarSpecialEnergy
															+ " special energy left and needs 6..");
												}
											}
										}), Spirit_Tz_Kih(7361, 12808, 12839, 15, FamiliarType.NORMAL, 0, 40, false,
												"Fireball assault", "", new FamiliarSpecial() {
													@Override
													public void execute(Object... arguments) {
														if (((Player) arguments[0])
																.getSummoning().familiarSpecialEnergy >= 7) {
															((Player) arguments[0]).sendMessage(
																	"This special attack is currently unvailable.");
														} else {
															((Player) arguments[0]).sendMessage("Your familiar has "
																	+ ((Player) arguments[0])
																			.getSummoning().familiarSpecialEnergy
																	+ " special energy left and needs 7..");
														}
													}
												}), Compost_Mound(6871, 12091, 12440, 15, FamiliarType.NORMAL, 0, 40,
														false, "Generate compost", "", new FamiliarSpecial() {
															@Override
															public void execute(Object... arguments) {
																if (((Player) arguments[0])
																		.getSummoning().familiarSpecialEnergy >= 8) {
																	((Player) arguments[0]).sendMessage(
																			"This special attack is currently unvailable.");
																} else {
																	((Player) arguments[0])
																			.sendMessage("Your familiar has "
																					+ ((Player) arguments[0])
																							.getSummoning().familiarSpecialEnergy
																					+ " special energy left and needs 8..");
																}
															}
														}), Vampire_Bat(6835, 12053, 12447, 15, FamiliarType.NORMAL, 0,
																40, false, "Vampyre touch", "", new FamiliarSpecial() {
																	@Override
																	public void execute(Object... arguments) {
																		if (((Player) arguments[0])
																				.getSummoning().familiarSpecialEnergy >= 9) {
																			((Player) arguments[0]).sendMessage(
																					"This special attack is currently unvailable.");
																		} else {
																			((Player) arguments[0])
																					.sendMessage("Your familiar has "
																							+ ((Player) arguments[0])
																									.getSummoning().familiarSpecialEnergy
																							+ " special energy left and needs 9..");
																		}
																	}
																}), Honey_Badger(6845, 12065, 12433, 15,
																		FamiliarType.NORMAL, 0, 40, false,
																		"Insane ferocity", "", new FamiliarSpecial() {
																			@Override
																			public void execute(Object... arguments) {
																				if (((Player) arguments[0])
																						.getSummoning().familiarSpecialEnergy >= 9) {
																					((Player) arguments[0]).sendMessage(
																							"This special attack is currently unvailable.");
																				} else {
																					((Player) arguments[0]).sendMessage(
																							"Your familiar has "
																									+ ((Player) arguments[0])
																											.getSummoning().familiarSpecialEnergy
																									+ " special energy left and needs 9..");
																				}
																			}
																		}), Beaver(6807, 12021, 12429, 15,
																				FamiliarType.NORMAL, 0, 40, false,
																				"Multichop", "", new FamiliarSpecial() {
																					@Override
																					public void execute(
																							Object... arguments) {
																						if (((Player) arguments[0])
																								.getSummoning().familiarSpecialEnergy >= 3) {
																							((Player) arguments[0])
																									.sendMessage(
																											"This special attack is currently unvailable.");
																						} else {
																							((Player) arguments[0])
																									.sendMessage(
																											"Your familiar has "
																													+ ((Player) arguments[0])
																															.getSummoning().familiarSpecialEnergy
																													+ " special energy left and needs 3..");
																						}
																					}
																				}), Void_Shifter(7367, 12814, 12443, 15,
																						FamiliarType.NORMAL, 0, 40,
																						false, "Call to Arms", "",
																						new FamiliarSpecial() {
																							@Override
																							public void execute(
																									Object... arguments) {
																								if (((Player) arguments[0])
																										.getSummoning().familiarSpecialEnergy >= 5) {
																									((Player) arguments[0])
																											.sendMessage(
																													"This special attack is currently unvailable.");
																								} else {
																									((Player) arguments[0])
																											.sendMessage(
																													"Your familiar has "
																															+ ((Player) arguments[0])
																																	.getSummoning().familiarSpecialEnergy
																															+ " special energy left and needs 5..");
																								}
																							}
																						}), Void_Torcher(7351, 12798,
																								12443, 15,
																								FamiliarType.NORMAL, 0,
																								40, false,
																								"Call to Arms", "",
																								new FamiliarSpecial() {
																									@Override
																									public void execute(
																											Object... arguments) {
																										if (((Player) arguments[0])
																												.getSummoning().familiarSpecialEnergy >= 5) {
																											((Player) arguments[0])
																													.sendMessage(
																															"This special attack is currently unvailable.");
																										} else {
																											((Player) arguments[0])
																													.sendMessage(
																															"Your familiar has "
																																	+ ((Player) arguments[0])
																																			.getSummoning().familiarSpecialEnergy
																																	+ " special energy left and needs 5..");
																										}
																									}
																								}), Void_Spinner(7333,
																										12780, 12443,
																										15,
																										FamiliarType.NORMAL,
																										0, 40, false,
																										"Call To Arms",
																										"",
																										new FamiliarSpecial() {
																											@Override
																											public void execute(
																													Object... arguments) {
																												if (((Player) arguments[0])
																														.getSummoning().familiarSpecialEnergy >= 5) {
																													((Player) arguments[0])
																															.sendMessage(
																																	"This special attack is currently unvailable.");
																												} else {
																													((Player) arguments[0])
																															.sendMessage(
																																	"Your familiar has "
																																			+ ((Player) arguments[0])
																																					.getSummoning().familiarSpecialEnergy
																																			+ " special energy left and needs 5..");
																												}
																											}
																										}), Bull_Ant(
																												6867,
																												12087,
																												12431,
																												15,
																												FamiliarType.BOB,
																												9, 40,
																												false,
																												"Unburden",
																												"",
																												new FamiliarSpecial() {
																													@Override
																													public void execute(
																															Object... arguments) {
																														if (((Player) arguments[0])
																																.getSummoning().familiarSpecialEnergy >= 10) {
																															((Player) arguments[0])
																																	.sendMessage(
																																			"This special attack is currently unvailable.");
																														} else {
																															((Player) arguments[0])
																																	.sendMessage(
																																			"Your familiar has "
																																					+ ((Player) arguments[0])
																																							.getSummoning().familiarSpecialEnergy
																																					+ " special energy left and needs 10..");
																														}
																													}
																												}), Evil_Turnip(
																														6833,
																														12051,
																														12448,
																														15,
																														FamiliarType.NORMAL,
																														0,
																														40,
																														false,
																														"Evil flames",
																														"",
																														new FamiliarSpecial() {
																															@Override
																															public void execute(
																																	Object... arguments) {
																																if (((Player) arguments[0])
																																		.getSummoning().familiarSpecialEnergy >= 10) {
																																	((Player) arguments[0])
																																			.sendMessage(
																																					"This special attack is currently unvailable.");
																																} else {
																																	((Player) arguments[0])
																																			.sendMessage(
																																					"Your familiar has "
																																							+ ((Player) arguments[0])
																																									.getSummoning().familiarSpecialEnergy
																																							+ " special energy left and needs 10..");
																																}
																															}
																														}), Spirit_Cockatrice(
																																6875,
																																12095,
																																12458,
																																15,
																																FamiliarType.NORMAL,
																																0,
																																40,
																																false,
																																"Petrifying Gaze",
																																"",
																																new FamiliarSpecial() {
																																	@Override
																																	public void execute(
																																			Object... arguments) {
																																		if (((Player) arguments[0])
																																				.getSummoning().familiarSpecialEnergy >= 7) {
																																			((Player) arguments[0])
																																					.sendMessage(
																																							"This special attack is currently unvailable.");
																																		} else {
																																			((Player) arguments[0])
																																					.sendMessage(
																																							"Your familiar has "
																																									+ ((Player) arguments[0])
																																											.getSummoning().familiarSpecialEnergy
																																									+ " special energy left and needs 7..");
																																		}
																																	}
																																}), Spirit_Guthatrice(
																																		6877,
																																		12097,
																																		12458,
																																		15,
																																		FamiliarType.NORMAL,
																																		0,
																																		40,
																																		false,
																																		"Petrifying Gaze",
																																		"",
																																		new FamiliarSpecial() {
																																			@Override
																																			public void execute(
																																					Object... arguments) {
																																				if (((Player) arguments[0])
																																						.getSummoning().familiarSpecialEnergy >= 7) {
																																					((Player) arguments[0])
																																							.sendMessage(
																																									"This special attack is currently unvailable.");
																																				} else {
																																					((Player) arguments[0])
																																							.sendMessage(
																																									"Your familiar has "
																																											+ ((Player) arguments[0])
																																													.getSummoning().familiarSpecialEnergy
																																											+ " special energy left and needs 7..");
																																				}
																																			}
																																		}), Spirit_Saratrice(
																																				6879,
																																				12099,
																																				12458,
																																				15,
																																				FamiliarType.NORMAL,
																																				0,
																																				40,
																																				false,
																																				"Petrifying Gaze",
																																				"",
																																				new FamiliarSpecial() {
																																					@Override
																																					public void execute(
																																							Object... arguments) {
																																						if (((Player) arguments[0])
																																								.getSummoning().familiarSpecialEnergy >= 7) {
																																							((Player) arguments[0])
																																									.sendMessage(
																																											"This special attack is currently unvailable.");
																																						} else {
																																							((Player) arguments[0])
																																									.sendMessage(
																																											"Your familiar has "
																																													+ ((Player) arguments[0])
																																															.getSummoning().familiarSpecialEnergy
																																													+ " special energy left and needs 7..");
																																						}
																																					}
																																				}), Spirit_Zamatrice(
																																						6881,
																																						12101,
																																						12458,
																																						15,
																																						FamiliarType.NORMAL,
																																						0,
																																						40,
																																						false,
																																						"Petrifying Gaze",
																																						"",
																																						new FamiliarSpecial() {
																																							@Override
																																							public void execute(
																																									Object... arguments) {
																																								if (((Player) arguments[0])
																																										.getSummoning().familiarSpecialEnergy >= 7) {
																																									((Player) arguments[0])
																																											.sendMessage(
																																													"This special attack is currently unvailable.");
																																								} else {
																																									((Player) arguments[0])
																																											.sendMessage(
																																													"Your familiar has "
																																															+ ((Player) arguments[0])
																																																	.getSummoning().familiarSpecialEnergy
																																															+ " special energy left and needs 7..");
																																								}
																																							}
																																						}), Spirit_Pengatrice(
																																								6883,
																																								12103,
																																								12458,
																																								15,
																																								FamiliarType.NORMAL,
																																								0,
																																								40,
																																								false,
																																								"Petrifying Gaze",
																																								"",
																																								new FamiliarSpecial() {
																																									@Override
																																									public void execute(
																																											Object... arguments) {
																																										if (((Player) arguments[0])
																																												.getSummoning().familiarSpecialEnergy >= 7) {
																																											((Player) arguments[0])
																																													.sendMessage(
																																															"This special attack is currently unvailable.");
																																										} else {
																																											((Player) arguments[0])
																																													.sendMessage(
																																															"Your familiar has "
																																																	+ ((Player) arguments[0])
																																																			.getSummoning().familiarSpecialEnergy
																																																	+ " special energy left and needs 7..");
																																										}
																																									}
																																								}), Spirit_Coraxatrice(
																																										6885,
																																										12105,
																																										12458,
																																										15,
																																										FamiliarType.NORMAL,
																																										0,
																																										40,
																																										false,
																																										"Petrifying Gaze",
																																										"",
																																										new FamiliarSpecial() {
																																											@Override
																																											public void execute(
																																													Object... arguments) {
																																												if (((Player) arguments[0])
																																														.getSummoning().familiarSpecialEnergy >= 7) {
																																													((Player) arguments[0])
																																															.sendMessage(
																																																	"This special attack is currently unvailable.");
																																												} else {
																																													((Player) arguments[0])
																																															.sendMessage(
																																																	"Your familiar has "
																																																			+ ((Player) arguments[0])
																																																					.getSummoning().familiarSpecialEnergy
																																																			+ " special energy left and needs 7..");
																																												}
																																											}
																																										}), Spirit_Vulatrice(
																																												6887,
																																												12107,
																																												12458,
																																												15,
																																												FamiliarType.NORMAL,
																																												0,
																																												40,
																																												false,
																																												"Petrifying Gaze",
																																												"",
																																												new FamiliarSpecial() {
																																													@Override
																																													public void execute(
																																															Object... arguments) {
																																														if (((Player) arguments[0])
																																																.getSummoning().familiarSpecialEnergy >= 7) {
																																															((Player) arguments[0])
																																																	.sendMessage(
																																																			"This special attack is currently unvailable.");
																																														} else {
																																															((Player) arguments[0])
																																																	.sendMessage(
																																																			"Your familiar has "
																																																					+ ((Player) arguments[0])
																																																							.getSummoning().familiarSpecialEnergy
																																																					+ " special energy left and needs 7..");
																																														}
																																													}
																																												}), Bloated_Leech(
																																														6843,
																																														12061,
																																														12444,
																																														15,
																																														FamiliarType.NORMAL,
																																														0,
																																														40,
																																														false,
																																														"Blood Drain",
																																														"",
																																														new FamiliarSpecial() {
																																															@Override
																																															public void execute(
																																																	Object... arguments) {
																																																if (((Player) arguments[0])
																																																		.getSummoning().familiarSpecialEnergy >= 11) {
																																																	((Player) arguments[0])
																																																			.sendMessage(
																																																					"This special attack is currently unvailable.");
																																																} else {
																																																	((Player) arguments[0])
																																																			.sendMessage(
																																																					"Your familiar has "
																																																							+ ((Player) arguments[0])
																																																									.getSummoning().familiarSpecialEnergy
																																																							+ " special energy left and needs 11..");
																																																}
																																															}
																																														}), Abyssal_Parasite(
																																																6818,
																																																12035,
																																																12454,
																																																15,
																																																FamiliarType.BOB,
																																																3,
																																																40,
																																																false,
																																																"Abyssal Drain",
																																																"",
																																																new FamiliarSpecial() {
																																																	@Override
																																																	public void execute(
																																																			Object... arguments) {
																																																		if (((Player) arguments[0])
																																																				.getSummoning().familiarSpecialEnergy >= 8) {
																																																			((Player) arguments[0])
																																																					.sendMessage(
																																																							"This special attack is currently unvailable.");
																																																		} else {
																																																			((Player) arguments[0])
																																																					.sendMessage(
																																																							"Your familiar has "
																																																									+ ((Player) arguments[0])
																																																											.getSummoning().familiarSpecialEnergy
																																																									+ " special energy left and needs 8..");
																																																		}
																																																	}
																																																}), Spirit_Jelly(
																																																		6992,
																																																		12037,
																																																		12453,
																																																		15,
																																																		FamiliarType.NORMAL,
																																																		0,
																																																		40,
																																																		false,
																																																		"Dissolve",
																																																		"",
																																																		new FamiliarSpecial() {
																																																			@Override
																																																			public void execute(
																																																					Object... arguments) {
																																																				if (((Player) arguments[0])
																																																						.getSummoning().familiarSpecialEnergy >= 4) {
																																																					((Player) arguments[0])
																																																							.sendMessage(
																																																									"This special attack is currently unvailable.");
																																																				} else {
																																																					((Player) arguments[0])
																																																							.sendMessage(
																																																									"Your familiar has "
																																																											+ ((Player) arguments[0])
																																																													.getSummoning().familiarSpecialEnergy
																																																											+ " special energy left and needs 4..");
																																																				}
																																																			}
																																																		}), Ibis(
																																																				6990,
																																																				12531,
																																																				12424,
																																																				15,
																																																				FamiliarType.NORMAL,
																																																				0,
																																																				40,
																																																				false,
																																																				"Fish Rain",
																																																				"",
																																																				new FamiliarSpecial() {
																																																					@Override
																																																					public void execute(
																																																							Object... arguments) {
																																																						if (((Player) arguments[0])
																																																								.getSummoning().familiarSpecialEnergy >= 3) {
																																																							((Player) arguments[0])
																																																									.sendMessage(
																																																											"This special attack is currently unvailable.");
																																																						} else {
																																																							((Player) arguments[0])
																																																									.sendMessage(
																																																											"Your familiar has "
																																																													+ ((Player) arguments[0])
																																																															.getSummoning().familiarSpecialEnergy
																																																													+ " special energy left and needs 3..");
																																																						}
																																																					}
																																																				}), Spirit_Graahk(
																																																						7363,
																																																						12810,
																																																						12835,
																																																						15,
																																																						FamiliarType.NORMAL,
																																																						0,
																																																						40,
																																																						false,
																																																						"Goad",
																																																						"",
																																																						new FamiliarSpecial() {
																																																							@Override
																																																							public void execute(
																																																									Object... arguments) {
																																																								if (((Player) arguments[0])
																																																										.getSummoning().familiarSpecialEnergy >= 10) {
																																																									((Player) arguments[0])
																																																											.sendMessage(
																																																													"This special attack is currently unvailable.");
																																																								} else {
																																																									((Player) arguments[0])
																																																											.sendMessage(
																																																													"Your familiar has "
																																																															+ ((Player) arguments[0])
																																																																	.getSummoning().familiarSpecialEnergy
																																																															+ " special energy left and needs 10..");
																																																								}
																																																							}
																																																						}), Spirit_Kyatt(
																																																								7365,
																																																								12812,
																																																								12836,
																																																								15,
																																																								FamiliarType.NORMAL,
																																																								0,
																																																								40,
																																																								false,
																																																								"Ambush",
																																																								"",
																																																								new FamiliarSpecial() {
																																																									@Override
																																																									public void execute(
																																																											Object... arguments) {
																																																										if (((Player) arguments[0])
																																																												.getSummoning().familiarSpecialEnergy >= 10) {
																																																											((Player) arguments[0])
																																																													.sendMessage(
																																																															"This special attack is currently unvailable.");
																																																										} else {
																																																											((Player) arguments[0])
																																																													.sendMessage(
																																																															"Your familiar has "
																																																																	+ ((Player) arguments[0])
																																																																			.getSummoning().familiarSpecialEnergy
																																																																	+ " special energy left and needs 10..");
																																																										}
																																																									}
																																																								}), Spirit_Larupia(
																																																										7337,
																																																										12784,
																																																										12840,
																																																										15,
																																																										FamiliarType.NORMAL,
																																																										0,
																																																										40,
																																																										false,
																																																										"Rending",
																																																										"",
																																																										new FamiliarSpecial() {
																																																											@Override
																																																											public void execute(
																																																													Object... arguments) {
																																																												if (((Player) arguments[0])
																																																														.getSummoning().familiarSpecialEnergy >= 10) {
																																																													((Player) arguments[0])
																																																															.sendMessage(
																																																																	"This special attack is currently unvailable.");
																																																												} else {
																																																													((Player) arguments[0])
																																																															.sendMessage(
																																																																	"Your familiar has "
																																																																			+ ((Player) arguments[0])
																																																																					.getSummoning().familiarSpecialEnergy
																																																																			+ " special energy left and needs 10..");
																																																												}
																																																											}
																																																										}), Karamthulhu_Overlord(
																																																												6809,
																																																												12023,
																																																												12455,
																																																												15,
																																																												FamiliarType.NORMAL,
																																																												0,
																																																												40,
																																																												false,
																																																												"Doomsphere",
																																																												"",
																																																												new FamiliarSpecial() {
																																																													@Override
																																																													public void execute(
																																																															Object... arguments) {
																																																														if (((Player) arguments[0])
																																																																.getSummoning().familiarSpecialEnergy >= 7) {
																																																															((Player) arguments[0])
																																																																	.sendMessage(
																																																																			"This special attack is currently unvailable.");
																																																														} else {
																																																															((Player) arguments[0])
																																																																	.sendMessage(
																																																																			"Your familiar has "
																																																																					+ ((Player) arguments[0])
																																																																							.getSummoning().familiarSpecialEnergy
																																																																					+ " special energy left and needs 7..");
																																																														}
																																																													}
																																																												}), Smoke_Devil(
																																																														6865,
																																																														12085,
																																																														12468,
																																																														15,
																																																														FamiliarType.NORMAL,
																																																														0,
																																																														40,
																																																														false,
																																																														"Dust cloud",
																																																														"",
																																																														new FamiliarSpecial() {
																																																															@Override
																																																															public void execute(
																																																																	Object... arguments) {
																																																																if (((Player) arguments[0])
																																																																		.getSummoning().familiarSpecialEnergy >= 10) {
																																																																	((Player) arguments[0])
																																																																			.sendMessage(
																																																																					"This special attack is currently unvailable.");
																																																																} else {
																																																																	((Player) arguments[0])
																																																																			.sendMessage(
																																																																					"Your familiar has "
																																																																							+ ((Player) arguments[0])
																																																																									.getSummoning().familiarSpecialEnergy
																																																																							+ " special energy left and needs 10..");
																																																																}
																																																															}
																																																														}), Stranger_Plant(
																																																																6827,
																																																																12045,
																																																																12467,
																																																																15,
																																																																FamiliarType.NORMAL,
																																																																0,
																																																																40,
																																																																false,
																																																																"Poisonous blast",
																																																																"",
																																																																new FamiliarSpecial() {
																																																																	@Override
																																																																	public void execute(
																																																																			Object... arguments) {
																																																																		if (((Player) arguments[0])
																																																																				.getSummoning().familiarSpecialEnergy >= 15) {
																																																																			((Player) arguments[0])
																																																																					.sendMessage(
																																																																							"This special attack is currently unvailable.");
																																																																		} else {
																																																																			((Player) arguments[0])
																																																																					.sendMessage(
																																																																							"Your familiar has "
																																																																									+ ((Player) arguments[0])
																																																																											.getSummoning().familiarSpecialEnergy
																																																																									+ " special energy left and needs 10..");
																																																																		}
																																																																	}
																																																																}), Fruit_Bat(
																																																																		6816,
																																																																		12033,
																																																																		12423,
																																																																		15,
																																																																		FamiliarType.NORMAL,
																																																																		0,
																																																																		40,
																																																																		false,
																																																																		"Fruitfall",
																																																																		"",
																																																																		new FamiliarSpecial() {
																																																																			@Override
																																																																			public void execute(
																																																																					Object... arguments) {
																																																																				if (((Player) arguments[0])
																																																																						.getSummoning().familiarSpecialEnergy >= 3) {
																																																																					((Player) arguments[0])
																																																																							.sendMessage(
																																																																									"This special attack is currently unvailable.");
																																																																				} else {
																																																																					((Player) arguments[0])
																																																																							.sendMessage(
																																																																									"Your familiar has "
																																																																											+ ((Player) arguments[0])
																																																																													.getSummoning().familiarSpecialEnergy
																																																																											+ " special energy left and needs 3..");
																																																																				}
																																																																			}
																																																																		}), Ravenous_Locust(
																																																																				7372,
																																																																				12820,
																																																																				12830,
																																																																				15,
																																																																				FamiliarType.NORMAL,
																																																																				0,
																																																																				40,
																																																																				false,
																																																																				"Famine",
																																																																				"",
																																																																				new FamiliarSpecial() {
																																																																					@Override
																																																																					public void execute(
																																																																							Object... arguments) {
																																																																						if (((Player) arguments[0])
																																																																								.getSummoning().familiarSpecialEnergy >= 12) {
																																																																							((Player) arguments[0])
																																																																									.sendMessage(
																																																																											"This special attack is currently unvailable.");
																																																																						} else {
																																																																							((Player) arguments[0])
																																																																									.sendMessage(
																																																																											"Your familiar has "
																																																																													+ ((Player) arguments[0])
																																																																															.getSummoning().familiarSpecialEnergy
																																																																													+ " special energy left and needs 12..");
																																																																						}
																																																																					}
																																																																				}), Arctic_Bear(
																																																																						6839,
																																																																						12057,
																																																																						12451,
																																																																						15,
																																																																						FamiliarType.NORMAL,
																																																																						0,
																																																																						40,
																																																																						false,
																																																																						"Arctic Blast",
																																																																						"",
																																																																						new FamiliarSpecial() {
																																																																							@Override
																																																																							public void execute(
																																																																									Object... arguments) {
																																																																								if (((Player) arguments[0])
																																																																										.getSummoning().familiarSpecialEnergy >= 12) {
																																																																									((Player) arguments[0])
																																																																											.sendMessage(
																																																																													"This special attack is currently unvailable.");
																																																																								} else {
																																																																									((Player) arguments[0])
																																																																											.sendMessage(
																																																																													"Your familiar has "
																																																																															+ ((Player) arguments[0])
																																																																																	.getSummoning().familiarSpecialEnergy
																																																																															+ " special energy left and needs 12..");
																																																																								}
																																																																							}
																																																																						}), Obsidian_Golem(
																																																																								7345,
																																																																								12792,
																																																																								12826,
																																																																								15,
																																																																								FamiliarType.NORMAL,
																																																																								0,
																																																																								40,
																																																																								false,
																																																																								"Volcanic strength",
																																																																								"",
																																																																								new FamiliarSpecial() {
																																																																									@Override
																																																																									public void execute(
																																																																											Object... arguments) {
																																																																										if (((Player) arguments[0])
																																																																												.getSummoning().familiarSpecialEnergy >= 13) {
																																																																											((Player) arguments[0])
																																																																													.sendMessage(
																																																																															"This special attack is currently unvailable.");
																																																																										} else {
																																																																											((Player) arguments[0])
																																																																													.sendMessage(
																																																																															"Your familiar has "
																																																																																	+ ((Player) arguments[0])
																																																																																			.getSummoning().familiarSpecialEnergy
																																																																																	+ " special energy left and needs 13..");
																																																																										}
																																																																									}
																																																																								}), Praying_Mantis(
																																																																										6798,
																																																																										12011,
																																																																										12450,
																																																																										15,
																																																																										FamiliarType.NORMAL,
																																																																										0,
																																																																										40,
																																																																										false,
																																																																										"Mantis strike",
																																																																										"",
																																																																										new FamiliarSpecial() {
																																																																											@Override
																																																																											public void execute(
																																																																													Object... arguments) {
																																																																												if (((Player) arguments[0])
																																																																														.getSummoning().familiarSpecialEnergy >= 6) {
																																																																													((Player) arguments[0])
																																																																															.sendMessage(
																																																																																	"This special attack is currently unvailable.");
																																																																												} else {
																																																																													((Player) arguments[0])
																																																																															.sendMessage(
																																																																																	"Your familiar has "
																																																																																			+ ((Player) arguments[0])
																																																																																					.getSummoning().familiarSpecialEnergy
																																																																																			+ " special energy left and needs 6..");
																																																																												}
																																																																											}
																																																																										}), Forge_Regent(
																																																																												7335,
																																																																												12782,
																																																																												12841,
																																																																												15,
																																																																												FamiliarType.NORMAL,
																																																																												0,
																																																																												40,
																																																																												false,
																																																																												"Inferno",
																																																																												"",
																																																																												new FamiliarSpecial() {
																																																																													@Override
																																																																													public void execute(
																																																																															Object... arguments) {
																																																																														if (((Player) arguments[0])
																																																																																.getSummoning().familiarSpecialEnergy >= 7) {
																																																																															((Player) arguments[0])
																																																																																	.sendMessage(
																																																																																			"This special attack is currently unvailable.");
																																																																														} else {
																																																																															((Player) arguments[0])
																																																																																	.sendMessage(
																																																																																			"Your familiar has "
																																																																																					+ ((Player) arguments[0])
																																																																																							.getSummoning().familiarSpecialEnergy
																																																																																					+ " special energy left and needs 7..");
																																																																														}
																																																																													}
																																																																												}), Talon_Beast(
																																																																														7347,
																																																																														12794,
																																																																														12831,
																																																																														15,
																																																																														FamiliarType.NORMAL,
																																																																														0,
																																																																														40,
																																																																														false,
																																																																														"Deadly claw",
																																																																														"",
																																																																														new FamiliarSpecial() {
																																																																															@Override
																																																																															public void execute(
																																																																																	Object... arguments) {
																																																																																if (((Player) arguments[0])
																																																																																		.getSummoning().familiarSpecialEnergy >= 8) {
																																																																																	((Player) arguments[0])
																																																																																			.sendMessage(
																																																																																					"This special attack is currently unvailable.");
																																																																																} else {
																																																																																	((Player) arguments[0])
																																																																																			.sendMessage(
																																																																																					"Your familiar has "
																																																																																							+ ((Player) arguments[0])
																																																																																									.getSummoning().familiarSpecialEnergy
																																																																																							+ " special energy left and needs 8..");
																																																																																}
																																																																															}
																																																																														}), Giant_Ent(
																																																																																6800,
																																																																																12013,
																																																																																12457,
																																																																																15,
																																																																																FamiliarType.NORMAL,
																																																																																0,
																																																																																40,
																																																																																false,
																																																																																"Acorn Missile",
																																																																																"",
																																																																																new FamiliarSpecial() {
																																																																																	@Override
																																																																																	public void execute(
																																																																																			Object... arguments) {
																																																																																		if (((Player) arguments[0])
																																																																																				.getSummoning().familiarSpecialEnergy >= 11) {
																																																																																			((Player) arguments[0])
																																																																																					.sendMessage(
																																																																																							"This special attack is currently unvailable.");
																																																																																		} else {
																																																																																			((Player) arguments[0])
																																																																																					.sendMessage(
																																																																																							"Your familiar has "
																																																																																									+ ((Player) arguments[0])
																																																																																											.getSummoning().familiarSpecialEnergy
																																																																																									+ " special energy left and needs 11..");
																																																																																		}
																																																																																	}
																																																																																}), Hydra(
																																																																																		6811,
																																																																																		12025,
																																																																																		12442,
																																																																																		15,
																																																																																		FamiliarType.NORMAL,
																																																																																		0,
																																																																																		40,
																																																																																		false,
																																																																																		"Regrowth",
																																																																																		"",
																																																																																		new FamiliarSpecial() {
																																																																																			@Override
																																																																																			public void execute(
																																																																																					Object... arguments) {
																																																																																				if (((Player) arguments[0])
																																																																																						.getSummoning().familiarSpecialEnergy >= 5) {
																																																																																					((Player) arguments[0])
																																																																																							.sendMessage(
																																																																																									"This special attack is currently unvailable.");
																																																																																				} else {
																																																																																					((Player) arguments[0])
																																																																																							.sendMessage(
																																																																																									"Your familiar has "
																																																																																											+ ((Player) arguments[0])
																																																																																													.getSummoning().familiarSpecialEnergy
																																																																																											+ " special energy left and needs 5..");
																																																																																				}
																																																																																			}
																																																																																		}), Spirit_Dagannoth(
																																																																																				6804,
																																																																																				12017,
																																																																																				12456,
																																																																																				15,
																																																																																				FamiliarType.NORMAL,
																																																																																				0,
																																																																																				40,
																																																																																				false,
																																																																																				"Spike Shot",
																																																																																				"",
																																																																																				new FamiliarSpecial() {
																																																																																					@Override
																																																																																					public void execute(
																																																																																							Object... arguments) {
																																																																																						if (((Player) arguments[0])
																																																																																								.getSummoning().familiarSpecialEnergy >= 12) {
																																																																																							((Player) arguments[0])
																																																																																									.sendMessage(
																																																																																											"This special attack is currently unvailable.");
																																																																																						} else {
																																																																																							((Player) arguments[0])
																																																																																									.sendMessage(
																																																																																											"Your familiar has "
																																																																																													+ ((Player) arguments[0])
																																																																																															.getSummoning().familiarSpecialEnergy
																																																																																													+ " special energy left and needs 12..");
																																																																																						}
																																																																																					}
																																																																																				}), Lava_Titan(
																																																																																						7341,
																																																																																						12788,
																																																																																						12837,
																																																																																						15,
																																																																																						FamiliarType.NORMAL,
																																																																																						0,
																																																																																						40,
																																																																																						false,
																																																																																						"Ebon Thunder",
																																																																																						"",
																																																																																						new FamiliarSpecial() {
																																																																																							@Override
																																																																																							public void execute(
																																																																																									Object... arguments) {
																																																																																								if (((Player) arguments[0])
																																																																																										.getSummoning().familiarSpecialEnergy >= 13) {
																																																																																									((Player) arguments[0])
																																																																																											.sendMessage(
																																																																																													"This special attack is currently unvailable.");
																																																																																								} else {
																																																																																									((Player) arguments[0])
																																																																																											.sendMessage(
																																																																																													"Your familiar has "
																																																																																															+ ((Player) arguments[0])
																																																																																																	.getSummoning().familiarSpecialEnergy
																																																																																															+ " special energy left and needs 13..");
																																																																																								}
																																																																																							}
																																																																																						}), Swamp_Titan(
																																																																																								7329,
																																																																																								12776,
																																																																																								12832,
																																																																																								15,
																																																																																								FamiliarType.NORMAL,
																																																																																								0,
																																																																																								40,
																																																																																								false,
																																																																																								"Swamp Plague",
																																																																																								"",
																																																																																								new FamiliarSpecial() {
																																																																																									@Override
																																																																																									public void execute(
																																																																																											Object... arguments) {
																																																																																										if (((Player) arguments[0])
																																																																																												.getSummoning().familiarSpecialEnergy >= 13) {
																																																																																											((Player) arguments[0])
																																																																																													.sendMessage(
																																																																																															"This special attack is currently unvailable.");
																																																																																										} else {
																																																																																											((Player) arguments[0])
																																																																																													.sendMessage(
																																																																																															"Your familiar has "
																																																																																																	+ ((Player) arguments[0])
																																																																																																			.getSummoning().familiarSpecialEnergy
																																																																																																	+ " special energy left and needs 13..");
																																																																																										}
																																																																																									}
																																																																																								}), Rune_Minotaur(
																																																																																										6863,
																																																																																										12083,
																																																																																										12466,
																																																																																										15,
																																																																																										FamiliarType.NORMAL,
																																																																																										0,
																																																																																										40,
																																																																																										false,
																																																																																										"Rune Bull Rush",
																																																																																										"",
																																																																																										new FamiliarSpecial() {
																																																																																											@Override
																																																																																											public void execute(
																																																																																													Object... arguments) {
																																																																																												if (((Player) arguments[0])
																																																																																														.getSummoning().familiarSpecialEnergy >= 12) {
																																																																																													((Player) arguments[0])
																																																																																															.sendMessage(
																																																																																																	"This special attack is currently unvailable.");
																																																																																												} else {
																																																																																													((Player) arguments[0])
																																																																																															.sendMessage(
																																																																																																	"Your familiar has "
																																																																																																			+ ((Player) arguments[0])
																																																																																																					.getSummoning().familiarSpecialEnergy
																																																																																																			+ " special energy left and needs 12..");
																																																																																												}
																																																																																											}
																																																																																										}), Abyssal_Titan(
																																																																																												7349,
																																																																																												12796,
																																																																																												12827,
																																																																																												15,
																																																																																												FamiliarType.BOB,
																																																																																												3,
																																																																																												40,
																																																																																												false,
																																																																																												"Essence Shipment",
																																																																																												"",
																																																																																												new FamiliarSpecial() {
																																																																																													@Override
																																																																																													public void execute(
																																																																																															Object... arguments) {
																																																																																														if (((Player) arguments[0])
																																																																																																.getSummoning().familiarSpecialEnergy >= 14) {
																																																																																															((Player) arguments[0])
																																																																																																	.sendMessage(
																																																																																																			"This special attack is currently unvailable.");
																																																																																														} else {
																																																																																															((Player) arguments[0])
																																																																																																	.sendMessage(
																																																																																																			"Your familiar has "
																																																																																																					+ ((Player) arguments[0])
																																																																																																							.getSummoning().familiarSpecialEnergy
																																																																																																					+ " special energy left and needs 14..");
																																																																																														}
																																																																																													}
																																																																																												}), Iron_Titan(
																																																																																														7375,
																																																																																														12822,
																																																																																														12828,
																																																																																														15,
																																																																																														FamiliarType.NORMAL,
																																																																																														0,
																																																																																														40,
																																																																																														false,
																																																																																														"Iron Within",
																																																																																														"",
																																																																																														new FamiliarSpecial() {
																																																																																															@Override
																																																																																															public void execute(
																																																																																																	Object... arguments) {
																																																																																																if (((Player) arguments[0])
																																																																																																		.getSummoning().familiarSpecialEnergy >= 15) {
																																																																																																	((Player) arguments[0])
																																																																																																			.sendMessage(
																																																																																																					"This special attack is currently unvailable.");
																																																																																																} else {
																																																																																																	((Player) arguments[0])
																																																																																																			.sendMessage(
																																																																																																					"Your familiar has "
																																																																																																							+ ((Player) arguments[0])
																																																																																																									.getSummoning().familiarSpecialEnergy
																																																																																																							+ " special energy left and needs 15..");
																																																																																																}
																																																																																															}
																																																																																														}),

		/**
		 * HAS SPECIAL BELOW
		 */

		Spirit_Terrorbird(6794, 12007, 12441, 15, FamiliarType.BOB, 12, 0, false, "Tireless Run", "",
				new FamiliarSpecial() {
					@Override
					public void execute(Object... arguments) {
						if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 15) {
							/*
							 * int stuff = ((Client)
							 * arguments[0]).playerLevel[16] / 2; ((Client)
							 * arguments[0]).runEnergy = ((Client)
							 * arguments[0]).runEnergy + stuff; ((Client)
							 * arguments[0]).playerLevel[16] += 2; if (((Client)
							 * arguments[0]).runEnergy > 100) { ((Client)
							 * arguments[0]).runEnergy = 100; ((Client)
							 * arguments[0]).getPA().sendFrame126(((Client)
							 * arguments[0]).runEnergy+"", 149); } ((Client)
							 * arguments[0]).gfx0(1300); ((Client)
							 * arguments[0]).startAnimation(7660);
							 * GameEngine.npcHandler.startAnimation(6792,
							 * ((Client) arguments[0]).summoningMonsterId);
							 * ((Client) arguments[0]).summoned.gfx0(1521);
							 * ((Client)
							 * arguments[0]).getSummoning().specialTimer = 5;
							 * ((Client) arguments[0]).getSummoning().
							 * familiarSpecialEnergy -= 15; ((Client)
							 * arguments[0]).getItems().deleteItem2(((Client)
							 * arguments[0]).getSummoning().summonedFamiliar.
							 * scrollId, 1);
							 */
						} else {
							((Player) arguments[0]).sendMessage(
									"Your familiar has " + ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
											+ " special energy left and needs 15..");
						}

					}
				}), Wolpertinger(6869, 12089, 12437, 15, FamiliarType.NORMAL, 0, 40, false, "Magic Focus", "Mew!",
						new FamiliarSpecial() {
							@Override
							public void execute(Object... arguments) {
								if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 15) {
									GameEngine.npcHandler.startAnimation(8305,
											((Player) arguments[0]).getInstance().summoningMonsterId);
									((Player) arguments[0]).getInstance().summoned.gfx0(1410);// rett
																								// gfx
																								// wolpertinger
																								// en
																								// vantar
																								// retta
																								// anim
									((Player) arguments[0]).startAnimation(7660);
									((Player) arguments[0]).gfx(1313, 0);
									((Player) arguments[0]).getPotions().doWolperSpecial(7);
									((Player) arguments[0]).sendMessage("Your magic is boosted!");
									((Player) arguments[0]).getSummoning().specialTimer = 5;
									((Player) arguments[0]).getSummoning().familiarSpecialEnergy -= 15;
									((Player) arguments[0]).getItems().deleteItem2(
											((Player) arguments[0]).getSummoning().summonedFamiliar.scrollId, 1);
								} else {
									((Player) arguments[0]).sendMessage("Your familiar has "
											+ ((Player) arguments[0]).getSummoning().familiarSpecialEnergy
											+ " special energy left and needs 15..");
								}
							}
						}), Unicorn_Stallion(6822, 12039, 12434, 15, FamiliarType.NORMAL, 0, 40, false, "Healing Aura",
								"", new FamiliarSpecial() {
									@Override
									public void execute(Object... arguments) {
										if (((Player) arguments[0]).getSummoning().familiarSpecialEnergy >= 20) {
											int heal = 0;
											heal = ((Player) arguments[0]).getInstance().maxLifePoints;
											heal *= 0.15;
											((Player) arguments[0]).addHp(heal);
											GameEngine.npcHandler.startAnimation(6375,
													((Player) arguments[0]).getInstance().summoningMonsterId);
											((Player) arguments[0]).getInstance().summoned.gfx0(1356);// vantar
																										// retta
																										// anim
											((Player) arguments[0]).startAnimation(7660);
											((Player) arguments[0]).gfx(1313, 0);
											((Player) arguments[0]).sendMessage("You restored " + heal + " Health!");
											((Player) arguments[0]).getSummoning().specialTimer = 5;
											((Player) arguments[0]).getSummoning().familiarSpecialEnergy -= 20;
											((Player) arguments[0]).getItems().deleteItem2(
													((Player) arguments[0]).getSummoning().summonedFamiliar.scrollId,
													1);
										} else {
											((Player) arguments[0])
													.sendMessage("Your familiar has "
															+ ((Player) arguments[0])
																	.getSummoning().familiarSpecialEnergy
															+ " special energy left and needs 20..");
										}
									}
								}), War_Tortoise(6815, 12031, 12439, 30, FamiliarType.BOB, 18, 0, true, "Testudo", "",
										new FamiliarSpecial() {
											@Override
											public void execute(Object... arguments) {
												/* Special goes here */
												if (((Player) arguments[0])
														.getSummoning().familiarSpecialEnergy >= 15) {
													GameEngine.npcHandler.startAnimation(8288,
															((Player) arguments[0]).getInstance().summoningMonsterId);
													((Player) arguments[0]).getInstance().summoned.gfx0(1414);// unknown
																												// wartoise
																												// gfx
																												// ulti
													((Player) arguments[0]).startAnimation(7660);
													((Player) arguments[0]).gfx(1313, 0);
													((Player) arguments[0])
															.sendMessage("Your defence has been boosted!");
													((Player) arguments[0]).getPotions().doTortoiseSpecial(1);
													((Player) arguments[0]).getSummoning().specialTimer = 5;
													((Player) arguments[0]).getSummoning().familiarSpecialEnergy -= 15;
													((Player) arguments[0]).getItems()
															.deleteItem2(
																	((Player) arguments[0])
																			.getSummoning().summonedFamiliar.scrollId,
																	1);
												} else {
													((Player) arguments[0]).sendMessage("Your familiar has "
															+ ((Player) arguments[0])
																	.getSummoning().familiarSpecialEnergy
															+ " special energy left and needs 15..");
												}
											}
										}), Steel_Titan(7343, 12790, 12825, 30, FamiliarType.COMBAT, 18, 0, false,
												"Steel of Legends", "", new FamiliarSpecial() {
													@Override
													public void execute(Object... arguments) {
														if (((Player) arguments[0])
																.getSummoning().familiarSpecialEnergy >= 15) {
															GameEngine.npcHandler.startAnimation(8190,
																	((Player) arguments[0])
																			.getInstance().summoningMonsterId);
															((Player) arguments[0]).getInstance().summoned.gfx0(1449);
															((Player) arguments[0])
																	.getInstance().usingSummoningSpecial = true;
															((Player) arguments[0]).getSummoning().specialTimer = 5;
															((Player) arguments[0])
																	.getSummoning().familiarSpecialEnergy -= 15;
															((Player) arguments[0]).getItems().deleteItem2(
																	((Player) arguments[0])
																			.getSummoning().summonedFamiliar.scrollId,
																	1);
														} else {
															((Player) arguments[0]).sendMessage("Your familiar has "
																	+ ((Player) arguments[0])
																			.getSummoning().familiarSpecialEnergy
																	+ " special energy left and needs 15..");
														}
													}
												}), Geyser_Titan(7339, 12786, 12833, 30, FamiliarType.COMBAT, 18, 0,
														false, "Boil", "", new FamiliarSpecial() {
															@Override
															public void execute(Object... arguments) {
																if (((Player) arguments[0])
																		.getSummoning().familiarSpecialEnergy >= 15) {
																	GameEngine.npcHandler.startAnimation(7883,
																			((Player) arguments[0])
																					.getInstance().summoningMonsterId);
																	((Player) arguments[0]).getInstance().summoned
																			.gfx0(1373);
																	((Player) arguments[0])
																			.getInstance().usingSummoningSpecial = true;
																	((Player) arguments[0])
																			.getSummoning().specialTimer = 5;
																	((Player) arguments[0])
																			.getSummoning().familiarSpecialEnergy -= 15;
																	((Player) arguments[0])
																			.getInstance().summoned.projectileId = 1376;
																	((Player) arguments[0]).getItems().deleteItem2(
																			((Player) arguments[0])
																					.getSummoning().summonedFamiliar.scrollId,
																			1);
																} else {
																	((Player) arguments[0])
																			.sendMessage("Your familiar has "
																					+ ((Player) arguments[0])
																							.getSummoning().familiarSpecialEnergy
																					+ " special energy left and needs 15..");
																}
															}
														}), Fire_Titan(7355, 12802, 12824, 15, FamiliarType.NORMAL, 0,
																40, false, "Titan's Constitution", "",
																new FamiliarSpecial() {
																	@Override
																	public void execute(Object... arguments) {
																		if (((Player) arguments[0])
																				.getSummoning().familiarSpecialEnergy >= 20) {
																			GameEngine.npcHandler.startAnimation(8190,
																					((Player) arguments[0])
																							.getInstance().summoningMonsterId);
																			((Player) arguments[0])
																					.getInstance().lifePoints += 80;
																			if (((Player) arguments[0])
																					.getInstance().maxLifePoints
																					+ 80 <= ((Player) arguments[0])
																							.getInstance().lifePoints) {
																				((Player) arguments[0])
																						.getInstance().lifePoints = ((Player) arguments[0])
																								.getInstance().maxLifePoints
																								+ 80;
																			}
																			((Player) arguments[0])
																					.startAnimation(7660);
																			((Player) arguments[0]).gfx(1313, 0);
																			((Player) arguments[0]).getPotions()
																					.doTitanSpecial(1);
																			((Player) arguments[0]).sendMessage(
																					"Your Defence and Constitution have been boosted.");
																			((Player) arguments[0])
																					.getSummoning().specialTimer = 5;
																			((Player) arguments[0])
																					.getSummoning().familiarSpecialEnergy -= 20;
																			((Player) arguments[0]).getItems()
																					.deleteItem2(
																							((Player) arguments[0])
																									.getSummoning().summonedFamiliar.scrollId,
																							1);
																		} else {
																			((Player) arguments[0])
																					.sendMessage("Your familiar has "
																							+ ((Player) arguments[0])
																									.getSummoning().familiarSpecialEnergy
																							+ " special energy left and needs 20..");
																		}
																	}
																}), Moss_Titan(7357, 12804, 12824, 15,
																		FamiliarType.NORMAL, 0, 40, false,
																		"Titan's Constitution", "",
																		new FamiliarSpecial() {
																			@Override
																			public void execute(Object... arguments) {
																				if (((Player) arguments[0])
																						.getSummoning().familiarSpecialEnergy >= 20) {
																					GameEngine.npcHandler
																							.startAnimation(8190,
																									((Player) arguments[0])
																											.getInstance().summoningMonsterId);
																					((Player) arguments[0])
																							.getInstance().lifePoints += 80;
																					if (((Player) arguments[0])
																							.getInstance().maxLifePoints
																							+ 80 <= ((Player) arguments[0])
																									.getInstance().lifePoints) {
																						((Player) arguments[0])
																								.getInstance().lifePoints = ((Player) arguments[0])
																										.getInstance().maxLifePoints
																										+ 80;
																					}
																					((Player) arguments[0])
																							.startAnimation(7660);
																					((Player) arguments[0]).gfx(1313,
																							0);
																					((Player) arguments[0]).getPotions()
																							.doTitanSpecial(1);
																					((Player) arguments[0]).sendMessage(
																							"Your Defence and Constitution have been boosted.");
																					((Player) arguments[0])
																							.getSummoning().specialTimer = 5;
																					((Player) arguments[0])
																							.getSummoning().familiarSpecialEnergy -= 20;
																					((Player) arguments[0]).getItems()
																							.deleteItem2(
																									((Player) arguments[0])
																											.getSummoning().summonedFamiliar.scrollId,
																									1);
																				} else {
																					((Player) arguments[0]).sendMessage(
																							"Your familiar has "
																									+ ((Player) arguments[0])
																											.getSummoning().familiarSpecialEnergy
																									+ " special energy left and needs 20..");
																				}
																			}
																		}), Ice_Titan(7359, 12806, 12824, 30,
																				FamiliarType.COMBAT, 18, 0, false,
																				"Titan's Constitution", "I'm melting!",
																				new FamiliarSpecial() {
																					@Override
																					public void execute(
																							Object... arguments) {
																						if (((Player) arguments[0])
																								.getSummoning().familiarSpecialEnergy >= 20) {
																							GameEngine.npcHandler
																									.startAnimation(
																											8190,
																											((Player) arguments[0])
																													.getInstance().summoningMonsterId);
																							((Player) arguments[0])
																									.getInstance().lifePoints += 80;
																							if (((Player) arguments[0])
																									.getInstance().maxLifePoints
																									+ 80 <= ((Player) arguments[0])
																											.getInstance().lifePoints) {
																								((Player) arguments[0])
																										.getInstance().lifePoints = ((Player) arguments[0])
																												.getInstance().maxLifePoints
																												+ 80;
																							}
																							((Player) arguments[0])
																									.startAnimation(
																											7660);
																							((Player) arguments[0])
																									.gfx(1313, 0);
																							((Player) arguments[0])
																									.getPotions()
																									.doTitanSpecial(1);
																							((Player) arguments[0])
																									.sendMessage(
																											"Your Defence and Constitution have been boosted.");
																							((Player) arguments[0])
																									.getSummoning().specialTimer = 5;
																							((Player) arguments[0])
																									.getSummoning().familiarSpecialEnergy -= 20;
																							((Player) arguments[0])
																									.getItems()
																									.deleteItem2(
																											((Player) arguments[0])
																													.getSummoning().summonedFamiliar.scrollId,
																											1);
																						} else {
																							((Player) arguments[0])
																									.sendMessage(
																											"Your familiar has "
																													+ ((Player) arguments[0])
																															.getSummoning().familiarSpecialEnergy
																													+ " special energy left and needs 20..");
																						}
																					}
																				}), Pack_Yak(6873, 12093, 12435, 30,
																						FamiliarType.BOB, 30, 10, true,
																						"Winter Storage",
																						"Baroo baroo!",
																						new FamiliarSpecial() {
																							@Override
																							public void execute(
																									Object... arguments) {
																								if (((Player) arguments[0])
																										.getSummoning().familiarSpecialEnergy >= 15) {
																									((Player) arguments[0])
																											.getPA()
																											.sendString(
																													":moi: 24000",
																													50000);
																									((Player) arguments[0])
																											.sendMessage(
																													"Please select the item you would like to send to your bank!");
																								} else {
																									((Player) arguments[0])
																											.sendMessage(
																													"Your familiar has "
																															+ ((Player) arguments[0])
																																	.getSummoning().familiarSpecialEnergy
																															+ " special energy left and needs 15..");
																								}
																							}
																						});

		private Familiar(int npcId, int pouchId, int scrollId, int timeLimit, FamiliarType familiarType,
				int storeCapacity, int specialEnergyConsumption, boolean large, String clientTooltip, String textChat,
				FamiliarSpecial familiarSpecial) {
			this.npcId = npcId + 1;
			this.pouchId = pouchId;
			this.scrollId = scrollId;
			this.timeLimit = timeLimit;
			this.familiarType = familiarType;
			this.storeCapacity = storeCapacity;
			this.specialEnergyConsumption = specialEnergyConsumption;
			this.large = large;
			this.clientTooltip = clientTooltip;
			this.speakText = textChat;
			this.familiarSpecial = familiarSpecial;
		}

		public int npcId;
		public int pouchId;
		public int scrollId;
		public int timeLimit;
		public FamiliarType familiarType;
		public int storeCapacity;
		public int specialEnergyConsumption;
		public boolean large;
		public String clientTooltip;
		public String speakText;
		public FamiliarSpecial familiarSpecial;

		public static HashMap<Integer, Familiar> familiars = new HashMap<Integer, Familiar>();

		public static Familiar forPouchId(int id) {
			return familiars.get(id);
		}

		static {
			for (Familiar f : Familiar.values())
				familiars.put(f.pouchId, f);
		}
	}

	/**
	 * Summons a familiar based on pouch id
	 * 
	 * @param itemId
	 *            - Pouch Id from ActionButton packet.
	 */
	@SuppressWarnings("unused")
	public void summonFamiliar(int itemId, boolean login) {
		/*
		 * if (c.inConstruction()) { c.sendMessage(
		 * "You cannot have a summoning familiar in here."); return; }
		 */
		int summonMPoints = 7;
		/*
		 * if(c.summoningPoints < summonMPoints && summonedFamiliar == null &&
		 * c.getVariables().summoned == null && c.justLoggedIn == false) {
		 * c.sendMessage("You need to have atleast "+summonMPoints+
		 * " summoning points!"); return; } if(c.summoningPoints < summonMPoints
		 * && c.summonLoginFix == true && c.justLoggedIn == false) {
		 * c.sendMessage("You need to have atleast "+summonMPoints+
		 * " summoning points!"); return; }
		 */
		c.getInstance().summonedCanTeleport = true;
		Familiar summonedFamiliar = Familiar.forPouchId(itemId);
		if (summonedFamiliar != null && this.summonedFamiliar == null) {
			this.summonedFamiliar = summonedFamiliar;
			burdenedItems = new int[summonedFamiliar.storeCapacity];
			for (int i = 0; i < burdenedItems.length; i++)
				burdenedItems[i] = 0;
			c.getPA().sendSummOrbDetails(true, summonedFamiliar.clientTooltip);
			String familiarName = "" + summonedFamiliar.toString() + "";
			familiarName = familiarName.replace('_', ' ');
			c.getInstance().familiarName = familiarName;
			if (!login) {
				c.getInstance().specRestoreTimer = (((summonedFamiliar.timeLimit * 60) * 1000) / 600);
				c.getInstance().summoned = GameEngine.npcHandler.summonNPC(c, summonedFamiliar.npcId, c.getX(),
						c.getY() + (summonedFamiliar.large ? 2 : 1), c.heightLevel, 0,
						SummoningData.getSummonHealth(summonedFamiliar.npcId), 1, 1, 1);
				if (!canWalk()) {
					c.sendMessage("Your monster needs more space to be summoned!");
					c.getInstance().specRestoreTimer = 0;
					c.getInstance().summoned.isDead = true;
					c.getInstance().summoned.applyDead = true;
					c.getInstance().summoned.actionTimer = 0;
					c.getInstance().summoned.npcType = -1;
					c.getInstance().summoned.updateRequired = true;
					c.getInstance().usingSummoningSpecial = false;
					burdenedItems = null;
					summonedFamiliar = null;
					c.getInstance().summoned = null;
					c.getPA().sendSummOrbDetails(false, "");
					return;
				}
				callFamiliar();
				c.sendMessage("You summon a " + familiarName + ".");
				c.getPA().sendString(familiarName, 17017);
				c.setSidebarInterface(15, 17011);
				c.getPA().sendFrame75(summonedFamiliar.npcId, 17027);
				// c.summoningPoints -= summonMPoints;
				c.getPA().refreshSkill(23);
				c.getItems().deleteItem(itemId, 1);
			} else {
				loginCycle = 4;
			}
		} else if (this.summonedFamiliar != null && c.getInstance().summoned != null) {
			c.sendMessage("You already have a familiar!");
		}
	}

	/**
	 * Renews a familiar before death.
	 */
	public void renewFamiliar() {
		if (summonedFamiliar != null && this.summonedFamiliar != null) {
			if (c.getInstance().specRestoreTimer < 300) {
				if (c.getItems().playerHasItem(summonedFamiliar.pouchId, 1)) {
					c.getInstance().specRestoreTimer = (((summonedFamiliar.timeLimit * 60) * 1000) / 600);
					c.sendMessage("You sacrifice a pouch to renew your familiar's strength.");
					c.getItems().deleteItem(summonedFamiliar.pouchId, 1);
				} else {
					c.sendMessage("You need a pouch to sacrifice!");
				}
			} else {
				c.sendMessage("You still have excess time left on your current familiar!");
			}
		}
	}

	/**
	 * Dismisses/Destroy's the familiar.
	 */
	public void dismissFamiliar(boolean logout) {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			c.getInstance().summoned.isDead = true;
			c.getInstance().summoned.applyDead = true;
			c.getInstance().summoned.actionTimer = 0;
			c.getInstance().summoned.npcType = -1;
			c.getInstance().summoned.updateRequired = true;
			c.getInstance().usingSummoningSpecial = false;
			c.setSidebarInterface(15, -1);
			c.getPA().sendSummOrbDetails(false, "");
			if (!logout) {
				for (int i = 0; i < burdenedItems.length; i++) {
					ItemHandler.createGroundItem(c, burdenedItems[i] - 1, c.getInstance().summoned.absX,
							c.getInstance().summoned.absY, c.summoned.heightLevel, 1,
							c.getInstance().summoned.summonedFor);
				}
				burdenedItems = null;
				summonedFamiliar = null;
			}
			c.getInstance().summoned = null;
		}
	}
	// 2 dissmiss?

	public void dismissFamiliar() {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			c.getInstance().summoned.isDead = true;
			c.getInstance().summoned.applyDead = true;
			c.getInstance().summoned.actionTimer = 0;
			c.getInstance().summoned.npcType = -1;
			c.getInstance().summoned.updateRequired = true;
			c.getInstance().usingSummoningSpecial = false;
			c.setSidebarInterface(15, -1);
			c.getPA().sendSummOrbDetails(false, "");
			burdenedItems = null;
			summonedFamiliar = null;
			c.getInstance().summoned = null;
		}
	}

	/**
	 * If player dies, the summoning will dismiss
	 */
	public void dismissOnDeath() {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			dismissFamiliar();
			c.getInstance().summoned.npcType = -1;
		}
	}

	/**
	 * If player gets offscreen (most likely teleports), familiar will come to
	 * them
	 */
	public void callOnTeleport() {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			c.getInstance().summoned.killerId = 0;
			c.getInstance().summoned.underAttackBy = 0;
			c.getInstance().summoned.npcTeleport(0, 0, 0);
			c.getInstance().summoned.updateRequired = true;
			c.getInstance().summoned = GameEngine.npcHandler.summonNPC(c, summonedFamiliar.npcId, c.getX(),
					c.getY() + (summonedFamiliar.large ? 2 : 1), c.heightLevel, 0, c.getInstance().summoned.HP, 1, 1,
					1);
			callFamiliar();
		} else {
			c.getPA().sendSummOrbDetails(false, "");
		}
	}

	public boolean canWalk() {
		int squares = c.getInstance().summoned.getNpcSize() * c.getInstance().summoned.getNpcSize();
		int x = 0;
		int y = 0;
		for (int i = 0; i < squares; i++) {
			if (i == 1)
				x--;
			if (i == 2)
				y++;
			if (i == 3)
				x++;
			if (!((Region.getClipping(c.absX + x, c.absY + (summonedFamiliar.large ? 2 : 1) + y, c.heightLevel)
					& 0x1280180) == 0)) {
				c.getInstance().summoned.killerId = 0;
				c.getInstance().summoned.underAttackBy = 0;
				return false;
			}
		}
		return true;
	}

	/**
	 * 1. 3100 3500 1 3 2. 3100 3501 2 4 3. 3099 3500 4. 3099 3501
	 * 
	 */

	/**
	 * x - y x-1 - y x - y+1 x-1 - y+1
	 */
	/**
	 * Calls the familiar to the owner.
	 */
	public void callFamiliar() {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
		} else {
			c.getPA().sendSummOrbDetails(false, "");
		}

		if (!canWalk()) {
			if (!c.getInstance().summoned.canTeleport) {
				c.getInstance().summoned.isDead = true;
				c.getInstance().summoned.applyDead = true;
				c.getInstance().summoned.actionTimer = 0;
				c.getInstance().summoned.updateRequired = true;
				c.sendMessage("Your monster needs more space to teleport.");
				c.getInstance().summoned.canTeleport = true;
			}
			return;
		}
		c.getInstance().summonedCanTeleport = false;
		c.getInstance().summoned.killerId = 0;
		c.getInstance().summoned.underAttackBy = 0;
		c.getInstance().summoned.npcTeleport(0, 0, 0);
		c.getInstance().summoned.npcTeleport(c.absX, c.absY + (summonedFamiliar.large ? 2 : 1), c.heightLevel);
		c.getInstance().summoned.updateRequired = true;
	}

	/**
	 * Array of the burdened items.
	 */
	public int[] burdenedItems;

	/**
	 * Gets the next slot for the specified itemId.
	 * 
	 * @param itemId
	 *            - The specified item id.
	 */
	public int getSlotForId(int itemId) {
		if (summonedFamiliar.familiarType == FamiliarType.BOB) {
			for (int i = 0; i < burdenedItems.length; i++) {
				if ((burdenedItems[i] + 1) == itemId)
					return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the count of the specified item.
	 * 
	 * @return the amount of contained items.
	 */
	public int getItemCount(int itemId) {
		int count = 0;
		if (summonedFamiliar.familiarType == FamiliarType.BOB) {
			for (int i = 0; i < burdenedItems.length; i++) {
				if ((burdenedItems[i] - 1) == itemId) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Shifts and re-arranges the familiar's inventory.
	 */
	@SuppressWarnings("unused")
	public void shift() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < summonedFamiliar.storeCapacity; i++) {
			if (burdenedItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}
		for (int i = 0; i <= highestSlot; i++) {
			if (burdenedItems[i] == 0) {
				boolean stop = false;
				for (int k = i; k <= highestSlot; k++) {
					if (burdenedItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							burdenedItems[j - spots] = burdenedItems[j];
							stop = true;
							burdenedItems[j] = 0;
						}
					}
				}
			}
		}
	}

	/**
	 * Withdraws an item from the familiar's inventory, to the owner's
	 * inventory.
	 * 
	 * @param id
	 *            - Item id
	 * @param slot
	 *            - Item slot
	 * @param amount
	 *            - Item amount
	 */
	public void withdrawItem(int id, int amount) {
		if (c.getInstance().inWild() && c.getInstance().wildLevel <= 30) {
			c.sendMessage("You can't use This option if your below 30 wilderness.");// this
																					// sends
																					// a
																					// message
																					// for
																					// each
																					// item
																					// need
																					// to
																					// fix
			return;
		}
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			if (summonedFamiliar.familiarType == FamiliarType.BOB) {
				if (amount > 0 && id > 0) {
					int slot = getSlotForId(id + 2);
					while (amount > 0 && slot != -1) {
						if (c.getItems().addItem(burdenedItems[slot] - 1, 1)) {
							burdenedItems[slot] = 0;
							slot = getSlotForId(id + 2);
							amount--;
						} else {
							break;
						}
					}
				}
				c.getItems().resetItems(24006);
				c.getItems().resetItems(24002, burdenedItems);
				c.startAnimation(827);// does the animation everytime even if
										// the npc doesn't have items in it
			}
		}
	}

	/**
	 * Deposits an item from the owner's inventory, to the familiar's inventory.
	 * 
	 * @param id
	 *            - Item id
	 * @param slot
	 *            - Item slot
	 * @param amount
	 *            - Item amount
	 */
	public void depositItem(int id, int slot, int amount) {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			if (summonedFamiliar.familiarType == FamiliarType.BOB) {
				if (amount > 0 && c.getInstance().playerItems[slot] > 0 && slot >= 0 && slot < 28) {
					if (!c.getItems().isStackable(c.getInstance().playerItems[slot] - 1)) {
						if (itemIsAllowed(c.getInstance().playerItems[slot] - 1)) {
							while (amount > 0 && slot != -1) {
								if (addItem(c.getInstance().playerItems[slot])) {
									int tempVar = c.getInstance().playerItems[slot] - 1;
									c.getItems().deleteItem(c.getInstance().playerItems[slot] - 1, slot, 1);
									slot = c.getItems().getItemSlot(tempVar);
									amount--;
									c.startAnimation(827);
								} else {
									break;
								}
							}
						} else {
							c.sendMessage("You cannot deposit this item!");
						}
					}
				}
				c.getItems().resetItems(24006);
				c.getItems().resetItems(24002, burdenedItems);
			}
		}
	}

	/**
	 * Checks if the specific item is allowed to be deposited.
	 * 
	 * @return returns if the item is allowed.
	 */
	public boolean itemIsAllowed(int itemId) {
		switch (itemId) {
		case 15272: // Rocktail
		case 7060: // Tuna potato
		case 6685:
		case 6687:
		case 6689:
		case 6691: // Saradomin brew
		case 3024:
		case 3026:
		case 3028:
		case 3030: // Super restore
		case 391: // Manta Ray
		case 385: // Shark
		case 229: // Vial
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the item was added to the container successfully.
	 */
	public boolean addItem(int itemId) {
		int nextFreeSlot = getSlotForId(1);
		if (itemId <= 0)
			return false;
		if (nextFreeSlot != -1 && burdenedItems[nextFreeSlot] == 0) {
			burdenedItems[nextFreeSlot] = itemId;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Opens the beast of burden.
	 */
	public void openBoB() {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			if (summonedFamiliar.familiarType == FamiliarType.BOB) {
				shift();
				if (c.getOutStream() != null && c != null) {
					c.getItems().resetItems(24002, burdenedItems);
					c.flushOutStream();
				}
				c.getItems().resetItems(24006);
				c.getOutStream().createFrame(248);
				c.getOutStream().writeWordA(24000);
				c.getOutStream().writeWord(24005);
				c.flushOutStream();
			}
		}
	}

	/**
	 * Handles the left click option.
	 */
	public void handleLeftClick() {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			if (summonedFamiliar.familiarType == FamiliarType.BOB) {
				for (int i = 0; i < burdenedItems.length; i++) {
					if (c.getItems().freeSlots() > 0)
						withdrawItem(burdenedItems[i] - 1, 1);
				}
			} else {
				useSpecial(c);
			}
		} else {
			c.getPA().sendSummOrbDetails(false, "");
		}
	}

	/**
	 * Executes the familiar's special using the specified arguments (for
	 * compatibility)
	 * 
	 * @param arguements
	 *            - The correct arguments.
	 */
	public void useSpecial(Object... arguments) {
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			if (summonedFamiliar.familiarSpecial != null && specialTimer < 1) {
				if (c.getItems().playerHasItem(summonedFamiliar.scrollId)) {
					summonedFamiliar.familiarSpecial.execute(arguments);
				} else {
					c.sendMessage("You must have the required scroll to do this special.");
				}
			} else {
				c.sendMessage("You must wait before casting this again.");
			}
		}
	}

	private boolean bobWithSpec(int npcType) {
		switch (npcType) {
		case 6873:
			return true;
		}
		return false;
	}

	public void handleButtonClick(int buttonId) {
		int[] buttons = { 61044, 61051, 61048, 61050, 66122, 61049, 66122, 66127, 66126, 66142, 66119, 66117 };
		for (int i = 0; i < buttons.length; i++)
			if (buttonId == buttons[i]) {
				if (summonedFamiliar == null) {
					c.sendMessage("You don't have a familiar.");
					return;
				}
				if (!bobWithSpec(summonedFamiliar.npcId)) {
					if (c.getSummoning().summonedFamiliar == Familiar.Pack_Yak
							|| c.getSummoning().summonedFamiliar == Familiar.Spirit_Terrorbird
							|| c.getSummoning().summonedFamiliar == Familiar.War_Tortoise
							|| c.getSummoning().summonedFamiliar == Familiar.Thorny_Snail
							|| c.getSummoning().summonedFamiliar == Familiar.Bull_Ant
							|| c.getSummoning().summonedFamiliar == Familiar.Abyssal_Parasite
							|| c.getSummoning().summonedFamiliar == Familiar.Abyssal_Titan) {
						if (buttonId == 61049 || buttonId == 66122)
							handleLeftClick();
					}
					switch (buttonId) {
					case 66119:
					case 66117:
					case 61044:
						useSpecial(c);
						break;
					case 61051:
					case 66126:
						c.getSummoning().callFamiliar();
						break;
					case 61048:
					case 66142:
						c.getSummoning().renewFamiliar();
						break;
					case 61050:
					case 66127:
						dismissFamiliar(false);
						break;
					default:
						return;
					}
				}
			}
	}

	private int specialRestoreCycle = 0;
	private int speakTimer = 70;
	public int familiarSpecialEnergy = 60;
	private int loginCycle = -1;
	public int specialTimer = 0;
	public int renewTimer = -1;
	public int actionTimer = 0;
	public boolean attacked = false;

	/**
	 * The "process" for familiars.
	 */
	public void familiarTick() {
		attacked = false;
		if (summonedFamiliar != null && c.getInstance().summoned != null) {
			if (!c.goodDistance(c.getX(), c.getY(), c.getInstance().summoned.getX(), c.getInstance().summoned.getY(),
					8)) {
				callOnTeleport();
			}
			if (!c.goodDistance(c.getX(), c.getY(), c.getInstance().summoned.getX(), c.getInstance().summoned.getY(),
					8)) {
				c.getSummoning().callFamiliar();
				return;
			}
			if (c.getInstance().playerIndex != 0
					&& NPCHandler.npcs[c.getInstance().summoningMonsterId].spawnedBy == c.getId()) {
				NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackingAPerson = true;
			} else if (c.getInstance().playerIndex == 0
					&& NPCHandler.npcs[c.getInstance().summoningMonsterId].spawnedBy == c.getId()) {
				NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackingAPerson = false;
			}
			if (c.getInstance().underAttackBy != 0
					&& NPCHandler.npcs[c.getInstance().summoningMonsterId].spawnedBy == c.getId()) {
				NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackedByPerson = true;
			} else if (c.getInstance().underAttackBy == 0
					&& NPCHandler.npcs[c.getInstance().summoningMonsterId].spawnedBy == c.getId()) {
				NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackedByPerson = false;
			}
			if (c.getInstance().npcIndex != 0
					&& !NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackingAPerson
					&& NPCHandler.npcs[c.getInstance().summoningMonsterId].spawnedBy == c.getId()) {
				NPCHandler.npcs[c.getInstance().summoningMonsterId].IsAttackingNPC = true;
				NPCHandler.npcs[c.getInstance().summoningMonsterId].facePlayer(0);
			} else if (NPCHandler.npcs[c.getInstance().summoningMonsterId].spawnedBy == c.getId()) {
				if (NPCHandler.npcs[c.getInstance().summoningMonsterId].IsAttackingNPC == true)
					callFamiliar();
				NPCHandler.npcs[c.getInstance().summoningMonsterId].IsAttackingNPC = false;
			}
			if (c.getInstance().summoned.killerId != 0 && c.getInstance().summoned.underAttackBy == 0) {
				c.getInstance().summoned.killerId = 0;
				c.getInstance().summoned.underAttackBy = 0;
				c.getSummoning().callFamiliar();
			}
			if (attacked != true) {
				if (NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackedByPerson == true) {
					attacked = true;
					Player o = PlayerHandler.players[c.getInstance().underAttackBy];
					if (o != null) {
						if (o.getInstance().inMulti()) {
							if (actionTimer == 0) {
								GameEngine.npcHandler.followPlayer(c.getInstance().summoningMonsterId, o.playerId,
										c.playerId);
								NPCHandler.npcs[c.getInstance().summoningMonsterId].facePlayer(o.playerId);
								GameEngine.npcHandler.attackPlayer(o, c.getInstance().summoningMonsterId);
								c.getInstance().summoned.updateRequired = true;
								NPCHandler.npcs[c.getInstance().summoningMonsterId].index = o.playerId;
								actionTimer = 7;
							} else {
								actionTimer--;
							}
						}
					} else {
					}
				}
			}

			if (attacked != true) {
				if (NPCHandler.npcs[c.getInstance().summoningMonsterId].isAttackingAPerson == true) {
					attacked = true;
					Player o = PlayerHandler.players[c.getInstance().playerIndex];
					if (o != null) {
						if (o.getInstance().inMulti()) {
							if (actionTimer == 0) {
								GameEngine.npcHandler.followPlayer(c.getInstance().summoningMonsterId, o.playerId,
										c.playerId);
								NPCHandler.npcs[c.getInstance().summoningMonsterId].facePlayer(o.playerId);
								GameEngine.npcHandler.attackPlayer(o, c.getInstance().summoningMonsterId);
								c.getInstance().summoned.updateRequired = true;
								NPCHandler.npcs[c.getInstance().summoningMonsterId].index = o.playerId;
								actionTimer = 7;
							} else {
								actionTimer--;
							}
						}
					} else {
					}
				}
			}

			if (attacked != true) {
				if (NPCHandler.npcs[c.getInstance().summoningMonsterId].IsAttackingNPC == true) {
					attacked = true;
					NPC n = NPCHandler.npcs[c.getInstance().npcIndex];
					if (n != null) {
						// if(n.inMulti()) {
						NPCHandler.npcs[c.getInstance().summoningMonsterId].IsAttackingNPC = true;
						NPCHandler.npcs[c.getInstance().npcIndex].IsUnderAttackNpc = true;
						NPCHandler.npcs[c.getInstance().npcIndex].randomWalk = false;
						// GameEngine.npcHandler.attackNPC(c.npcIndex,
						// c.getVariables().summoningMonsterId, c);
						GameEngine.npcHandler.NpcVersusNpc(c.getInstance().summoningMonsterId,
								c.getInstance().npcIndex, c);
						// }
					} else {
					}
				}
			}
			speakTimer++;
			if (speakTimer == 100) {
				c.getInstance().summoned.forceChat(summonedFamiliar.speakText);
				speakTimer = 0;
			}
			c.getInstance().specRestoreTimer -= 1;
			int hours = (c.getInstance().specRestoreTimer / 2) / 3600;
			int minutes = ((c.getInstance().specRestoreTimer / 2) - hours * 3600) / 60;
			int seconds = ((c.getInstance().specRestoreTimer / 2) - (hours * 3600 + minutes * 60));

			String timer = String.format("%02d:%02d", minutes, seconds);
			c.getPA().sendString(timer, 17021);
			if (c.getInstance().specRestoreTimer == 100) {
				c.sendMessage("@red@Your familiar will run out in approximately 1 minute.");
				c.sendMessage("@red@Warning! Item's stored in familiar will be dropped upon death!");
			} else if (c.getInstance().specRestoreTimer == 50) {
				c.sendMessage("@red@Your familiar will run out in approximately 30 seconds.");
				c.sendMessage("@red@Warning! Item's stored in familiar will be dropped upon death!");
			} else if (c.getInstance().specRestoreTimer == 25) {
				c.sendMessage("@red@Your familiar will run out in approximately 15 seconds.");
				c.sendMessage("@blu@ You can renew your familiar with a new pouch by clicking @red@ Renew pouch");
				c.sendMessage("@red@Warning! Item's stored in familiar will be dropped upon death!");
			} else if (c.getInstance().specRestoreTimer <= 0) {
				dismissFamiliar(false);
			}
			if (familiarSpecialEnergy != 60) {
				specialRestoreCycle++;
				if (specialRestoreCycle == 50) {
					if (familiarSpecialEnergy != 60) {
						familiarSpecialEnergy += 15;
						if (familiarSpecialEnergy >= 60) {
							familiarSpecialEnergy = 60;
						}
					}
					specialRestoreCycle = 0;
				}
			}
			specialTimer--;
		}
		if (renewTimer > 0) {
			renewTimer--;
			if (renewTimer == 0 && c.getInstance().summoned == null && summonedFamiliar != null) {
				c.getInstance().summoned = GameEngine.npcHandler.summonNPC(c, summonedFamiliar.npcId, c.getX(),
						c.getY() + (summonedFamiliar.large ? 2 : 1), c.heightLevel, 0,
						SummoningData.getSummonHealth(summonedFamiliar.npcId), 1, 1, 1);
				callFamiliar();
			}
		}
		if (loginCycle > 0) {
			loginCycle--;
			if (loginCycle == 0 && c.getInstance().summoned == null && summonedFamiliar != null) {
				c.getInstance().summoned = GameEngine.npcHandler.summonNPC(c, summonedFamiliar.npcId, c.getX(),
						c.getY() + (summonedFamiliar.large ? 2 : 1), c.heightLevel, 0,
						SummoningData.getSummonHealth(summonedFamiliar.npcId), 1, 1, 1);
				callFamiliar();
			}
		}
	}
}