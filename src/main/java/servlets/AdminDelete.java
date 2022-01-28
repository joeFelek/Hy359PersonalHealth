package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AdminDelete", value = "/AdminDelete")
public class AdminDelete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        try {
            if(payload.getString("userType").equals("user")) {
                EditSimpleUserTable eut = new EditSimpleUserTable();
                eut.databaseDeleteSimpleUser(payload.getInt("id"));
            }else if(payload.getString("userType").equals("doctor")) {
                EditDoctorTable edt = new EditDoctorTable();
                edt.databaseDeleteDoctor(payload.getInt("id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
        }
    }

}
