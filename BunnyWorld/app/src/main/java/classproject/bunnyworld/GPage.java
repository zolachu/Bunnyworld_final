package classproject.bunnyworld;

import android.graphics.Canvas;
import java.util.*;

public class GPage {

	private String name;
	private List<GShape> shapes;


	public GPage(String name) {
		this.name = name;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/*
	 * Draws all shapes in the page by calling
	 * draw method in each shape in the shapes list
	 */
	public void draw(Canvas canvas) {
		for (GShape shape: shapes) {
			if (!shape.isPossession()) {
				shape.draw(canvas);
			}
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

	public void bringToTop(GShape shape) {
		if (this.shapes.contains(shape)) {
			this.shapes.remove(shape);
			this.shapes.add(shape);
		}
	}

	public boolean equals(GPage page) {
		return this.name.toLowerCase().equals(
				page.getName().toLowerCase());
	}

	public String assignDefaultShapeName() {
		int length = shapes.size();
		StringBuilder shapeName = new StringBuilder("Shape");
		shapeName.append(Integer.valueOf(length + 1));
		String name = shapeName.toString();

		int offset = 1;
		while (duplicateShapeName(name)) {
			shapeName = new StringBuilder("Shape");
			shapeName.append(Integer.valueOf(length + 1 + offset));
			name = shapeName.toString();
			offset++;
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
