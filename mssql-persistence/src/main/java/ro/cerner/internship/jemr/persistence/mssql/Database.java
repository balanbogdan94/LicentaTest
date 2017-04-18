package ro.cerner.internship.jemr.persistence.mssql;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	private static Database instance = new Database();
	private Connection con;

	private Database() {

	}

	public static Database getInstance() {
		return instance;
	}

	public Connection getConnect() {

		if (con == null) {
			final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			// RALUCA
//			final String DB_URL = "jdbc:sqlserver://CERNER-NLIVUU6B;databaseName=InternshipProjectDB;loginTimeout=1";
//			final String user = "sa";
//			final String password = "Root123456";
			// BOBO
			final String DB_URL = "jdbc:sqlserver://PCBOBO;databaseName=InternshipProjectDB;loginTimeout=1";
			final String user = "sa";
			final String password = "1234";
			// BIRS
/*			final String DB_URL = "jdbc:sqlserver://CERNER-LV73NN8J;databaseName=InternshipProjectDB;loginTimeout=1";
			final String user = "sa";
			final String password = "123456";*/
			try {
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL, user, password);
				System.out.println("Connected");

			}

			catch (Exception e) {

				System.out.println("Database connect error...");
			}
		}
		return con;

	}

	public void Disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		con = null;
	}
}
