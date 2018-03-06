package classproject.bunnyworld;


import android.graphics.Canvas;

import java.util.*;

public class GPage {

	private String name;
	private List<GShape> shapes;

	public GPage(String name) {
		this.name = name.toLowerCase();
		this.shapes = new ArrayList<GShape>();
	}


	public GShape getTopShape(float x, float y) {
		for (int i = shapes.size()-1; i >= 0; i--) {
			if (shapes.get(i).containsPoint(x,y) &&
					!shapes.get(i).isHidden()) {
				return shapes.get(i);
			}
		}
		return null;
	}


	public String getName() { 
		return this.name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
	}

	/* returns pointer to GShape given its name
     * or returns null if a GShape by that name does not
 	 * exist.
 	 */
	public GShape getShape(String name) {
		for(GShape shape: shapes) {
			if (shape.getName().toLowerCase().equals(name.toLowerCase())) {
				return shape;
			}
		}
		return null;
	}

	public void draw(Canvas canvas) {
		for (GShape shape: shapes) {
			shape.draw(canvas);
		}
	}

	public void addShape(GShape shape) {
		if (!shapes.contains(shape)) {
			this.shapes.add(shape);
		}
	}

	public void removeShape(GShape shape) {
		if (shapes.contains(shape)) {
			this.shapes.remove(shape);
		}
	}
	
	public void removeShape(String name) { 
		for (GShape shape : this.shapes) {
			if (shape.getName().toLowerCase().equals(
					name.toLowerCase())) {
				this.shapes.remove(shape);
			}
		}
	}

	public boolean equals(GPage page) {
		return this.name.toLowerCase().equals(
				page.getName().toLowerCase());
	}
}
