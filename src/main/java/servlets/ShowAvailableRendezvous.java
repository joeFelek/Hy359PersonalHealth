package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import model.Randevouz;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ShowAvailableRendezvous", value = "/ShowAvailableRendezvous")
public class ShowAvailableRendezvous extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        EditDoctorTable edt = new EditDoctorTable();
        EditRandevouzTable ert = new EditRandevouzTable();
        try {
            int doctor_id = edt.databaseGetDoctorId(payload.getString("email"), payload.getString("telephone"));
            ArrayList<Randevouz> rds = ert.databaseGetFreeRandevouz(doctor_id);
            JSONArray ja = new JSONArray();
            for (Randevouz rd : rds) {
                JSONObject jo = new JSONObject();
                jo.put("doctor_id", rd.getDoctor_id());
                jo.put("date", rd.getDate_time());
                jo.put("price", rd.getPrice());
                ja.put(jo);
            }
            JSONObject res = new JSONObject();
            res.put("data", ja);
            response.setStatus(200);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(res);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
