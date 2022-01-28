package servlets;

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

@WebServlet(name = "BookRendezvous", value = "/BookRendezvous")
public class BookRendezvous extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditRandevouzTable ert = new EditRandevouzTable();
        EditMessageTable emt = new EditMessageTable();
        try {
            String username = request.getSession().getAttribute("loggedIn").toString();
            int user_id = eut.databaseGetSimpleUserId(username);
            String name = eut.databaseGetSimpleUserName(user_id);
            ert.databaseBook(user_id, payload.getInt("id"), payload.getString("date"), payload.getString("user_info"));
            JSONObject msg = new JSONObject();
            msg.put("doctor_id", payload.getInt("id"));
            msg.put("user_id", 0);
            msg.put("sender", "system");
            msg.put("seen", "0");
            msg.put("message", name+" selected a rendezvous at "+payload.getString("date")+".");
            emt.addMessageFromJSON(msg.toString());
            response.setStatus(200);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
