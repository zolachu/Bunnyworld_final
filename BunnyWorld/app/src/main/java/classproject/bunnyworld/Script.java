package classproject.bunnyworld;

import java.util.HashMap;
import java.lang.String;

/**
 * Script text is set of clauses where words are separated by spaces and clauses
 * are separated by semicolon(;). Clause order is insignificant
 *
 * TRIGGERS
 * onclick<action>
 * onenter<actions>
 * ondrop<shape name><actions>
 *
 * ACTIONS
 * goto<page_name>
 * play< sound_name>
 * hide<shape_name>
 * show<shape_name>
 * @author nicholasseay
 *
 */
public  class Script {

	/* Populates the given map using the string of raw script text
	 * keys will be one of the Triggers above, and the value will be the complete
	 * list of instructions to be completed in the event of the trigger.
	 */
	static void parse(String scriptText, HashMap<String, String> map) { // <trigger, instruction>
		String[] allInstructions = scriptText.split(";");

		for(String instruct: allInstructions) {
			int index = instruct.indexOf(' ');
			String trigger = instruct.substring(0, index);
			String action = instruct.substring(index + 1); // eliminate space at the end
			map.put(trigger, action);
		}
	}
	
	/* Given a clause, the Script decides what the action is and what its inputs
	 * are, then performs the desired action
	 */
	static void perform(String instruct) {
		if(instruct == null) return;

		int index = instruct.indexOf(' ');
		String action = instruct.substring(0, index);
		String param = instruct.substring(index + 1); // eliminate space at the end

		switch (action) {
			case "goTo":
				goTo(param);
				break;
			case "play":
				play(param);
				break;
			case "hide":
				hide(param);
				break;
			case "show":
				show(param);
				break;
		}
	}

	//reads the trigger

	/* switches game's current page
	 * this function assumes only one page
	 */
	static void goTo(String param) {

	}

	/* plays the sounds given by param
	 * (switches their hidden boolean to true)
	 */
	static void play(String param) {

	}

	/* hides the shapes given by param
	 */
	static void hide(String param) {

	}

	/* shows the shapes given by param
	 * (switches their hidden boolean to false)
	 */
	static void show(String param) {

	}


}
