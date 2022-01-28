package servlets;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import model.Doctor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet(name = "DoctorListForUser", value = "/DoctorListForUser")
public class DoctorListForUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject user;
        JSONArray doctors_json;
        ArrayList<Doctor> doctors;
        JSONObject res = new JSONObject();
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        try {
            user = new JSONObject(eut.databaseUserToJSON(request.getSession().getAttribute("loggedIn").toString()));
            doctors = edt.databaseToCertifiedDoctors();
            doctors_json = new JSONArray();

            String origin = user.getString("lat") + "%2C" + user.getString("lon");
            StringBuilder destination = new StringBuilder();
            for (Doctor d : doctors)
                destination.append(d.getLat()).append("%2C").append(d.getLon()).append("%3B");
            JSONObject distAndDur = GetDistancesAndDurations(origin, destination.toString());
            JSONArray durations = convertToMin(distAndDur.getJSONArray("durations").getJSONArray(0));
            JSONArray distances = convertToKm(distAndDur.getJSONArray("distances").getJSONArray(0));

            int i = 0;
            for (Doctor d : doctors) {
                JSONObject jo = new JSONObject();
                jo.put("name", d.getFirstname() + " " + d.getLastname());
                jo.put("address", d.getAddress());
                jo.put("city", d.getCity());
                jo.put("info", d.getDoctor_info());
                jo.put("specialty", d.getSpecialty());
                jo.put("telephone", d.getTelephone());
                jo.put("email", d.getEmail());
                jo.put("dist", distances.get(i).toString());
                jo.put("dur", durations.get(i).toString());
                doctors_json.put(jo);
                i++;
            }
            res.put("data", doctors_json);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(res);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private JSONArray convertToKm(JSONArray distances) {
        JSONArray ja = new JSONArray();

        for (int i = 0; i < distances.length(); i++) {
            Object o = distances.get(i);
            if (!o.toString().equals("null")) {
                float n = (int) o;
                ja.put(i, n / 1000);
            } else
                ja.put(i, o);
        }

        return ja;
    }

    private JSONArray convertToMin(JSONArray durations) {
        JSONArray ja = new JSONArray();

        for (int i = 0; i < durations.length(); i++) {
            Object o = durations.get(i);
            if (!o.toString().equals("null")) {
                int n = (int) o;
                ja.put(i, n / 60);
            } else
                ja.put(i, o);
        }

        return ja;
    }

    private JSONObject GetDistancesAndDurations(String origin, String destination) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?origins=" + origin + "&destinations=" + destination)
                .get()
                .addHeader("x-rapidapi-host", "trueway-matrix.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "905067515fmsh7514e7890c1e788p1d7da2jsnaa1e9a0f4cbb")
                .build();

        Response response = client.newCall(request).execute();
        return new JSONObject(Objects.requireNonNull(response.body()).string());
    }
}
