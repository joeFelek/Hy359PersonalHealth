<%@ page import="model.SimpleUser" %>
<%@ page import="model.Doctor" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="database.tables.EditSimpleUserTable" %>
<%@ page import="database.tables.EditDoctorTable" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <style>tr th {
        font-weight: bold
    }

    h3 {
        font-weight: bold;
        margin-top: 20px;
        margin-bottom: 40px;
    }

    table {
        margin-bottom: 150px;
        width: 100%;
    }
    </style>
</head>
<body>
<%
    ArrayList<SimpleUser> users;
    ArrayList<Doctor> doctors;
    EditSimpleUserTable eut = new EditSimpleUserTable();
    EditDoctorTable edt = new EditDoctorTable();
    try {
        doctors = edt.databaseToDoctors();
        users = eut.databaseToUsers();
%>
<div class="container">
    <h3>Doctors:</h3>
    <table id="doctor-table" class="display">
        <thead>
        <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Name</th>
            <th>Birthdate</th>
            <th>Certified</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Doctor doc : doctors) {
        %>
        <tr>
            <td><%=doc.getUsername()%>
            </td>
            <td><%=doc.getEmail()%>
            </td>
            <td><%=doc.getFirstname() + " " + doc.getLastname()%>
            </td>
            <td><%=doc.getBirthdate()%>
            </td>
            <td><%=doc.getCertified()%>
            </td>
            <td>
                <button class="btn btn-outline-primary" id="admin-certify" type="button"
                        onclick="adminCertify(<%=doc.getDoctor_id()%>);"><i class="fas fa-user-check"></i></button>
                <button class="btn btn-outline-primary" id="admin-delete" type="button"
                        onclick="adminDelete(<%=doc.getDoctor_id()%>, 'doctor');"><i class="fas fa-trash-alt"></i>
                </button>
            </td>
        </tr>
        <% }%>
        </tbody>
    </table>
</div>
<div class="container">
    <h3>Users:</h3>
    <table id="user-table" class="display">
        <thead>
        <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Name</th>
            <th>Birthdate</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (SimpleUser user : users) {
        %>
        <tr>
            <td><%=user.getUsername()%>
            </td>
            <td><%=user.getEmail()%>
            </td>
            <td><%=user.getFirstname() + " " + user.getLastname()%>
            </td>
            <td><%=user.getBirthdate()%>
            </td>
            <td>
                <button class="btn btn-outline-primary" id="admin-delete" type="button"
                        onclick="adminDelete(<%=user.getUser_id()%>, 'user');"><i class="fas fa-trash-alt"></i></button>
            </td>
        </tr>
        <%
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        %>
        </tbody>
    </table>
</div>
<script>
    $(document).ready(function () {
        $('#user-table').DataTable();
        $('#doctor-table').DataTable();
    });
</script>
</body>
</html>
