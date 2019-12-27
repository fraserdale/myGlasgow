package betterGlasgow;

import java.util.Scanner;
import java.util.Base64;

public class User {
	
    private String user;
    private String pass;
    //set if not wanting to input passw
    private String creds;
    
    public User() {
    	setUser();
    	setPass();
    }
    
    public User(String creds) {
    	this.creds = creds;
    }
    
    
    //input.close() causing error.
    public void setUser() {
    	System.out.println("Enter student number (E.g. 2987654a): ");
    	Scanner input = new Scanner(System.in);
    	this.user = input.next();
    }
    
    public void setPass() {
    	System.out.println("Enter password (E.g. Passw0rd_123): ");
    	Scanner input = new Scanner(System.in);
    	this.pass = input.next();
    }
    
    public String getCreds() {
    	if (creds!=null) {
    		return creds;
    	}else {
    		//put user pass in user:pass form and encode in base64  
        	String plainCreds = this.user.toString()  +':' + this.pass;
        	//System.out.println(Base64.getEncoder().encodeToString(plainCreds.getBytes()));
        	return Base64.getEncoder().encodeToString(plainCreds.getBytes());
    	}
    	
    }
    
    
    /*public static void main(String[] args) {
    	UserCredentials mine = new UserCredentials();
    	System.out.println(mine.getCreds());
    }*/
}

