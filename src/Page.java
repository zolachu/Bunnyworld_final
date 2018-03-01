import java.util.*;

public class Page {

	private String name;
	private List<Shape> shapes;

	public Page(String name) {
		this.name = name;
		this.shapes = new ArrayList<Shape>(); 
	}

	public String getName() { 
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addShape(Shape shape) {
		shapes.add(shape);
	}

	public void removeShape(String shapeName) { 
		for (int i = 0; i < this.shapes.size(); i++) {
			if (this.shapes.get(i).getName().equals(shapeName)) {
				this.shapes.remove(i);
				break;
			}
		}
	}

	public boolean equals(Page page) {
		return this.name.equals(page.getName());
	}
}
