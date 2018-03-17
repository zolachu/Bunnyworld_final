package classproject.bunnyworld;

import android.media.AudioManager;
import android.media.MediaPlayer;
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

        for(GPage page: curGame.getPages()) {
            for (GShape shape: page.getShapes()) {
                shape.unselectShape();
                shape.unselectOnDrop();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //gameManager.cleanMediaPlayers();
        //System.err.print("media cleaned");
    }
}
