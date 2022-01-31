package database.init;

import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;

public class InitDatabase {

    public void addToDatabaseExamples() throws ClassNotFoundException {
        //Users
        String userJSON = "{\"username\":\"mountanton\",\"email\":\"mike@mike.com\",\"password\":\"123456\","
                + "\"firstname\":\"Michalis\",\"lastname\":\"Mountanton\",\"birthdate\":\"1992-06-03\",\"gender\":\"Male\","
                + "\"amka\":\"03069200000\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"CSD Voutes\",\"lat\":\"35.3053121\","
                + "\"lon\":\"25.0722869\",\"telephone\":\"1234567890\",\"height\":\"173\",\"weight\":\"82.0\",\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A+\"}";

        String admin = "{\"username\":\"admin\",\"email\":\"admin@admin.gr\","
                + "\"password\":\"admin\","
                + "\"firstname\":\"Admin\",\"lastname\":\"Admin\",\"birthdate\":\"1970-01-01"
                + "\",\"gender\":\"Male\","
                + "\"amka\":\"01234567890\",\"country\":\"Greece\",\"city\":\"Heraklion\","
                + "\"address\":\"Liontaria\",\"lat\":\"0.5\","
                + "\"lon\":\"0.1\",\"telephone\":\"281000000\",\"height\":\"200\",\"weight\":\"100\",\"blooddonor\":\"0\","
                + "\"bloodtype\":\"A+\"}";

        EditSimpleUserTable eut = new EditSimpleUserTable();
        eut.addSimpleUserFromJSON(userJSON);
        eut.addSimpleUserFromJSON(admin);
        //Doctors
        String jsonDoctor = "{\"username\":\"papadakis\",\"email\":\"papadakis@doctor.gr\",\"password\":\"doctor12*\","
                + "\"firstname\":\"Nikos\",\"lastname\":\"Papadakis\",\"birthdate\":\"1982-10-03\",\"gender\":\"Male\","
                + "\"amka\":\"03108200123\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Evans 83\",\"lat\":\"35.3361866\","
                + "\"lon\":\"25.1342504\",\"telephone\":\"2810123456\",\"height\":\"182\",\"weight\":\"80.0\",\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A+\","
                + "\"specialty\":\"GeneralDoctor\","
                + "\"doctor_info\":\"Exei megali empiria se axiologisi emvoliwn.\","
                + "\"certified\":\"1\""
                + "}";
        String jsonDoctor2 = "{\"username\":\"stefanos\",\"email\":\"stefanos@doctor.gr\",\"password\":\"abcd12$3\","
                + "\"firstname\":\"Stefanos\",\"lastname\":\"Kapelakis\",\"birthdate\":\"1958-01-10\",\"gender\":\"Male\","
                + "\"amka\":\"10015800234\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Kalokairinou 50\",\"lat\":\"35.3376963\","
                + "\"lon\":\"25.1276121\",\"telephone\":\"2810654321\",\"height\":\"170\",\"weight\":\"68.0\",\"blooddonor\":\"0\","
                + "\"bloodtype\":\"B+\","
                + "\"specialty\":\"Pathologist\","
                + "\"doctor_info\":\"O kaluteros giatros gia ti gripi.\","
                + "\"certified\":\"1\""
                + "}";
        String jsonDoctor3 = "{\"username\":\"papadopoulou\","
                + "\"email\":\"papadopoulou@doctor.gr\",\"password\":\"doct12##\","
                + "\"firstname\":\"Eleni\",\"lastname\":\"Papopoulou\",\"birthdate\":\"1980-05-05\",\"gender\":\"Female\","
                + "\"amka\":\"05058000123\",\"country\":\"Greece\",\"city\":\"Heraklion\",\"address\":\"Machis Kritis 10\","
                + "\"lat\":\"35.3375925\","
                + "\"lon\":\"25.1219286\",\"telephone\":\"2810281028\",\"height\":\"170\",\"weight\":\"60.0\",\"blooddonor\":\"1\","
                + "\"bloodtype\":\"AB+\","
                + "\"specialty\":\"GeneralDoctor\","
                + "\"doctor_info\":\"Exei kanei metaptyxiakes spoudes stin ameriki.\","
                + "\"certified\":\"1\""
                + "}";
        String jsonDoctor4 = "{\"username\":\"aggelopoulos\",\"email\":\"aggelopoulos@doctor.gr\","
                + "\"password\":\"agge58$1\","
                + "\"firstname\":\"Giorgos\",\"lastname\":\"Aggelopoulos\",\"birthdate\":\"1978-01-12\","
                + "\"gender\":\"Male\","
                + "\"amka\":\"01127800111\",\"country\":\"Greece\",\"city\":\"Heraklion\","
                + "\"address\":\"Leoforos Knossou 200\",\"lat\":\"35.3152534\","
                + "\"lon\":\"25.1474208\",\"telephone\":\"2811111111\",\"height\":\"175\",\"weight\":\"60.0\","
                + "\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A-\","
                + "\"specialty\":\"Pathologist\","
                + "\"doctor_info\":\"Kathigitis iatrikis sto panepistimio.\","
                + "\"certified\":\"1\""
                + "}";

        String jsonDoctor5 = "{\"username\":\"papatheodorou\",\"email\":\"papatheodorou@doctor.gr\","
                + "\"password\":\"papap$75\","
                + "\"firstname\":\"Konstantina\",\"lastname\":\"Papatheodorou\","
                + "\"birthdate\":\"1968-01-03\","
                + "\"gender\":\"Female\","
                + "\"amka\":\"03016800111\",\"country\":\"Greece\",\"city\":\"Heraklion\","
                + "\"address\":\"Leoforos 62 Martyron 100\",\"lat\":\"35.3361846\","
                + "\"lon\":\"35.3361846\",\"telephone\":\"2811121111\",\"height\":\"160\",\"weight\":\"65.0\","
                + "\"blooddonor\":\"1\","
                + "\"bloodtype\":\"A-\","
                + "\"specialty\":\"Pathologist\","
                + "\"doctor_info\":\"Exei empiria se zaxaro kai xolisterini.\","
                + "\"certified\":\"1\""
                + "}";

        EditDoctorTable edt = new EditDoctorTable();
        edt.addDoctorFromJSON(jsonDoctor);
        edt.addDoctorFromJSON(jsonDoctor2);
        edt.addDoctorFromJSON(jsonDoctor3);
        edt.addDoctorFromJSON(jsonDoctor4);
        edt.addDoctorFromJSON(jsonDoctor5);

    }

}
