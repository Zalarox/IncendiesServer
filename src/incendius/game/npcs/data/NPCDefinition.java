package incendius.game.npcs.data;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Relex lawl / iRageQuit2012
 *
 *         Manages NPC attributes, such as size, attack speed, poison immunity,
 *         etc. Still to add: hitpoints/constitution, attack (accuracy),
 *         defense, combat styles which can be performed.
 */

public class NPCDefinition {

	/**
	 * NPCDefinition array containing every npc definition instance.
	 */
	public static NPCDefinition[] definitions;

	/**
	 * Initializes the reading of xml file containing the item definitions.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */

	public static void init() throws SAXException, IOException, ParserConfigurationException {
		/*
		 * int pointers = 0; long startup = System.currentTimeMillis();
		 * System.out.println("Loading npc definitions..."); definitions = new
		 * NPCDefinition[15002]; File fXmlFile = new File(DIRECTORY);
		 * DocumentBuilderFactory dbFactory =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder dBuilder =
		 * dbFactory.newDocumentBuilder(); Document doc =
		 * dBuilder.parse(fXmlFile); doc.getDocumentElement().normalize();
		 * NodeList nList = doc.getElementsByTagName("npcDefinition"); for (int
		 * temp = 0; temp < nList.getLength(); temp++) { Node nNode =
		 * nList.item(temp); if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 * Element element = (Element) nNode; int id =
		 * Integer.parseInt(getValue("id", element)); try { NPCDefinition
		 * definition = new NPCDefinition(); definition.id = id; String name =
		 * getValue("name", element); int combatLevel =
		 * Integer.parseInt(getValue("combat", element)); int hitpoints =
		 * Integer.parseInt(getValue("hitpoints", element)); int maxHit =
		 * Integer.parseInt(getValue("maxHit", element)); int respawn =
		 * Integer.parseInt(getValue("respawn", element)); int attackspeed =
		 * Integer.parseInt(getValue("attackSpeed", element));
		 * definition.attackAnim = Integer.parseInt(getValue("attackAnim",
		 * element)); definition.defenceAnim =
		 * Integer.parseInt(getValue("defenceAnim", element));
		 * definition.deathAnim = Integer.parseInt(getValue("deathAnim",
		 * element));
		 * 
		 * definition.poisonous = Boolean.parseBoolean(getValue("poisonous",
		 * element)); definition.poisonImmunity =
		 * Boolean.parseBoolean(getValue("poisonImmunity", element));
		 * definitions[id] = definition; pointers++;
		 * 
		 * } catch (Exception exception) { } } } System.out.println("Loaded " +
		 * pointers + " npc definitions in " + (System.currentTimeMillis() -
		 * startup) + "ms");
		 */
	}

	/**
	 * Returns NPCDefinition instance for a specified NPC Id.
	 * 
	 * @param id
	 *            NPC Id to fetch definition for.
	 * @return definitions[id].
	 */
	public static NPCDefinition forId(int id) {
		/*
		 * if (definitions[id] == null) { return null; }
		 */
		return null;
	}

	public static String getValue(String sTag, Element eElement) {
		NodeList nodeList = eElement.getElementsByTagName(sTag);
		Element element = (Element) nodeList.item(0);
		NodeList lastNodeList = element.getChildNodes();
		return lastNodeList.item(0).getNodeValue();
	}

	private int id;

	public int getId() {
		return id;
	}

	private int attackAnim;

	public int getAttackAnimation() {
		return attackAnim;
	}

	public int defenceAnim;

	public int getDefenceAnimation() {
		return deathAnim;
	}

	private int deathAnim;

	public int getDeathAnimation() {
		return deathAnim;
	}

}