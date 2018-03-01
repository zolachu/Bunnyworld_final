import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.*;

public class Game {

	private List<Page> pages;
	private List<Shape> possessionList;
	private Page firstPage;
	private Page currPage;

	Game() {
		this.pages = new ArrayList<Page>();
		this.possessionList = new ArrayList<Shape>();
		this.firstPage = new Page("page1");
		this.currPage  = firstPage;
		this.pages.add(firstPage);
	}
	

	public void changeCurrPage(Page page) {
		this.currPage = page;
		//updateCanvas();
		//currPage.onEnter() // call at end of function
	}
	
	
	
	public void addPage(Page page) {
		this.pages.add(page);
	}
	
	public void deletePage(Page page) {
		for (int i = 0; i < this.pages.size(); i++) {
			if (this.pages.get(i).equals(page)) {
				this.pages.remove(i);
				break;
			}
		}
	}
	
	public void deletePage(String name) {
		for (int i = 0; i < this.pages.size(); i++) {
			if (this.pages.get(i).getName().equals(name)) {
				this.pages.remove(i);
				break;
			}
		}
	}	


	
	// Random stuff for checking. Delete these
	public static void main(String[] args) {
		Game g = new Game();	
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
