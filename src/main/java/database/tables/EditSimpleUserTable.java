package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import model.SimpleUser;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditSimpleUserTable {
    public void addSimpleUserFromJSON(String json) throws ClassNotFoundException {
        SimpleUser user = jsonToSimpleUser(json);
        addNewSimpleUser(user);
    }

    public SimpleUser jsonToSimpleUser(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SimpleUser.class);
    }

    public void updateSimpleUser(JSONObject jo) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE users"
                + " SET "
                + "email='" + jo.getString("email") + "',"
                + "password='" + jo.getString("password") + "',"
                + "firstname='" + jo.getString("firstname") + "',"
                + "lastname='" + jo.getString("lastname") + "',"
                + "birthdate='" + jo.getString("birthdate") + "',"
                + "gender='" + jo.getString("gender") + "',"
                + "country='" + jo.getString("country") + "',"
                + "city='" + jo.getString("city") + "',"
                + "address='" + jo.getString("address") + "',"
                + "lat='" + jo.getString("lat") + "',"
                + "lon='" + jo.getString("lon") + "',"
                + "telephone='" + jo.getString("telephone") + "',"
                + "height='" + jo.getString("height") + "',"
                + "weight='" + jo.getString("weight") + "',"
                + "blooddonor='" + jo.getString("blooddonor") + "',"
                + "bloodtype='" + jo.getString("bloodtype") + "'"
                + "WHERE "
                + "username ='" + jo.getString("username") + "' AND amka='" + jo.getString("amka") + "'";
        stmt.executeUpdate(update);
        stmt.close();
        con.close();
    }

    public String databaseUserToJSON(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            String result = null;
            if(rs.next())
                result = DB_Connection.getResultsToJSON(rs);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databaseFindUserToJSON(String property, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(user_id) AS count FROM users WHERE " + property + " = '" + value + "'");
            String result = null;
            if(rs.next())
                result = DB_Connection.getResultsToJSON(rs);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databaseFindUserToJSON(String property, String value, String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(user_id) AS count FROM users WHERE " + property + " = '" + value + "' AND NOT username='" + username + "'");
            String result = null;
            if(rs.next())
                result = DB_Connection.getResultsToJSON(rs);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void addNewSimpleUser(SimpleUser user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO "
                    + " users (username,email,password,firstname,lastname,birthdate,gender,amka,country,city,address,"
                    + "lat,lon,telephone,height,weight,blooddonor,bloodtype)"
                    + " VALUES ("
                    + "'" + user.getUsername() + "',"
                    + "'" + user.getEmail() + "',"
                    + "'" + user.getPassword() + "',"
                    + "'" + user.getFirstname() + "',"
                    + "'" + user.getLastname() + "',"
                    + "'" + user.getBirthdate() + "',"
                    + "'" + user.getGender() + "',"
                    + "'" + user.getAmka() + "',"
                    + "'" + user.getCountry() + "',"
                    + "'" + user.getCity() + "',"
                    + "'" + user.getAddress() + "',"
                    + "'" + user.getLat() + "',"
                    + "'" + user.getLon() + "',"
                    + "'" + user.getTelephone() + "',"
                    + "'" + user.getHeight() + "',"
                    + "'" + user.getWeight() + "',"
                    + "'" + user.isBloodDonor() + "',"
                    + "'" + user.getBloodtype() + "'"
                    + ")";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The user was successfully added in the database.");
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditSimpleUserTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean databaseSimpleUserDoesExist(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("select exists(select * from users where username = '" + username + "' and password ='" + password + "')");
            boolean result = false;
            if(rs.next())
                result = rs.getInt(1) == 1;
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void databaseDeleteSimpleUser(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        try {
            stmt.executeUpdate("DELETE FROM users where user_id =" + id);
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public ArrayList<SimpleUser> databaseToUsers() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<SimpleUser> users = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                SimpleUser user = gson.fromJson(json, SimpleUser.class);
                users.add(user);
            }
            stmt.close();
            con.close();
            return users;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databaseGetSimpleUserName(int user_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT firstname,lastname FROM users WHERE user_id = " + user_id);
            String result = null;
            if(rs.next())
                result = rs.getString(1) + " " + rs.getString(2);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int databaseGetSimpleUserId(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT user_id FROM users WHERE username = '" + username + "'");
            int result = -1;
            if(rs.next())
                result = rs.getInt(1);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return -1;
    }
}
