package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Check user credentials (username, password)
 * - If credentials are incorrect return with status code 401
 * - If credentials are correct but doctor is not certified return with status code 403
 * - If credentials are correct set session attributes username and userType
 * and return json (username, userType, success message)
 */

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        JSONObject jo = new JSONObject();
        jo.put("username", payload.getString("username"));
        try {
            if (edt.databaseDoctorDoesExist(payload.getString("username"), payload.getString("password"))) {
                jo.put("userType", "doctor");
                if (edt.databaseDoctorIsCertified(payload.getString("username")).equals("0")) {
                    response.sendError(403, "Doctor is not certified");
                    return;
                }
            } else if (eut.databaseSimpleUserDoesExist(payload.getString("username"), payload.getString("password"))) {
                if (payload.getString("username").equals("admin"))
                    jo.put("userType", "admin");
                else
                    jo.put("userType", "user");
            } else {
                response.sendError(401, "Invalid username or password");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("loggedIn", jo.getString("username"));
            session.setAttribute("userType", jo.getString("userType"));
            response.setStatus(200);
            response.setContentType("application/json");
            jo.put("message", "Successful login");
            PrintWriter out = response.getWriter();
            out.print(jo);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
