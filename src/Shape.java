import java.util.*;

public class Shape {

	private String name;
	private String pictureName;
	private String textString;
	private String scriptText;
	private int fontSize;
	private double x;
	private double y;
	private double width;
	private double height;
	private boolean hidden;
	private boolean movable;
	private Map<String, String> scriptMap; 
	
			
	public Shape(String name) {

		//checking whether the name is valid (not taken already)
		//should somehow be check before the constructor is run by the game object?
		// From the game object we look at the shape array and assign the default name
		// later this name can be changed.
		this.name = name;
		this.pictureName = "";
		this.textString  = "";
		this.scriptText  = "";
		this.fontSize = 20;
		this.x = 0;
		this.y = 0;
		this.width  = 1;
		this.height = 1;
		this.hidden  = false;
		this.movable = false;
		this.scriptMap = new HashMap<String, String>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}

	
	public void setHidden() {
		this.hidden = true;
	}
	public void setMovable() {
		this.movable = true;
	}
	
	public void setPictureName(String name) {
		this.pictureName = name;
	}
	
	public void setTextString(String textString) {
		this.textString = textString;
		
	}
	
	public void setScriptText(String scriptText) {
		this.scriptText = scriptText;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	public void setHeight(double height) {
		this.height = height;
	}

	
	


}
