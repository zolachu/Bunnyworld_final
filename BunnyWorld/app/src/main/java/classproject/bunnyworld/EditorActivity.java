package classproject.bunnyworld;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Game curGame;
    private GPage curPage;
    private GameManager gameManager;
    private String imgName;

    private GameView myView;
    private EditText shapeName;
    private EditText x_coordinate;
    private EditText y_coordinate;
    private EditText width;
    private EditText height;
    private TextView scripts;
    private EditText fontSizes;
    private CheckBox hidden_box;
    private CheckBox movable_box;
    private EditText pageName;

    private Spinner imageSpinner;
    private ArrayAdapter<String> adapter;

    public static int viewWidth, viewHeight;
    private static float initX, initY;


    /*
     * Initializes the editor activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        gameManager = GameManager.getInstance();
        curGame = gameManager.getCurGame();
        curPage = curGame.getCurrPage();
        curGame.setEditOn();

        gameManager.setGameView(this);

        TextView gameName = findViewById(R.id.gameName_textView);
        StringBuilder displayGameName = new StringBuilder("Current Game: ");
        displayGameName.append(curGame.getName());
        gameName.setText(displayGameName.toString());

        initViews();

        initX = 0.1f * (float) viewWidth;
        initY = 0.1f * (float) viewHeight;

        displayCurPageName();

        Toast toast = Toast.makeText(getApplicationContext(),
                "Edit Mode Enabled",
                Toast.LENGTH_SHORT);
        toast.show();

        spinnerSetUp();
    }

    private void spinnerSetUp() {
        imageSpinner = findViewById(R.id.shape_imgName_spinner);
        imageSpinner.setOnItemSelectedListener(this);

        List<String> adapterList = new ArrayList<>();
        adapterList.add("");
        adapterList.add("carrot");
        adapterList.add("carrot2");
        adapterList.add("death");
        adapterList.add("duck");
        adapterList.add("fire_pic");
        adapterList.add("mystic");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        imgName = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void initViews() {
        myView       = findViewById(R.id.myCanvas);
        shapeName    = findViewById(R.id.shape_name_editText);
        x_coordinate = findViewById(R.id.shape_X_editText);
        y_coordinate = findViewById(R.id.shape_Y_editText);
        width        = findViewById(R.id.shape_W_editText);
        height       = findViewById(R.id.shape_H_editText);
//        texts        = findViewById(R.id.shape_text_editText);
        scripts      = findViewById(R.id.shape_script_text);
        fontSizes    = findViewById(R.id.shape_fontSize_editText);
        hidden_box   = findViewById(R.id.shape_hidden_checkBox);
        movable_box  = findViewById(R.id.shape_movable_checkBox);
        pageName     = findViewById(R.id.page_name_editText);
    }

    /*
     * Updates a currently existing shape's nonempty fields
     */
    public void updateShape(View view) {
        // step 1: get currently selected shape, in shape class
        GShape curShape = myView.getSelectedShape();

        if (curShape == null) return;

        // step 2: check which shape views are nonempty and update those fields using setters
        String name = shapeName.getText().toString();
        String curName = curShape.getName();
        if (!name.equals(curName)) {
            if (!curGame.duplicateShapeName(name)) {
                curShape.setName(name);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Shape name already exists!",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        String x = x_coordinate.getText().toString();
        curShape.setX(Float.valueOf(x));

        String y = y_coordinate.getText().toString();
        curShape.setY(Float.valueOf(y));

        String w = width.getText().toString();
        curShape.setWidth(Float.valueOf(w));

        String h = height.getText().toString();
        curShape.setHeight(Float.valueOf(h));

//        String text = texts.getText().toString();
//        curShape.setTextString(text);

        String script = curShape.getScript();
        if (!script.isEmpty()) {
            curShape.setScriptText(script);
            scripts.setText(script);
        } else {
            scripts.setText("Script");
        }

        String fontSize = fontSizes.getText().toString();
        if (!fontSize.isEmpty()) {
            curShape.setFontSize(Integer.valueOf(fontSize));
            Paint richtext = curShape.getRichTextPaint();
            if (richtext != null) {
                richtext.setTextSize(Float.valueOf(fontSize));
            }
        }

        if (hidden_box.isChecked()) {
            curShape.setHidden(true);
        } else {
            curShape.setHidden(false);
        }
        if (movable_box.isChecked()) {
            curShape.setMovable(true);
        } else {
            curShape.setMovable(false);
        }

        curShape.setPictureName(imgName);

        myView.invalidate();
    }

    /*
     * Adds a new shape to the current page with user-specified fields
     */
    public void addShape(View view) {
        String name = shapeName.getText().toString();
        if (name.isEmpty()) {
            name = curGame.assignDefaultShapeName();
        } else {
            if (curGame.duplicateShapeName(name)) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Shape name already exists!",
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        GShape newShape = createNewShape(name);
        setNewShape(newShape);
    }

    private GShape createNewShape(String name) {
        String x = x_coordinate.getText().toString();
        String y = y_coordinate.getText().toString();

        if (x.isEmpty() || y.isEmpty()) {
            x = Float.toString(initX);
            y = Float.toString(initY);
            updateInitialPosition();
        }

//        String text = texts.getText().toString();

//        int type;
//        if (!text.isEmpty()) {
//            type = GShape.TEXT;
//        } else {
//            type = GShape.IMAGE;
//        }

        GShape newShape = new GShape(name, Float.valueOf(x), Float.valueOf(y));
        return newShape;
    }


    //Use view parameters to set properties of the shape object
    private void setNewShape(GShape newShape) {

//        String text  = texts.getText().toString();
        String image = imgName;
        String script = gameManager.getCurScript();
        String fontSize = fontSizes.getText().toString();

        String w = width.getText().toString();
        String h = height.getText().toString();

        if (!image.isEmpty()) newShape.setPictureName(image);
        if (!script.isEmpty()) newShape.setScriptText(script);
        if (!fontSize.isEmpty()) newShape.setFontSize(Integer.valueOf(fontSize));

        if (!h.isEmpty()) { newShape.setHeight(Float.parseFloat(h)); }
        if (!w.isEmpty()) { newShape.setWidth(Float.parseFloat(w)); }

        newShape.setMovable(movable_box.isChecked());
        newShape.setHidden(hidden_box.isChecked());

        // add newShape to the current page's list of shapes
        curPage.addShape(newShape);
        myView.invalidate();

        // save these values before clear
        boolean movableBoxValue = movable_box.isChecked();
        boolean hiddenboxValue = hidden_box.isChecked();

        // clear the shape info panel
        clearShapeInfo();

        //display a default name for a shape to be created next
        shapeName.setText(curGame.assignDefaultShapeName());
    }

    /*
     * Removes the currently selected shape
     */
    public void removeShape(View view) {
        // step 1: get currently selected shape
        GShape curShape = myView.getSelectedShape();

        // step 2: remove it from the current page's list of shapes
        if (curShape != null) {
            curPage.removeShape(curShape);
        }
        myView.invalidate();
    }

    /*
     * Adds a new page to the current game and displays it
     */
    public void addPage(View view) {
        String pageName = curGame.assignDefaultPageName();
        GPage newPage = new GPage(pageName);


        // step 1: add a new page to the current game
        curGame.addPage(newPage);
        curGame.setCurrPage(newPage);
        curPage = curGame.getCurrPage();

        displayCurPageName();
        resetInitialPosition();

        // step 2: ask the custom view to redraw the new empty page
        myView.invalidate();
    }

    /*
     * Displays the previous page in the current game
     */
    public void goToPrevPage(View view) {
        // step 1: get the previous page from the current game's list of pages
        GPage prevGPage = curGame.prePage();
        curGame.setCurrPage(prevGPage);
        curPage = curGame.getCurrPage();
        displayCurPageName();

        resetInitialPosition();

        // step 2: ask the custom view to redraw the previous page
        myView.invalidate();
    }

    /*
     * Displays the next page in the current game
     */
    public void goToNextPage(View view) {
        // step 1: get the previous page from the current game's list of pages
        GPage nextGPage = curGame.nextPage();
        curGame.setCurrPage(nextGPage);
        curPage = curGame.getCurrPage();
        displayCurPageName();
        resetInitialPosition();

        // step 2: ask the custom view to redraw the previous page
        myView.invalidate();
    }

    /*
     * This method renames the current page
     */
    public void updatePage(View view) {
        String name = pageName.getText().toString();

        // get current page
        // rename current page
        if (!curGame.duplicatePageName(name)) {
            curPage.setName(name);
            displayCurPageName();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Page name already exists!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /*
     * Deletes the current page and displays the first page
     */
    public void deletePage(View view) {
        // get current page
        // delete current page
        if (!curGame.isFirstPage(curPage)) {
            curGame.removePage(curPage);
            curGame.setCurrPage(curGame.getFirstPage());
            curPage = curGame.getCurrPage();
            displayCurPageName();
            resetInitialPosition();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Cannot delete the first page!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*
     * Clear the shapes in the page, not including the possession area
     */
    public void clearPage(View view) {
        curPage.removeAllShapes();
        clearShapeInfo();
        resetInitialPosition();
        shapeName.setText(curGame.assignDefaultShapeName());
        myView.invalidate();
    }

    /*
     * Saves the current game to database by calling the singleton's write method
     */
    public void saveGame(View view) {
        curGame.setCurrPage(curGame.getFirstPage());
        gameManager.saveGame(curGame);
        gameManager.addGameToList(curGame);
        Toast toast = Toast.makeText( getApplicationContext(),
                curGame.getName() + " was saved", Toast.LENGTH_SHORT);
                toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clearShapeInfo() {
        shapeName.setText("");
        x_coordinate.setText("");
        y_coordinate.setText("");
        width.setText("");
        height.setText("");
//        texts.setText("");
        scripts.setText("Script");
        fontSizes.setText("");
        hidden_box.setChecked(false);
        movable_box.setChecked(false);

        int spinnerPosition = adapter.getPosition("");
        imageSpinner.setSelection(spinnerPosition);
    }

    /*
     * Displays info for the currently selected shape
     */
    public void displayShapeInfo(GShape shape) {
        if (shape == null) {
            clearShapeInfo();
        } else {
            String name = shape.getName();
            float x = shape.getX();
            float y = shape.getY();
            float w = shape.getWidth();
            float h = shape.getHeight();
//            String text = shape.getText();
            String picName = shape.getPictureName();
            int fontSize = shape.getFontSize();
            String script = shape.getScript();
            boolean hidden = shape.isHidden();
            boolean movable = shape.isMovable();

            hidden_box.setChecked(hidden);
            movable_box.setChecked(movable);
            if (!script.isEmpty()) {
                scripts.setText(script);
            } else {
                scripts.setText("Script");
            }

            shapeName.setText(name);
            x_coordinate.setText(Float.toString(x));
            y_coordinate.setText(Float.toString(y));
            width.setText(Float.toString(w));
            height.setText(Float.toString(h));
            fontSizes.setText(Integer.toString(fontSize));
//            texts.setText(text);
            
            int spinnerPosition = adapter.getPosition(picName);
            imageSpinner.setSelection(spinnerPosition);

        }
    }


    public void updateCoordinates(GShape shape) {
        if (shape != null) {
            x_coordinate.setText(Float.toString(shape.getX()));
            y_coordinate.setText(Float.toString(shape.getY()));
        }
    }

    public void updateInitialPosition() {

        initX += 0.05f * (float) viewWidth;
        initY += 0.1f * (float) viewHeight;

        if (initX > 0.9f * (float) viewWidth) {
            initX = 0.1f * (float) viewWidth;
        }
        if (initY > 0.7f * (float) viewHeight) {
            initY = 0.1f * (float) viewHeight;
        }
    }

    public void resetInitialPosition() {
        initX = 0.1f * (float) viewWidth;
        initY = 0.1f * (float) viewHeight;
    }

    /*
     * Displays current page name
     */
    public void displayCurPageName() {
        String curPageName = curGame.getCurrPage().getName();
        pageName.setText(curPageName);
    }

    public void editScript(View view) {
        GShape curShape = myView.getSelectedShape();
        if (curShape != null) {
            String curScript = curShape.getScript();
            gameManager.setCurScript(curScript);

            Intent intent = new Intent(this, ScriptActivity.class);
            startActivity(intent);
        }
    }

    public void editText(View view) {
        GShape curShape = myView.getSelectedShape();
        if (curShape != null) {
//            String curText = curShape.getText();
//            gameManager.setCurScript(curScript);

            Intent intent = new Intent(this, TextActivity.class);
            startActivity(intent);
        }
    }

    public void errorCheckingScript(View view) {
        List<GPage> allPages = curGame.getPages();
        Set<String> pageNames = new HashSet<>();
        Set<String> soundList = new HashSet<>();
        soundList.add("");
        soundList.add("carrotcarrotcarrot");
        soundList.add("evillaugh");
        soundList.add("fire");
        soundList.add("hooray");
        soundList.add("munch");
        soundList.add("munching");
        soundList.add("woof");

        for(GPage page: allPages) {
            pageNames.add(page.getName());
        }

        List<GShape> allShapes = curGame.getAllShapes();
        Set<String> shapeNames = new HashSet<>();
        for(GShape shape: allShapes) {
            shapeNames.add(shape.getName());
        }

        for(GShape shape: allShapes) {


            String[] clicks = shape.getOnClickActionArray();
            if(clicks != null) {
                int clickLen = clicks.length;
                for (int i = 1; i < clickLen; i += 2) {
                    if (!pageNames.contains(clicks[i]) && !shapeNames.contains(clicks[i]) &&
                            !soundList.contains(clicks[i])) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                shape.getName() +" has a script containing " + clicks[i] +", which" +
                                        " does not exist.",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

            String[] enters = shape.getOnEnterActionArray();
            if(enters != null) {
                int enterLen = enters.length;
                for (int i = 1; i < enterLen; i += 2) {
                    if (!pageNames.contains(enters[i]) && !shapeNames.contains(enters[i]) &&
                            !soundList.contains(enters[i])) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                shape.getName() +" has a script containing " + enters[i] +", which" +
                                        " does not exist.",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

            String[] drops = shape.getOnDropActionArray();
            if(drops != null) {
                int dropLen = drops.length;
                for (int i = 2; i < dropLen; i += 2) {
                    if (!pageNames.contains(drops[i]) && !shapeNames.contains(drops[i]) &&
                            !soundList.contains(drops[i])) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                shape.getName() +" has a script containing " + drops[i] +", which" +
                                        " does not exist.",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

        }
    }



}
