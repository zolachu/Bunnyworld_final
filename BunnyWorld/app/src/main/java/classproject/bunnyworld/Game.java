package classproject.bunnyworld;

//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//import javax.imageio.ImageIO;

public class Game {

	private String name;
	private GPage firstPage;
	private GPage currPage;
	private Set<GPage> pages;
	private Set<GShape> possessions;

	Game(String name) {
		this.name = name;
		this.pages = new HashSet<GPage>();
		this.possessions = new HashSet<GShape>();
		this.firstPage = new GPage("page1");
		this.currPage  = firstPage;
		this.pages.add(firstPage);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void changeCurrPage(GPage page) {
		this.currPage = page;
		//updateCanvas();
		//currPage.onEnter();
	}

	public void addPage(GPage page) {
		this.pages.add(page);
	}

	public void removePage(GPage page) {
		this.pages.remove(page);
	}

	public void removePage(String name) {
		for (GPage page : this.pages) {
			if (page.getName().toLowerCase().equals(
					name.toLowerCase())) {
				this.pages.remove(page);
				break;
			}
		}
	}

	/* returns pointer to GPage given its name
	 * or returns null if a GPage by that name does not
	 * exist.
	 */
	public GPage getPage(String pageID) {
		for(GPage page: pages) {
			if (page.getName().equals(pageID)) return page;
		}
		return null;
	}

	public void addPossession(GShape shape) {
		this.possessions.add(shape);
	}

	public void removePossession(GShape shape) {
		this.possessions.remove(shape);
	}

	public void removePossession(String name) {
		for (GShape shape : this.possessions) {
			if (shape.getName().toLowerCase().equals(
					name.toLowerCase())) {
				this.possessions.remove(shape);
				break;
			}
		}
	}

	public boolean equals(Game game) {
		return this.name.toLowerCase().equals(
				game.getName().toLowerCase()); 
	}

	public String toString() {
		return this.name;
	}


}
