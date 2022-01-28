package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import model.Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EditMessageTable {
    public void addMessageFromJSON(String json) throws ClassNotFoundException {
        Message msg = jsonToMessage(json);
        createNewMessage(msg);
    }

    public Message jsonToMessage(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }


    public ArrayList<Message> databaseToMessage(int user_id, int doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Message> rds = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM message WHERE user_id=" + user_id + " AND doctor_id=" + doctor_id);
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Message msg = gson.fromJson(json, Message.class);
                rds.add(msg);
            }
            con.close();
            stmt.close();
            return rds;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Message> databaseGetSystemMessages(String currentUserType, int currentUserId) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Message> rds = new ArrayList<>();
        String var = currentUserType + "_id";
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM message WHERE " + var + "=" + currentUserId + " AND sender = 'system'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Message msg = gson.fromJson(json, Message.class);
                rds.add(msg);
            }
            con.close();
            stmt.close();
            return rds;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void createNewMessage(Message msg) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO "
                    + " message (doctor_id,user_id,message,sender,seen) "
                    + " VALUES ("
                    + "'" + msg.getDoctor_id() + "',"
                    + "'" + msg.getUser_id() + "',"
                    + "'" + msg.getMessage() + "',"
                    + "'" + msg.getSender() + "',"
                    + "'" + msg.getSeen() + "'"
                    + ")";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void databaseSeen(String user_id, String doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("UPDATE message SET seen=1 WHERE user_id=" + user_id + " AND doctor_id=" + doctor_id);
        stmt.close();
        con.close();
    }

    public int databaseGetNotifications(int id, String userType) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String var = userType + "_id";
        ResultSet rs;
        rs = stmt.executeQuery("SELECT COUNT(message_id) FROM message WHERE " + var + "=" + id + " AND seen=0 AND NOT sender='" + userType + "'");
        int val = -1;
        if (rs.next())
            val = rs.getInt(1);
        stmt.close();
        con.close();
        return val;
    }


}
