package tools;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import org.json.JSONObject;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseDuplicates {

    public boolean hasDuplicates(String payload, String type) {
        JSONObject jsonPayload = new JSONObject(payload);
        JSONObject testPayload = new JSONObject();
        ArrayList<String> keys = new ArrayList<>();
        keys.add("username");
        keys.add("email");
        keys.add("amka");
        DatabaseDuplicates dd = new DatabaseDuplicates();
        for(int i=0; i<3; i++) {
            testPayload.put(keys.get(i) , jsonPayload.getString(keys.get(i)));
            if (type.equals("normal")) {
                if (dd.checkDuplicateDoctorTable(testPayload) || dd.checkDuplicateSimpleUserTable(testPayload))
                    return true;
            } else {
                if (dd.checkDuplicateDoctorTable(testPayload))
                    return true;
            }
            testPayload.remove(keys.get(i));
        }
        return false;
    }

    private void formatPayload(JSONObject payload) {
        String key;
        if(payload.has("username"))
            key = "username";
        else if(payload.has("email"))
            key = "email";
        else
            key = "amka";
        if(payload.getString(key).isEmpty())
            payload.put(key, "empty");
        else
            payload.put(key, payload.getString(key).replaceAll("[^a-zA-Z0-9\\s@.+]", ""));
    }

    public boolean checkDuplicateSimpleUserTable(JSONObject payload) {
        formatPayload(payload);
        EditSimpleUserTable eut = new EditSimpleUserTable();
        try {
            String property = getProperty(payload);
            String res;
            if(payload.has("user"))
                res = eut.databaseFindUserToJSON(property, payload.getString(property), payload.getString("user"));
            else
                res = eut.databaseFindUserToJSON(property, payload.getString(property));

            JSONObject jo = new JSONObject(res);
            return !jo.getString("count").equals("0");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkDuplicateDoctorTable(JSONObject payload) {
        formatPayload(payload);
        EditDoctorTable edt = new EditDoctorTable();
        try {
            String property = getProperty(payload);
            String res;
            if(payload.has("user"))
                res = edt.databaseFindDoctorToJSON(property, payload.getString(property), payload.getString("user"));
            else
                res = edt.databaseFindDoctorToJSON(property, payload.getString(property));

            JSONObject jo = new JSONObject(res);
            return !jo.getString("count").equals("0");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getProperty(JSONObject payload) {
        if(payload.has("email")) {
            return "email";
        }else if(payload.has("username")) {
            return "username";
        }else {
            return "amka";
        }
    }
}
