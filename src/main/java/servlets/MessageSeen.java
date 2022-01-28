package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditMessageTable;
import database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "MessageSeen", value = "/MessageSeen")
public class MessageSeen extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        EditMessageTable emt = new EditMessageTable();
        String userType = request.getSession().getAttribute("userType").toString();
        String username = request.getSession().getAttribute("loggedIn").toString();
        String currentUserId;
        try {
            if (userType.equals("user")) {
                currentUserId = "" + eut.databaseGetSimpleUserId(username);
                emt.databaseSeen(currentUserId, payload.getString("id"));
            }else {
                currentUserId = "" + edt.databaseGetDoctorId(username);
                emt.databaseSeen(payload.getString("id"), currentUserId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
