import java.util.*;

public class Page {

	private String name;
	private Set<Shape> shapes;

	public Page(String name) {
		this.name = name;
		this.shapes = new HashSet<Shape>(); 
	}

	public String getName() { 
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addShape(Shape shape) {
		this.shapes.add(shape);
	}

	public void removeShape(Shape shape) {
		this.shapes.remove(shape);
	}
	
	public void removeShape(String name) { 
		for (Shape shape : this.shapes) {
			if (shape.getName().equals(name)) {
				this.shapes.remove(shape);
			}
		}
	}

	public boolean equals(Page page) {
		return this.name.equals(page.getName());
	}
}
