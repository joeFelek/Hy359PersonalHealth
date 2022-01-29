<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Doctor List</title>
</head>
<body>

<script type="text/javascript" src="js/userListDoctors.js"></script>

<div class="container-fluid" style="width: 80%">
    <h3 style="margin-bottom: 50px">Available Doctors:</h3>
    <table id="doctor-table" class="display" style="width: 100%">
        <thead>
        <tr>
            <th>Specialty</th>
            <th>Name</th>
            <th>Doctor's Info</th>
            <th>City</th>
            <th>Address</th>
            <th>Telephone</th>
            <th>Email</th>
            <th>Distance</th>
            <th>Duration</th>
        </tr>
        </thead>
    </table>
</div>

<div id="Modal" class="modal">
    <div id="modal-container" class="modal-content container">
        <span class="close" style="margin-bottom: 50px">&times;</span>
        <table id="rendezvous-table" class="display" style="width: 100%">
            <thead>
            <tr>
                <th></th>
                <th>Date</th>
                <th>Price</th>
            </tr>
            </thead>
        </table>
        <div id="book"></div>
    </div>
</div>

</body>
</html>
