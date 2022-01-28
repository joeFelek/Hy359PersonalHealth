package servlets;

import com.google.gson.Gson;
import database.tables.EditConversationTable;
import database.tables.EditDoctorTable;
import database.tables.EditMessageTable;
import database.tables.EditSimpleUserTable;
import model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import tools.DatabaseDuplicates;
import tools.JSON_Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet(name = "Submit", value = "/Submit")
public class Submit extends HttpServlet {
    private Double lon = 0.0;
    private Double lat = 0.0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        JSON_Converter jc = new JSON_Converter();
        String payload = jc.getJSONFromAjax(request.getReader());
        String userType = payload.contains("normal") ? "normal" : "doctor";

        DatabaseDuplicates dd = new DatabaseDuplicates();
        if (dd.hasDuplicates(payload, userType)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Duplicate values: " + userType + " user already exist");
            return;
        }

        System.out.println("Get lat,lon...");
        getLatLon(payload);
        payload = appendLatLon(payload);

        if (userType.equals("normal")) {
            EditSimpleUserTable eut = new EditSimpleUserTable();
            try {
                eut.addSimpleUserFromJSON(payload);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            EditDoctorTable edt = new EditDoctorTable();
            try {
                edt.addDoctorFromJSON(payload);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        JSONObject res = new JSONObject(payload);
        if (userType.equals("normal")) {
            res.append("message", "Registration completed successfully");
        } else {
            res.append("message", "Registration completed, but you have to get certified by the administrator");
            res.append("certified", "0");
        }
        PrintWriter out = response.getWriter();
        out.print(res);

        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        EditMessageTable emt = new EditMessageTable();
        JSONObject welcome = new JSONObject();
        try {
            if (userType.equals("normal")) {
                welcome.put("doctor_id", "0");
                welcome.put("user_id", "" + eut.databaseGetSimpleUserId(res.getString("username")));
            }else {
                welcome.put("doctor_id", ""+edt.databaseGetDoctorId(res.getString("username")));
                welcome.put("user_id", "0");
            }
            welcome.put("sender", "system");
            welcome.put("seen", "0");
            welcome.put("message", "Welcome "+res.getString("username")+"!");
            emt.addMessageFromJSON(welcome.toString());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getLatLon(String payload) throws IOException {

        String address = getAddressFromJson(payload);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&accept-language=en&polygon_threshold=0.0")
                .get()
                .addHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "905067515fmsh7514e7890c1e788p1d7da2jsnaa1e9a0f4cbb")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseString = Objects.requireNonNull(response.body()).string();
            if (responseString.equals("{}")) {
                lat = 0.0;
                lon = 0.0;
                return;
            }
            JSONObject jo = new JSONObject(responseString.substring(1, responseString.length() - 1));
            this.lat = Double.parseDouble(jo.getString("lat"));
            this.lon = Double.parseDouble(jo.getString("lon"));
            Objects.requireNonNull(response.body()).close();
        }
    }

    private String getAddressFromJson(String payload) {
        Gson gson = new Gson();
        User user = gson.fromJson(payload, User.class);

        return (user.getAddress() + " " + user.getCity() + " " + user.getCountry())
                .replaceAll(" ", "%20");
    }

    private String appendLatLon(String payload) {
        String pl = payload.substring(0, payload.length() - 1);
        return pl + ",\"lat\":\"" + lat + "\",\"lon\":\"" + lon + "\"}";
    }
}
