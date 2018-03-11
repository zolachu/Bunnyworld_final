package classproject.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import java.util.*;

public class GShape {

	private static final int RECT = 0;
	public static final int IMAGE = 1;
	public static final int TEXT  = 2;

	private static final float INITIAL_WIDTH  = 100;
	private static final float INITIAL_HEIGHT = 100;

	private static Paint fillPaint;
	private static Paint textPaint;
	Paint selectedPaint;  //Paints an outline on shape selected in the editor
	Paint onDropPaint;    //Paints an outline of a different color for on drop actions


	private boolean isSelected;
	private boolean isOnDrop;

	private String name;
	private String pictureName;
	private String text;
	private String script;
	private int fontSize;
	private float x;
	private float y;
	private float width;
	private float height;
	private boolean hidden;
	private boolean movable;
	private Map<String, String[]> scriptMap;

	public GShape(String name, float x, float y) {

		fillPaint = new Paint();
		fillPaint.setColor(Color.GRAY);
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);

		selectedPaint = new Paint();
		selectedPaint.setColor(Color.BLUE);
		selectedPaint.setStyle(Paint.Style.STROKE);

		onDropPaint   = new Paint();
		onDropPaint.setColor(Color.GREEN);
		onDropPaint.setStyle(Paint.Style.STROKE);

		this.name = name;
		this.pictureName = "";
		this.text   = "";
		this.script = "";
		this.fontSize = 20;
		this.x = x;
		this.y = y;
		this.width  = INITIAL_WIDTH;
		this.height = INITIAL_HEIGHT;
		this.hidden  = false;
		this.movable = false;
		this.scriptMap = new HashMap<String, String[]>();
	}

	public GShape(String name, float x, float y, String text, int type) {
		this(name, x, y);
		if (type == TEXT) {
			this.text = text;
		} else if (type == IMAGE) {
			this.pictureName = text;
		}
	}

	/*
	 * Checks whether some point (touchX, touchY) is cointained in a
	 * bounding box, with dimension (x,y,width,height) and
	 * returns true if the box contains the point
	 */
	public static boolean containsPoints(float x, float y, float width, float height,
										 float touchX, float touchY) {
		return (touchX >= x && touchX <= x + width &&
				touchY >= y && touchY <= y + height);
	}

	/*
	 * Checks whether the shape contains some point (x, y)
	 * and returns true if the shape contains the point.
	 */
	public boolean containsPoint(Float x, Float y) {
		return (x >= this.x && x <= this.x + this.width &&
				y >= this.y && y <= this.y + this.height);
	}

	public void draw(Canvas canvas) {
		float left   = this.x;
		float right  = this.x + this.width;
		float top    = this.y;
		float bottom = this.y + this.height;

		if (isHidden()) {
			return;
		}
		if (this.getState() == TEXT) {
			canvas.drawText(this.text, this.x, this.y, textPaint);
		} else if (this.getState() == IMAGE) {
			Context cont = GameManager.getInstance().getGameView().getContext();
			int resID = cont.getResources().getIdentifier(this.getPictureName(),
					"drawable", cont.getPackageName());
			BitmapDrawable picToDraw = (BitmapDrawable) cont.getResources().getDrawable(resID);
			Bitmap bitmap = picToDraw.getBitmap();
			canvas.drawBitmap(bitmap, null, new RectF(left, top, right, bottom), null);
		} else {
			canvas.drawRect(left, top, right, bottom, fillPaint);
		}
		if (this.getOnDrop() == true) {
			canvas.drawRect(left, top, right, bottom, onDropPaint);
		} else if(this.getSelected() == true) {
			canvas.drawRect(left, top, right, bottom, selectedPaint);
		}
	}

	public String getName() {
		return this.name;
	}

	public int getState() {
		if (!this.text.isEmpty()) {
			return TEXT;
		} else if (!this.pictureName.isEmpty()) {
			return IMAGE;
		} else {
			return RECT;
		}
	}

	/*
	 * Uses its current x and y and the passed in canvasBottom
	 * to figure out whether half of itself is within possessions
	 * Returns true if yes
	 */
	public boolean inPossessions(Float canvasBottom) {
		return false;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public boolean isMovable() {
		return this.movable;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	public String getText() { return this.text; }

	public String getPictureName() { return this.pictureName; }

	public String getScript() { return this.script; }

	public int getFontSize() { return this.fontSize; }

	public boolean getHidden() { return this.hidden; }

	public boolean getMovable() { return this.movable; }

	public void setName(String name) {
		this.name = name;
	}

	public void setHidden(boolean hide) {
		this.hidden = hide;
	}

	public void setMovable(boolean move) {
		this.movable = move;
	}

	public void setPictureName(String name) {
		this.pictureName = name;
	}

	public void setTextString(String textString) {
		this.text = textString;
	}

	public void setScriptText(String scriptText) {
		this.script = scriptText;
		Script.parse(scriptText, scriptMap);
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(float x1, float y1) {
		this.x = x1;
		this.y = y1;
	}

	public void selectShape() { this.isSelected = true; }

	public void unselectShape() { this.isSelected = false; }

	public boolean getSelected() { return this.isSelected; }

	public void selectOnDrop() { this.isOnDrop = true; }

	public void unselectOnDrop() { this.isOnDrop = false; }

	public boolean getOnDrop() { return this.isOnDrop; }

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean equals(GShape shape) {
		return this.name.toLowerCase().equals(
				shape.getName().toLowerCase());
	}

	/*
	 * This function checks whether a shape has an onDrop.
	 * If so, it checks to see if the sender for that onDrop
	 * is the same as the currently selected shape being dragged
	 * in the game, and returns true if so.
	 */
	public boolean isOnDropTarget(GShape selectedShape) {
		String[] onDropActionArray = this.getOnDropActionArray();
		if(onDropActionArray != null) {
			String possibleSender = onDropActionArray[Script.ON_DROP_SENDER_INDEX];
			return possibleSender.toLowerCase().equals(selectedShape.getName().toLowerCase());
		} else {
			return false;
		}
	}

	/*
	 * Returns true if the shape has an OnDrop Trigger
	 */
	public boolean hasOnDrop() {
		return (this.scriptMap.containsKey(Script.ON_DROP));
	}

	/*
	 * Returns true if the shape has an OnClick Trigger
	 */
	public boolean hasOnClick() {
		return (this.scriptMap.containsKey(Script.ON_CLICK));
	}

	/*
	 * Returns true if the shape has an OnEnter Trigger
	 */
	public boolean hasOnEnter() {
		return (this.scriptMap.containsKey(Script.ON_ENTER));
	}

	/*
	 * Returns the action array for the onDrop Trigger if the shape has an OnDrop Trigger.
	 * Else, return null
	 */
	public String[] getOnDropActionArray() {
		if (this.hasOnDrop()) {
			return this.scriptMap.get(Script.ON_DROP);
		} else {
			return null;
		}
	}

	/*
	 * Returns the action array for the onClick Trigger if the shape has an OnClick Trigger.
	 * Else, return null
	 */
	public String[] getOnClickActionArray() {
		if (this.hasOnClick()) {
			return this.scriptMap.get(Script.ON_CLICK);
		} else {
			return null;
		}
	}

	/*
	 * Returns the action array for the onEnter Trigger if the shape has an OnEnter Trigger.
	 * Else, return null
	 */
	public String[] getOnEnterActionArray() {
		if (this.hasOnEnter()) {
			return this.scriptMap.get(Script.ON_ENTER);
		} else {
			return null;
		}
	}
}
