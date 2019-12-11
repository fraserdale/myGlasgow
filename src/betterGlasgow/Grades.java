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
	public List<HashMap<String, String>> grades = null;
	public User user;
	
	//constructor
	public Grades(User user) {
		this.user = user;
		try {
			this.grades = getGrades();
		} catch (IOException e) {
			this.grades = null;
		}
	}
	
	// public HashMap<String,String> getGrades(User user) throws IOException {
	public List<HashMap<String, String>> getGrades() throws IOException {
		//make a get request with users login credentials
		Document doc = Jsoup.connect(gradesURL).header("authorization", "Basic " + this.user.getCreds()).get();
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
		this.grades = yearMarks;
		return yearMarks;
	}
	
	public void prettify() {
		println("\u2500",42);
		System.out.println(center("Course ID",12)  +  " \u2502 " + center("Course Name",18)  +  " \u2502 " + center("Grade",3));
		println("\u2500",42);
		for (HashMap<String, String> subject : this.grades) {
			System.out.println(String.format("%1$-12s",subject.get("courseID").substring(0, Math.min(subject.get("courseID").length(), 12)))  +  " \u2502 " + String.format("%1$-18s",subject.get("courseName").substring(0, Math.min(subject.get("courseName").length(), 18)))  +  " \u2502 " + String.format("%1$-3s",subject.get("grade")));
		}
	}


	public Integer getGPA() {
		if(this.grades == null) {
			return 0;
		}
		Integer subjects = 0;
		Integer gradePointsRevised = 0; // into 22 points system grade points / (divided by num of credits)

		for (HashMap<String, String> subject : this.grades) {
			//if grade points have been given
			if (!subject.get("gradePoints").equals("")) {
				//calculate according to 22 points system
				gradePointsRevised += (Integer.parseInt(subject.get("gradePoints"))	/ Integer.parseInt(subject.get("credits")));
				subjects++;
			}
		}
		
		// how to calculate average
		if(subjects != 0) {
			return gradePointsRevised / subjects;
		}else {
			return 0;
		}
	}
	
	//string utils 
	
	public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }
    
    private void println(String s,int n){
        if(n > 0){
            System.out.print(s);
            println(s,n-1);
        }else if(n==0) {
        	System.out.print("\n");
        }
    }
}
