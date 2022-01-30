package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import database.tables.EditSimpleUserTable;
import model.Randevouz;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Returns all rendezvous of the current doctor and
 * removes rendezvous that have status equal to free and are expired (datetime < now)
 */

@WebServlet(name = "DoctorRendezvous", value = "/DoctorRendezvous")
public class DoctorRendezvous extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = session.getAttribute("loggedIn").toString();
        EditDoctorTable edt = new EditDoctorTable();
        EditSimpleUserTable eut = new EditSimpleUserTable();
        try {
            int doc_id = edt.databaseGetDoctorId(username);
            EditRandevouzTable ert = new EditRandevouzTable();
            ArrayList<Randevouz> rds = ert.databaseGetRandevouz("doctor_id", doc_id);
            JSONArray ja = new JSONArray();
            for (Randevouz rd : rds) {
                if (removeOldFreeRendezvous(rd) || rd.getStatus().equals("cancelled"))
                    continue;
                JSONObject jo = new JSONObject();
                jo.put("doc_name", username);
                String clientName = eut.databaseGetSimpleUserName(rd.getUser_id());
                if (clientName == null) clientName = " ";
                jo.put("user_name", clientName);
                jo.put("date", rd.getDate_time());
                jo.put("price", rd.getPrice());
                jo.put("user_info", rd.getUser_info());
                jo.put("status", rd.getStatus());
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

    private boolean removeOldFreeRendezvous(Randevouz rd) throws SQLException, ClassNotFoundException {
        LocalDateTime input = LocalDateTime.parse(rd.getDate_time(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if(rd.getStatus().equals("free") && rd.getUser_id() == 0 && input.isBefore(LocalDateTime.now())) {
            EditRandevouzTable ert = new EditRandevouzTable();
            ert.databaseDeleteRendezvous(rd.getDoctor_id(), rd.getDate_time());
            return true;
        }
        return false;
    }

}
