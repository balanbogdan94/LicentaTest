package ro.cerner.internship.jemr.persistence.mssql;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	private static Database instance = new Database();
	private Connection con;

	private Database() {}

	public static Database getInstance() 
	{
		return instance;
	}

	public Connection getConnection() 
	{
		if (con == null) 
		{
			final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			final String DB_URL = "jdbc:sqlserver://PCBOBO;databaseName=InternshipProjectDB;loginTimeout=1";
			final String user = "sa";
			final String password = "1234";
			try {
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL, user, password);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	public void Disconnect()
	{
		if (con != null) 
		{
			try 
			{
				con.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		con = null;
	}
}
