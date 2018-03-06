package classproject.bunnyworld;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicholasseay on 3/5/18.
 * Zola, I wrote some basic functions that I think the game will need but left
 * the more involved ones empty. Please feel free to change variable names, or
 * to change/add functions. This was just what I thought of off the top of my head.
 * 
 */

class GameManager {
   db = SQLiteDatabase.openOrCreateDatabase("games.db", MODE_PRIVATE, null);
        allGames = new HashSet<Game>();
        //deepLoad();

        // gameName | page  | currPage | firstPage | id
        // game1    | page1 |  YES     |  YES      | 0
        // game1    | page2 |  NO      |   NO      | 1
        // game1    | page3 |  NO      |   NO      | 2
        // game1    | page4 |  NO      |   NO      | 3
        // game2    | page1 |  NO      |   YES     | 4
        // game2    | page2 |  YES     |   NO      | 5


        // Table for Pages inside Game
        Cursor pagesCursor = db.rawQuery("SELECT * FROM sqlite_master "
                + "WHERE type='table' AND name='pages';", null);


        if (pagesCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE pages ("
                    + "gameName TEXT, page TEXT,"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";

            db.execSQL(setupStr);

            String dataStr= "INSERT INTO cities VALUES "
                    + "('Game1','Page1', 'YES', 'YES', NULL),"
                    + ");";
            db.execSQL(dataStr);
        }


        // gameName | shape  |  page   |script  | x | y | w | h | id
        // game1    | shape1 |  page1  |"   "   | 1 |   |   |   | 0
        // game1    | shape2 |  page1  |        |   |   |   |   | 1
        // game1    | shape3 |  page2  |        |   |   |   |   | 2
        // game1    | shape4 |  page1  |        |   |   |   |   | 3
        // game2    | shape1 |  page3  |        |   |   |   |   | 4
        // game2    | shape2 |  page1  |        |   |   |   |   | 5

        // Table for Shapes
        Cursor shapesCursor = db.rawQuery("SELECT * FROM sqlite_master "
                + "WHERE type='table' AND name ='shapes';" , null);

        if (shapesCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE shapes ("
                    + "game TEXT, shape TEXT, page TEXT , script TEXT,  x NUMERIC, y NUMERIC, w NUMERIC, h NUMERIC"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(setupStr);
        }
            // We know the coordinates of the shapes => we can find the location of a particular shape
            // and we can know whether it is in the possessions or in a particular shape.

    }

    /* saves all of its games
     */
    public void deepSave() {
        for(Game game: allGames) {
            saveGame(game);
        }
    }

    /* Probably used by Editor to save games
     */
    public void saveGame(Game game) {

        String gameName = game.getName();
        String firstPageName = game.firstPageName();
        String currPageName = game.currPageName();

        Set<GPage> pages = game.pages();
        String curr = "YES";
        String first = "YES";


        for (GPage page : pages) {

            String pageName = page.getName();

            //  INSERT INTO shapes VALUES ('gameName', 'pageName', x, y, w, h, null);
            Set<GShape> shapes = page.shapes();
            for (GShape shape : shapes) {
                String shapeName = shape.getName();
                String script = shape.getScriptText();
                Double x = shape.getX();
                Double y = shape.getY();
                Double w = shape.getWidth();
                Double h = shape.getHeight();


                String setupStr = "INSERT INTO shapes VALUES("
                        + "(\'" + gameName +"\',\'" + shapeName + "\',\'" + pageName + "\',\'" + script + "\',"
                        + x + "," + y + "," + w + "," + h + "," + null + ");";
                db.execSQL(setupStr);
            }


            // INSERT INTO pages VALUES ('gameName', 'pageName', YES, YES, null);


            if (!pageName.equals(curr)) curr = "NO";
            if (!pageName.equals(first)) first = "NO";

            String setupStr = "INSERT INTO pages VALUES("
                    + "(\'" + gameName +"\',\'" + pageName + "\',\'" + curr + "\',\'" + first + "\',\'"
                    + null + ");";
            db.execSQL(setupStr);

        }

    }


    /* saves all of its games
     */
    public void deepSave() {
        for(Game game: allGames) {
            saveGame(game);
        }
    }

    /* Probably used by Editor to save games
     */
    public void saveGame(Game game) {

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
    public void loadGame(String gameName) {
        // load from database
        getGame(gameName);
    }

    /* returns a game referred to by gameName
     * to the asking class. Returns null if a game
     * by that name does not exist.
     */
    public Game getGame(String gameName) {
        for (Game game : allGames) {
            if(game.getName().equals(gameName)) return game;
        }
        return null;
    }
}
