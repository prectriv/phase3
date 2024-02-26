import java.sql.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Date;

public class Phase3 {
	// Replace the "USERID" and "PASSWORD" with your PostgreSQL username and
	// password (the postgreSQL user you created in Phase2).

	private static final String USERID = "remy";
	private static final String PASSWORD = "0509";
	static Scanner in = new Scanner(System.in);

	public static Connection connect2postgres() {
		Connection connection = null;
		try {
			// Register the PostgreSQL driver
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver?");
			e.printStackTrace();
			return connection;
		}
		System.out.println("PostgreSQL JDBC Driver Registered!");
		try {
			// create the connection string
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/yelpdb", USERID, PASSWORD);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return connection;
		}
		System.out.println("PostgreSQL JDBC Driver Connected!");
		return connection;
	}

	//TASK 1:
	public static void searchByCategory(Connection conn) throws SQLException {
		// prompt for state, city, and categories seperated by commas
		System.out.print("Enter the state: ");
		String state = in.nextLine();
		System.out.print("Enter the city: ");
		String city = in.nextLine();
		System.out.print("Enter the categories (comma seperated): ");
		String categories = in.nextLine();

		// create a statement and execute the query
		Statement stmt = conn.createStatement();
		// for each category, create a subquery and intersect them
		String initalQuery = "SELECT business_id, name, street_address, num_tips from (";

		// dynamically builds the query based off of the number of categories
		String[] cats = categories.split(",");

		StringBuilder queryBuilder = new StringBuilder(initalQuery);

		for (int i = 0; i < cats.length; i++) {
			queryBuilder
					.append("(select business.business_id, business.name, business.street_address, business.num_tips")
					.append(" from Business")
					.append(" join businesscategories on business.business_id = businesscategories.business_id")
					.append(" where business.state = '").append(state).append("'")
					.append(" and business.city = '").append(city).append("'")
					.append(" and businesscategories.category_name = '").append(cats[i]).append("')");
			if (i < cats.length - 1) {
				queryBuilder.append(" INTERSECT ");
			} else {
				queryBuilder.append(") as b order by name;");
			}
		}

		final String query = queryBuilder.toString();
		// execute the query
		final ResultSet rs = stmt.executeQuery(query);
		// print the results
		int ctr = 1;
		String header = String.format("\n%-3s | %-22s | %-40s | %-35s | %-3s", "No.", "Business ID", "Name",
				"Address", "Tips");
		System.out.println(header);

		// create a line of dashes to separate the header from the data
		char[] chars = new char[header.length()];
		Arrays.fill(chars, '-');
		System.out.println(new String(chars));

		while (rs.next()) {
			String id = rs.getString("business_id");
			String name = rs.getString("name");
			String address = rs.getString("street_address");
			String tips = rs.getString("num_tips");

			String str = String.format("%-3d | %-22s | %-40s | %-35s | %-3s", ctr, id, name, address,
					tips);
			System.out.println(str);
			ctr++;
		}

		// close the result set and the statement
		rs.close();
		stmt.close();
		businessOperations(conn);

	}

	//TASK2:
	public static void businessOperations(Connection conn) {

		System.out.println("\nBusiness operations:");
		System.out.println("1 - Disiplay tips for given business");
		System.out.println("2 - Add tip for given business");
		System.out.println("3 - Return to main menu");
		System.out.print("Enter the number of the operation you would like to perform: ");
		String input = "";
		// ensure that input is equal to one of the options and break if so
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			input = in.nextLine().toLowerCase();
		}

		switch (input) {
			case "1":
				try {
					displayTips(conn);
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
				}
				break;
			case "2":
				try {
					addTip(conn);
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
				}
				break;
			case "3":
				chooseStartingOption(conn);
				break;
			default:
				System.out.println("Invalid input");

		}
	}

	//TASK 3:
	public static void displayTips(Connection conn) throws SQLException {
		// prompt for business id
		System.out.print("Enter the business id: ");
		String id = in.nextLine();
		String str = String.format(
				"SELECT name, tip_timestamp, likes, tip_text FROM tips join users on tips.user_id = users.user_id where business_id = '%s'",
				id);

		// create a statement and execute the query
		Statement stmt = conn.createStatement();
		final ResultSet rs = stmt.executeQuery(str);

		// print the results
		int ctr = 1;
		String header = String.format("\n%-3s | %-12s | %-19s | %-5s | %-50s", "No.", "Name", "Timestamp",
				"Likes", "Tip Text");
		System.out.println(header);
		// print a line of dashes to separate the header from the data
		char[] chars = new char[header.length()];
		Arrays.fill(chars, '-');
		System.out.println(new String(chars));
		// print the data itself
		while (rs.next()) {
			String name = rs.getString("name");
			String timestamp = rs.getString("tip_timestamp");
			String likes = rs.getString("likes");
			String tip = rs.getString("tip_text");

			String str2 = String.format("%-3d | %-12s | %-19s | %-5s | %-50s", ctr, name, timestamp, likes,
					tip);
			System.out.println(str2);
			ctr++;
		}

		// close the result set and the statement
		rs.close();
		stmt.close();
		// back to menu
		businessOperations(conn);

	}

	//TASK4:
	public static void addTip(Connection conn) throws SQLException {
		// prompt for user ID, business ID and tip text
		System.out.print("Enter the user id: ");
		String user_id = in.nextLine();
		System.out.print("Enter the business id: ");
		String business_id = in.nextLine();
		System.out.print("Enter the tip text: ");
		String tip_text = in.nextLine();
		
		Date curr_date = new Date();

		// create statement and execute the query
		Statement stmt = conn.createStatement();
		String str = String.format("INSERT INTO tips (user_id, business_id, tip_timestamp, likes,tip_text) VALUES ('%s', '%s', '%s', '%s', '%s')",
				user_id, business_id, curr_date, 0, tip_text);
		stmt.executeUpdate(str);
		System.out.println("Tip added successfully!");

		// close the statement
		stmt.close();
		// back to menu
		businessOperations(conn);

	}

	public static void searchFriendsTips(Connection conn) throws SQLException {

	}

	public static void chooseStartingOption(Connection conn) {
		// create a scanner so we can read the command-line input

		String input = "";

		// Loop until the user inputs 'q' to quit
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.print("\n1 - Search Businesses\n2 - Display friends tips\n3 - Exit\nEnter your choice: ");
			input = in.nextLine().toLowerCase();
			// ensure that input is equal to one of the options and break if so
		}

		switch (input) {
			case "1":
				try {
					searchByCategory(conn);
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
				}
				break;
			case "2":
				try {
					searchFriendsTips(conn);
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
				}
				break;
			case "3":
				try {
					conn.close();
					System.out.println("Connection closed.");
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Invalid input");

		}

	}

	public static void main(String[] args) {
		// Create PostgreSQL connection
		Connection connection = connect2postgres();
		if (connection == null) {
			System.out.println("Connection Failed! Check output console");
			return;
		}

		chooseStartingOption(connection);

		// Pass the "connection object to your functions as argument.
	}
}