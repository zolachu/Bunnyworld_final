package classproject.bunnyworld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.*;


public class GShape {

	private static final int RECT = 0;
	public static final int IMAGE = 1;
	public static final int TEXT  = 2;

	private static final float INITIAL_WIDTH  = 5;
	private static final float INITIAL_HEIGHT = 5;

	private static Paint fillPaint;
	private static Paint textPaint;


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
		//checking whether the name is valid (not taken already)
		//should somehow be check before the constructor is run by the game object?
		// From the game object we look at the shape array and assign the default name
		// later this name can be changed.

		fillPaint = new Paint();
		fillPaint.setColor(Color.GRAY);
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);

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

	public static boolean containsPoints(float x, float y, float width, float height,
										 float touchX, float touchY) {
		return (touchX >= x && touchX <= x + width &&
				touchY >= y && touchY <= y + height);
	}

	public boolean containsPoint(Float x, Float y) {
		return (x >= this.x && x <= this.x + this.width &&
				y >= this.y && y <= this.y + this.height);
	}

	public void draw(Canvas canvas ) {
		if (isHidden()) {
			return;
		}

		if (this.getState() == TEXT) {
			canvas.drawText(this.text, this.x, this.y, textPaint);
		} else if (this.getState() == IMAGE) {
			//canvas.drawBitmap(something...)
		} else {
			float left   = this.x;
			float right  = this.x + this.width;
			float top    = this.y;
			float bottom = this.y + this.height;
			canvas.drawRect(left, top, right, bottom, fillPaint);
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

	/* Uses its current x and y and the passed in canvasBottom
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

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

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

	/* This function checks whether a shape has an onDrop.
	 * If so, it checks to see if the sender for that onDrop
	 * is the same as the currently selected shape being dragged
	 * in the game, and returns true if so.
	 */
	public boolean isOnDropTarget(GShape selectedShape) {
		String[] onDropActionArray = scriptMap.get(Script.ON_DROP);
		if(onDropActionArray == null) return false;
		String possibleSender = onDropActionArray[Script.ON_DROP_SENDER_INDEX];
		return possibleSender.equals(selectedShape.getName());
	}
}
