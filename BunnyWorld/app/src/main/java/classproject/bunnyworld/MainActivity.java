package classproject.bunnyworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.*;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//         Game game = new Game("g1");
//         System.err.println(game);
    }

    public void onNewGame(View view) {
        GameManager gameManager = GameManager.getInstance();
        EditText gameName = findViewById(R.id.game_name_editText);
        String name = gameName.getText().toString();
        boolean duplicate = gameManager.duplicateGameName(name);
        if (duplicate) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Game already exists! Please change game name.",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            gameManager.setCurGame(name);

            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        }
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
