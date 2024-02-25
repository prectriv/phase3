import java.sql.*;
import java.util.Scanner;

public class Phase3 {
	// Replace the "USERID" and "PASSWORD" with your PostgreSQL username and
	// password (the postgreSQL user you created in Phase2).

	private static final String USERID = "xavier";
	private static final String PASSWORD = "0509";

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
		Statement x = conn.createStatement();
		x.execute("SELECT * FROM business WHERE categories LIKE '%restaurant%'");
		ResultSet rs = x.executeQuery("SELECT * FROM business WHERE categories LIKE '%restaurant%'");

	}

	public static void search_friends_tips(Connection conn) throws SQLException {

	}

	public static void display_tips(Connection conn) throws SQLException {

	}

	public static void add_tip(Connection conn) throws SQLException {

	}

	public static void choose_option(Connection conn) {
		// create a scanner so we can read the command-line input
		Scanner scanner = new Scanner(System.in);

		String input = "";

		// Loop until the user inputs 'q' to quit
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.println("\n1 - Search busnesses\n2 - Display friends tips\n3 - Exit");
			input = scanner.nextLine().toLowerCase();
			// ensure that input is equal to one of the options and break if so
		}

		scanner.close();

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
