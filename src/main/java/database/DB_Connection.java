package database;

import com.google.gson.JsonObject;

import java.sql.*;

public class DB_Connection {
    private static final String url = "jdbc:mysql://j5zntocs2dn6c3fj.chr7pe7iynqr.eu-west-1.rds.amazonaws.com";
    private static final String databaseName = "f791bdn6bzragni8";
    private static final int port = 3306;
    private static final String username = "hokbd0p4epj4wuwa";
    private static final String password = "in1fxcd9hwjquouz";
    public static Connection getConnection() throws SQLException,
            ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url + ":" + port + "/" +
                databaseName + "?enabledTLSProtocols=TLSv1.2", username, password);
    }

    public static String getResultsToJSON(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        JsonObject object = new JsonObject();


        for (int i = 1; i <= columnCount; i++) {
            String name = metadata.getColumnName(i);
            String value = rs.getString(i);
            object.addProperty(name,value);
        }
        return object.toString();
    }
}