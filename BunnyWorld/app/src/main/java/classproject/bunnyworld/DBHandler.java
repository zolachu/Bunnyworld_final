package classproject.bunnyworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.channels.FileLock;
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
    private static final String DATABASE_NAME = "database.db";

    // Games and Shapes table name
    private static final String SHAPE_TABLE = "gameTable";

    // Table column names
    private static final String KEY_PRIMARY = "id";
    private static final String GAME = "game";
    private static final String PAGE = "page";
    private static final String CURRENTPAGE = "currentPage";
    private static final String SHAPE = "shape";


    float initX = 50, initY = 50;

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
                + CURRENTPAGE + " TEXT,"
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



    public void addNewGame(Game game) {
        String gameName = game.getName();

        SQLiteDatabase db = this.getWritableDatabase();

        List<GPage> pageList = game.getPages();
        GPage currPage = game.getCurrPage();

        for (GPage page : pageList) {    // dont add existing page
            String isCurrentPage = "YES";

            String pageName = page.getName();
            if (!pageName.equals(currPage.getName())) {
                isCurrentPage = "NO";
            }


            String pageInfo = pageName;

            List<GShape> shapes = page.getShapes();

            String allShapeInfo = "";

            for (GShape shape : shapes) {
                String shapeName = shape.getName();
                String imageName = shape.getPictureName();
                String script = shape.getScript();
                String font = Integer.toString(shape.getFontSize());
                String text = shape.getText();
                String x = String.valueOf(shape.getX());
                String y = String.valueOf(shape.getY());
                String w = String.valueOf(shape.getWidth());
                String h = String.valueOf(shape.getHeight());


                String shapeInfo =  shapeName + "," + imageName + "," + script + "," + font + "," + text +"," + x + "," + y + "," + w + "," + h + ";";
                allShapeInfo += shapeInfo;
            }

            ContentValues values = new ContentValues();

            values.put(GAME, gameName);
            values.put(PAGE, pageInfo);
            values.put(CURRENTPAGE, isCurrentPage);
            values.put(SHAPE, allShapeInfo);


            db.insert(SHAPE_TABLE, null, values);
            System.out.println(game + " is added for the first time");
            System.out.println("added game:" + gameName + " page info: " + pageInfo + " all shapes: " + allShapeInfo);
        }

        db.close();
    }

    public void updateGame(Game game) {


        String gameName = game.getName();

        SQLiteDatabase db = this.getWritableDatabase();


        String query = "Select * FROM " + SHAPE_TABLE + " WHERE " + GAME + " = '" + gameName + "'";
        Cursor cursor = db.rawQuery(query, null);



        System.out.println(game + "is added");
        List<GPage> pageList = game.getPages();
        GPage currPage = game.getCurrPage();

        db.delete(SHAPE_TABLE, GAME + " = ?",
                new String[]{gameName});

        for (GPage page : pageList) {    // dont add existing page
            String isCurrentPage = "YES";

            String pageName = page.getName();
            if (!pageName.equals(currPage.getName())) {
                isCurrentPage = "YES";
            }


            String pageInfo = pageName;


            List<GShape> shapes = page.getShapes();

            String allShapeInfo = "";

            for (GShape shape : shapes) {
                String shapeName = shape.getName();
                String imageName = shape.getPictureName();
                String script = shape.getScript();
                String font = Integer.toString(shape.getFontSize());
                String text = shape.getText();
                Float x = shape.getX();
                String y = Float.toString(shape.getY());
                String w = Float.toString(shape.getWidth());
                String h = Float.toString(shape.getHeight());


                String shapeInfo =  shapeName + "," + imageName + "," + script + "," + font + "," + text +"," + x + "," + y + "," + w + "," + h + ";";
                allShapeInfo += shapeInfo;
            }

            ContentValues values = new ContentValues();

            values.put(GAME, gameName);
            values.put(PAGE, pageInfo);
            values.put(SHAPE, allShapeInfo);
            values.put(CURRENTPAGE, isCurrentPage);

            // Inserting Row

//            a = db.update(SHAPE_TABLE, values, GAME + " = ? AND " + PAGE + " = ?",
//                    new String[]{gameName, pageName}) > 0;

//            if (a == false) {
                db.insert(SHAPE_TABLE, null, values);
//            }
            System.out.println("updated database " );
            System.out.println("updated game:" + gameName + " page info: " + pageInfo
                    + " all shapes: " + allShapeInfo + "current page: " + isCurrentPage);
        }

        db.close();

    }




    /* Load data
     * Returns null if a game with the given name does not exist in the database
     */
    public Game loadGameHandler(String gameName) {
        Game game = new Game(gameName);

        String query1 = "Select * FROM " + SHAPE_TABLE + " WHERE " + GAME + "=" + "'" + gameName + "'";

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(query1, null);


        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String pageName = cursor.getString(1);
                String isCurrentPage = cursor.getString(2);
                String shapeInfo = cursor.getString(3);

                // add a new page
                GPage page = new GPage(pageName);
                // step 1: add a new page to the current game
                game.addPage(page);
                game.setCurrPage(page);


                //parse page string


//                GPage page = game.getPage(pageName);
//                if (page == null) {

//                    game.addPage(page);   // add page to game
//                }
                System.out.println("new page added: " + pageName);


                if (isCurrentPage == "YES") {
                    game.setCurrPage(page);
                    System.out.println("sets current page to: " + pageName);
                }

                // parse shape string


                String[] shapeParts = shapeInfo.split(";");
                for (String shapePart : shapeParts) {
                    if (!shapePart.isEmpty()) {
                        String[] oneShapeParts = shapePart.split(",");


                        String shapeName = oneShapeParts[0];
                        String imageName = oneShapeParts[1];
                        String script = oneShapeParts[2];
                        String font = oneShapeParts[3];
                        String text = oneShapeParts[4];
                        String x = oneShapeParts[5];
                        String y = oneShapeParts[6];
                        String width = oneShapeParts[7];
                        String height = oneShapeParts[8];



                        GShape newShape = new GShape(shapeName, initX, initY);



                        if (!imageName.isEmpty()) newShape.setPictureName(imageName);
                        if (!script.isEmpty()) newShape.setScriptText(script);
                        if (!font.isEmpty()) newShape.setFontSize(Integer.valueOf(font));

                        if (!height.isEmpty()) { newShape.setHeight(Float.parseFloat(height)); }
                        if (!width.isEmpty()) { newShape.setWidth(Float.parseFloat(width)); }


                        // add newShape to the current page's list of shapes
                        page.addShape(newShape);

                        //TODO add movable and hidden object boolean values

                        // left these items since I found this to be more convenient
                        // user can clear these fields by clicking a background


//                       game.assignDefaultShapeName();


//                        page.addShape(new GShape(shapeName, 300.0f, 300.0f, text, 0));
                        System.out.println("fucking teext: " + text + "x:" + x + y);


//                        System.out.println("Loading game: " + gameName + "Page: " + pageName + ", Added shapes: " + shapeName + ", Size: " + "x is " + x + ", y is " + y + ", w is " + width + ", h is " + height + ", " + "current page sets to" + isCurrentPage + " from database");

                        //TODO Add Shapes to Possessions iff y < some number.
//                        if (y < 100) {   //change 100 later
//                            game.addPossession(newShape);
//                        }
                    }

//                    page.addShape(new GShape("shape1", 200.0f, 200.0f, "aear", 2));
                    game.addPage(page);
                    System.out.println("added page " + pageName);
                }

            }
            cursor.close();
        } else {
            game = null;
            return game;
        }

        db1.close();

        return game;
    }


    public Set<Game> getAllGames() {
        Set<Game> gameList = new HashSet<>();

        String selectQuery = "SELECT  * FROM " + SHAPE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                String gameName = cursor.getString(0);
                Game game = loadGameHandler(gameName);

                gameList.add(game);
            } while (cursor.moveToNext());
        }

        // return contact list
        return gameList;
    }

}

