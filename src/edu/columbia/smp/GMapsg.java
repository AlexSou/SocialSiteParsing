/* A Soulier
 * Columbia University
 * April 2012
 * as4094@columbia.edu
 * Social Parser
 */

/* GMapsg.java
 * This function accesses a given URL from All Ears and reads the
 * complete HTML code, parsing the data needed for the trends analysis.
 * 
 * Input: String -> Current directory.
 */

package edu.columbia.smp;

import java.net.*;
import java.io.*;

//Class that parses Google Maps URLs.
public class GMapsg {

	static String curDir = "";

	// Class constructor.
	// Input: String -> current directory.
	public  GMapsg (String dir) {
		curDir = dir;
	}

	// Dummy function, call write1 with extra parameter first time.
	// Input: String -> URL to parse.
	public void write(String URLstr) throws Exception {
		write1(URLstr, 0);
	}

	// Function that writes URL data in a CSV file.
	// Input: String -> URL to parse, int-> current page being parsed.
	public void write1(String URLstrRaw, int curPage) throws Exception {
		String line = "";
		String URLstr = "";
		String Website;
		String Date;
		String Restaurant;
		String UserName;
		int Rating;
		String Review;
		String outLine, cid,  next = null;
		Boolean isNext;
		PrintWriter output;
		int reviewsLen, hrefCount;
		String[] reviews;

		if (URLstrRaw.contains("cid=")) { //Google site with reviews.
			String out = curDir + "/GoogleMaps.csv";
			File outFile = new File(out);
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			output = new PrintWriter (new FileWriter (out, true));
			isNext = false;

			if (URLstrRaw.contains("&q")) { //Trim URL to download reviews.
				cid = URLstrRaw.split("cid=")[1].split("&q")[0];
			}
			else {
				cid = URLstrRaw.split("cid=")[1];
			}

			//Review site URL for current cid.
			URLstr = "http://maps.google.com/maps/place?cid=" + cid + "&hl=en&gl=us&view=feature&mcsrc=google_reviews";

			URL currURL = new URL(URLstr);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(currURL.openStream()));
			Website = "Google Maps";

			//Loop until Restaurant found.
			while (!line.contains("<title>")) {
				line = in.readLine();
				if(line == null){ break;}
			}
			Restaurant = line.split("<title>")[1].split("</title>")[0];

			//Loop until end of file.
			while ((line = in.readLine()) != null) {

				//Start review.
				if (line.contains("</script> <script type=\"text/javascript\">window.onunload =")){
					reviewsLen = line.split("target=\"_blank\"><span>").length;
					reviews = new String[reviewsLen];
					reviews = line.split("target=\"_blank\"><span>");

					//For loop with reviews array.
					for(int i = 1;i < reviewsLen -1; i++) {
						if (!reviews[i].contains("Flag as inappropriate")) { // New review.
							UserName = reviews[i].split("</span>")[0];
							Date = reviews[i].split("class=\"date\">")[2].split("</span>")[0];
							Rating = reviews[i].split("class=\"rsw-starred\"").length - 1; //Rating, 1 star per rsw class in line.
							//Parse different kinds of reviews.
							if (reviews[i].contains("<span class=\"title comment-box-readonly-first-n\">") && reviews[i].contains("pp-google-review-snippet-")) {
								Review = reviews[i].split("<span class=\"title comment-box-readonly-first-n\">")[1].split("</span>")[0] + " " + reviews[i].split("pp-google-review-snippet-")[1].split("</span>")[0].split("\">")[1];
							}
							else if (reviews[i].contains("<span class=\"title comment-box-readonly-first-n\">")) {
								Review = reviews[i].split("<span class=\"title comment-box-readonly-first-n\">")[1].split("</span>")[0];
							}
							else if (reviews[i].contains("pp-google-review-snippet-")) {
								Review = reviews[i].split("pp-google-review-snippet-")[1].split("</span>")[0].split("\">")[1];
							}
							else {
								Review = "";
							}
							outLine = URLstrRaw.replace(",", " ") + "," +  Website.replace(",", " ") + "," + Date.replace(",", " ") + "," + Restaurant.replace(",", " ") + "," + UserName.replace(",", " ") + "," + Rating + ",," + Review.replace(",", " ");
							output.println(outLine);
						}
					}

					curPage = curPage + 10; //Move to next page.
					if (line.contains("start=" + curPage)) { //New page available.
						isNext = true;
						hrefCount = line.split("start=" + curPage)[0].split("href=\"").length;
						next = "http://maps.google.com" + line.split("start=" + curPage)[0].split("href=\"")[hrefCount - 1] + "start=" + curPage;
					}
				}


			}
			in.close();
			output.close();

			//If new page, create class for new page.
			if (isNext && next != null){
				GMaps gm = new GMaps(curDir);
				gm.write1(next, curPage);
			}
		}
	}
}

// End of code.
