package classproject.bunnyworld;

import android.view.View;
import android.app.Activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nicholasseay on 3/5/18.
 * Zola, I wrote some basic functions that I think the game will need but left
 * the more involved ones empty. Please feel free to change variable names, or
 * to change/add functions. This was just what I thought of off the top of my head.
 */

class GameManager {
    private Set<Game> allGames; // essentially active games for edit or play
    private GameView gameView;
    private Game curGame;

    private String curScript = "";
    private DBHandler db;

    private static final GameManager ourInstance = new GameManager();

    static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
        allGames = new HashSet<Game>();
        gameView = null;
        //deepLoad();
    }

    public void setDb(DBHandler db) {
        this.db = db;
    }

    /*
     * Saves all of its games
     */
    public void deepSave() {
        for (Game game : allGames) {
            saveGame(game);
        }
    }

    /* Probably used by Editor to save games
     */
    public void saveGame(Game game) {
        String gameName = game.getName();
        List<GPage>  pageList = game.getPages();
        GPage currPage = game.getCurrPage();
        String isCurrentPage = "YES";
        String isFirstPage = "YES";
        for (GPage page : pageList) {
            String pageName = page.getName();
            if (!page.equals(currPage)) {
                isCurrentPage = "NO";
            }
            if(!game.isFirstPage(page)) {
                isFirstPage = "NO";
            }

            db.addGameHandler(game, page, isCurrentPage, isFirstPage);
            List<GShape> shapes = page.getShapes();
            for (GShape shape : shapes) {
                String shapeName = shape.getName();
                String imageName = shape.getPictureName();
                String script = shape.getScript();
                Integer font = shape.getFontSize();
                Float x = shape.getX();
                Float y = shape.getY();
                Float w = shape.getWidth();
                Float h = shape.getHeight();
                db.addShapeHandler(game, shape, page, imageName, script, font, x, y, w, h);
            }
        }

    }


    /* used when booting up the game on phone
     * runs through underlying database and
     * creates all game instances, then saves them
     * to the list of games
     * Probably would be needed while in editor but not
     * necessarily when playing a single game.
     */
    private void deepLoad() {


    }

    /* Checks underlying database for a game by
     * its name, then creates an instance of the
     * game if it exists, putting it in its set of
     * games. Returns game or null or none exists
     */
    public Game loadGame(String gameName) {
        // load from database
        Game game = db.loadGameHandler(gameName);
        return game;

//        getGame(gameName);
    }

    /* returns a game referred to by gameName
     * to the asking class. Returns null if a game
     * by that name does not exist.
     */
    public Game getGame(String gameName) {
        for (Game game : allGames) {
            if (game.getName().toLowerCase().equals(gameName.toLowerCase())) {
                return game;
            }
        }
        return null;
    }


    public void setGameView(Activity activity) {
        gameView = (GameView) activity.findViewById(R.id.myCanvas);
    }

    /* grants access to the GameView
     */
    public GameView getGameView() {
        return gameView;
    }


    // Below are Cindy's changes
    // set the current game
    public void setCurGame(String gameName) {
        for (Game game : allGames) {
            String curGameName = game.getName().toLowerCase();
            if (curGameName.equals(gameName.toLowerCase())) {
                curGame = game;
                return;
            }
        }

        curGame = loadGame(gameName);
        if (curGame == null) {
            curGame = new Game(gameName);
        }
        GPage firstPage = curGame.getFirstPage();
        db.addGameHandler(curGame, firstPage, "YES", "YES");

//        allGames.add(curGame);

    }

    public void addGameToList(Game game) {
        allGames.add(game);
    }

    // get the current game
    public Game getCurGame() {
        return curGame;
    }

    /*
     * Check for duplicate game name
     * returns true when duplicate name exists
     * returns false when game name has no duplicates
     */
    public boolean duplicateGameName(String name) {

        Game game = db.loadGameHandler(name);
        if (game != null) {
            return true;
        }
        return false;

//        for (Game game : allGames) {
//            String curGameName = game.getName().toLowerCase();
//            if (curGameName.equals(name.toLowerCase())) {
//                return true;
//            }
//        }
//        return false;
    }

    public void setCurScript(String script) {
        curScript = script;
    }

    public String getCurScript() {
        return curScript;
    }

    //zola adds her stuff here

}
