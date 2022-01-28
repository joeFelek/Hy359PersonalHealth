package servlets;

import database.tables.EditConversationTable;
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

@WebServlet(name = "StartConversation", value = "/StartConversation")
public class StartConversation extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        EditConversationTable ect = new EditConversationTable();
        EditMessageTable emt = new EditMessageTable();
        try {
            String username = request.getSession().getAttribute("loggedIn").toString();
            int user_id = eut.databaseGetSimpleUserId(username);
            int doctor_id = edt.databaseGetDoctorId(payload.getString("doc_name"));
            if(ect.databaseCheckIfConversationExist(user_id,doctor_id)) {
                return;
            }
            JSONObject conv = new JSONObject();
            conv.put("doctor_id", doctor_id);
            conv.put("user_id", user_id);
            ect.addConversationFromJSON(conv.toString());
            JSONObject hello = new JSONObject();
            hello.put("doctor_id", doctor_id);
            hello.put("user_id", user_id);
            JSONObject doc = new JSONObject(edt.databaseGetDoctor(doctor_id));
            hello.put("message", "Hello " + doc.getString("lastname"));
            hello.put("sender", "user");
            hello.put("seen", "0");
            emt.addMessageFromJSON(hello.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
