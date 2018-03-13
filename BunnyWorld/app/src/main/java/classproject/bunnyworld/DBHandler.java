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
    private static final String DATABASE_NAME = "DatabeH";

    // Games and Shapes table name
    private static final String SHAPE_TABLE = "Tabless";

    // Table column names
    private static final String KEY_PRIMARY = "id";
    private static final String GAME = "game";
    private static final String PAGE = "page";
    private static final String CURR_PAGE = "currPage";
    private static final String FIRST_PAGE = "firstPage";;
    private static final String SHAPE = "shape";
    private static final String IMAGE = "imageName";
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

        // Create table

        // gameName | page   | isCurrPage | isFirstPage | shape   |script     |imagename   | fontSize | x | y | w | h
        // game1    | page1  |            |             |  shape1 |"hoho"     | "something"|   10     | 1 |   |   |
        // game1    |        |            |             |  shape2 |"something"|            |          |   |   |   |
        // game1    |        |            |             |  shape3 |"something"|            |          |   |   |   |
        // game1    |  page1 |            |             |  shape4 |"something"|            |          |   |   |   |
        // game2    | page3  |            |             | shape1  |"something"|            |          |   |   |   |
        // game2    |        |            |             | shape2  |  "   "    | "something"|          |   |   |   |

        String CREATE_SHAPES_TABLE = "CREATE TABLE " + SHAPE_TABLE + "("
                + GAME + " TEXT,"
                + PAGE + " TEXT,"
                + CURR_PAGE + " TEXT,"
                + FIRST_PAGE + " TEXT,"
                + SHAPE + " TEXT,"
                + IMAGE + " TEXT,"
                + SCRIPT + " TEXT,"
                + FONT + " INTEGER,"
                + X + " NUMERIC,"
                + Y + " NUMERIC,"
                + W + " NUMERIC,"
                + H + " NUMERIC,"
                + KEY_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        db.execSQL(CREATE_SHAPES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);
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


                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(GAME, game.getName());
                values.put(PAGE, page.getName());
                values.put(CURR_PAGE, isCurrentPage);
                values.put(FIRST_PAGE, isFirstPage);
                values.put(SHAPE, shape.getName());
                values.put(IMAGE, imageName);
                values.put(SCRIPT, script);
                values.put(FONT, font);
                values.put(X, x);
                values.put(Y, y);
                values.put(W, w);
                values.put(H, h);

                // Inserting Row
                db.insert(SHAPE_TABLE, null, values);
                db.close(); // Closing database connection
//                addShapes(game, page, isCurrentPage, isFirstPage, shape, imageName, script, font, x, y, w, h);
            }
        }
    }




    /* Load data
     * Returns null if a game with the given name does not exist in the database
     */
    public Game loadGameHandler(String gameName) {
        Game game = new Game(gameName);

        String query1 = "Select * FROM " + SHAPE_TABLE + " WHERE " + GAME + "=" + "'" + gameName + "'";

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            game = null;

            return game;
        }
        while (cursor.moveToNext()) {
            String pageName = cursor.getString(1);
            GPage page = new GPage(pageName);
            String currPage = cursor.getString(2);
            String firstPage = cursor.getString(3);
            if (currPage == "YES") {
                game.setCurrPage(page);
            }
            String shapeName = cursor.getString(4);
            String imageName = cursor.getString(5); //  ??
            String script = cursor.getString(6);
            Integer font = cursor.getInt(7);
            Float x = cursor.getFloat(8);
            Float y = cursor.getFloat(9);
            Float w = cursor.getFloat(10);
            Float h = cursor.getFloat(11);

            GShape shape = new GShape(shapeName, x, y);
//            shape.setName();   // ??
            shape.setWidth(w);
            shape.setHeight(h);
            shape.setName(imageName);
            shape.setScriptText(script);
            shape.setFontSize(font);

            page.addShape(shape);   // add shape to page


            game.addPage(page);   // add page to game

            //TODO Add Shapes to Possessions iff y < some number.
            if (y < 100) {   //change 100 later
                game.addPossession(shape);
            }
        }
        cursor.close();
        db1.close();

        return game;
    }
}
