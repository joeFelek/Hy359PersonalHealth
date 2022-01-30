package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import org.json.JSONObject;
import tools.JSON_Converter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Set Rendezvous status to done
 */
@WebServlet(name = "DoneRendezvous", value = "/DoneRendezvous")
public class DoneRendezvous extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        JSONObject payload = new JSONObject(jc.getJSONFromAjax(request.getReader()));

        try {
            EditDoctorTable edt = new EditDoctorTable();
            EditRandevouzTable ert = new EditRandevouzTable();
            int doc_id = edt.databaseGetDoctorId(payload.getString("doc_name"));
            ert.databaseDoDone(doc_id, payload.getString("date"));
            response.setStatus(200);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
