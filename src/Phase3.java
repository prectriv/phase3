import java.sql.*;
import java.util.Scanner;

public class Phase3 {
	// Replace the "USERID" and "PASSWORD" with your PostgreSQL username and
	// password (the postgreSQL user you created in Phase2).

	private static final String USERID = "xavier";
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

	public static void search_by_category(Connection conn) throws SQLException {
		// prompt for state, city, and categories seperated by commas
		System.out.println("Enter the state: ");
		Scanner in = new Scanner(System.in);
		String state = in.nextLine();
		System.out.println("Enter the city: ");
		String city = in.nextLine();
		System.out.println("Enter the categories (comma seerated): ");
		String categories = in.nextLine();

		// create a statement and execute the query
		Statement stmt = conn.createStatement();
		String query = "SELECT b.name, b.address, b.city, b.state, b.stars, b.review_count, b.is_open, bc.categories FROM business b, business_categories bc WHERE b.business_id = bc.business_id AND b.state = '"
				+ state + "' AND b.city = '" + city + "' AND bc.categories IN (" + categories + ")";
		ResultSet rs = stmt.executeQuery(query);
		// print the results
		while (rs.next()) {
			System.out.println("Name: " + rs.getString("name"));
			System.out.println("Address: " + rs.getString("address"));
			System.out.println("City: " + rs.getString("city"));
			System.out.println("State: " + rs.getString("state"));
			System.out.println("Stars: " + rs.getString("stars"));
			System.out.println("Review Count: " + rs.getString("review_count"));
			System.out.println("Is Open: " + rs.getString("is_open"));
			System.out.println("Categories: " + rs.getString("categories"));
			System.out.println();
		}

	}

	public static void search_friends_tips(Connection conn) throws SQLException {

	}

	public static void display_tips(Connection conn) throws SQLException {

	}

	public static void add_tip(Connection conn) throws SQLException {

	}

	public static void choose_option(Connection conn) {
		// create a scanner so we can read the command-line input

		String input = "";

		// Loop until the user inputs 'q' to quit
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.println("\n1 - Search busnesses\n2 - Display friends tips\n3 - Exit");
			input = in.nextLine().toLowerCase();
			// ensure that input is equal to one of the options and break if so
		}

		switch (input) {
			case "1":
				try {
					search_by_category(conn);
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
					return;
				}
				break;
			case "2":
				try {
					search_friends_tips(conn);
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
					return;
				}
				break;
			case "3":
				try {
					conn.close();
					System.out.println("Connection closed.");
				} catch (SQLException e) {
					System.out.println("Get Data Failed! Check output console");
					e.printStackTrace();
					return;
				}
				break;

		}

	}

	public static void main(String[] args) {
		// Create PostgreSQL connection
		Connection connection = connect2postgres();
		if (connection == null) {
			System.out.println("Connection Failed! Check output console");
			return;
		}

		choose_option(connection);

		// Pass the "connection object to your functions as argument.
	}

}
