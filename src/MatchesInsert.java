import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.String;

public class MatchesInsert {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ResultTracker";
	
	static final String USER = "root";
	static final String PASSWORD = "mypassword";
	
	private static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		
		// This loop removes the quotation marks
		for(int i = 0; i < values.size(); i ++) {
			values.set(i, values.get(i).replaceAll("\"", ""));
		}
		
		return values;
	}
	
	public static void insertMatches(Connection conn, String filename) {
		PreparedStatement statement = null;
		String insertSql = " INSERT IGNORE  into matches values (?, ?, ?, ?, ?, ?, ?, ?)";
		
		// Reading the csv file 
		List<List<String>> matchesRecords = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File("matches.csv"));) {
			while (scanner.hasNextLine()) {
				matchesRecords.add(getRecordFromLine(scanner.nextLine()));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			for(int i = 0; i < matchesRecords.size(); i++) {
				statement = conn.prepareStatement(insertSql);
                // setXXX() methods to set the values of these ?
//                statement.setInt(1, Integer.parseInt(matchesRecords.get(i).get(0)));
				statement.setString(1, matchesRecords.get(i).get(0));
				statement.setString(2, matchesRecords.get(i).get(1));
                statement.setString(3, matchesRecords.get(i).get(2));
                statement.setString(4, matchesRecords.get(i).get(3));
                statement.setString(5, matchesRecords.get(i).get(4));
                statement.setString(6, matchesRecords.get(i).get(5));
                statement.setString(7, matchesRecords.get(i).get(6));
                statement.setString(8, matchesRecords.get(i).get(7));
                
                System.out.println(statement); 
                statement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		
		// open connection
		// execute a query -> constructed with String concatenation
		try {
			System.out.println("Connecting to database ....");
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			
			insertMatches(conn, "matches.csv");
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
