package incendius.game.players.content.skills.construction;

/**
 * 
 * @author TheLife
 *
 */

public class ConstructionData {

	public int OBJECT_TYPE = 0, NAIL_TYPE = 0, NAIL_AMOUNT = 0, OBJECT_AMOUNT = 0, OBJECT_ID = 0, LEVEL_NEEDED = 0,
			EXP_GAIN = 0, LOAD_AMOUNT = 0, SET_PLACE = 0, HEIGHT = 0, FACE_ID = 0, LOAD_TIMER = 0,
			INTERFACE_TIMER_V1 = 0, INTERFACE_TIMER_V2 = 0, INTERFACE_TIMER_V3 = 0;

	public int[] CONSTRUCTION_ID = new int[300], CONSTRUCTION_FACE_ID = new int[300],
			CONSTRUCTION_LOCATION_X = new int[300], CONSTRUCTION_LOCATION_Y = new int[300],
			CONSTRUCTION_ID_OTHERS = new int[300], CONSTRUCTION_FACE_OTHERS = new int[300],
			CONSTRUCTION_LOCATION_X_OTHERS = new int[300], CONSTRUCTION_LOCATION_Y_OTHERS = new int[300];

	public static final int BRONZE_NAILS = 4819, IRON_NAILS = 4820, BLACK_NAILS = 4821, MITHRIL_NAILS = 4822,
			ADAMANT_NAILS = 4823, RUNE_NAILS = 4824;

	public String OBJECT_NAME = "", NAIL_NAME = "", TYPE = "", UPDATER = "";

	public boolean WALKING_WAY = false, OBJECTS_CAN_LOAD = false, OWNS_HOUSE = false, NEEDS_SAW = false,
			NAILS_NEEDED = false, CAN_GO_HOUSES = false, CAN_MAKE_OBJECTS = false, HOUSE_IS_LOCKED = false,
			KICKED_EVERYONE = false, IN_BUILDING_MODE = false, INTERFACE_LOADED_V1 = false, INTERFACE_LOADED_V2 = false,
			LOADED = false, IN_FRIENDS_HOUSE = false;

}
