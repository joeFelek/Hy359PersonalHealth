package servlets;

import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Check session attributes to see if user is already logged in
 */
@WebServlet(name = "isLogged", value = "/isLogged")
public class isLogged extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        if(session.getAttribute("loggedIn")!=null){
            response.setStatus(200);
            JSONObject jo = new JSONObject();
            jo.put("username", session.getAttribute("loggedIn").toString());
            jo.put("userType", session.getAttribute("userType").toString());
            PrintWriter out = response.getWriter();
            out.print(jo);
        }
        else{
            response.setStatus(100);
        }
    }

}
