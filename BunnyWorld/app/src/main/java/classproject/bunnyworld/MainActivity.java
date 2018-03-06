package classproject.bunnyworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.*;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("games.db", MODE_PRIVATE, null);

        
        // Table for Shapes
        // each shape belongs to either one of Possession or a particular Page.

        Cursor shapesCursor = db.rawQuery("SELECT * FROM sqlite_master "
                + "WHERE type='table' AND name ='shapes';" , null);

        if (shapesCursor.getCount() == 0) {
            String setupStr = "CREATE TABLE shapes ("
                    + "gameName TEXT, page TEXT, shape TEXT, script TEXT,  x INTEGER, y INTEGER, w INTEGER, h INTEGER"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(setupStr);

            String dataStr= "INSERT INTO pages VALUES "
                    + "('Game1','Page1','',,,,, NULL);";

            db.execSQL(dataStr);
        }
        
//         Game game = new Game("g1");
//         System.err.println(game);
    }

    public void onNewGame(View view) {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivity(intent);
    }

//    public void onEditGame(View view) {
//        Intent intent = new Intent(this,EditorActivity.class);
//        startActivity(intent);
//    }

    public void onPlayGame(View view) {
        Intent intent = new Intent(this,PlayActivity.class);
        startActivity(intent);
    }

}
