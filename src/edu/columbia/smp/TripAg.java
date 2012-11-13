/* A Soulier
 * Columbia University
 * April 2012
 * as4094@columbia.edu
 * Social Parser
 */

/* TripAg.java
 * This function accesses a given URL from All Ears and reads the
 * complete HTML code, parsing the data needed for the trends analysis.
 * 
 * Input: String -> Current directory.
 */

package edu.columbia.smp;

import java.net.*;
import java.io.*;

//Class that parses Trip Advisor URLs.
public class TripAg {

	static String curDir = "";

	//Class constructor.
	// Input: String -> current directory.
	public  TripAg (String dir) {
		curDir = dir;
	}

	//Start function, called the first time. Calls write1 with extra parameter.
	// Input: String -> URL to parse.
	public void write(String URLstrRaw) throws Exception {
		write1( URLstrRaw, false);
	}

	// Function that writes URL data in a CSV file.
	// Input: String -> URL to parse, Boolean -> true iff the first page of 
	// reviews is being processed.
	public void write1(String URLstrRaw, Boolean repeat) throws Exception {
		String line = "";
		String lineRaw = "";
		String Website = null;
		String Date;
		String Restaurant = "";
		String UserName;
		String Rating;
		String Review;
		String outLine, URLstr = null, next = null;
		Boolean isNext, empty;
		PrintWriter output;
		BufferedReader in = null;
		String Value, Atmosphere, Service, Food, prevLine, ratType;

		String out = curDir + "/TripAdvisor.csv";
		File outFile = new File(out);
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		output = new PrintWriter (new FileWriter (out, true));

		isNext = false;
		empty = false;

		if(!repeat) { //URL is of restaurant, not reviews.
			URL rawURL = new URL(URLstrRaw);
			BufferedReader inRaw = new BufferedReader(
					new InputStreamReader(rawURL.openStream()));

			while (  !lineRaw.contains("/ShowUserReviews")){ //Find reviews page.
				lineRaw = inRaw.readLine();
				//No reviews, exit.
				if(lineRaw == null || lineRaw.contains("</html>")){ empty = true; break; }
			}
		}
		if(!empty) { //Reviews present.
			if(!repeat) { //First page.
				URLstr = "http://www.tripadvisor.com" +lineRaw.split("href=\"")[1].split("\" onclick")[0].replace("\"/>", "");
			}
			else { 
				URLstr = URLstrRaw;
			}


			URL currURL = new URL(URLstr);
			in = new BufferedReader(
					new InputStreamReader(currURL.openStream()));
			Website = "Trip Advisor";

			//Loop until reviews found.
			while (  !line.contains("<meta name=\"keywords\" content=\"")){ 
				line = in.readLine();
				if(line == null || line.contains("</html>")){ empty = true;}
			}}

		if(empty){ //No reviews.
			System.out.println("Empty page");
		}
		else{
			Restaurant = line.split("<meta name=\"keywords\" content=\"")[1].split(",")[0];
			INITIAL: while (true){ //Repeat loop until break called.
				if (line.contains("<link rel=\"next\" href=") && !isNext) { //Next page found.
					next = "http://www.tripadvisor.com" + line.split("href=\"")[1].split("\"/")[0];
					isNext = true;
				}

				else if (line.contains("<div class=\"username mo\">")){ //New review
					line = in.readLine();
					if(line.contains("scrname")){
						UserName = line.split("\">")[1].split("<")[0];
					}
					else { //No username present.
						UserName = "";
					}

					Service = "";
					Value = "";
					Food = "";
					Atmosphere = "";
					//Loop until ratings found.
					while (!line.contains("<span class=\"rate rate_s s")){
						line = in.readLine();
						if(line == null || line.contains("</html>")){ break INITIAL;}
					}

					Rating = line.split("alt=\"")[1].split(" ")[0]; //Parse data.
					Date = in.readLine().split("Reviewed ")[1];

					while (!line.contains("<p id=\"review_")) {
						line = in.readLine();
						//End of file, break loop.
						if(line == null || line.contains("</html>")){ break INITIAL;}
					}
					Review = in.readLine();

					while (!line.contains("<span class=\"rate rate_ss ss") && !line.contains("<div class=\"username mo\">")){
						line = in.readLine();		
						if(line == null || line.contains("</html>")){ break INITIAL;}
					}

					if(line.contains("<span class=\"rate rate_ss ss")) { //Ratings present.
						while(!line.contains("<div class=\"username mo\">")) { //Find new review.
							if(line == null || line.contains("</html>")){ //Review finished.
								//Output review.
								outLine = URLstrRaw.replace(",", " ") + "," +  Website.replace(",", " ") + "," + Date.replace(",", " ") + "," + Restaurant.replace(",", " ") + "," + UserName.replace(",", " ") + "," + Rating.replace(",", " ") + ",," + Review.replace(",", " ") + ",,,,,," + Service.replace(",", " ") + ",," + Value.replace(",", " ") + "," + Food.replace(",", " ") + "," + Atmosphere.replace(",", " ");										
								output.println(outLine);
								break INITIAL;}
							while (!line.contains("<span class=\"rate rate_ss ss") && !line.contains("<div class=\"username mo\">")){
								line = in.readLine();		
								if(line == null || line.contains("</html>")){ //Review finished.
									outLine = URLstrRaw.replace(",", " ") + "," +  Website.replace(",", " ") + "," + Date.replace(",", " ") + "," + Restaurant.replace(",", " ") + "," + UserName.replace(",", " ") + "," + Rating.replace(",", " ") + ",," + Review.replace(",", " ") + ",,,,,," + Service.replace(",", " ") + ",," + Value.replace(",", " ") + "," + Food.replace(",", " ") + "," + Atmosphere.replace(",", " ");										
									output.println(outLine);
									break INITIAL;}
							}

							if((line.contains("<span class=\"rate rate_ss ss"))) { //Ratings present.
								prevLine = line;
								line = in.readLine(); //Need to buffer 2 lines.
								ratType = line.split("<")[0];
								if(ratType.equals("Value")){ //Value rating found.
									Value = prevLine.split("alt=\"")[1].split(" ")[0];

								}
								else if(ratType.equals("Atmosphere")){
									Atmosphere = prevLine.split("alt=\"")[1].split(" ")[0];

								}
								else if(ratType.equals("Service")){
									Service = prevLine.split("alt=\"")[1].split(" ")[0];

								}
								else if(ratType.equals("Food")){
									Food = prevLine.split("alt=\"")[1].split(" ")[0];

								}
								line = in.readLine(); //Check next line.
							}

						}

					}

					outLine = URLstrRaw.replace(",", " ") + "," +  Website.replace(",", " ") + "," + Date.replace(",", " ") + "," + Restaurant.replace(",", " ") + "," + UserName.replace(",", " ") + "," + Rating.replace(",", " ") + ",," + Review.replace(",", " ") + ",,,,,," + Service.replace(",", " ") + ",," + Value.replace(",", " ") + "," + Food.replace(",", " ") + "," + Atmosphere.replace(",", " ");
					output.println(outLine);

				}
				else {
					line = in.readLine();
					if (line == null || line.contains("</html>")) {
						break INITIAL;
					}
				}


			}
			endSeq(next, isNext, in, output); //End of review, finish up.


		}
	}

	// Function to complete review.
	// Input: String -> URL of next page to process, Boolean -> true iff there is another page to process,
	// BufferedReader -> input stream from website, PrintWriter -> output text stream to CSV file.
	public void endSeq(String next, Boolean isNext, BufferedReader in, PrintWriter output) throws Exception{
		in.close();
		output.close();

		if (isNext && next != null){ //If new page available, call it.
			TripA ta = new TripA(curDir);
			ta.write1(next, true);
		}
	}
}

//End of code.