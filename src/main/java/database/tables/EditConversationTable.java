package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import model.Conversation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EditConversationTable {

    public void addConversationFromJSON(String json) throws ClassNotFoundException {
        Conversation msg = jsonToConversation(json);
        createNewConversation(msg);
    }

    public Conversation jsonToConversation(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Conversation.class);
    }

    public ArrayList<Conversation> databaseGetConversations(String var, int id) throws ClassNotFoundException, SQLException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Conversation> rds = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM conversation WHERE " + var + " =" + id);
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Conversation msg = gson.fromJson(json, Conversation.class);
                rds.add(msg);
            }
            stmt.close();
            con.close();
            return rds;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void createNewConversation(Conversation conv) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO "
                    + " conversation (doctor_id,user_id) "
                    + " VALUES ("
                    + "'" + conv.getDoctor_id() + "',"
                    + "'" + conv.getUser_id() + "'"
                    + ")";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("Got an exception! ");
            System.err.println(ex.getMessage());
        }
    }

    public boolean databaseCheckIfConversationExist(int user_id, int doctor_id) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select exists(select * from conversation where user_id =" + user_id + " and doctor_id =" + doctor_id + ")");
            if(rs.next()) {
                boolean var = rs.getInt(1) == 1;
                stmt.close();
                con.close();
                return var;
            }
        } catch (SQLException ex) {
            System.err.println("Got an exception! ");
            System.err.println(ex.getMessage());
        }
        return false;
    }
}
