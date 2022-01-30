package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import tools.DatabaseDuplicates;
import tools.JSON_Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Update doctor's or user's info
 */
@WebServlet(name = "Update", value = "/Update")
public class Update extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        JSON_Converter jc = new JSON_Converter();
        String payload = jc.getJSONFromAjax(request.getReader());
        JSONObject jo = new JSONObject(payload);

        DatabaseDuplicates dd = new DatabaseDuplicates();
        JSONObject check = new JSONObject();
        check.put("user", jo.getString("username"));
        check.put("email", jo.getString("email"));
        if (dd.checkDuplicateSimpleUserTable(check) || dd.checkDuplicateDoctorTable(check)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Duplicate values: user email already exist");
            return;
        }
        String userType = request.getSession().getAttribute("userType").toString();
        if(userType.equals("doctor")) {
            if(jo.getString("doctor_info").isEmpty())
                jo.put("doctor_info", "N/A");
        }
        if (jo.getString("height").isEmpty())
            jo.put("height", "0");
        if (jo.getString("weight").isEmpty()) {
            jo.put("weight", "0");
        }

        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        try {
            if(userType.equals("doctor"))
                edt.updateDoctor(jo);
            else
                eut.updateSimpleUser(jo);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
