package classproject.bunnyworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    public void updateShape(View view) {
        // step 1: get currently selected shape
        // step 2: check which shape views are nonempty and update those fields using setters
    }

    public void addShape(View view) {
        EditText shapeName = findViewById(R.id.shape_name_editText);
        String name = shapeName.getText().toString();

        EditText x_coordinate = findViewById(R.id.shape_X_editText);
        float x = Float.valueOf(x_coordinate.getText().toString());

        EditText y_coordinate = findViewById(R.id.shape_Y_editText);
        float y = Float.valueOf(y_coordinate.getText().toString());

        EditText texts = findViewById(R.id.shape_text_editText);
        String text = texts.getText().toString();

        EditText images = findViewById(R.id.shape_imgName_editText);
        String image = images.getText().toString();

        int type;
        if (!text.isEmpty()) {
            type = GShape.TEXT;
        } else {
            type = GShape.IMAGE;
        }

        GShape newShape = new GShape(name, x, y, text, type);

        EditText scripts = findViewById(R.id.shape_script_editText);
        String script = scripts.getText().toString();

        EditText fontSizes = findViewById(R.id.shape_fontSize_editText);
        String fontSize = fontSizes.getText().toString();

        if (!script.isEmpty()) newShape.setScriptText(script);
        if (!image.isEmpty()) newShape.setPictureName(image);
        if (fontSize.isEmpty()) newShape.setFontSize(Integer.valueOf(fontSize));

        // add newShape to the current page's list of shapes

        GameView myView = findViewById(R.id.myCanvas);
        myView.invalidate();

    }

    public void removeShape(View view) {
        // step 1: get currently selected shape
        // step 2: remove it from the current page's list of shapes
    }

    public void addPage(View view) {
        GPage newPage = new GPage("default name");

        // step 1: add a new page to the current game
        // step 2: ask the custom view to redraw the new empty page
    }

    public void goToPrevPage(View view) {
        // step 1: get the previous page from the current game's list of pages
        // step 2: ask the custom view to redraw the previous page
    }

    public void goToNextPage(View view) {
        // same thing as goToPrevPage()
    }

    public void updatePage(View view) {
        EditText pageName = findViewById(R.id.page_name_editText);
        String name = pageName.getText().toString();

        // pass the name to the current game and get obtain requested page
        // ask the custom view to redraw the requested page
    }

    public void saveGame(View view) {
        // same the current game to database by calling the singleton's write method
    }


}
