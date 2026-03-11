                                              OBJECT ORIENTED PROGRAMMING WITH JAVA

                                                          GROUP 4 

                                                  CONTACT MANAGEMENT SYSTEM

GROUP MEMBERS:

WAMBUGU ANGELA WANJIKU

OKUNGU MERCY AKOTH

OBIERO WILLIS OTEMA

MUURU BRIDGET GATWIRI

MUNYOKI DENNIS NGULA

MBULA JOSHUA KIEMA

KOSAR  ZAKARIA ABDI

KANYIRI MAXWELL MUNGAI

GACHIRA  OWEN MBUGUA

CIIRA PETER NJOROGE


                                 
                                 Contact Management System
                                 
Project Overview

The Contact Management System is a desktop application built with Java Swing and MySQL that allows users to securely store, organize, and manage their personal and business contacts. The application features user authentication, a modern graphical user interface, and full CRUD (Create, Read, Update, Delete) operations for contact management.

  
################################  Features  ###############################  

User Management

*User Registration - Create new account with first name, last name, username, and password
*	User Login - Secure authentication to access personal contacts
*	 Password Validation - Password confirmation during registration
*	 Username Uniqueness - Prevents duplicate usernames
*	 Logout - Securely end session and return to login screen

Contact Management

*  Add Contacts - Store name, phone number, and email address
*	 View Contacts - Display all contacts in a clean, sortable table
*	 Edit Contacts - Update existing contact information
*	 Delete Contacts - Remove contacts with confirmation prompt
*	 Search Contacts - Filter contacts by name in real-time
* Refresh Contacts - Clear search and display all contacts

User Interface

* Modern Design - Gradient backgrounds, custom buttons with hover effects
* Responsive Layout - Clean, professional appearance
* Tooltips - Helpful hints for all buttons
* Confirmation Dialogs - Prevent accidental deletions
* Error Messages - Clear feedback for user actions


########################### Technologies Used ###################

Technology	         &          Purpose

Java JDK 25	        -        Core programming language

Java Swing	        -        Graphical User Interface (GUI) framework

MySQL     	     -           Database for persistent data storage

JDBC	             -         Java Database Connectivity for MySQL communication

Maven             -         	Build tool and dependency management

NetBeans IDE	      -        Development environment

MySQL Connector/J	    -      JDBC driver for MySQL


###########################  System Requirements #####################

Minimum Requirements

*Operating System: Windows 10/11
*	RAM: 4 GB minimum
*	Storage: 100 MB free space
*	Java: JDK 17 or higher
*	MySQL: MySQL Server 8.0 or higher
* Screen Resolution: 1366 x 768 or higher

Recommended Requirements

* RAM: 8 GB or more
*	Processor: Intel Core i3 or equivalent
*	Screen Resolution: 1920 x 1080


####################### Database Setup #####################

Step 1: Create Database

CREATE DATABASE contactdb;

USE contactdb;

Step 2: Create Tables

You can manually create Tables:

CREATE TABLE users 

