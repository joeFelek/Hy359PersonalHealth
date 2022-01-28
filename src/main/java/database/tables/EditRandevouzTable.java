package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import model.Randevouz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditRandevouzTable {
    public void addRandevouzFromJSON(String json) throws ClassNotFoundException {
        Randevouz r = jsonToRandevouz(json);
        createNewRandevouz(r);
    }

    public Randevouz jsonToRandevouz(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Randevouz.class);
    }

    public void databaseDeleteRendezvous(int doctor_id, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM randevouz WHERE doctor_id=" + doctor_id + " AND date_time='" + date + "'";
        stmt.executeUpdate(deleteQuery);
        System.out.println(deleteQuery);
        stmt.close();
        con.close();
    }

    /**
     * Establish a database connection and add in the database.
     */
    public void createNewRandevouz(Randevouz rand) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO "
                    + " randevouz (doctor_id,user_id,date_time,price,doctor_info,user_info,status)"
                    + " VALUES ("
                    + "'" + rand.getDoctor_id() + "',"
                    + "'" + rand.getUser_id() + "',"
                    + "'" + rand.getDate_time() + "',"
                    + "'" + rand.getPrice() + "',"
                    + "'" + rand.getDoctor_info() + "',"
                    + "'" + rand.getUser_info() + "',"
                    + "'" + rand.getStatus() + "'"
                    + ")";
            stmt.executeUpdate(insertQuery);
            System.out.println("# The randevouz was successfully added in the database.");
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditRandevouzTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Randevouz> databaseGetRandevouz(String property, int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> rds = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz where " + property + "=" + id);
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rd = gson.fromJson(json, Randevouz.class);
                rds.add(rd);
            }
            stmt.close();
            con.close();
            return rds;
        } catch (Exception e) {
            System.err.println("Got an exception! Get");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Randevouz> databaseGetFreeRandevouz(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> rds = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz where doctor_id =" + id + " AND date_time>=NOW() AND status='free'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rd = gson.fromJson(json, Randevouz.class);
                rds.add(rd);
            }
            stmt.close();
            con.close();
            return rds;
        } catch (Exception e) {
            System.err.println("Got an exception! in getFreeRendezvous");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int databaseGetUser(int doc_id, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "SELECT user_id FROM randevouz WHERE doctor_id = " + doc_id + " AND date_time='" + date + "'";
        ResultSet rs = stmt.executeQuery(updateQuery);
        int res = 0;
        if (rs.next())
            res = rs.getInt(1);
        stmt.close();
        con.close();
        return res;
    }

    public void databaseDoUserCancel(int doc_id, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET user_id=0,user_info='null',status='free' WHERE doctor_id = " + doc_id + " AND date_time='" + date + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void databaseDoDone(int doc_id, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET status='done' WHERE doctor_id = " + doc_id + " AND date_time='" + date + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void databaseBook(int user_id, int doc_id, String date, String user_info) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET user_id=" + user_id + ",user_info='" + user_info + "', status='selected' WHERE doctor_id = " + doc_id + " AND date_time='" + date + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

}
