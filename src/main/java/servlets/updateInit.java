package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "updateInit", value = "/updateInit")
public class updateInit extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get username from session
        String username = request.getSession().getAttribute("loggedIn").toString();
        String userType = request.getSession().getAttribute("userType").toString();
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        try {
            String res = null;
            if (userType.equals("doctor"))
                res = edt.databaseToDoctor(username);
            else
                res = eut.databaseUserToJSON(username);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(res);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
