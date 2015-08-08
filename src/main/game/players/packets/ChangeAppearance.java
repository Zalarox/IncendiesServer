package main.game.players.packets;

import main.game.players.PacketType;
import main.game.players.Player;

public class ChangeAppearance implements PacketType {

	private static final int[][] MALE_VALUES = { { 0, 8 }, // head
			{ 10, 17 }, // jaw
			{ 18, 25 }, // torso
			{ 26, 31 }, // arms
			{ 33, 34 }, // hands
			{ 36, 40 }, // legs
			{ 42, 43 }, // feet
	};

	private static final int[][] FEMALE_VALUES = { { 45, 54 }, // head
			{ -1, -1 }, // jaw
			{ 56, 60 }, // torso
			{ 61, 65 }, // arms
			{ 67, 68 }, // hands
			{ 70, 77 }, // legs
			{ 79, 80 }, // feet
	};

	private static final int[][] ALLOWED_COLORS = { { 0, 11 }, // hair color
			{ 0, 15 }, // torso color
			{ 0, 15 }, // legs color
			{ 0, 5 }, // feet color
			{ 0, 7 } // skin color
	};

	@Override
	public void processPacket(final Player client, final int packetType, final int packetSize) {
		final int gender = client.getInStream().readSignedByte();
		if (client.getInstance().teleTimer > 0) {
			return;
		}
		if (gender != 0 && gender != 1)
			return;

		final int[] apperances = new int[MALE_VALUES.length]; // apperance's
																// value
		// check
		for (int i = 0; i < apperances.length; i++) {
			int value = client.getInStream().readSignedByte();
			if (value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0])
					|| value > (gender == 0 ? MALE_VALUES[i][1] : FEMALE_VALUES[i][1]))
				value = (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]);
			apperances[i] = value;
		}

		final int[] colors = new int[ALLOWED_COLORS.length]; // color value
																// check
		for (int i = 0; i < colors.length; i++) {
			int value = client.getInStream().readSignedByte();
			if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
				value = ALLOWED_COLORS[i][0];
			colors[i] = value;
		}

		if (client.getInstance().canChangeAppearance) {
			client.getInstance().playerAppearance[0] = gender; // gender
			client.getInstance().playerAppearance[1] = apperances[0]; // head
			client.getInstance().playerAppearance[2] = apperances[2]; // torso
			client.getInstance().playerAppearance[3] = apperances[3]; // arms
			client.getInstance().playerAppearance[4] = apperances[4]; // hands
			client.getInstance().playerAppearance[5] = apperances[5]; // legs
			client.getInstance().playerAppearance[6] = apperances[6]; // feet
			client.getInstance().playerAppearance[7] = apperances[1]; // beard
			client.getInstance().playerAppearance[8] = colors[0]; // hair
																	// colour
			client.getInstance().playerAppearance[9] = colors[1]; // torso
																	// colour
			client.getInstance().playerAppearance[10] = colors[2]; // legs
																	// colour
			client.getInstance().playerAppearance[11] = colors[3]; // feet
																	// colour
			client.getInstance().playerAppearance[12] = colors[4]; // skin
																	// colour

			client.getPA().removeAllWindows();
			client.getPA().requestUpdates();
			client.getInstance().canChangeAppearance = false;
		}
	}

}