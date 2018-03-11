package classproject.bunnyworld;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class Game {

	private String name;
	private GPage firstPage;
	private GPage currPage;
	private List<GPage> pages;
	private List<GShape> possessions;

	Game(String name) {
		this.name  = name;
		this.pages = new ArrayList<GPage>();
		this.possessions = new ArrayList<GShape>();
		this.firstPage = new GPage(assignDefaultPageName());
		this.currPage  = firstPage;
		this.pages.add(firstPage);
	}

	void draw(Canvas canvas){
		this.getCurrPage().draw(canvas);
		for (GShape shape : this.possessions) {
			shape.draw(canvas);
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GPage getCurrPage() {
		return this.currPage;
	}

	public void setCurrPage(GPage page) {
		this.currPage = page;
	}

	public List<GPage> getPages() {
		return this.pages;
	}

	public void addPage(GPage page) {
		this.pages.add(page);
	}

	public void removePage(GPage page) {
		this.pages.remove(page);
	}

	/*
	 * Returns GPage object given its name
	 * or returns null if a GPage by that name does not
	 * exist.
	 */
	public GPage getPage(String name) {
		for(GPage page: pages) {
			if (page.getName().toLowerCase().equals(name.toLowerCase())) {
				return page;
			}
		}
		return null;
	}

	/*
	 * Searches pages for and returns a GShape
	 * by the name or null if none exists
 	 */
	GShape getShape(String name) {
		for (GPage page : getPages()) {
			GShape shape = page.getShape(name);
			if (shape != null) return shape;
		}
		return null;
	}

	public List<GShape> getPossessions() {
		return possessions;
	}

	public void addPossession(GShape shape) {
		if (!this.possessions.contains(shape)) {
			this.possessions.add(shape);
			this.currPage.removeShape(shape);
			shape.setPossession(true);
		}
	}

	public void removePossession(GShape shape) {
		if (this.possessions.contains(shape)) {
			this.possessions.remove(shape);
			this.currPage.addShape(shape);
			shape.setPossession(false);
		}
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

	/*
	 * Returns a top GShape object pointed by the mouse
	 * at coordinates (x,y), if there is one. If not,
	 * returns null.
	 */
	public GShape getTopShape(float x, float y) {

		// Search in the possessions first
		for (int i = this.possessions.size()-1; i >= 0; i--) {
			if (this.possessions.get(i).containsPoint(x,y) &&
					!this.possessions.get(i).isHidden()) {
				return this.possessions.get(i);
			}
		}

		// Search in the current page
		for (int i = this.currPage.getShapes().size()-1; i >= 0; i--) {
			if (this.currPage.getShapes().get(i).containsPoint(x,y) &&
					!this.currPage.getShapes().get(i).isHidden()) {
				return this.currPage.getShapes().get(i);
			}
		}

		return null;
	}

	public String assignDefaultPageName() {
		int length = pages.size();
		StringBuilder pageName = new StringBuilder("Page");
		pageName.append(Integer.valueOf(length+1));
		String name = pageName.toString();

		int offset = 1;
		while (duplicatePageName(name)) {
			pageName = new StringBuilder("Page");
			pageName.append(Integer.valueOf(length + 1 + offset));
			name = pageName.toString();
			offset++;
		}
		return name;
	}

	public boolean duplicatePageName(String pageName) {
		for (GPage page : pages) {
			String curName = page.getName().toLowerCase();
			if (curName.equals(pageName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public GPage prePage() {
		int currentPageIndex = indexOfPage(this.currPage);
		if (currentPageIndex == 0) {
			return this.currPage;
		} else {
			return this.pages.get(currentPageIndex - 1);
		}
	}

	public GPage nextPage() {
		int currentPageIndex = indexOfPage(this.currPage);
		if (currentPageIndex == this.pages.size()-1) {
			return this.currPage;
		} else {
			return this.pages.get(currentPageIndex + 1);
		}
	}

	public boolean isFirstPage(GPage page) {
		if (page.equals(firstPage)) {
			return true;
		} else {
			return false;
		}
	}

	public GPage getFirstPage() {
		return this.firstPage;
	}

	private int indexOfPage (GPage page) {
		for (int i = 0; i < this.pages.size(); i++) {
			if (this.pages.get(i).equals(page)) {
				return i;
			}
		}
		return -1;
	}
}
