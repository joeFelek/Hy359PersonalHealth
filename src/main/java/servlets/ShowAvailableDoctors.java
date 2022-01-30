package servlets;

import database.tables.EditDoctorTable;
import model.Doctor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * return all certified Doctors
 */
@WebServlet(name = "ShowAvailableDoctors", value = "/ShowAvailableDoctors")
public class ShowAvailableDoctors extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Doctor> doctors;
        EditDoctorTable edt = new EditDoctorTable();
        try {
            doctors = edt.databaseToDoctors();
            JSONArray doctorsArray = new JSONArray();
            for(Doctor doctor : doctors) {
                if(doctor.getCertified() == 1) {
                    JSONObject doctorJson = new JSONObject();
                    doctorJson.put("specialty", doctor.getSpecialty());
                    doctorJson.put("firstName", doctor.getFirstname());
                    doctorJson.put("lastName", doctor.getLastname());
                    doctorJson.put("country", doctor.getCountry());
                    doctorJson.put("city", doctor.getCity());
                    doctorJson.put("address", doctor.getAddress());
                    doctorJson.put("email", doctor.getEmail());
                    doctorJson.put("telephone", doctor.getTelephone());
                    doctorsArray.put(doctorJson);
                }
            }
            response.setContentType("application/json");
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.print(doctorsArray);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
