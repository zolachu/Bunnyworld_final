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

    //Declare instance variables
    Game game;            //Game object(assuming this is intialized somehow...)
    Paint selectedPaint;  //Paints an outline on shape selected in the editor
    Paint onDropPaint;    //Paints an outline of a different color for on drop actions
    GShape selectedShape; //currently selected shape (selection triggered by clicks (action up/down)

    float x, y;         // x and y of the mouse cursor
    float downX, downY; // x and y of the shape when the action_down is detected
    float distX, distY; // distance of a click location (inside a shape) from x and y fo the shape

    //Constructor
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //Helper method to initialize instance variables
    private void init() {
        selectedPaint = new Paint();
        selectedPaint.setColor(Color.BLUE);
        onDropPaint   = new Paint();
        onDropPaint.setColor(Color.GREEN);
        selectedShape = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO Iterate over list of shapes in the current page and draw items

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        game = GameManager.getInstance().getCurGame();

        switch (event.getAction()) {

            //Handles beginning of a click or a drag
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();

                selectedShape = game.getCurrPage().getTopShape(x, y);
                if (selectedShape != null) {
                    downX = selectedShape.getX();
                    downY = selectedShape.getY();
                    distX = x - downX;
                    distY = y - downY;

                    //TODO highlight the selected shape with the select paint


                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE: //Handles dragging
                if (selectedShape != null && selectedShape.isMovable()) {
                    selectedShape.setPosition(
                            event.getX() - distX,
                            event.getY() - distY);

                    // iterate over the shapes in the currpage,
                    // and for the shapes whose map contains on drop,
                    // check if dropShape (as in on drop <dropShape>)
                    // matches the selectedShape, then highlight (green outline)

                    //you're checking whether the selectedshape is the same as
                    //the targetshapes of shapes in the page

                    for (GShape shape : game.getCurrPage().getShapes()) {
                        if (!shape.equals(selectedShape)) {
                            if (shape.isOnDropTarget(selectedShape)) {
                                // TODO highlight shape

                            }
                        }
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                if (selectedShape != null) {
                    if (!selectedShape.isMovable() &&
                            GShape.containsPoints(downX, downY,
                                    selectedShape.getWidth(), selectedShape.getHeight(), x, y)) {
                        //this means it was a click so trigger on click here
                        if (selectedShape.hasOnClick()) {
                            Script.perform(game, selectedShape.getOnClickActionArray());
                        }

                    } else if (selectedShape.isMovable()) {
                        // it's either a click or a release after a drag.
                        // To be able to drag the shape has to be movable.

                        // if the shape was dragged (moved) by less than
                        // 5% of the width and height, then detect it as a click
                        // rather than a drag.
                        if (Math.abs(downX + distX - x) <= 0.05 * selectedShape.getWidth() &&
                                Math.abs(downY + distY - y) <= 0.05 * selectedShape.getHeight()) {
                            if (selectedShape.hasOnClick()) {
                                Script.perform(game, selectedShape.getOnClickActionArray());
                            }
                        } else {
                            // it's a drag

                            // iterate over the shapes in the page,
                            // check if their shapes overlap with the currently selected shape
                            // at its current position, and if it does, and contains ondrop,
                            // and the drop target matches the selected shape,
                            // perform on drop action
                            for (GShape shape : game.getCurrPage().getShapes()) {

                                //left:   x
                                //right:  x + width
                                //top:    y
                                //bottom: y + height

                                //left or right
                                // if selectedShape's left is greater than shape's right
                                // if selectedShape's right is less than shape's left
                                //above or below
                                // if selectedShape's top is greater than shape's bottom
                                // if selectedShape's bottom is less than shape's top
                                // then there is no overlap

                                if (!(selectedShape.getX() > shape.getX() + shape.getWidth() ||
                                        selectedShape.getX() + selectedShape.getWidth() < shape.getX() ||
                                        selectedShape.getY() > shape.getY() + shape.getHeight() ||
                                        selectedShape.getY() + selectedShape.getHeight() < shape.getY())) {
                                    if (!shape.equals(selectedShape) &&
                                            shape.isOnDropTarget(selectedShape)){
                                        Script.perform(game, shape.getOnDropActionArray());
                                    }
                                }
                            }
                        }
                    }
                    invalidate();
                }

                //TODO also un-highlight the selected shape and the highlighted shapes
                // (triggered by ondrop clause) upon release (action up)


                break;
        } //end of switch
        return true;
    } // end of method

    /*
     * Returns the shape that's currently selected by the user
     */
    public GShape getSelectedShape() {
        return selectedShape;
    }
}
