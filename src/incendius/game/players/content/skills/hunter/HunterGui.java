package incendius.game.players.content.skills.hunter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import incendius.game.npcs.NPC;

public class HunterGui extends JFrame {

	private static final long serialVersionUID = 1L;
	static String columnNames[] = { "Name:", "Caught:", "X Coord", "Y Coord" };
	private JPanel contentPane;
	BufferedImage image;
	public static boolean showGUI;
	public static JTable table;
	static String dataValues[][] = new String[0][4];

	static JLabel lblKingly, lblDragon, lblPirate, lblNinja, lblMagpie, lblNature, lblEclectic, lblEssence, lblEarth,
			lblGourmet, lblYoung, lblBaby, lblNewLabel;

	public static void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	public HunterGui() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {
		if (!showGUI)
			return;
		HunterGui frame = new HunterGui("Impling list");
		centreWindow(frame);
		frame.setVisible(true);
	}

	public static void setCaught(int i, int npcId) {
		if (!showGUI)
			return;
		HunterNpcs.implingName(npcId, -1);
		dataValues[i - 1][1] = "true";
		try {
			table.setModel(new DefaultTableModel(dataValues, columnNames));
			renameAll();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	public static void renameAll() {
		lblKingly.setText("Kingly: " + HunterNpcs.kinglyImpling);
		lblDragon.setText("Dragon: " + HunterNpcs.dragonImpling);
		lblPirate.setText("Pirate: " + HunterNpcs.pirateImpling);
		lblNinja.setText("Ninja: " + HunterNpcs.ninjaImpling);
		lblMagpie.setText("Magpie: " + HunterNpcs.magpieImpling);
		lblNature.setText("Nature: " + HunterNpcs.natureImpling);
		lblEclectic.setText("Eclectic: " + HunterNpcs.eclecticImpling);
		lblEssence.setText("Essence: " + HunterNpcs.essenceImpling);
		lblEarth.setText("Earth: " + HunterNpcs.earthImpling);
		lblGourmet.setText("Gourmet: " + HunterNpcs.gourmetImpling);
		lblYoung.setText("Young: " + HunterNpcs.youngImpling);
		lblBaby.setText("Baby: " + HunterNpcs.babyImpling);

		lblNewLabel.setText("Amount of spawned implings: " + HunterNpcs.impSpawned);
	}

	public static void newImpling(String name, boolean caught, int x, int y, NPC n) {
		if (!showGUI)
			return;
		String data[][] = new String[dataValues.length + 1][4];
		if (dataValues.length != 0) {
			for (int i = 0; i < dataValues.length; i++) {
				data[i][0] = dataValues[i][0];
				data[i][1] = dataValues[i][1];
				data[i][2] = dataValues[i][2];
				data[i][3] = dataValues[i][3];
			}
		}
		data[dataValues.length][0] = name;
		data[dataValues.length][1] = "" + caught;
		data[dataValues.length][2] = "" + x;
		data[dataValues.length][3] = "" + y;
		dataValues = data;
		n.columnId = dataValues.length;
		if (table != null) {
			try {
				table.setModel(new DefaultTableModel(dataValues, columnNames));
				renameAll();
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
	}

	public HunterGui(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException, IOException {
		super(name);
		setResizable(false);
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		setBounds(100, 100, 612, 434);
		contentPane = new JPanel();
		setContentPane(contentPane);
		table = new JTable(dataValues, columnNames) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.setBounds(10, 10, 574, 300);

		contentPane.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 10, 586, 300);
		contentPane.add(scrollPane);
		table.setDragEnabled(false);

		lblNewLabel = new JLabel("Amount of spawned implings:");
		lblNewLabel.setBounds(10, 321, 202, 14);
		contentPane.add(lblNewLabel);

		lblKingly = new JLabel("Kingly: ");
		lblKingly.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblKingly.setBounds(10, 346, 59, 14);
		contentPane.add(lblKingly);

		lblDragon = new JLabel("Dragon: ");
		lblDragon.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblDragon.setBounds(10, 371, 59, 14);
		contentPane.add(lblDragon);

		lblPirate = new JLabel("Pirate: ");
		lblPirate.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPirate.setBounds(104, 371, 59, 14);
		contentPane.add(lblPirate);

		lblNinja = new JLabel("Ninja:");
		lblNinja.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNinja.setBounds(104, 346, 59, 14);
		contentPane.add(lblNinja);

		lblMagpie = new JLabel("Magpie:");
		lblMagpie.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblMagpie.setBounds(198, 346, 59, 14);
		contentPane.add(lblMagpie);

		lblNature = new JLabel("Nature: ");
		lblNature.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNature.setBounds(198, 371, 59, 14);
		contentPane.add(lblNature);

		lblEclectic = new JLabel("Eclectic: ");
		lblEclectic.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEclectic.setBounds(305, 346, 59, 14);
		contentPane.add(lblEclectic);

		lblEssence = new JLabel("Essence:");
		lblEssence.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEssence.setBounds(305, 371, 59, 14);
		contentPane.add(lblEssence);

		lblEarth = new JLabel("Earth:");
		lblEarth.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEarth.setBounds(419, 346, 59, 14);
		contentPane.add(lblEarth);

		lblGourmet = new JLabel("Gourmet:");
		lblGourmet.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblGourmet.setBounds(419, 371, 59, 14);
		contentPane.add(lblGourmet);

		lblYoung = new JLabel("Young:");
		lblYoung.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblYoung.setBounds(518, 346, 67, 14);
		contentPane.add(lblYoung);

		lblBaby = new JLabel("Baby:");
		lblBaby.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblBaby.setBounds(518, 371, 78, 14);
		contentPane.add(lblBaby);
		table.removeEditor();
		renameAll();
	}
}
