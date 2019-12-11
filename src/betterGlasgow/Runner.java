package betterGlasgow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Runner {

	public static void main(String[] args) {
		//https://www.intertech.com/Blog/a-static-method-should-be-accessed-in-a-static-way/
		//create new user 
		User me = new User();
		try {
			List<HashMap<String,String>> myGrades = new Grades().getGrades(me);
			Integer GPA = new Grades().getGPA(myGrades);
			//print user grade, make prettify function with (table?)
			//print GPA
			System.out.println(myGrades);
			System.out.println(GPA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