(
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_picture LONGBLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

*CREATE TABLE contacts 

(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


Step 3: Update Database Credentials

*In DatabaseConnection.java, update the following lines with your MySQL credentials:

*private static final String USERNAME = "root"; // Change to your MySQL username

*private static final String PASSWORD = ""; // Change to your MySQL password


##################  File Descriptions   ################

1. pom.xml
   
Purpose: Maven configuration file that manages dependencies and build settings

  *Declares MySQL Connector dependency
  *Sets Java version to JDK 25
  *Defines the main class for execution

2. ContactManagementSystem.java
   
Purpose: Entry point of the application.

*	Sets system look and feel
*	Initializes database tables
*	Launches the login window

3. DatabaseConnection.java
   
Purpose: Handles all database communication.

*	Stores database credentials
*	Establishes MySQL connections
*	Creates database tables if they don't exist
*	Provides connection objects to other classes

4. ModernLoginRegister.java
   
Purpose: Manages user authentication.

*Creates login and registration UI
*Handles user login validation
*	Processes new user registration
*Switches between login and register forms
*Opens main contact window after successful login

5. ModernContactManagementGUI.java
   
Purpose: Main contact management interface.

*	Displays user's contacts in a table
*	Provides Add, Edit, Delete, Search, and Refresh functions
*	Handles all CRUD operations with the database
*	Manages logout functionality
*	Custom table cell rendering for action buttons



###################  How to Run the Application  #################

Using NetBeans IDE
1.	Open NetBeans
2.	Click File → Open Project
3.	Navigate to the project folder and select it
4.	Right-click on the project and select Clean and Build
5.	Click the Run button (green triangle)


######################  Usage Guide  ##################

       1. Registration
1.	Launch the application
2.	Click "Sign Up" link
3.	Fill in:
o	First Name
o	Last Name
o	Username
o	Password
o	Retype Password
4.	Click "Create Account"
5.	Success message appears, then login
   
   
      2. Login
1.	Enter your Username and Password
2.	Click "Login"
   
3.	Main contact dashboard opens
   
  
       4. Adding a Contact
1.	Click "+ Add Contact" button
2.	Fill in:
o	Name
o	Phone
o	Email
3.	Click "Save"

   
          5. Viewing Contacts
•	All contacts appear automatically in the table
•	Columns: Name, Phone, Email, Actions


         6. Searching Contacts
1.	Type a name in the search box
2.	Click "Search" button
3.	Table updates to show matching contacts

   
           6. Refreshing Contacts
•	Click "View contacts " button to clear search and show all contacts


           7. Editing a Contact
1.	Click "Edit" button next to the contact
2.	Update the information in the dialog
3.	Click "OK"

   
            8. Deleting a Contact
1.	Click "Delete" button next to the contact
2.	Confirm deletion when prompted

   
           9. Logging Out
1.	Click "Logout" button
2.	Confirm logout
3.	Returns to login screen
   

######################  OOP Concepts Implemented #################

1. Encapsulation
File: DatabaseConnection.java

public class DatabaseConnection {
    // Private fields - data hidden from outside
    private static final String URL = "jdbc:mysql://localhost:3306/contactdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    // Public methods to access the data
    public static Connection getConnection() throws SQLException {
        // Implementation hidden from callers
    }
}
Explanation: Database credentials and connection logic are hidden within the class. Other classes only see the public methods, not the internal implementation.

2. Inheritance
File: ModernLoginRegister.java

public class ModernLoginRegister extends JFrame {
    // Inherits all properties and methods from JFrame
    public ModernLoginRegister() {
        setTitle("Contact Management System"); // Method inherited from JFrame
        setSize(900, 600); // Method inherited from JFrame
    }
}
Explanation: GUI classes extend JFrame to inherit window functionality without rewriting code for window management.

3. Polymorphism
File: ModernContactManagementGUI.java

// Multiple forms of event handling
searchButton.addActionListener(e -> searchContacts()); // Lambda form

registerLink.addMouseListener(new MouseAdapter() {     // Anonymous inner class
    public void mouseClicked(MouseEvent e) {
        cardLayout.show(mainPanel, "register");
    }
});

// Implementing the same interface in different ways
class ActionsRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(...) {
        // Custom rendering
    }
}
Explanation: The same concept (event handling) is implemented in multiple ways, demonstrating polymorphism.

4. Abstraction
File: ModernLoginRegister.java

private boolean authenticateUser(String username, String password) {
    // Complex database operation hidden behind a simple method call
    Connection conn = DatabaseConnection.getConnection(); // Abstraction
    
    // Caller doesn't need to know how connection works
}
Explanation: Complex database operations are hidden behind simple method names. Users of these methods don't need to understand the underlying complexity.


#############  Troubleshooting   ####################

       Common Issues and Solutions
Issue 1 "MySQL JDBC Driver not found"
*Solution:
1)	Ensure MySQL Connector/J is in the classpath
2)	Run Maven clean and build to download dependencies
   
Issue 2: "Access denied for user"
*Solution:
1)	Check username and password in DatabaseConnection.java
2)	Verify MySQL is running
3)	Ensure user has proper 

Issue 3: "Unknown database 'contactdb'"
*Solution:
*Create the database manually in MySQL:
CREATE DATABASE contactdb;

Issue 4: "Port 3306 is already in use"
*Solution:
1)	Stop any other MySQL instances
2)	Or change the port in the URL
   
Issue 5: Application doesn't start
*Solution:
1)	Check Java version: java -version
2)	Ensure all files are in correct package structure
3)	Clean and rebuild the project

   
#################  Error Messages  ###############

           Error Message	Likely Cause	Solution
           
"Please fill all fields!"	   -  Empty input during registration	Fill all form fields

"Passwords do not match!"      -	Password confirmation mismatch	Retype password correctly

"Username already exists!"	  - Duplicate username	Choose different username

"Invalid username or password!"  - Wrong credentials	Check username/password

"Error adding contact"	    - Database connection issue	Check MySQL connection


###############  Future Enhancements  #################

         Planned Features
*Password Hashing - Implement Hashing for secure password storage
*	Profile Pictures - Upload and display user profile images
*	 Contact Groups - Categorize contacts (Family, Friends, Work)
*	Import Contacts - Import from CSV or phone contacts
*	Dark Mode - Theme switching capability
*	Email Integration - Send emails directly from the application
*	Backup & Restore - Cloud backup functionality
*	 Two-Factor Authentication - Enhanced security


######################## CONCLUSION ########################

The Contact Management System successfully demonstrates:

*Full CRUD operations with MySQL
*	User authentication system
*	Modern GUI design using Java Swing
*	Object-Oriented Programming principles
*	Database integration using JDBC


