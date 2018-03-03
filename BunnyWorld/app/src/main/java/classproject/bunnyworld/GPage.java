package classproject.bunnyworld;


import java.util.*;

public class GPage {

	private String name;
	private Set<GShape> shapes;

	public GPage(String name) {
		this.name = name.toLowerCase();
		this.shapes = new HashSet<GShape>();
	}

	public String getName() { 
		return this.name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
	}

	public void addShape(GShape shape) {
		this.shapes.add(shape);
	}

	public void removeShape(GShape shape) {
		this.shapes.remove(shape);
	}
	
	public void removeShape(String name) { 
		for (GShape shape : this.shapes) {
			if (shape.getName().toLowerCase().equals(
					name.toLowerCase())) {
				this.shapes.remove(shape);
			}
		}
	}

	/* returns pointer to GShape given its name
	 * or returns null if a GShape by that name does not
	 * exist.
	 */
	public GShape getShape(String shapeID) {
		for(GShape shape: shapes) {
			if (shape.getName().equals(shapeID)) return shape;
		}
		return null;
	}

	public boolean equals(GPage page) {
		return this.name.toLowerCase().equals(
				page.getName().toLowerCase());
	}
}
