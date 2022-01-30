package servlets;

import database.tables.EditDoctorTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Admin Certify Doctor
 */

@WebServlet(name = "AdminCertify", value = "/AdminCertify")
public class AdminCertify extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        try {
            EditDoctorTable edt = new EditDoctorTable();
            edt.databaseCertifyDoctor(payload.getInt("id"));
        }catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
        }
    }
}
