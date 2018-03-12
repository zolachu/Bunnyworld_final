package classproject.bunnyworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    private Game curGame;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        gameManager = GameManager.getInstance();
        curGame = gameManager.getCurGame();
        curGame.setCurrPage(curGame.getFirstPage());
        curGame.setEditOff();

        Toast toast = Toast.makeText(getApplicationContext(),
                "Edit Mode Off",
                Toast.LENGTH_SHORT);
        toast.show();

    }
}
