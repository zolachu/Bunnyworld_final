package classproject.bunnyworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vihan on 3/4/18.
 */

public class GameView extends View {

    private Game game = GameManager.getInstance().getCurGame();
    private Paint possessionPaint;
    private GShape selectedShape; //currently selected shape (selection triggered by clicks (action up/down)

    private float x, y;         // x and y of the mouse cursor
    private float downX, downY; // x and y of the shape when the action_down is detected
    private float distX, distY; // distance of a click location (inside a shape) from x and y fo the shape

    private int viewWidth, viewHeight;
    private static float possessionAreaProportion = 0.2f;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        possessionPaint = new Paint();
        possessionPaint.setColor(Color.BLACK);
        selectedShape = null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth  = w;
        viewHeight = h;
        EditorActivity.viewWidth  = viewWidth;
        EditorActivity.viewHeight = viewHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String curPage = game.getCurrPage().getName();

        //Draw the possession area boundary line
        canvas.drawLine(0.0f, (1 - possessionAreaProportion) * viewHeight,
                viewWidth, (1 - possessionAreaProportion) * viewHeight,
                possessionPaint);

        //draw game
        game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        game = GameManager.getInstance().getCurGame();

        System.err.println("On touch");

        switch (event.getAction()) {

            /*
             * Handles beginning of a click or a drag
             * Click is not detected in action_down, but when the mouse is released
             * So we only determine selectedShape and save x,y variables
             */
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();

                for (GShape shape : game.getPossessions()) {
                    shape.unselectShape();
                }
                for (GShape shape : game.getCurrPage().getShapes()) {
                    shape.unselectShape();
                }

                selectedShape = game.getTopShape(x, y);
                if (selectedShape != null) {
                    downX = selectedShape.getX();
                    downY = selectedShape.getY();
                    distX = x - downX;
                    distY = y - downY;
                    selectedShape.selectShape();

                    if (isInPossessionArea(selectedShape)) {
                        game.bringToTop(selectedShape);
                        game.addPossession(selectedShape);
                    } else {
                        game.getCurrPage().bringToTop(selectedShape);
                    }

                }
                try {
                    ((EditorActivity) getContext()).displayShapeInfo(selectedShape);
                } catch (Exception e) {
                }
                invalidate();

                break;

            /*
             * Handles dragging. If the selectedShape is movable, then
             * the position of the selectedShape is updated.
             * Also highlights the shapes in the page with green paint
             * if a shape's dropTarget is the selectedShape
             */
            case MotionEvent.ACTION_MOVE:
                if (selectedShape != null) {
                    if (selectedShape.isMovable()) {
                        selectedShape.setPosition(
                                event.getX() - distX,
                                event.getY() - distY);

                        for (GShape shape : game.getCurrPage().getShapes()) {
                            if (!shape.equals(selectedShape)) {
                                if (shape.isOnDropTarget(selectedShape)) {
                                    selectedShape.selectOnDrop();
                                }
                            }
                        }
                        try {
                            ((EditorActivity) getContext()).updateCoordinates(selectedShape);
                        } catch (Exception e) {
                        }
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                if (selectedShape != null) {
                    if (isInPossessionArea(selectedShape)) {
                        game.addPossession(selectedShape);
                    } else {
                        game.removePossession(selectedShape);
                        if (!selectedShape.isMovable() && selectedShape.containsPoint(x, y)) {
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
                            if (Math.abs(downX + distX - x) <= 0.05f * selectedShape.getWidth() &&
                                    Math.abs(downY + distY - y) <= 0.05f * selectedShape.getHeight()) {
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
                                                shape.isOnDropTarget(selectedShape)) {
                                            Script.perform(game, shape.getOnDropActionArray());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (GShape shape : game.getCurrPage().getShapes()) {
                    shape.unselectOnDrop();
                }

                invalidate();
                break;
        }
        return true;
    }

    /*
     * Returns the shape that's currently selected by the user
     */
    public GShape getSelectedShape() {
        return selectedShape;
    }

    public boolean isInPossessionArea(GShape shape) {
        if (shape.getY() + 0.5f * shape.getHeight() > (1 - possessionAreaProportion) * viewHeight) {
            return true;
        } else {
            return false;
        }
    }
}
