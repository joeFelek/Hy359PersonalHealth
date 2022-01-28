package servlets;

import org.json.JSONObject;
import tools.DatabaseDuplicates;
import tools.JSON_Converter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CheckAvailability", value = "/CheckAvailability")
public class CheckAvailability extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));
        DatabaseDuplicates dd = new DatabaseDuplicates();
        String type = payload.getString("userType");
        if(type.equals("normal")) {
            if(dd.checkDuplicateDoctorTable(payload) || dd.checkDuplicateSimpleUserTable(payload)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"Duplicate values: user already exist");
            }
        }else {
            if(dd.checkDuplicateDoctorTable(payload)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"Duplicate values: doctor already exist");
            }
        }
    }


}
