package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import model.Randevouz;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "NewRendezvous", value = "/NewRendezvous")
public class

NewRendezvous extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        try {
            EditDoctorTable edt = new EditDoctorTable();
            EditRandevouzTable ert = new EditRandevouzTable();
            HttpSession session=request.getSession(true);
            JSONObject rendezvous = new JSONObject();
            int doctor_id = edt.databaseGetDoctorId(session.getAttribute("loggedIn").toString());
            String date = payload.getString("date") + " " + payload.getString("time")+":00";
            String doctor_info = edt.databaseGetDoctorInfo(doctor_id);
            rendezvous.put("doctor_id", doctor_id+"");
            rendezvous.put("date_time", date);
            rendezvous.put("price", payload.getString("price"));
            rendezvous.put("user_id", "0");
            rendezvous.put("user_info", "null");
            rendezvous.put("doctor_info", doctor_info);
            rendezvous.put("status", "free");
            ert.addRandevouzFromJSON(rendezvous.toString());
            response.setStatus(200);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
