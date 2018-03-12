package classproject.bunnyworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Zolbuu on 3/11/18.
 */

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "database";

    // Games and Shapes table name
    private static final String GAME_TABLE = "gameTable";
    private static final String SHAPE_TABLE = "shapeTable";

    // Games Table Columns names
    private static final String KEY_PRIMARY = "id";
    private static final String GAME = "game";
    private static final String PAGE = "page";
    private static final String CURR_PAGE = "currPage";
    private static final String FIRST_PAGE = "firstPage";


    // Shapes Table Column names
    private static final String GAMENAME = "gameName";
    private static final String SHAPE = "shape";
    private static final String PAGENAME = "pageName";
    private static final String IMAGENAME = "imageName";
    private static final String SCRIPT = "script";
    private static final String FONT = "font";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String W = "w";
    private static final String H = "h";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Game table

        // gameName | page  | currPage | firstPage
//        // game1    | page1 |  YES     |  YES
//        // game1    | page2 |  NO      |   NO
//        // game1    | page3 |  NO      |   NO
//        // game1    | page4 |  NO      |   NO
//        // game2    | page1 |  NO      |   YES
//        // game2    | page2 |  YES     |   NO


        String CREATE_GAME_TABLE = "CREATE TABLE " + GAME_TABLE + "("
                + KEY_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GAME + " TEXT,"
                + PAGE + " TEXT,"
                + CURR_PAGE + " TEXT,"
                + FIRST_PAGE + " TEXT " + ")";

        db.execSQL(CREATE_GAME_TABLE);

        // gameName | shape  |  page   |script  |imagename   | fontSize | x | y | w | h
        // game1    | shape1 |  page1  |"hoho"  | "something"|   10     | 1 |   |   |
        // game1    | shape2 |  page1  |        | "something"|          |   |   |   |
        // game1    | shape3 |  page2  |        | "something"|          |   |   |   |
        // game1    | shape4 |  page1  |        | "something"|          |   |   |   |
        // game2    | shape1 |  page3  |        | "something"|          |   |   |   |
        // game2    | shape2 |  page1  |        | "something"|          |   |   |   |

        String CREATE_SHAPES_TABLE = "CREATE TABLE " + SHAPE_TABLE + "("
                + KEY_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GAMENAME + " TEXT,"
                + SHAPE + " TEXT,"
                + PAGENAME + " TEXT,"
                + IMAGENAME + " TEXT,"
                + SCRIPT + " TEXT,"
                + FONT + " INTEGER"
                + X + " NUMERIC,"
                + Y + " NUMERIC,"
                + W + " NUMERIC,"
                + H + " NUMERIC" + ")";

        db.execSQL(CREATE_SHAPES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SHAPE_TABLE);

        // Create tables again
        onCreate(db);

    }


    public void saveGame(Game game) {
        String gameName = game.getName();
        List<GPage> pageList = game.getPages();
        GPage currPage = game.getCurrPage();
        String isCurrentPage = "YES";
        String isFirstPage = "YES";
        for (GPage page : pageList) {
            String pageName = page.getName();
            if (!page.equals(currPage)) {
                isCurrentPage = "NO";
            }
            if (!game.isFirstPage(page)) {
                isFirstPage = "NO";
            }

            addPages(game, page, isCurrentPage, isFirstPage);
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
                addShapes(game, shape, page, imageName, script, font, x, y, w, h);
            }
        }
    }


    /* addPages and addShapes are helper methods for saveGame(Game game).
     *
     */

    // Adding new Game information
    private void addPages(Game game, GPage page, String isCurrentPage, String isFirstPage) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(GAME, game.getName());
        values.put(PAGE, page.getName());
        values.put(CURR_PAGE, isCurrentPage);
        values.put(FIRST_PAGE, isFirstPage);


        // Inserting Row
        db.insert(GAME_TABLE, null, values);
        db.close(); // Closing database connection
    }

    /* Add Shapes to game
     *
     */

    private void addShapes(Game game, GShape shape, GPage page, String imageName, String script,
                         Integer font, float x, float y, float w, float h) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GAMENAME, game.getName());
        values.put(SHAPE, shape.getName());
        values.put(PAGENAME, page.getName());
        values.put(IMAGENAME, imageName);
        values.put(SCRIPT, script);
        values.put(FONT, font);
        values.put(X, x);
        values.put(Y, y);
        values.put(W, w);
        values.put(H, h);

        // Inserting Row
        db.insert(SHAPE_TABLE, null, values);
        db.close(); // Closing database connection
    }



    /* Load data
     * Returns null if a game with the given name does not exist in the database
     */
    public Game loadGameHandler(String gameName) {
        Game game = new Game(gameName);
        List<GPage> pages = new ArrayList<GPage>();

        String query1 = "Select * FROM " + GAME_TABLE + " WHERE " + GAME + "=" + "'" + gameName + "'";

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor1 = db1.rawQuery(query1, null);
        if (!cursor1.moveToFirst()) {
            cursor1.close();
            game = null;

            return game;
        }
        while (cursor1.moveToNext()) {
            String pageName = cursor1.getString(2);
            GPage page = new GPage(pageName);
            String currPage = cursor1.getString(3);
            String firstPage = cursor1.getString(4);
            if (currPage == "YES") {
                game.setCurrPage(page);
            }


            pages.add(page);
            game.addPage(new GPage(pageName));
        }
        cursor1.close();
        db1.close();


        String query2 = "Select * FROM " + SHAPE_TABLE + " WHERE " + GAMENAME + "=" + "'" + gameName + "'";

        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery(query2, null);
        while (cursor2.moveToNext()) {
            String shapeName = cursor2.getString(1);
            String pageName = cursor2.getString(2);
            String imageName = cursor2.getString(3); //  ??
            String script = cursor2.getString(4);
            Integer font = cursor2.getInt(5);
            Float x = cursor2.getFloat(6);
            Float y = cursor2.getFloat(7);
            Float w = cursor2.getFloat(8);
            Float h = cursor2.getFloat(9);

            // shape of the coordinates (x,y) and the width and height of w,h, and the script of "script"
            GShape shape = new GShape(shapeName, x, y);
//            shape.setName();   // ??
            shape.setWidth(w);
            shape.setHeight(h);
            shape.setName(imageName);
            shape.setScriptText(script);
            shape.setFontSize(font);

            game.getPage(pageName).addShape(shape);

            //TODO Add Shapes to Possessions iff y < some number.
            if (y < 100) {   //change 100 later
                game.addPossession(shape);
            }
        }

        cursor2.close();
        db2.close();
        return game;
    }

    // delete a game
    public boolean deleteGameHandler(String gameName) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(GAME_TABLE, GAME + "=" + gameName, null) > 0;

    }


    // delete a shape in game with name gameName
    public boolean deleteShapeHandler(String gameName, String shapeName) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(SHAPE_TABLE, GAMENAME + "=" + gameName + " AND " + SHAPE + "=" + shapeName, null) > 0;
    }

    // Set of all games
    public Set<Game> setAllGames() {
        Set<Game> gameSet = new HashSet<>();
        String query = "Select * FROM " + SHAPE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String gameName = cursor.getString(1);
            Game game = loadGameHandler(gameName);
            gameSet.add(game);
        }
        return gameSet;
    }



}
