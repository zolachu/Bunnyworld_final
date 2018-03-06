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
    Paint selectedPaint;  //Paints an outline on shape selected in the editor
    Paint onDropPaint;    //Paints an outline of a different color for on drop actions
    GShape selectedShape; //currently selected shape (selection triggered by clicks (action up/down)

    float downX;
    float downY;
//    float downWidth; //Don't think we need this
//    float downHeight;

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
        selectedShape = null;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO Iterate over set corresponding to current page and draw items

    }

    float x, y;

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

            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();

                //from the game, get currentpage
                //from the current page, get the shapes (list)
                selectedShape = game.getCurrPage().getTopShape(x, y);  // determine what the top shape is given x y
                if (selectedShape != null) {
                    downX = selectedShape.getX();
                    downY = selectedShape.getY();
                }

                //check if map contains onclick trigger, perform if it does




                invalidate();


            case MotionEvent.ACTION_MOVE: //Handles dragging
                if (selectedShape.isMovable()){
                    selectedShape.setPosition(event.getX(), event.getY());

                    //iterate over the shapes in the currpage,
                    // and for the shapes whose map contains on drop,
                    // check if dropShape (as in on drop <dropShape>)
                    // matches the selectedShape, then highlight (green outline)



                }

                //Update items that match on drop script
                invalidate();
                break;

            case MotionEvent.ACTION_UP:

                x = event.getX();
                y = event.getY();

                if(!selectedShape.isMovable() &&
                        GShape.containsPoints(downX, downY, selectedShape.getWidth(),
                        selectedShape.getHeight(), x, y)) {
                    //this means it was a click so trigger on click
                } else if (selectedShape.isMovable()){
                        //this means that we want to drag the shape
                        //check if dropShape (as in on drop <dropShape>)
                        // matches the selectedShape, and perform on drop triggered action
                } 



                }

                // it's either a click or a release after a drag.
                // to be able to drag, the shape is not hidden and movable.
                //





        }
        return true;

    }





}
