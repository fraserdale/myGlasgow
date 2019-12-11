package betterGlasgow;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Grades {
	private final String gradesURL = "https://uogstudents.mycampus.gla.ac.uk/psc/campus/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL?PORTALPARAM_PTCNAV=HC_SSR_SSENRL_GRADE&PortalActualURL=https%3a%2f%2fuogstudents.mycampus.gla.ac.uk%2fpsc%2fcampus%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL&PortalContentURL=https%3a%2f%2fuogstudents.mycampus.gla.ac.uk%2fpsc%2fcampus%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL&PortalContentProvider=HRMS&PortalCRefLabel=View%20My%20Grades&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fuogstudents.mycampus.gla.ac.uk%2fpsp%2fcampus%2f&PortalURI=https%3a%2f%2fuogstudents.mycampus.gla.ac.uk%2fpsc%2fcampus%2f&PortalHostNode=HRMS&NoCrumbs=yes";

	// public HashMap<String,String> getGrades(User user) throws IOException {
	public List<HashMap<String, String>> getGrades(User user) throws IOException {
		//make a get request with users login credentials
		Document doc = Jsoup.connect(gradesURL).header("authorization", "Basic " + user.getCreds()).get();
		//table with all current year grades in it
		Element gradeTable = doc.getElementsByClass("PSLEVEL1GRIDWBO").first();
		Elements gradeRows = gradeTable.select("tr");
		gradeRows.remove(0);
		gradeRows.remove(0);
		List<HashMap<String, String>> yearMarks = new ArrayList<HashMap<String, String>>();
		for (Element gradeRow : gradeRows) {
			//take each element in the row
			Elements rowElements = gradeRow.select("td");
			HashMap<String, String> subject = new HashMap<String, String>();
			int c = 0;
			//need to work out how to do better
			for (Element content : rowElements) {
				switch (c) {
				case 0:
					subject.put("courseID", content.text());
					break;
				case 1:
					subject.put("courseName", content.text());
					break;
				case 2:
					subject.put("credits", content.text());
					break;
				case 3:
					subject.put("schedule", content.text());
					break;
				case 4:
					subject.put("grade", content.text());
					break;
				case 5:
					subject.put("gradePoints", content.text());
					break;
				}
				c++;
				// check if a no credit course
			}
			if (!subject.get("credits").equals("")) {
				yearMarks.add(subject);
			}
		}
		return yearMarks;
	}

	public static Integer getGPA(List<HashMap<String, String>> grades) {
		Integer subjects = 0;
		Integer gradePointsRevised = 0; // into 22 points system grade points / (divided by num of credits)

		for (HashMap<String, String> subject : grades) {
			//if grade points have been given
			if (!subject.get("gradePoints").equals("")) {
				//calculate according to 22 points system
				gradePointsRevised += (Integer.parseInt(subject.get("gradePoints"))	/ Integer.parseInt(subject.get("credits")));
				subjects++;
			}
		}
		
		// how to calculate average
		return gradePointsRevised / subjects;

	}
}
