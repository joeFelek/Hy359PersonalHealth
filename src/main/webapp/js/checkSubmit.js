$(document).ready(function () {

    $('#username-error-msg').hide()
    $('#email-error-msg').hide()
    $('#amka-error-msg').hide()

    $('#username, #email, #amka').change(function () {
        checkAvailable($(this))
    });

    $('#normal, #doctor').change(function () {
        checkAvailable($('#username'))
        checkAvailable($('#email'))
        checkAvailable($('#amka'))
    });

    function checkAvailable(element) {
        let id = element.attr('id')
        const data = {};
        data["" + id] = element.val()
        if ($('#doctor').is(':checked'))
            data["userType"] = $('#doctor').val()
        else
            data["userType"] = $('#normal').val()

        let jsonData = JSON.stringify(data);

        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                if (id === "username") {
                    $('#username-error-msg').hide()
                } else if (id === "email") {
                    $('#email-error-msg').hide()
                } else {
                    $('#amka-error-msg').hide()
                }
            } else if (xhr.status === 403) {
                if (id === "username") {
                    $('#username-error-msg').show()
                } else if (id === "email") {
                    $('#email-error-msg').show()
                } else {
                    $('#amka-error-msg').show()
                }
            } else if (xhr.status !== 200) {
                alert('Request failed. Returned status of ' + xhr.status);
            }
        };
        xhr.open('POST', '../CheckAvailability');
        xhr.setRequestHeader("Content-type", "application/json")
        xhr.send(jsonData);
    }

});