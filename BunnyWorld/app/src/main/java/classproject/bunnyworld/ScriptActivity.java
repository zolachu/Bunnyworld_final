package classproject.bunnyworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static classproject.bunnyworld.Script.GO_TO;
import static classproject.bunnyworld.Script.HIDE;
import static classproject.bunnyworld.Script.ON_CLICK;
import static classproject.bunnyworld.Script.ON_DROP;
import static classproject.bunnyworld.Script.ON_ENTER;
import static classproject.bunnyworld.Script.PLAY;
import static classproject.bunnyworld.Script.SHOW;

public class ScriptActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private StringBuilder curScript;
    private Game curGame;
    private GShape curShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        GameManager gameManager = GameManager.getInstance();
        String script = gameManager.getCurScript();
        curScript = new StringBuilder(script);
        curGame = gameManager.getCurGame();

        GameView myView = gameManager.getGameView();
        curShape = myView.getSelectedShape();
        TextView curShapeTextView = findViewById(R.id.curShape_textView);
        curShapeTextView.setText("Current Shape: " + curShape.getName());

        spinnerSetUp1();
        spinnerSetUp2();
        spinnerSetUp3();
        spinnerSetUp4();
        spinnerSetUp5();
    }

    private void spinnerSetUp1() {
        Spinner pageSpinner = findViewById(R.id.script_goto_spinner);
        pageSpinner.setOnItemSelectedListener(this);

        List<String> pageNameList = new ArrayList<>();
        List<GPage> pageList = curGame.getPages();
        pageNameList.add("");
        for (GPage page : pageList) {
            String pageName = page.getName();
            pageNameList.add(pageName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageNameList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);
    }

    private void spinnerSetUp2() {
        Spinner musicSpinner = findViewById(R.id.script_play_spinner);
        musicSpinner.setOnItemSelectedListener(this);

        List<String> adapterList = new ArrayList<>();
        adapterList.add("");
        adapterList.add("carrotcarrotcarrot");
        adapterList.add("evillaugh");
        adapterList.add("fire");
        adapterList.add("hooray");
        adapterList.add("munch");
        adapterList.add("munching");
        adapterList.add("woof");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        musicSpinner.setAdapter(adapter);
    }

    private void spinnerSetUp3() {
        Spinner shapeSpinner = findViewById(R.id.script_hide_spinner);
        shapeSpinner.setOnItemSelectedListener(this);

        List<String> adapterList = makeAdaptorList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shapeSpinner.setAdapter(adapter);
    }

    private void spinnerSetUp4() {
        Spinner shapeSpinner = findViewById(R.id.script_show_spinner);
        shapeSpinner.setOnItemSelectedListener(this);

        List<String> adapterList = makeAdaptorList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shapeSpinner.setAdapter(adapter);
    }

    private void spinnerSetUp5() {
        Spinner shapeSpinner = findViewById(R.id.script_onDropTarget_spinner);
        shapeSpinner.setOnItemSelectedListener(this);

        List<String> adapterList = makeAdaptorList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shapeSpinner.setAdapter(adapter);
    }

    private List<String> makeAdaptorList() {
        List<String> adapterList = new ArrayList<>();
        List<GShape> shapeList = curGame.getAllShapes();
        adapterList.add("");
        for (GShape shape: shapeList) {
            String shapeName = shape.getName();
            adapterList.add(shapeName);
        }
        return adapterList;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String item = parent.getItemAtPosition(pos).toString();
        curScript.append(item + " ");
        displayScript();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onClickTrigger(View view) {
        curScript.append(ON_CLICK + " ");
        displayScript();
    }

    public void onEnterTrigger(View view) {
        curScript.append(ON_ENTER + " ");
        displayScript();
    }

    public void onDropTrigger(View view) {
        curScript.append(ON_DROP + " ");
        displayScript();
    }

    public void goToAction(View view) {
        curScript.append(GO_TO + " ");
        displayScript();
    }

    public void playAction(View view) {
        curScript.append(PLAY + " ");
        displayScript();
    }

    public void hideAction(View view) {
        curScript.append(HIDE + " ");
        displayScript();
    }

    public void showAction(View view) {
        curScript.append(SHOW + " ");
        displayScript();
    }

    private void displayScript() {
        TextView scriptView = findViewById(R.id.script_editor_textView);
        scriptView.setText(curScript);
    }

    public void resetScript(View view) {
        curScript = new StringBuilder();
        displayScript();
    }

    public void addSemicolon(View view) {
        int len = curScript.length();
        curScript.deleteCharAt(len-1);
        curScript.append("; ");
        displayScript();
    }

    public void saveScript(View view) {
        GameManager gameManager = GameManager.getInstance();
        gameManager.setCurScript(curScript.toString());
        curShape.setScriptText(curScript.toString());
    }


}
