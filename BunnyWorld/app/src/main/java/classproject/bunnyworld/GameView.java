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

    //TODO get an instance of Game passed to this class via singleton
    //TODO Assuming we have such a design in this current implementation

    //Declare instance variables
    Game game;            //Game object(assuming this is intialized somehow...)
    Paint possessionPaint;
    GShape selectedShape; //currently selected shape (selection triggered by clicks (action up/down)

    float x, y;
    float downX, downY;

    private static final float POSS_OFFSET = 100.0f; //vertical coordinate of possession boundary

    //Constructor
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //Helper method to initialize instance variables
    private void init() {
//        selectedPaint = new Paint();
//        selectedPaint.setColor(Color.BLUE);
//        onDropPaint   = new Paint();
//        onDropPaint.setColor(Color.GREEN);
        possessionPaint = new Paint();
        possessionPaint.setColor(Color.BLACK);
        selectedShape = null;
    }

    int viewWidth, viewHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw the possession area boundary line
        canvas.drawLine(0.0f, viewHeight - POSS_OFFSET, viewWidth,
                viewHeight-POSS_OFFSET, possessionPaint);
        game.getCurrPage().draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: //Handles beginning of a click or a drag
                x = event.getX();
                y = event.getY();

                //from the game, get currentpage
                //from the current page, get the shapes (list)
                //determine what the top shape is given x y
                selectedShape = game.getCurrPage().getTopShape(x, y);
                if (selectedShape != null) {
                    downX = selectedShape.getX();
                    downY = selectedShape.getY();
                }

                //TODO check if map contains onclick trigger, perform if it does
                invalidate();

            case MotionEvent.ACTION_MOVE: //Handles dragging
                if (selectedShape.isMovable()){
                    selectedShape.setPosition(event.getX(), event.getY());

                    // iterate over the shapes in the currpage,
                    // and for the shapes whose map contains on drop,
                    // check if dropShape (as in on drop <dropShape>)
                    // matches the selectedShape, then highlight (green outline)

                    //you're checking whether the selectedshape is the same as
                    //the targetshapes of shapes in the page

                    for(GShape shape: game.getCurrPage().getShapes()) {
                        if(!shape.equals(selectedShape)) {
                            if(shape.isOnDropTarget(selectedShape)) {
                                // highlight shape
                            }
                        }
                    }
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
                    //TODO this means it was a click so trigger on click here

                } else if (selectedShape.isMovable()){
                    // it's either a click or a release after a drag.
                    // To be able to drag the shape has to be movable.
                    // TODO need a way to distinguish short drag and a click for movable objects
                    // TODO check if dropShape (on drop <dropShape>) matches selectedShape,
                    // TODO perform on drop triggered action
                }

        }

        return true;
    }

    /*
     * Returns the shape that's currently selected by the user
     */
    public GShape getSelectedShape() {
        return selectedShape;
    }
}
