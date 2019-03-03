import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Lesson7Database {

	//Hardcoded connection variables, could be changed to inputs
	private static final String url = "jdbc:derby://localhost:1527/COREJAVA;create=true";
	private static final String username = "dbuser";
	private static final String password = "secret";
	private static final String sqlPath = "lib/Lessons.sql";
	
	public static void main(String[] args) throws SQLException, IOException {
		
		
		//Populate database
		
		Scanner in = new Scanner(Paths.get(sqlPath));
		Connection conn = DriverManager.getConnection(url, username, password);
		Statement stat = conn.createStatement();
		
		//Drop table if it exists
		try { stat.execute("DROP TABLE LESSONS"); } catch(SQLException e) { /*Do Nothing*/ }
		
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.endsWith(";")) {
				line = line.substring(0,line.length()-1);
			}
			stat.execute(line);
		}
		in.close();
		
		
		//Read from database
		
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet columns = meta.getColumns(null, null, "LESSONS", null);
		
		//Display column names
		while(columns.next()) {
			System.out.print(columns.getString("COLUMN_NAME") + " ");
		}
		System.out.println();
		
		//Display Data
		ResultSet rows = stat.executeQuery("SELECT * FROM Lessons");
		while(rows.next()) {
			System.out.println(String.format("%-10s %s", rows.getString(1), rows.getString(2)));
		}
		
		//Close connections
		stat.close();
		conn.close();
	}

}
