package classproject.bunnyworld;

//import java.awt.image.BufferedImage;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.lang.String;

//import javax.imageio.ImageIO;

public class Game {

	private String name;
	private GPage firstPage;
	private GPage currPage;
	private Set<GPage> pages;
	private Set<GShape> possessions;



	/**
	 * Script text is set of clauses where words are separated by spaces and clauses
	 * are separated by semicolon(;). Clause order is insignificant
	 *
	 * TRIGGERS
	 * onclick<action>
	 * onenter<actions>
	 * ondrop<shape name><actions>
	 *
	 * ACTIONS
	 * goto<page_name>
	 * play< sound_name>
	 * hide<shape_name>
	 * show<shape_name>
	 * @author nicholasseay
	 *
	 */
	public  class Script {

		/* Populates the given map using the string of raw script text
         * keys will be one of the Triggers above, and the value will be the complete
         * list of instructions to be completed in the event of the trigger.
         */
		void parse(String scriptText, HashMap<String, String> scriptMap) { // <trigger, instruction>
			String[] allInstructions = scriptText.split(";");

			for(String instruct: allInstructions) {
				int index = instruct.indexOf(' ');
				String trigger = instruct.substring(0, index);
				String action = instruct.substring(index + 1); // eliminate space at the end
				scriptMap.put(trigger, action);
			}
		}

		/* Given a clause, the Script decides what the action is and what its inputs
         * are, then performs the desired action
         */
		void perform(String instruct) {
			if(instruct == null) return;

			int index = instruct.indexOf(' ');
			String action = instruct.substring(0, index);
			String param = instruct.substring(index + 1); // eliminate space at the end

			switch (action) {
				case "goTo":
					goTo(param);
					break;
				case "play":
					play(param);
					break;
				case "hide":
					hideOrShow(param, true);
					//updateCanvas();
					break;
				case "show":
					hideOrShow(param, false);
					//updateCanvas();
					break;
			}
		}

		//reads the trigger

		/* switches game's current page
         * this function assumes only one page
         */
		void goTo(String param) {
			GPage page = getPage(param);
			if(page != null) changeCurrPage(page);
		}

		/* plays the sounds given by param
         * assumes there are no spaces within file names
         */
		void play(String param) {/*
			MediaPlayer mp = new MediaPlayer();
			String[] soundsToPlay = param.split(" ");
			for(String sound: soundsToPlay) {
				try {
					AssetFileDescriptor descriptor = res.openRawResourceFd("R.raw." + sound);
					FileDescriptor fd = descriptor.getFileDescriptor();

					mp.setDataSource(fd);
					mp.prepare();
					mp.start();
					while(mp.isPlaying()) {} // for now freezes while sound plays
					mp.stop();
					// need to close data descriptor
				} catch (Exception e) {
					// file by that name does not exist
				}
			}
			mp.release();
		*/
		}

		/* hides or shows the shapes given by param
         * based on the boolean passed into hide.
         * If hide is true, shape will be hidden.
         * (switches their hidden boolean to true)
         * assumes shape names contain no spaces
         */
		void hideOrShow(String param, boolean hide) {
			String[] shapesToHide = param.split(" ");
			for(String shapeName: shapesToHide) {
				GShape curr = findShape(shapeName);
				if(curr != null) curr.setHidden(hide);
			}
		}

		/* searches pages for and returns a GShape by the name
         * of shapeID or null if none exists
         */
		GShape findShape(String shapeID) {
			for(GPage page: pages) {
				GShape shape = page.getShape(shapeID);
				if(shape != null) return shape;
			}
			return null;
		}


	}



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
