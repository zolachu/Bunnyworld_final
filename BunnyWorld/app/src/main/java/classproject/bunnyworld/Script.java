package classproject.bunnyworld;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;

/**
 * Created by nicholasseay on 3/6/18.
 */

public class Script {

    /**
     * Script text is set of clauses where words are separated by spaces and clauses
     * are separated by semicolon(;). Clause order is insignificant
     * <p>
     * TRIGGERS
     *                                  *** Note: no space within trigger words***
     * onclick <action>
     * onenter <actions>
     * ondrop <shape name><actions>
     * <p>
     * ACTIONS
     * goto <page_name>
     * play < sound_name>
     * hide <shape_name>
     * show <shape_name>
     *
     *
     * Map populated will be in format:
     * HashMap<String (trigger), String[] (actions)>
     *
     * @author nicholasseay
     */

    // Access to TRIGGERS
    static final String ON_CLICK = "onClick";
    static final String ON_ENTER = "onEnter";
    static final String ON_DROP  = "onDrop";

    // Access to actions
    static final String GO_TO = "goto"; // format different because goto is a keyword in Java apparently
    static final String PLAY  = "play";
    static final String HIDE  = "hide";
    static final String SHOW  = "show";


    // ondrop has extra string in argument which is the thing being dropped on the object
    // it will be the first element of the actionArray as denoted below
    static final int ON_DROP_SENDER_INDEX = 0;



    /* Populates the given map using the string of raw script text
         * keys will be one of the Triggers above, and the value will be the complete
         * list of instructions to be completed in the event of the trigger.
         */
    static void parse(String scriptText, HashMap<String, String[]> scriptMap) { // <trigger, instruction>
        String[] allInstructions = scriptText.split(";");

        for (String instruct : allInstructions) {
            int spaceIndex = instruct.indexOf(' ');
            String trigger = instruct.substring(0, spaceIndex);
            String action = instruct.substring(spaceIndex + 1); // eliminate space at the beginning
            String[] actionArray = action.split(" ");
            scriptMap.put(trigger, actionArray);
        }
    }

    /* Given an array of actions and targets, the Script decides what the action is and what its inputs
     * are, then performs the desired action
     */
    static void perform(Game game, String[] actionArray) {
        if (actionArray == null) return;

        int i = 0;
        if(actionArray.length % 2 == 1) i++; // [0] was dragged and dropped item aka sender from ondrop trigger

        // loop used to check actions and parameters
        for(int j = i; j < actionArray.length; j+=2) {


            //int index = instruct.indexOf(' ');
            //String action = instruct.substring(0, index);
            //String param = instruct.substring(index + 1); // eliminate space at the end

            String action = actionArray[j];
            String param = actionArray[j+1];

            switch (action) {
                case GO_TO:
                    goTo(game, param);
                    break;
                case PLAY:
                    play(param);
                    break;
                case HIDE:
                    hideOrShow(game, param, true);
                    //updateCanvas();
                    break;
                case SHOW:
                    hideOrShow(game, param, false);
                    //updateCanvas();
                    break;
            }
        }
    }

    /* switches game's current page
     * this function assumes only one page
     */
    static void goTo(Game game, String pageName) {
        GPage page = game.getPage(pageName);
        if (page != null) game.setCurrPage(page);
    }

    /* plays the sounds given by param
     * assumes there are no spaces within file names
     */
    static void play(String sound) {
        try {
            Context cont = GameManager.getInstance().getGameView().getContext();
            int resID = cont.getResources().getIdentifier(sound, "raw", cont.getPackageName());
            MediaPlayer mp = MediaPlayer.create(cont, resID);
            //AssetFileDescriptor descriptor = res.openRawResourceFd("R.raw." + sound);
            //FileDescriptor fd = descriptor.getFileDescriptor();

            //mp.setDataSource(fd);
            //mp.prepare();
            mp.start();
            while (mp.isPlaying()) {
            } // for now freezes while sound plays
            mp.stop();
            mp.release();

            // need to close data descriptor
        } catch (Exception e) {
            // file by that name does not exist
            //toast maybe
        }
    }

    /* hides or shows the shapes given by param
     * based on the boolean passed into hide.
     * If hide is true, shape will be hidden.
     * (switches their hidden boolean to true)
     * assumes shape names contain no spaces
     */
    static void hideOrShow(Game game, String shapeName, boolean hide) {
        GShape shape = game.getShape(shapeName);
        if (shape != null) shape.setHidden(hide);
    }
}
