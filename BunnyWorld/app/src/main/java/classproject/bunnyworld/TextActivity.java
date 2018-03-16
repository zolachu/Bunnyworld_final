package classproject.bunnyworld;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TextActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private SpannableStringBuilder myStr;
    private String spinnerItem;
    private Typeface newFont = Typeface.DEFAULT;

    private int textSize = 50;
    private int color = Color.GRAY;
    private Typeface fontNstyle = Typeface.create(newFont,Typeface.NORMAL);
//    private String fontFamilyName = "sans-serif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        GameManager gameManager = GameManager.getInstance();
        GameView myView = gameManager.getGameView();
        GShape curShape = myView.getSelectedShape();
        myStr = curShape.getRichText();

        if (myStr == null) {
            myStr = new SpannableStringBuilder("");

        } else {
            EditText enterText = findViewById(R.id.text_textString_editText);
            enterText.setText(myStr.toString());
            setMyStr(enterText);

            Paint curPaint = curShape.getRichTextPaint();
            textSize = (int)(curPaint.getTextSize());
        }

        EditText sizes = findViewById(R.id.text_fontSize_editText);
        sizes.setText(Integer.toString(textSize));

        spinnerSetUp1();
        spinnerSetUp2();
    }

    // Color
    private void spinnerSetUp1() {
        Spinner spinner = findViewById(R.id.text_color_spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> adapterList = new ArrayList<>();
        adapterList.add("");
        adapterList.add("BLACK");
        adapterList.add("BLUE");
        adapterList.add("CYAN");
        adapterList.add("GRAY");
        adapterList.add("GREEN");
        adapterList.add("MAGENTA");
        adapterList.add("RED");
        adapterList.add("WHITE");
        adapterList.add("YELLOW");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Font (Typeface)
    private void spinnerSetUp2() {
        Spinner spinner = findViewById(R.id.text_font_spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> adapterList = new ArrayList<>();
        adapterList.add("");
//        adapterList.add("DEFAULT");
        adapterList.add("SANS-SERIF");
        adapterList.add("MONOSPACE");
        adapterList.add("SERIF");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, adapterList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        spinnerItem = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private String getUserInput() {
        EditText userInput = findViewById(R.id.text_textString_editText);
        String input = userInput.getText().toString();
        return input;
    }

    public void setMyStr(View view) {
        myStr.clear();
        myStr.append(getUserInput());
        displayText();
    }

    public void changeStyle(View view) {
        setStyle();
    }

    private void setStyle() {
        CheckBox myBold = findViewById(R.id.text_bold_checkbox);
        CheckBox myItal = findViewById(R.id.text_italic_checkbox);

        if (myBold.isChecked() && myItal.isChecked()) {
            fontNstyle = Typeface.create(newFont,Typeface.BOLD_ITALIC);
            int len = myStr.length();
            if (len > 0) {
                myStr.setSpan(new StyleSpan(Typeface.BOLD), 0, len, 0);
                displayText();
                myStr.setSpan(new StyleSpan(Typeface.ITALIC), 0, len, 0);
                displayText();
            }
        } else if (myBold.isChecked() && !myItal.isChecked()) {
            fontNstyle = Typeface.create(newFont,Typeface.BOLD);
            int len = myStr.length();
            if (len > 0) {
                myStr.setSpan(new StyleSpan(Typeface.BOLD), 0, len, 0);
                displayText();
            }
        } else if (!myBold.isChecked() && myItal.isChecked()) {
            fontNstyle = Typeface.create(newFont,Typeface.ITALIC);
            int len = myStr.length();
            if (len > 0) {
                myStr.setSpan(new StyleSpan(Typeface.ITALIC), 0, len, 0);
                displayText();
            }
        } else {
            fontNstyle = Typeface.create(newFont,Typeface.NORMAL);
            int len = myStr.length();
            if (len > 0) {
                myStr.setSpan(new StyleSpan(Typeface.NORMAL), 0, len, 0);
                displayText();
            }
        }
    }

//    public void makeBold(View view) {
//        int len = myStr.length();
//        if (len > 0) {
//            myStr.setSpan(new StyleSpan(Typeface.BOLD), 0, len, 0);
//            displayText();
//        }
//        fontNstyle = Typeface.DEFAULT_BOLD;
//    }
//
//    public void makeItalic(View view) {
//        int len = myStr.length();
//        if (len > 0) {
//            myStr.setSpan(new StyleSpan(Typeface.ITALIC), 0, len, 0);
//            displayText();
//        }
//        fontNstyle = Typeface.create("Arial",Typeface.ITALIC);
//    }

    public void resetStyle(View view) {
        String original = myStr.toString();
        myStr = new SpannableStringBuilder(original);
        fontNstyle = Typeface.DEFAULT;
        displayText();
    }

//    public void resetText(View view) {
//        myStr = new SpannableStringBuilder();
//        displayText();
//    }

    public void setTextColor(View view) {
        color = getColor();

        if (color != 0) {
            int len = myStr.length();
            if (len > 0) {
                myStr.setSpan(new ForegroundColorSpan(color), 0, len, 0);
            }
        }
        displayText();
    }

//    public void setHighlightColor(View view) {
//        int color = getColor();
//
//        if (color != 0) {
//            int len = myStr.length();
//            myStr.setSpan(new BackgroundColorSpan(color),0,len-1,0);
//        }
//        displayText();
//    }

    private int getColor() {
        int color = 0;
        switch (spinnerItem) {
            case "BLACK":
                color = Color.BLACK;
                return color;
            case "BLUE":
                color = Color.BLUE;
                return color;
            case "CYAN":
                color = Color.CYAN;
                return color;
            case "GRAY":
                color = Color.GRAY;
                return color;
            case "GREEN":
                color = Color.GREEN;
                return color;
            case "MAGENTA":
                color = Color.MAGENTA;
                return color;
            case "RED":
                color = Color.RED;
                return color;
            case "WHITE":
                color = Color.WHITE;
                return color;
            case "YELLOW":
                color = Color.YELLOW;
        }
        return color;
    }

    public void setFont(View view) {
        switch (spinnerItem) {
//            case "DEFAULT":
//                newFont = Typeface.DEFAULT;
//                setStyle();
//                break;
            case "MONOSPACE":
                newFont = Typeface.MONOSPACE;
                setStyle();
                break;
            case "SERIF":
                newFont = Typeface.SERIF;
                setStyle();
                break;
            case "SANS-SERIF":
                newFont = Typeface.SANS_SERIF;
                setStyle();
        }
        displayText();
    }

//    public void setFont(View view) {
//        switch (spinnerItem) {
//            case "CASUAL":
//                fontFamilyName = "casual";
//                break;
//            case "MONOSPACE":
//                fontFamilyName = "monospace";
//                break;
//            case "SERIF":
//                fontFamilyName = "serif";
//                break;
//            case "CURSIVE":
//                fontFamilyName = "cursive";
//        }
//        displayText();
//    }

    public void setFontSize(View view) {
        EditText fontSize = findViewById(R.id.text_fontSize_editText);
        String myFontSize = fontSize.getText().toString();
        textSize = Integer.valueOf(myFontSize);
        int len = myStr.length();
        if (len > 0) {
            myStr.setSpan(new AbsoluteSizeSpan(textSize,true), 0, len, 0);
            displayText();
        }
    }

    public void saveText(View view) {
        GameManager gameManager = GameManager.getInstance();
        GameView myView = gameManager.getGameView();
        GShape curShape = myView.getSelectedShape();
//        curShape.setMyFont(newFont);
        curShape.setTextString(myStr.toString());
        curShape.setRichText(myStr);
        curShape.setRichTextPaint(makePaint());
        curShape.setFontSize(textSize);

        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    private Paint makePaint() {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setTypeface(fontNstyle);

        return paint;
    }

    private void displayText() {
        TextView display = findViewById(R.id.text_showText_textView);
        display.setText(myStr);
        display.setTextSize(textSize);
        display.setTypeface(fontNstyle);
    }
}
