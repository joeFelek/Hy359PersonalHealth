package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditMessageTable;
import database.tables.EditRandevouzTable;
import database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CancelRendezvous", value = "/CancelRendezvous")
public class CancelRendezvous extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        String userType = request.getSession().getAttribute("userType").toString();
        JSONObject msg = new JSONObject();
        EditDoctorTable edt = new EditDoctorTable();
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditRandevouzTable ert = new EditRandevouzTable();
        EditMessageTable emt = new EditMessageTable();
        try {
            int doc_id = edt.databaseGetDoctorId(payload.getString("doc_name"));
            String doc_name = edt.databaseGetDoctorName(doc_id);

            if (userType.equals("doctor")) {
                int user_id = ert.databaseGetUser(doc_id, payload.getString("date"));
                if (user_id != 0) {
                    msg.put("doctor_id", 0);
                    msg.put("user_id", user_id);
                    msg.put("sender", "system");
                    msg.put("seen", "0");
                    msg.put("message", doc_name + " cancelled your rendezvous at " + payload.getString("date") + ".");
                    emt.addMessageFromJSON(msg.toString());
                }
                ert.databaseDeleteRendezvous(doc_id, payload.getString("date"));
            } else {
                ert.databaseDoUserCancel(doc_id, payload.getString("date"));
                int user_id = eut.databaseGetSimpleUserId(request.getSession().getAttribute("loggedIn").toString());
                String user_name = eut.databaseGetSimpleUserName(user_id);
                msg.put("doctor_id", doc_id);
                msg.put("user_id", 0);
                msg.put("sender", "system");
                msg.put("seen", "0");
                msg.put("message", user_name + " cancelled a rendezvous at " + payload.getString("date") + ".");
                emt.addMessageFromJSON(msg.toString());
            }

            response.setStatus(200);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
