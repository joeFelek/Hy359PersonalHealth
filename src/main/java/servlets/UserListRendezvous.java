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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * return all rendezvous of the current user in json format
 */
@WebServlet(name = "UserListRendezvous", value = "/UserListRendezvous")
public class UserListRendezvous extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditRandevouzTable ert = new EditRandevouzTable();
        EditDoctorTable edt = new EditDoctorTable();
        try {
            int user_id = eut.databaseGetSimpleUserId(request.getSession().getAttribute("loggedIn").toString());
            ArrayList<Randevouz> rds = ert.databaseGetRandevouz("user_id", user_id);
            JSONArray ja = new JSONArray();
            for (Randevouz rd : rds) {
                if(rd.getStatus().equals("cancelled"))
                    continue;
                JSONObject doc = new JSONObject(edt.databaseGetDoctor(rd.getDoctor_id()));
                JSONObject jo = new JSONObject();
                jo.put("doc_name", doc.getString("username"));
                jo.put("name", doc.getString("firstname") + " " + doc.getString("lastname"));
                jo.put("address", doc.getString("address") + ", " + doc.getString("city"));
                jo.put("email", doc.getString("email"));
                jo.put("date", rd.getDate_time());
                jo.put("price", rd.getPrice());
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

}
