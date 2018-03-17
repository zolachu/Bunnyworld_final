package classproject.bunnyworld;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    SQLiteDatabase db;
    private Game selectedGame;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameManager = GameManager.getInstance();

        spinnerSetUp();
    }

    private void spinnerSetUp() {
        Spinner spinner = findViewById(R.id.game_name_spinner);
        spinner.setOnItemSelectedListener(this);

        Set<Game> gameSet = gameManager.getAllGames();

        List<String> gameNameList = new ArrayList<>();
        gameNameList.add("");
        for (Game game: gameSet) {
            String gameName = game.getName();
            gameNameList.add(gameName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gameNameList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String item = parent.getItemAtPosition(pos).toString();
        selectedGame = gameManager.getGame(item);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onNewGame(View view) {
        EditText gameName = findViewById(R.id.game_name_editText);
        String name = gameName.getText().toString();
        boolean duplicate = gameManager.duplicateGameName(name);
        if(name.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Must give the game a name!",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else if (duplicate) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Game already exists!",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            gameManager.setCurGame(name);

            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        }
    }

    public void onEditGame(View view) {
        if (selectedGame != null) {
            String name = selectedGame.getName();
            gameManager.setCurGame(name);

            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        } else {
            makeToast();
        }
    }

    public void onPlayGame(View view) {
        if (selectedGame != null) {
            String name = selectedGame.getName();
            gameManager.setCurGame(name);

            Intent intent = new Intent(this,PlayActivity.class);
            startActivity(intent);
        } else {
            makeToast();
        }
    }

    private void makeToast() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "No existing game selected!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

}
