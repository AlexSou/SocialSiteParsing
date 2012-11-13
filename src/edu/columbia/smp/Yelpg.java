/* A Soulier
 * Columbia University
 * April 2012
 * as4094@columbia.edu
 * Social Parser
 */

/* Yelpg.java
 * This function accesses a given URL from Yelp and reads the
 * complete HTML code, parsing the data needed for the trends analysis.
 * 
 * Input: String -> Current directory.
 */

package edu.columbia.smp;

import java.net.*;
import java.io.*;

//Class that parses Yelp URLs.
public class Yelpg {

	static String curDir = "";

	// Class constructor.
	// Input: String -> current directory.
	public  Yelpg (String dir) {
		curDir = dir;
	}

	// Function that writes URL data in a CSV file.
	// Input: String -> URL to parse.
	public void write(String URLstr) throws Exception {
		String line = "";
		String Website;
		String Date;
		String Restaurant;
		String UserName;
		String Rating;
		String Review;
		String outLine, next = null;
		Boolean isNext;
		PrintWriter output;

		if (URLstr.contains("/biz/")) { //Valid restaurant site. 
			String out = curDir + "/Yelp.csv";
			File outFile = new File(out);
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			output = new PrintWriter (new FileWriter (out, true));
			isNext = false;

			URL currURL = new URL(URLstr);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(currURL.openStream()));
			Website = "Yelp";

			Restaurant = URLstr.split("/biz/")[1]; //Take place name from URL.

			//Loop URL.
			while ((line = in.readLine()) != null) {

				if (!isNext && line.contains("<link rel=\"next\" href=\"")) {
					next = line.split("href=\"")[1].split("\" />")[0];
					isNext = true;
				}

				if (line.contains("Review from")){ //New review
					UserName = line.split("from ")[1].split("</")[0];

					while (!line.contains("star-img stars_")){
						line = in.readLine();
						if(line == null){ break;}
					}
					Rating = line.split("stars_")[1].split("\">")[0];


					while (!line.contains("dtreviewed")){
						line = in.readLine();
						if(line == null){ break;}
					}
					Date = line.split("title=\"")[1].split("\">")[0];


					while (!line.contains("review_comment description")){
						line = in.readLine();
						if(line == null){ break;}
					}
					Review = line.split("ieSucks\">")[1].split("</p")[0];
					outLine = URLstr.replace(",", " ") + "," +  Website.replace(",", " ") + "," + Date.replace(",", " ") + "," + Restaurant.replace(",", " ") + "," + UserName.replace(",", " ") + "," + Rating.replace(",", " ") + ",," + Review.replace(",", " ");
					output.println(outLine);
				}

			}
			in.close();
			output.close();

			//New page available.
			if (isNext && next != null && next.contains("/biz/")){
				Yelp yelp = new Yelp(curDir);
				yelp.write(next);
			}

		}

	}
}

//End of code.