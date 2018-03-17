package classproject.bunnyworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String DATABASE_NAME = "database.db";

    // Games and Shapes table name
    private static final String TABLE = "DataTable";

    // Table column names
    private static final String ID   = "id";
    private static final String GAME = "game";
    private static final String PAGE = "page";
    private static final String CURRENTPAGE = "currentPage";
    private static final String SHAPE = "shape";
    private static final String CREATE_SHAPES_TABLE = "CREATE TABLE " + TABLE + "("
            + GAME + " TEXT,"
            + PAGE + " TEXT,"
            + CURRENTPAGE + " TEXT,"
            + SHAPE + " TEXT,"
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + ");";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating Tables
    // Table format
    // gameName | page   | shape
    // game1    | page1  |
    // game1    |        |
    // game1    |        |
    // game1    |  page1 |
    // game2    | page3  |
    // game2    |        |

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHAPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }

    public void gameToTable(Game game) {

        SQLiteDatabase db = this.getWritableDatabase();
        String gameName = game.getName();

        // delete the existing entries corresponding to the game if any
        db.delete(TABLE, GAME + "=?",
                new String[]{gameName});

        // for all pages in the game
        for (GPage page : game.getPages()) {
            String pageName = page.getName();
            String isCurrentPage = "YES";
            if (!page.equals(game.getCurrPage())) {
                isCurrentPage = "NO";
            }

            // for all shapes in the page
            for (GShape shape : page.getShapes()) {
                String shapeName = shape.getName();
                String text      = shape.getText();
                String imageName = shape.getPictureName();
                String script    = shape.getScript();
                String font      = Integer.toString(shape.getFontSize());
                String x = Float.toString(shape.getX());
                String y = Float.toString(shape.getY());
                String w = Float.toString(shape.getWidth());
                String h = Float.toString(shape.getHeight());
                String hidden  = String.valueOf(shape.isHidden());
                String movable = String.valueOf(shape.isMovable());

                String shapeInfo =  shapeName + "," + text + "," + imageName + "," +
                        script + "," + font + "," +
                        x + "," + y + "," + w + "," + h + "," +
                        hidden + "," + movable;

                ContentValues values = new ContentValues();
                values.put(GAME, gameName);
                values.put(PAGE, pageName);
                values.put(CURRENTPAGE, isCurrentPage);
                values.put(SHAPE, shapeInfo);

                // Inserting the game data into database
                db.insert(TABLE, null, values);
            } // end of shape loop
        } // end of page loop

        for (GShape shape : game.getPossessions()) {
            String shapeName = shape.getName();
            String text      = shape.getText();
            String imageName = shape.getPictureName();
            String script    = shape.getScript();
            String font      = Integer.toString(shape.getFontSize());
            String x = Float.toString(shape.getX());
            String y = Float.toString(shape.getY());
            String w = Float.toString(shape.getWidth());
            String h = Float.toString(shape.getHeight());
            String hidden  = String.valueOf(shape.isHidden());
            String movable = String.valueOf(shape.isMovable());

            String shapeInfo =  shapeName + "," + text + "," + imageName + "," +
                    script + "," + font + "," +
                    x + "," + y + "," + w + "," + h + "," +
                    hidden + "," + movable;

            ContentValues values = new ContentValues();
            values.put(GAME, gameName);
            values.put(PAGE, "possessionArea");
            values.put(CURRENTPAGE, "NO");
            values.put(SHAPE, shapeInfo);

            // Inserting the game data into database
            db.insert(TABLE, null, values);
        }

        db.close();
    }


    /* Load data
     * Returns null if a game with the given name does not exist in the database
     */
    public Set<Game> tableToGame() {
        SQLiteDatabase db = this.getReadableDatabase();

        // get a list of game names
        String queryForGameNames = "SELECT DISTINCT " + GAME + " FROM " + TABLE + ";";
        Cursor cursorForGameNames = db.rawQuery(queryForGameNames, null);
        List<String> gameNames = new ArrayList<>();
        while (cursorForGameNames.moveToNext()) {
             gameNames.add(cursorForGameNames.getString(0));
        }
        cursorForGameNames.close();

        Set<Game> games = new HashSet<Game>();
        //for all game names
        for (String gameName : gameNames) {

            //get a cursor for a given game
            String queryForAGame = "Select * FROM " + TABLE + " WHERE " + GAME + "=" + "'" + gameName + "'";
            Cursor cursorForAGame = db.rawQuery(queryForAGame, null);
            Game game = new Game(gameName);
            games.add(game);

            // loops over the entries in game with cursor
            boolean isFirstPage = true;
            while (cursorForAGame.moveToNext()) {

                /*GAME + " TEXT,"
                        + PAGE + " TEXT,"
                        + CURRENTPAGE + " TEXT,"
                        + SHAPE + " TEXT,"
                        + ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ");";*/

                String pageName = cursorForAGame.getString(1);
                String isCurrentPage = cursorForAGame.getString(2);
                String shapeInfo = cursorForAGame.getString(3);

                //if the page is new, then add to the game
                if (!game.duplicatePageName(pageName) && !pageName.equals("possessionArea")) {
                    GPage newPage = new GPage(pageName);
                    game.addPage(newPage);
                }

                //TODO do something about the currpage and firstpage (set up game)
                // if first in the list, make it a first page
                if (isFirstPage) {
                    game.setFirstPage(game.getPage(pageName));
                    isFirstPage = false;
                }

                //if current page, make it a current page
                if (isCurrentPage.equals("YES")) {
                    game.setCurrPageForDB(game.getPage(pageName));
                }

                // parse and get a shape object
                String[] oneShapeParts = shapeInfo.split(",");
                String shapeName = oneShapeParts[0];
                String text = oneShapeParts[1];
                String imageName = oneShapeParts[2];
                String script    = oneShapeParts[3];
                String font = oneShapeParts[4];
                String x    = oneShapeParts[5];
                String y    = oneShapeParts[6];
                String width  = oneShapeParts[7];
                String height = oneShapeParts[8];
                String hidden  = oneShapeParts[9];
                String movable = oneShapeParts[10];

                int type;
                if (!text.isEmpty()) {
                    type = GShape.TEXT;
                } else {
                    type = GShape.IMAGE;
                }

                GShape newShape = new GShape(shapeName, Float.valueOf(x), Float.valueOf(y), text, type);

                if (!imageName.isEmpty()) newShape.setPictureName(imageName);
                if (!script.isEmpty()) newShape.setScriptText(script);

                //TODO also populate the scriptMap

                if (!font.isEmpty()) newShape.setFontSize(Integer.valueOf(font));

                if (!height.isEmpty()) {
                    newShape.setHeight(Float.parseFloat(height));
                }
                if (!width.isEmpty()) {
                    newShape.setWidth(Float.parseFloat(width));
                }

                if (!movable.isEmpty()) {
                    newShape.setMovable(Boolean.valueOf(movable));
                }
                if (!hidden.isEmpty()) {
                    newShape.setHidden(Boolean.valueOf(hidden));
                }

                // add newShape to the current page's list of shapes
                if (pageName.equals("possessionArea")) {
                    game.addPossession(newShape);
                } else {
                    game.getPage(pageName).addShape(newShape);
                }
            }
            cursorForAGame.close();
        }// end of for loop over game names
        db.close();

        return games;
    }
}
