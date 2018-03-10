package classproject.bunnyworld;

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
	public GPage getPage(String name) {
		for(GPage page: pages) {
			if (page.getName().toLowerCase().equals(name.toLowerCase())) {
				return page;
			}
		}
		return null;
	}

	/* searches pages for and returns a GShape
	 * by the name or null if none exists
 	 */
	GShape getShape(String name) {
		for (GPage page : getPages()) {
			GShape shape = page.getShape(name);
			if (shape != null) return shape;
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

	public String assignDefaultPageName() {
		int length = pages.size();
		StringBuilder pageName = new StringBuilder();
		pageName.append("Page");
		pageName.append(Integer.valueOf(length+1));
		String name = pageName.toString();

		while (duplicatePageName(name)) {
			pageName.append(Integer.valueOf(length));
			name = pageName.toString();
		}

		return name;
	}

	public boolean duplicatePageName(String pageName) {
		for (GPage page : pages) {
			String curName = page.getName();
			if (curName.equals(pageName)) {
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
