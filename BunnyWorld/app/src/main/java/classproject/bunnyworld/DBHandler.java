package classproject.bunnyworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zolbuu on 3/11/18.
 */

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myDatabase";

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
    private static final String GAMENAME ="gameName";
    private static final String SHAPE = "shape";
    private static final String PAGENAME = "pageName";
    private static final String IMAGENAME = "imageName";
    private static final String SCRIPT = "script";
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

        // gameName | shape  |  page   |script  |imagename   | x | y | w | h
        // game1    | shape1 |  page1  |"hoho"  | "something"| 1 |   |   |
        // game1    | shape2 |  page1  |        | "something"|   |   |   |
        // game1    | shape3 |  page2  |        | "something"|   |   |   |
        // game1    | shape4 |  page1  |        | "something"|   |   |   |
        // game2    | shape1 |  page3  |        | "something"|   |   |   |
        // game2    | shape2 |  page1  |        | "something"|   |   |   |

        String CREATE_SHAPES_TABLE= "CREATE TABLE " + SHAPE_TABLE + "("
                + KEY_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GAMENAME + " TEXT,"
                + SHAPE + " TEXT,"
                + PAGENAME + " TEXT,"
                + IMAGENAME + " TEXT,"
                + SCRIPT + " TEXT,"
                + X + " REAL,"
                + Y + " REAL,"
                + W + " REAL,"
                + H + " REAL" + ")";

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


    /* Add Game
     *
     */
    // Adding new Game information
    void addGame(Game game, GPage page) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(GAME, game.getName());
        values.put(PAGE, page.getName());
        values.put(CURR_PAGE, game.getCurrPage().getName());
        values.put(FIRST_PAGE, game.getFirstPage().getName());


        // Inserting Row
        db.insert(GAME_TABLE, null, values);
        db.close(); // Closing database connection
    }

    /* Add Shape to game in database
     *
     */

    void addShape(Game game, GShape shape, GShape page, String imageName, String script, float x, float y, float w, float h) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GAMENAME, game.getName());
        values.put(SHAPE, shape.getName());
        values.put(PAGENAME, page.getName());
        values.put(IMAGENAME, imageName);
        values.put(SCRIPT, script);
        values.put(X, x);
        values.put(Y, y);
        values.put(W, w);
        values.put(H, h);

        // Inserting Row
        db.insert(SHAPE_TABLE, null, values);
        db.close(); // Closing database connection
    }

    /* Find Game from database
     *
     */

    public Game findGame(String gameName) {
        String query = "Select * FROM " + GAME_TABLE + " WHERE " + GAME + " = " + "'" + gameName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Game game = new Game(gameName);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
//            game.setCurrPage(cursor.getString(3));
            game.setName(cursor.getString(1));
            cursor.close();
        } else {
            game = null;
        }
        db.close();
        return game;
    }



    // delete a game
    public boolean deleteGame(String gameName) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(GAME_TABLE, GAME + "=" + gameName, null) > 0;

    }


    // delete a shape in game with gameName
    public boolean deleteShape(String gameName, String shapeName) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(SHAPE_TABLE, GAMENAME + "=" + gameName + " AND " + SHAPE + "=" + shapeName, null) > 0;
    }



    /*
     *  load data
     */
    public Game loadGame(String gameName) {
        Game game = new Game(gameName);
        List<GPage> pages = new ArrayList<GPage>();

        String query1 = "Select * FROM " + GAME_TABLE + " WHERE " + GAME + " = " + "'" + gameName + "'";

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor1 = db1.rawQuery(query1, null);
        while (cursor1.moveToNext()) {
            String pageName = cursor1.getString(1);
            GPage page = new GPage(pageName);
            pages.add(page);
            game.addPage(new GPage(pageName));
        }
        cursor1.close();
        db1.close();


        String query2 = "Select * FROM " + SHAPE_TABLE + " WHERE" + GAMENAME + "=" + "'" + gameName + "'";

        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery(query2, null);
        while (cursor2.moveToNext()) {
            String shapeName = cursor2.getString(1);
            String pageName = cursor2.getString(2);
            String imageName = cursor2.getString(3); //  ??
            String script = cursor2.getString(4);
            Float x = cursor2.getFloat(5);
            Float y = cursor2.getFloat(6);
            Float w = cursor2.getFloat(7);
            Float h = cursor2.getFloat(8);

            // shape of the coordinates (x,y) and the width and height of w,h, and the script of "script"
            GShape shape = new GShape(shapeName, x, y);
//            shape.setName();   // ??
            shape.setWidth(w);
            shape.setHeight(h);
            shape.setScriptText(script);
            shape.setScriptText(script);

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

}
