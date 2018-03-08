package classproject.bunnyworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    private Game curGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        GameManager gameManager = GameManager.getInstance();
        curGame = gameManager.getCurGame();

        TextView gameName = findViewById(R.id.gameName_textView);
        gameName.setText(curGame.getName());
    }

    public void updateShape(View view) {
        // step 1: get currently selected shape, in shape class
        GameView myView = findViewById(R.id.myCanvas);
        GShape curShape = myView.getSelectedShape();

        // step 2: check which shape views are nonempty and update those fields using setters
        EditText shapeName = findViewById(R.id.shape_name_editText);
        String name = shapeName.getText().toString();
        if (!name.isEmpty()) curShape.setName(name);

        EditText x_coordinate = findViewById(R.id.shape_X_editText);
        String x = x_coordinate.getText().toString();
        if (!x.isEmpty()) curShape.setX(Float.valueOf(x));

        EditText y_coordinate = findViewById(R.id.shape_Y_editText);
        String y = y_coordinate.getText().toString();
        if (!y.isEmpty()) curShape.setY(Float.valueOf(y));

        EditText width = findViewById(R.id.shape_W_editText);
        float w = Float.valueOf(width.getText().toString());
        curShape.setWidth(w);

        EditText height = findViewById(R.id.shape_H_editText);
        float h = Float.valueOf(height.getText().toString());
        curShape.setHeight(h);

        EditText texts = findViewById(R.id.shape_text_editText);
        String text = texts.getText().toString();
        if (!text.isEmpty()) curShape.setTextString(text);

        EditText images = findViewById(R.id.shape_imgName_editText);
        String image = images.getText().toString();
        if (!image.isEmpty()) curShape.setPictureName(image);

        EditText scripts = findViewById(R.id.shape_script_editText);
        String script = scripts.getText().toString();
        if (!script.isEmpty()) curShape.setScriptText(script);

        EditText fontSizes = findViewById(R.id.shape_fontSize_editText);
        String fontSize = fontSizes.getText().toString();
        if (!fontSize.isEmpty()) curShape.setFontSize(Integer.valueOf(fontSize));

    }

    public void addShape(View view) {
        EditText shapeName = findViewById(R.id.shape_name_editText);
        String name = shapeName.getText().toString();
        GPage curPage = curGame.getCurrPage();
        if (name.isEmpty()) {
            name = curPage.assignDefaultShapeName();
        } else {
            if (curPage.duplicateShapeName(name)) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Shape name already exists!",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

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
        curPage.addShape(newShape);

        GameView myView = findViewById(R.id.myCanvas);
        myView.invalidate();

    }

    public void removeShape(View view) {
        // step 1: get currently selected shape
        GameView myView = findViewById(R.id.myCanvas);
        GShape curShape = myView.getSelectedShape();

        // step 2: remove it from the current page's list of shapes
        if (curShape != null) {
            GPage curPage = curGame.getCurrPage();
            curPage.removeShape(curShape);
        }
    }

    public void addPage(View view) {
        String pageName = curGame.assignDefaultPageName();
        GPage newPage = new GPage(pageName);

        // step 1: add a new page to the current game
        curGame.addPage(newPage);

        // step 2: ask the custom view to redraw the new empty page
        GameView myView = findViewById(R.id.myCanvas);
        //TODO How does GameView know which game and which page to display?????
    }

    public void goToPrevPage(View view) {
        // step 1: get the previous page from the current game's list of pages
        // step 2: ask the custom view to redraw the previous page
        GameView myView = findViewById(R.id.myCanvas);
        //TODO How does GameView know which game and which page to display?????
    }

    public void goToNextPage(View view) {
        // same thing as goToPrevPage()
    }

    // this method renames a page
    public void updatePage(View view) {
        EditText pageName = findViewById(R.id.page_name_editText);
        String name = pageName.getText().toString();

        // get current page
        GPage curPage = curGame.getCurrPage();

        // rename current page
        if (!curGame.duplicatePageName(name)) {
            curPage.setName(name);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Page name already exists!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void deletePage(View view) {
        // get current page
        GPage curPage = curGame.getCurrPage();

        // delete current page
        if (!curGame.isFirstPage(curPage)) {
            curGame.removePage(curPage);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Cannot delete the first page!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    // save the current game to database by calling the singleton's write method
    public void saveGame(View view) {
        GameManager gameManager = GameManager.getInstance();
        gameManager.saveGame(curGame);
        gameManager.addGameToList(curGame);
    }


}
