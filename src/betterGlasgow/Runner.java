package betterGlasgow;

public class Runner {

	public static void main(String[] args) {
		//https://www.intertech.com/Blog/a-static-method-should-be-accessed-in-a-static-way/
		//create new user 
		User me = new User();
		Grades myGrades = new Grades(me);
		Integer gpa = myGrades.getGPA();
		//print user grade, make prettify function with (table?)
		//print GPA
		myGrades.prettify();
		//System.out.println(myGrades);
		System.out.println(gpa);
	}
}
