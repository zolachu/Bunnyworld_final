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
     * on click <action>
     * on enter <actions>
     * on drop <shape name><actions>
     * <p>
     * ACTIONS
     * goto <page_name>
     * play < sound_name>
     * hide <shape_name>
     * show <shape_name>
     *
     * @author nicholasseay
     */

        /* Populates the given map using the string of raw script text
         * keys will be one of the Triggers above, and the value will be the complete
         * list of instructions to be completed in the event of the trigger.
         */
    void parse(String scriptText, HashMap<String, String> scriptMap) { // <trigger, instruction>
        String[] allInstructions = scriptText.split(";");

        for (String instruct : allInstructions) {
            int index = instruct.indexOf(' ');
            String trigger = instruct.substring(0, index);
            String action = instruct.substring(index + 1); // eliminate space at the end
            scriptMap.put(trigger, action);
        }
    }

    /* Given a clause, the Script decides what the action is and what its inputs
     * are, then performs the desired action
     */
    void perform(Game game, String instruct) {
        if (instruct == null) return;

        int index = instruct.indexOf(' ');
        String action = instruct.substring(0, index);
        String param = instruct.substring(index + 1); // eliminate space at the end

        switch (action) {
            case "goTo":
                goTo(game, param);
                break;
            case "play":
                play(param);
                break;
            case "hide":
                hideOrShow(game, param, true);
                //updateCanvas();
                break;
            case "show":
                hideOrShow(game, param, false);
                //updateCanvas();
                break;
        }
    }

    /* switches game's current page
     * this function assumes only one page
     */
    void goTo(Game game, String param) {
        GPage page = game.getPage(param);
        if (page != null) game.setCurrPage(page);
    }

    /* plays the sounds given by param
     * assumes there are no spaces within file names
     */
    void play(String param) {
        //MediaPlayer mp = new MediaPlayer();
        String[] soundsToPlay = param.split(" ");
        for (String sound : soundsToPlay) {
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

    }

    /* hides or shows the shapes given by param
     * based on the boolean passed into hide.
     * If hide is true, shape will be hidden.
     * (switches their hidden boolean to true)
     * assumes shape names contain no spaces
     */
    void hideOrShow(Game game, String param, boolean hide) {
        String[] shapesToHide = param.split(" ");
        for (String shapeName : shapesToHide) {
            GShape curr = game.getShape(shapeName);
            if (curr != null) curr.setHidden(hide);
        }
    }
}
