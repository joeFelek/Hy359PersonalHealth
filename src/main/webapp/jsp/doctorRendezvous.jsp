<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Rendezvous</title>
</head>
<body>
<script src="js/doctorRendezvous.js"> </script>

<div id="events" class="container">
    <table id="example" class="display" style="width:100%">
        <thead>
        <tr>
            <th></th>
            <th>Client Name</th>
            <th>Date and Time</th>
            <th>Price</th>
            <th>Client Info</th>
            <th>Status</th>
        </tr>
        </thead>
    </table>
</div>

<div id="Modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <form id="rendezvous-form" name="rendezvous" class="container" method="POST">
            <div id="meow">
                <div class="form-group" id="Date">
                    <label for="date">Date</label>
                    <input type="date" class="form-control" id="date" name="date" min="2021-05-11" required>
                    <small>Must be a future date</small>
                </div>
                <div class="form-group" id="Time">
                    <label for="time">Time</label>
                    <input type="time" class="form-control" id="time" name="time" min="08:00" max="20:30" required>
                    <small>Office hours are 8am to 8:30pm</small>
                </div>
                <div class="form-group" id="Price">
                    <label for="price">Price</label>
                    <input type="number" name="price" id="price" class="form-control" min="20" max="80" required>
                </div>
                <p id="newRendezvous-msg"></p>
                <input class="btn btn-primary" type="submit" name="submit" id="submit" value="Create">
            </div>
        </form>
    </div>
</div>

</body>
</html>
