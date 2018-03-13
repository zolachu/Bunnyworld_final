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
    private static final String DATABASE_NAME = "Datagb";

    // Games and Shapes table name
    private static final String SHAPE_TABLE = "Tabless";

    // Table column names
    private static final String KEY_PRIMARY = "id";
    private static final String GAME = "game";
    private static final String PAGE = "page";
    private static final String SHAPE = "shape";



    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create table

        // gameName | page   | shape
        // game1    | page1  |
        // game1    |        |
        // game1    |        |
        // game1    |  page1 |
        // game2    | page3  |
        // game2    |        |

        String CREATE_SHAPES_TABLE = "CREATE TABLE " + SHAPE_TABLE + "("
                + GAME + " TEXT,"
                + PAGE + " TEXT,"
                + SHAPE + " TEXT,"
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

        SQLiteDatabase db = this.getWritableDatabase();


        // Delete existing game
        String query = "Select * FROM " + SHAPE_TABLE + " WHERE " + GAME + " = '" + gameName + "'";
        Cursor cursor = db.rawQuery(query, null);

        //If game exists delete it first
        if (cursor.moveToFirst()) {
            db.delete(SHAPE_TABLE, GAME + "= '" + gameName + "'", null);
            System.out.println(game + " is deleted");
        }



        System.out.println(game + "is added");
        List<GPage> pageList = game.getPages();
        GPage currPage = game.getCurrPage();

        for (GPage page : pageList) {    // dont add existing page
            String isCurrentPage = "YES";
            String isFirstPage = "YES";
            String pageName = page.getName();
            if (pageName.equals(currPage.getName())) {
                isCurrentPage = "YES";
            }
            if (!game.isFirstPage(page)) {
                isFirstPage = "YES";
            }


            String pageInfo = pageName + ", " + isCurrentPage + ", " + isFirstPage;

            List<GShape> shapes = page.getShapes();

            String allShapeInfo = "";

            for (GShape shape : shapes) {
                String shapeName = shape.getName();
                String imageName = shape.getPictureName();
                String script = shape.getScript();
                String font = Integer.toString(shape.getFontSize());
                String x = Float.toString(shape.getX());
                String y = Float.toString(shape.getY());
                String w = Float.toString(shape.getWidth());
                String h = Float.toString(shape.getHeight());


                String shapeInfo =  shapeName + ", " + imageName + ", " + script + ", " + font + ", " + x + ", " + y + ", " + w + ", " + h + ";";
                allShapeInfo += shapeInfo;
            }

            ContentValues values = new ContentValues();

            values.put(GAME, gameName);
            values.put(PAGE, pageInfo);
            values.put(SHAPE, allShapeInfo);

            // Inserting Row
            boolean a = db.insert(SHAPE_TABLE, null, values) > 0;
            System.out.println(a );
            System.out.println("saved game:" + gameName + " page info: " + pageInfo + " all shapes: " + allShapeInfo);
        }

        db.close();
    }




    /* Load data
     * Returns null if a game with the given name does not exist in the database
     */
    public Game loadGameHandler(String gameName) {
        Game game = new Game(gameName);

        String query1 = "Select * FROM " + SHAPE_TABLE + " WHERE " + GAME + "=" + "'" + gameName + "'";

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);

        System.out.println("loading game "  + game + " from database");
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            System.out.println("loading game "  + game + " from database");
            do {
                String pageInfo = cursor.getString(1);
                String[] parts = pageInfo.split(",");
                String pageName = parts[0];
                String isCurrentPage = parts[1];
                String isFirstPage = parts[2];


                GPage page = game.getPage(pageName);
                if (page == null) {
                    page = new GPage(pageName);
                    game.addPage(page);   // add page to game
                }
                System.out.println("page " + pageName);


                if (isCurrentPage == "YES") {
                    game.setCurrPage(page);
                    System.out.println("sets current page to " + pageName);
                }

                String shapeInfo = cursor.getString(2);

                String[] shapeParts = shapeInfo.split(";");
                for (String shapePart : shapeParts) {
                    if (!shapePart.isEmpty()) {
                        String[] oneShapeParts = shapePart.split(",");

                        String shapeName = oneShapeParts[0];
                        String imageName = oneShapeParts[1];
                        String script = oneShapeParts[2];

                        int font =  Integer.getInteger(oneShapeParts[3], 0);
                        float x = (oneShapeParts[4].isEmpty() ? 0 : Float.parseFloat(oneShapeParts[4]));
                        float y =(oneShapeParts[5].isEmpty() ? 0 : Float.parseFloat(oneShapeParts[5]));
                        float w =(oneShapeParts[6].isEmpty() ? 0 : Float.parseFloat(oneShapeParts[6]));
                        float h = (oneShapeParts[7].isEmpty() ? 0 : Float.parseFloat(oneShapeParts[7]));


                        // shape set size, name, font etc
                        GShape shape = new GShape(shapeName, x, y);
                        shape.setWidth(w);
                        shape.setHeight(h);
                        shape.setName(imageName);
                        shape.setScriptText(script);
                        shape.setFontSize(font);

                        // add shape to page
                        page.addShape(shape);

                        System.out.println("Added shape " + shapeName + " size " + x + ", " + y + ", " + w + ", " + h + ", "+ " to gameView");

                        //TODO Add Shapes to Possessions iff y < some number.
                        if (y < 100) {   //change 100 later
                            game.addPossession(shape);
                        }
                    }

                }

            } while (cursor.moveToNext());

            cursor.close();
        } else {
            game = null;
            return game;
        }

        db1.close();

        return game;
    }

}
