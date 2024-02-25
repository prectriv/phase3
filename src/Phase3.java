import java.sql.*;
import java.util.Scanner;
import java.util.Arrays;

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
		// for each category, create a subquery and intersect them
		String initalQuery = "SELECT business_id, name, street_address, num_tips from (";

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
				queryBuilder.append(") as b")
						.append(" order by name;");
			}
		}
		String query = queryBuilder.toString();
		System.out.println(query);

		ResultSet rs = stmt.executeQuery(query);
		// print the results
		while (rs.next()) {
			String id = rs.getString("business_id");
			String name = rs.getString("name");
			String address = rs.getString("street_address");
			String tips = rs.getString("num_tips");
			System.out.println("ID: " + id + " Name: " + name + " Address: " + address + " Tips: " + tips);

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
