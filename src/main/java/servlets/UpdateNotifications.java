package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditMessageTable;
import database.tables.EditSimpleUserTable;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * returns the number of unread messages for the current doctor or user
 */
@WebServlet(name = "UpdateNotifications", value = "/UpdateNotifications")
public class UpdateNotifications extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        EditMessageTable emt = new EditMessageTable();
        String username = request.getSession().getAttribute("loggedIn").toString();
        String userType = request.getSession().getAttribute("userType").toString();
        try {
            int id;
            if (userType.equals("user"))
                id = eut.databaseGetSimpleUserId(username);
            else
                id = edt.databaseGetDoctorId(username);
            int notifications = emt.databaseGetNotifications(id, userType);
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.print(notifications);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
