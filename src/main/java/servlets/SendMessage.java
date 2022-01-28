package servlets;

import database.tables.EditMessageTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SendMessage", value = "/SendMessage")
public class SendMessage extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        EditMessageTable emt = new EditMessageTable();
        String userType = request.getSession().getAttribute("userType").toString();
        int receiver_id = payload.getInt("receiver_id");
        int sender_id = payload.getInt("sender_id");
        String message = payload.getString("message");
        if(message.isEmpty()) {
            response.setStatus(100);
            return;
        }
        JSONObject msg = new JSONObject();
        if(userType.equals("user")) {
            msg.put("user_id", sender_id);
            msg.put("doctor_id", receiver_id);
            msg.put("sender", "user");
        }else {
            msg.put("user_id", receiver_id);
            msg.put("doctor_id", sender_id);
            msg.put("sender", "doctor");
        }
        msg.put("message", message);
        msg.put("seen", 0);
        try {
            emt.addMessageFromJSON(msg.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        response.setStatus(200);
    }
}
