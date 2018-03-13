package classproject.bunnyworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);
    }

    public void onNewGame(View view) {
        GameManager gameManager = GameManager.getInstance();
        db = new DBHandler(this);
        gameManager.setDb(db);

        EditText gameName = findViewById(R.id.game_name_editText);
        String name = gameName.getText().toString();

        boolean duplicate = gameManager.duplicateGameName(name);

        if (duplicate) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Game already exists! Please change game name.",
                    Toast.LENGTH_SHORT);
            toast.show();

        } else {
            gameManager.setNewGame(name);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Game added", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        }
    }

    public void onEditGame(View view) {
        GameManager gameManager = GameManager.getInstance();
        db = new DBHandler(this);
        gameManager.setDb(db);


        EditText gameName = findViewById(R.id.game_existingName_editText);
        String name = gameName.getText().toString();
        boolean duplicate = gameManager.duplicateGameName(name);
        if (duplicate) {
            Game game = db.loadGameHandler(name);
            List<GPage> pages = game.getPages();
            for (GPage page: pages) {
                System.out.println("editing existing game : " + game.getName() + " has a page called " + page.getName());
                System.out.println(" it has a shapes ");
                for (GShape shape: page.getShapes()){
                    System.out.println(" it has a shape " + shape.getName() );
                }

            }

            gameManager.setCurGame(name);


            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Game doesn't exist!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onPlayGame(View view) {
        GameManager gameManager = GameManager.getInstance();
        db = new DBHandler(this);
        gameManager.setDb(db);
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

}
