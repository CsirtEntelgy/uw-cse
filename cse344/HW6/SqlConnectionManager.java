import java.sql.*;

public class SqlConnectionManager {

    public Connection conn;
    private String server;
    private String dbName;
    private String userId;
    private String password;

    public SqlConnectionManager(String server, String dbName, String userId, String password) throws SQLException {
        this.server = server;
        this.dbName = dbName;
        this.userId = userId;
        this.password = password;
    }

    public void openConnection() throws Exception {

        String jSQLDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String jSQLUrl = "jdbc:sqlserver://" + server + ".database.windows.net:1433;database=" + dbName;
        String jSQLUser = userId;
        String jSQLPassword = password;

        /* load jdbc drivers */
        Class.forName(jSQLDriver).newInstance();

        /* open connections to the flights database */
        conn = DriverManager.getConnection(jSQLUrl, // database
                jSQLUser, // user
                jSQLPassword); // password
        conn.setAutoCommit(true);
    }

    public ResultSet executeQuery(String query) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            conn.commit();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String query) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}