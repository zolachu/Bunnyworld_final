package classproject.bunnyworld;

import java.util.HashMap;

/**
 * Script text is set of clauses where words are separated by spaces and clauses
 * are separated by semicolon(;). Clause order is insignificant
 * @author nicholasseay
 *
 */
public  class Script {
	//HashMap scriptInstructions<triggerword, trigger and Action String (raw input)>
	
	//TRIGGERS
	//onclick<action>
	//onenter<actions>
	//ondrop<shape name><actions>

	//ACTIONS
	//goto<page name>
	//play< sound name>
	//hide<shape name>
	//show<shape name>

	
	static void parse(String allInstructions, HashMap<String, String> map) { // <trigger, instruction>
		
	}
	
	/* Given a clause, the Script decides what the action is and what its inputs
	 * are, then performs the desired action
	 */
	static void perform(String instruction) {
		
	}
	//reads the trigger

	static void onClick() {	// wait do actions need to be separated like this?
		
	}
	static void onEnter() {
		
	}
	
	static void onDrop() {
		
	}
}
