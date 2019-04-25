import java.sql.*;

public class DatabaseHelper {

    // Database informatie

    private static final String DB_URL = "jdbc:mysql://localhost/wideworldimporters?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    // Initialiseer statement en connection objecten

    private Statement stmt = null;
    private Connection connection = null;

    public DatabaseHelper() // Registreer de driver in je java applicatie
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void openConnection() // Open de connectie met de database
    {
        try{
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            stmt = connection.createStatement();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void closeConnection() // Sluit connectie met de database
    {
        try{
            stmt.close();
            connection.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet selectQuery(String sql) // Functie om select query's uit te voeren
    {
        ResultSet rows = null;

        try{
            rows = stmt.executeQuery(sql);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return rows;
    }

    public int executeUpdateQuery(String sql) // Functie om de rest van de query's uit te voeren
    {
        int rowsAffected = 0;

        try{
            rowsAffected = stmt.executeUpdate(sql);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return rowsAffected;
    }


}
