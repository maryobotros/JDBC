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

public class InsertTeams {
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
	
	public static void insertTeams(Connection conn, String filename) {
		PreparedStatement statement = null;
		String insertSql = " INSERT IGNORE  into teams values (?, ?, ?, ?)";
		
		// Reading the csv file 
		List<List<String>> teamsRecords = new ArrayList<>();
		try (Scanner scanner = new Scanner(new File("teams.csv"));) {
			while (scanner.hasNextLine()) {
				teamsRecords.add(getRecordFromLine(scanner.nextLine()));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			for(int i = 0; i < teamsRecords.size(); i++) {
				
					statement = conn.prepareStatement(insertSql);
	                // setXXX() methods to set the values of these ?
//	                statement.setInt(1, Integer.parseInt(playersRecords.get(i).get(0)));
					statement.setString(1, teamsRecords.get(i).get(0));
					statement.setString(2, teamsRecords.get(i).get(1));
	                statement.setString(3, teamsRecords.get(i).get(2));
	                if(teamsRecords.get(i).get(3) != null) {
	                	statement.setString(4, teamsRecords.get(i).get(3));
	                }
	                
	                System.out.println(statement);
	                statement.executeUpdate();
	                System.out.println();
				
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
			
			insertTeams(conn, "teams.csv");
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
