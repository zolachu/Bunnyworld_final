package classproject.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Set;

/**
 * Created by vihan on 3/4/18.
 */

public class GameView extends View {

    //TODO get an instance of Game passed to this class via singleton or masterlist
    //TODO Assuming we have such a design in this current implementation

    //Declare instance variables
    Game game; //Game object(assuming this is intialized somehow...)
    Paint selectedPaint; //Paints an outline on shape selected in the editor
    Paint onDropPaint; //Paints an outline of a different color for on drop actions


    //constructor
    public GameView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    //Helper method to initialize instance variables
    private void init() {
        selectedPaint = new Paint();
        selectedPaint.setColor(Color.BLUE);

        onDropPaint = new Paint();
        onDropPaint.setColor(Color.GREEN);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO Iterate over set corresponding to current page and draw items

    }

    float x1, y1, x2, y2;

    //Identifies if there is a shape located at the specific clicked point
    private GShape getShapeAtPoint() {
        //TODO
        //Iterate over shapes and check if point is within the bounds of that shape
        //Return shape if found otherwise return null
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: //Handles dragging
                x1 = event.getX();
                y1 = event.getY();
                //Update items that match on drop script
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                //Update shape to be selected
                invalidate();
        }
        return true;

    }


    // Below are the stuff Cindy added:

    /*
     * Helper method that displays the currently selected shape's
     * information on the right.
     */
    private void displayCurShapeData(GShape shape) {}

    /*
     * Helper method to update the currently selected shape
     */
    private void updateCurShape(GShape shape) {}

    /*
     * Helper method to add a new GShape to the current page
     */
    private void addNewShape () {}

    /*
     * Helper method to erase the currently selected shape
     */
    private void eraseCurShape(GShape shape) {}

    /*
     * Helper method to add a new page to the current game
     */
    private void addNewPage() {}

    /*
     * Helper method to switch pages
     */
    private void switchPage() {}

    /*
     * Helper method to update currently displayed page
     */
    private void savePage() {}

    /*
     * Helper method to save the currently editted game
     */
    private void saveGame() {}









}
