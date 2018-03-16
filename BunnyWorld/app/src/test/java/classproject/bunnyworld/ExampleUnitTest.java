package classproject.bunnyworld;

import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.TextViewCompat;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }
//
//    @Test
//    public void testScript() throws Exception {
//        String scriptText = "onClick hide carrot;onDrop carrot hide bunny;onEnter goto page1";
//        Map<String, String[]> map = new HashMap<String, String[]>();
//
//        Script.parse(scriptText, map);
//        assertTrue(map.containsKey(Script.ON_DROP));
//        assertTrue(map.containsKey(Script.ON_CLICK));
//
//        for(String key: map.keySet()) {
//            System.err.println("key: " + key);
//            String[] vals = map.get(key);
//            System.err.println("values: ");
//            for (String val : vals) {
//                System.err.print(val + ", ");
//            }
//        }
//    }

    @Test
    public void testTypeface() throws Exception {
        Typeface myFont = Typeface.create("monospace",Typeface.BOLD_ITALIC);
        assertEquals(myFont.getStyle(),3);
        assertTrue(myFont.isBold());
        assertTrue(myFont.isItalic());
    }
}