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

	/*
	 * Returns the shapes list
	 */
	public List<GShape> getShapes() {
		return this.shapes;
	}

	/*
	 * Returns a GShape object given its name
     * or returns null if a GShape by that name does not
 	 * exist.
 	 */
	public GShape getShape(String name) {
		for(GShape shape: this.shapes) {
			if (shape.getName().toLowerCase().equals(name.toLowerCase())) {
				return shape;
			}
		}
		return null;
	}

	/*
	 * Returns a top GShape object pointed by the mouse
	 * at coordinates (x,y), if there is one. If not,
	 * returns null.
	 */
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


	/*
	 * Draws all shapes in the page by calling
	 * draw method in each shape in the shapes list
	 */
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

	public String assignDefaultShapeName() {
		int length = shapes.size();
		StringBuilder shapeName = new StringBuilder();
		shapeName.append("Shape");
		shapeName.append(Integer.valueOf(length));
		String name = shapeName.toString();

		while (duplicateShapeName(name)) {
			shapeName.append(Integer.valueOf(length));
			name = shapeName.toString();
		}

		return name;
	}

	public boolean duplicateShapeName(String shapeName) {
		for (GShape shape : shapes) {
			String curName = shape.getName().toLowerCase();
			if (curName.equals(shapeName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
