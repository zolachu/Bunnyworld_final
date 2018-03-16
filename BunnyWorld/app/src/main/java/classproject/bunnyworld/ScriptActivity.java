package classproject.bunnyworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        Toast toast = Toast.makeText(getApplicationContext(),
                "Shapes may have 1 of any Trigger. Duplicates will overwrite the last.",
                Toast.LENGTH_LONG);
        toast.show();

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
        if(id > 0) {
            parent.setVisibility(View.INVISIBLE);
            thirdColumnStuffOn(view, false);
            secondColumnStuffOn(view, true);
            curScript.append(item + " ");
            displayScript();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void initializeButtonClickability(View view) {
        // first column (triggers) made clickable
        firstColumnStuffOn(view, true);

        // second column (actions) made unclickable
        secondColumnStuffOn(view, false);

        // third column (targets) made unclickable
        thirdColumnStuffOn(view, false);

        // End Instruction Button
        endClauseOn(view, false);
    }

    public void firstColumnStuffOn(View view, Boolean on) {
        // first column (triggers)
        Button onclick = (Button) findViewById(R.id.script_on_click_button);
        onclick.setEnabled(on);
        Button onenter = (Button) findViewById(R.id.script_on_enter_button);
        onenter.setEnabled(on);
        Button ondrop = (Button) findViewById(R.id.script_on_drop_button);
        ondrop.setEnabled(on);
        Spinner sender = (Spinner) findViewById(R.id.script_onDropTarget_spinner);
        sender.setVisibility(View.INVISIBLE);
    }

    public void firstColumnButtonsOn(View view, Boolean on) {
        // first column (triggers)
        Button onclick = (Button) findViewById(R.id.script_on_click_button);
        onclick.setEnabled(on);
        Button onenter = (Button) findViewById(R.id.script_on_enter_button);
        onenter.setEnabled(on);
        Button ondrop = (Button) findViewById(R.id.script_on_drop_button);
        ondrop.setEnabled(on);
    }

    public void secondColumnStuffOn(View view, Boolean on) {
        // second column (actions)
        Button go_to = (Button) findViewById(R.id.script_goto_button);
        go_to.setEnabled(on);
        Button play = (Button) findViewById(R.id.script_play_button);
        play.setEnabled(on);
        Button hide = (Button) findViewById(R.id.script_hide_button);
        hide.setEnabled(on);
        Button show = (Button) findViewById(R.id.script_show_button);
        show.setEnabled(on);
    }

    public void thirdColumnStuffOn(View view, Boolean on) {
        // third column (targets)
        int visibility;
        if(on) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }

        Spinner gopage = (Spinner) findViewById(R.id.script_goto_spinner);
        gopage.setVisibility(visibility);
        Spinner sound = (Spinner) findViewById(R.id.script_play_spinner);
        sound.setVisibility(visibility);
        Spinner hidden = (Spinner) findViewById(R.id.script_hide_spinner);
        hidden.setVisibility(visibility);
        Spinner shown = (Spinner) findViewById(R.id.script_show_spinner);
        shown.setVisibility(visibility);
        if(on) {
            gopage.setSelection(0);
            sound.setSelection(0);
            hidden.setSelection(0);
            shown.setSelection(0);
        }
    }

    public void endClauseOn(View view, Boolean on) {
        // first column (triggers)
        Button end = (Button) findViewById(R.id.script_semicolon_button);
        end.setEnabled(on);
    }

    public void onClickTrigger(View view) {
        curScript.append(ON_CLICK + " ");
        displayScript();
        firstColumnStuffOn(view, false);
        secondColumnStuffOn(view, true);
    }

    public void onEnterTrigger(View view) {
        curScript.append(ON_ENTER + " ");
        displayScript();
        firstColumnStuffOn(view, false);
        secondColumnStuffOn(view, true);
    }

    public void onDropTrigger(View view) {
        curScript.append(ON_DROP + " ");
        displayScript();
        Spinner senderSpinner = (Spinner) findViewById(R.id.script_onDropTarget_spinner);
        senderSpinner.setVisibility(View.VISIBLE);
        firstColumnButtonsOn(view, false);
    }

    public void goToAction(View view) {
        curScript.append(GO_TO + " ");
        displayScript();
        secondColumnStuffOn(view, false);
        Spinner gopage = (Spinner) findViewById(R.id.script_goto_spinner);
        gopage.setVisibility(View.VISIBLE);
        gopage.setSelection(0);
        endClauseOn(view, true);
    }

    public void playAction(View view) {
        curScript.append(PLAY + " ");
        displayScript();
        secondColumnStuffOn(view, false);
        Spinner sound = (Spinner) findViewById(R.id.script_play_spinner);
        sound.setVisibility(View.VISIBLE);
        sound.setSelection(0);
        endClauseOn(view, true);
    }

    public void hideAction(View view) {
        curScript.append(HIDE + " ");
        displayScript();
        secondColumnStuffOn(view, false);
        Spinner hidden = (Spinner) findViewById(R.id.script_hide_spinner);
        hidden.setVisibility(View.VISIBLE);
        hidden.setSelection(0);
        endClauseOn(view, true);
    }

    public void showAction(View view) {
        curScript.append(SHOW + " ");
        displayScript();
        secondColumnStuffOn(view, false);
        Spinner shown = (Spinner) findViewById(R.id.script_show_spinner);
        shown.setVisibility(View.VISIBLE);
        shown.setSelection(0);
        endClauseOn(view, true);
    }

    private void displayScript() {
        TextView scriptView = findViewById(R.id.script_editor_textView);
        scriptView.setText(curScript.toString().trim());
    }

    public void resetScript(View view) {
        curScript = new StringBuilder();
        displayScript();
        initializeButtonClickability(view);
    }

    public void deleteLastClause(View view) {
        String rightNow = curScript.toString();
        String[] clauses = rightNow.split(";");
        StringBuilder withoutLastClause = new StringBuilder();
        int len = clauses.length;
        for(int i = 0; i < len - 2; i++) { // minus 2 due to trailing space
            withoutLastClause.append(clauses[i].trim());
            withoutLastClause.append("; ");
        }

        curScript = withoutLastClause;
        displayScript();
        initializeButtonClickability(view);
    }

    public void addSemicolon(View view) {
        int len = curScript.length();
        curScript.deleteCharAt(len-1);
        curScript.append("; ");
        displayScript();
        initializeButtonClickability(view);
    }

    public void saveScript(View view) {
        if(curScript.charAt(curScript.length() - 2) != ';') {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please complete current instruction or delete it.",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        GameManager gameManager = GameManager.getInstance();
        gameManager.setCurScript(curScript.toString().trim());
        curShape.setScriptText(curScript.toString().trim());
        initializeButtonClickability(view);


        Intent intent = new Intent(this, EditorActivity.class);
        gameManager.setCurScript("");
        startActivity(intent);
    }


}
