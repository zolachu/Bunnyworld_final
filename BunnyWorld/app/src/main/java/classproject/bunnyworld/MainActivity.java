package classproject.bunnyworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Game game = new Game("g1");
        System.err.println(game);
    }

    public void onNewGame(View view) {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivity(intent);
    }

}
