package database.tables;

import com.google.gson.Gson;
import database.DB_Connection;
import model.Doctor;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditDoctorTable {
    public void addDoctorFromJSON(String json) throws ClassNotFoundException {
        Doctor doc = jsonToDoctor(json);
        addNewDoctor(doc);
    }

    public Doctor jsonToDoctor(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Doctor.class);
    }

    public String databaseToDoctor(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM doctors WHERE username = '" + username + "'");
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

    public ArrayList<Doctor> databaseToDoctors() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Doctor> doctors = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Doctor doc = gson.fromJson(json, Doctor.class);
                doctors.add(doc);
            }
            stmt.close();
            con.close();
            return doctors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Doctor> databaseToCertifiedDoctors() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Doctor> doctors = new ArrayList<>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM doctors WHERE certified='1'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Doctor doc = gson.fromJson(json, Doctor.class);
                doctors.add(doc);
            }
            stmt.close();
            con.close();
            return doctors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


    public String databaseFindDoctorToJSON(String property, String value) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(doctor_id) AS count FROM doctors WHERE " + property + " = '" + value + "'");
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

    public String databaseFindDoctorToJSON(String property, String value, String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(doctor_id) AS count FROM doctors WHERE " + property + " = '" + value + "' AND NOT username='" + username + "'");
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

    public void addNewDoctor(Doctor doc) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " doctors (username,email,password,firstname,lastname,birthdate,gender,amka,country,city,address,"
                    + "lat,lon,telephone,height,weight,blooddonor,bloodtype,specialty,doctor_info,certified)"
                    + " VALUES ("
                    + "'" + doc.getUsername() + "',"
                    + "'" + doc.getEmail() + "',"
                    + "'" + doc.getPassword() + "',"
                    + "'" + doc.getFirstname() + "',"
                    + "'" + doc.getLastname() + "',"
                    + "'" + doc.getBirthdate() + "',"
                    + "'" + doc.getGender() + "',"
                    + "'" + doc.getAmka() + "',"
                    + "'" + doc.getCountry() + "',"
                    + "'" + doc.getCity() + "',"
                    + "'" + doc.getAddress() + "',"
                    + "'" + doc.getLat() + "',"
                    + "'" + doc.getLon() + "',"
                    + "'" + doc.getTelephone() + "',"
                    + "'" + doc.getHeight() + "',"
                    + "'" + doc.getWeight() + "',"
                    + "'" + doc.isBloodDonor() + "',"
                    + "'" + doc.getBloodtype() + "',"
                    + "'" + doc.getSpecialty() + "',"
                    + "'" + doc.getDoctor_info() + "',"
                    + "'" + 0 + "'"
                    + ")";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The doctor was successfully added in the database.");
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditDoctorTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean databaseDoctorDoesExist(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("select exists(select * from doctors where username = '" + username + "' and password ='" + password + "')");
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

    public void databaseDeleteDoctor(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        try {
            stmt.executeUpdate("DELETE FROM doctors where doctor_id = " + id);
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public void databaseCertifyDoctor(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        try {
            stmt.executeUpdate("UPDATE doctors SET certified = '1' WHERE doctor_id = " + id);
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public String databaseDoctorIsCertified(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(1) FROM doctors where username='" + username + "' and certified='" + 1 + "'");
            String result = null;
            if(rs.next())
                result = rs.getString(1);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return "0";
    }

    public int databaseGetDoctorId(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT doctor_id FROM doctors WHERE username = '" + username + "'");
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

    public int databaseGetDoctorId(String email, String telephone) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT doctor_id FROM doctors WHERE email = '" + email + "' AND telephone='" + telephone + "'");
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

    public String databaseGetDoctorName(int doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT firstname,lastname FROM doctors WHERE doctor_id = " + doctor_id);
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

    public String databaseGetDoctorInfo(int doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT doctor_info FROM doctors WHERE doctor_id = " + doctor_id);
            String result = null;
            if(rs.next())
                result = rs.getString(1);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databaseGetDoctor(int doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM doctors WHERE doctor_id = " + doctor_id);
            String result = null;
            if(rs.next())
                result = DB_Connection.getResultsToJSON(rs);
            stmt.close();
            con.close();
            return result;
        } catch (Exception e) {
            System.err.println("Got an exception! GetDoctor");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void updateDoctor(JSONObject jo) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE doctors"
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
                + "bloodtype='" + jo.getString("bloodtype") + "',"
                + "specialty='" + jo.getString("specialty") + "',"
                + "doctor_info='" + jo.getString("doctor_info") + "'"
                + "WHERE "
                + "username ='" + jo.getString("username") + "' AND amka='" + jo.getString("amka") + "'";
        stmt.executeUpdate(update);
        stmt.close();
        con.close();
    }

}
