package servlets;

import database.tables.EditDoctorTable;
import model.Doctor;
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

@WebServlet(name = "DoctorsList", value = "/DoctorsList")
public class DoctorsList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EditDoctorTable edt = new EditDoctorTable();
        try {
            ArrayList<Doctor> doctors = edt.databaseToCertifiedDoctors();
            ArrayList<JSONObject> res = new ArrayList<>();
            for (Doctor d:doctors) {
                JSONObject jo = new JSONObject();
                jo.put("firstname", d.getFirstname());
                jo.put("lastname", d.getLastname());
                jo.put("address", d.getAddress());
                jo.put("city", d.getCity());
                jo.put("info", d.getDoctor_info());
                jo.put("specialty", d.getSpecialty());
                jo.put("telephone", d.getTelephone());
                res.add(jo);
            }
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(res);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
