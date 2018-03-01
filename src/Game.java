import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.*;

public class Game {

	private String name;
	private Page firstPage;
	private Page currPage;
	private Set<Page> pages;
	private Set<Shape> possessions;

	Game(String name) {
		this.name = name;
		this.pages = new HashSet<Page>();
		this.possessions = new HashSet<Shape>();
		this.firstPage = new Page("page1");
		this.currPage  = firstPage;
		this.pages.add(firstPage);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void changeCurrPage(Page page) {
		this.currPage = page;
		//updateCanvas();
		//currPage.onEnter();
	}

	public void addPage(Page page) {
		this.pages.add(page);
	}

	public void removePage(Page page) {
		this.pages.remove(page);
	}

	public void removePage(String name) {
		for (Page page : this.pages) { 
			if (page.getName().equals(name)) {
				this.pages.remove(page);
				break;
			}
		}
	}

	public void addPossession(Shape shape) {
		this.possessions.add(shape);
	}

	public void removePossession(Shape shape) {
		this.possessions.remove(shape);
	}

	public void removePossession(String name) {
		for (Shape shape : this.possessions) { 
			if (shape.getName().equals(name)) {
				this.possessions.remove(shape);
				break;
			}
		}
	}

	public boolean equals(Game game) {
		return this.name.equals(game.getName()); 
	}

	// Random stuff for checking. Delete these
	public static void main(String[] args) {
		Game g = new Game("game1");	
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("raw/carrot.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}		
		System.out.println(img.getHeight());
		System.out.println(img.getWidth());
		System.out.println(img.toString());
	}
}
