function loginPost() {
    let myForm = document.getElementById('login-form');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    let jsonData = JSON.stringify(data);

    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const res = JSON.parse(xhr.responseText)
            setPageForLoggedUser(res)
        } else if (xhr.status === 401) {
            $('#login-msg').html("Please provide a correct username and password")
            $('#login-msg').show();
        } else if (xhr.status === 403) {
            $('#login-msg').html("You are not certified, please contact the administrator")
            $('#login-msg').show();
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'Login');
    xhr.setRequestHeader("Content-type", "application/json")
    xhr.send(jsonData);
    $('#password').val('')
}

function setPageForLoggedUser(response) {
    $('#login').hide();
    $('#sign-up').hide();
    $('#user-details').html(response.username.charAt(0).toUpperCase());
    $('#user-details').show();
    $('#user-notifications').show();
    $('#logout').show();
    $('#notifications').show();
    if (response.userType === 'admin') {
        $('#nav-left').hide()
        $('#notifications').hide();
        $('#main-container').load('jsp/adminUserTable.jsp')
    } else if (response.userType === 'doctor') {
        $('#main-container').empty();
        $('#nav-left').html(' <li class="nav-item"> ' +
            '<button id="rendezvous" class="navbar-btn btn btn-custom override-nav-btn" type="button"' +
            '       onclick="rendezvous();">Rendezvous ' +
            '</button>' +
            '</li>')
        updateNotifications();
    } else if (response.userType === 'user') {
        $('#main-container').empty();
        $('#nav-left').append(' <li class="nav-item"> ' +
            '<button id="rendezvous" class="navbar-btn btn btn-custom override-nav-btn" type="button"' +
            '       onclick="rendezvousUser();">Rendezvous ' +
            '</button>' +
            '</li>')
        $('#available-doctors').attr('onclick', 'userListDoctors();')
        updateNotifications();
    }
}

function updateNotifications() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if (xhr.responseText !== '0') {
                $('#notifications-logo').css('color', '#D11A2A')
                $('#notifications-logo').removeClass('far')
                $('#notifications-logo').addClass('fas')
                $('#notifications-msg').html('<span class="badge badge-primary badge-pill">' + xhr.responseText + ' new messsages</span>')
                $('#notifications-msg').removeClass('text-muted')
            } else {
                $('#notifications-logo').css('color', '#1565c0')
                $('#notifications-logo').removeClass('fas')
                $('#notifications-logo').addClass('far')
                $('#notifications-msg').html('Empty')
                $('#notifications-msg').addClass('text-muted')
            }
        } else if (xhr.status !== 200) {

        }
    };
    xhr.open('GET', 'UpdateNotifications');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function rendezvous() {
    $('#main-container').load('jsp/doctorRendezvous.jsp')
}

function userListDoctors() {
    $('#main-container').load('jsp/userListDoctors.jsp')
}

function rendezvousUser() {
    $('#main-container').load('jsp/userRendezvous.jsp')
}

function checkAvailability() {
    const data = {}
    data["userType"] = "normal"
    data["email"] = $('#email').val()
    data["user"] = $('#username-update').val()
    let jsonData = JSON.stringify(data);

    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#email-error-msg').html('')
        } else if (xhr.status === 403) {
            $('#email-error-msg').html("Email already exist")
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'CheckAvailability');
    xhr.setRequestHeader("Content-type", "application/json")
    xhr.send(jsonData);
}

function updatePost() {
    let myForm = document.getElementById('update-form');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    let jsonData = JSON.stringify(data);

    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            location.reload();
        } else if (xhr.status === 403) {

        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'Update');
    xhr.setRequestHeader("Content-type", "application/json")
    xhr.send(jsonData);
}

function updateInit() {
    $('#main-container').load("html/user_details.html")
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            doUpdateInit(xhr.responseText)
        } else if (xhr.status !== 200) {
            alert(xhr.status)
        }
    };
    xhr.open('GET', 'updateInit');
    xhr.send();
}

function doUpdateInit(response) {
    const jsonRes = JSON.parse(response)
    if (jsonRes.specialty) {
        $('#doc-info').val(jsonRes.doctor_info)
        $('input[name="specialty"][value="' + jsonRes.specialty + '"]').prop('checked', true);
    } else {
        $('#DoctorExtra').html('')
    }
    $('#username-update ').val(jsonRes.username)
    $('#email').val(jsonRes.email)
    $('#pwd').val(jsonRes.password)
    $('#firstName').val(jsonRes.firstname)
    $('#lastName').val(jsonRes.lastname)
    $('#birth').val(jsonRes.birthdate)
    $('#amka').val(jsonRes.amka)
    $('#country').val(jsonRes.country)
    $('#city').val(jsonRes.city)
    $('#lat').val(jsonRes.lat)
    $('#lon').val(jsonRes.lon)
    $('#address').val(jsonRes.address)
    $('#phone').val(jsonRes.telephone)
    $('#height').val(jsonRes.height)
    $('#weight').val(jsonRes.weight)
    $('#bloodType').val(jsonRes.bloodtype)
    $('input[name="gender"][value="' + jsonRes.gender.toString().toLowerCase() + '"]').parent().addClass('active');
    $('input[name="gender"][value="' + jsonRes.gender.toString().toLowerCase() + '"]').prop('checked', true);
    $('input[name="blooddonor"][value="' + jsonRes.blooddonor + '"]').prop('checked', true);
}

function getFitnessDetails() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#main-container').load("html/fitness.html")
            const res = JSON.parse(xhr.responseText)
            if (res.weight.toString() === '0.0' || res.height.toString() === '0') {
                $('#fitness-msg').html('Please provide your weight and height if you want to see ur BMI and ideal weight')
                $('#fitness-result-container').hide()
                return;
            }
            bmiGet(res.weight, res.height, res.birthdate)
            getIdealWeight(res.gender, res.height)
        } else if (xhr.status !== 200) {
            alert(xhr.status)
        }
    };
    xhr.open('GET', 'updateInit');
    xhr.send();
}

function bmiGet(weight, height, birthdate) {
    const age = getAge(birthdate.toString())
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const res = JSON.parse(xhr.responseText)
            $('#bmi').html(res.data.bmi + ' ' + res.data.health)
        }
    });
    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/bmi?age=" + age + "&weight=" + weight + "&height=" + height);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "905067515fmsh7514e7890c1e788p1d7da2jsnaa1e9a0f4cbb");
    xhr.send();
}

function getAge(dateString) {
    const today = new Date();
    const birthDate = new Date(dateString);
    let age = today.getFullYear() - birthDate.getFullYear();
    const m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

function getIdealWeight(gender, height) {
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const res = JSON.parse(xhr.responseText)
            $('#idealWeight').html(res.data.Devine)
        }
    });
    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/idealweight?gender=" + gender.toString().toLowerCase() + "&height=" + height);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "905067515fmsh7514e7890c1e788p1d7da2jsnaa1e9a0f4cbb");
    xhr.send();
}

function listDoctors() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            doListDoctors(xhr.responseText);
        } else if (xhr.status !== 200) {
            alert(xhr.status)
        }
    };
    xhr.open('GET', 'DoctorsList');
    xhr.send();
}

function doListDoctors(response) {
    const res = JSON.parse(response)
    let html = '<div class="container"><table id="doctorlist-table" class="display"><thead>' +
        '<tr><th scope="col">Name</th><th scope="col">Address</th><th scope="col">City</th>' +
        '<th scope="col">Info</th><th scope="col">Specialty</th><th scope="col">Telephone</th></tr></thead><tbody>'

    for (const resKey in res) {
        html += '<tr>' +
            '<td>' + res[resKey].firstname + ' ' + res[resKey].lastname + '</td>' +
            '<td>' + res[resKey].address + '</td>' +
            '<td>' + res[resKey].city + '</td>' +
            '<td>' + res[resKey].info + '</td>' +
            '<td>' + res[resKey].specialty + '</td>' +
            '<td>' + res[resKey].telephone + '</td>' +
            '</tr>'
    }
    html += '</tbody></table></div>'
    $('#main-container').html(html)
    $('#doctorlist-table').DataTable();
}

function adminDelete(id, userType) {
    let data = {}
    data['id'] = id + ''
    data['userType'] = userType
    let jsonData = JSON.stringify(data);
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#main-container').load('jsp/adminUserTable.jsp')
        } else if (xhr.status !== 200) {
            alert(xhr.status)
        }
    };
    xhr.open('POST', 'AdminDelete');
    xhr.send(jsonData);
}

function adminCertify(id) {
    let data = {'id': id + ''}
    const jsonData = JSON.stringify(data)
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#main-container').load('jsp/adminUserTable.jsp')
        } else if (xhr.status !== 200) {
            alert(xhr.status)
        }
    };
    xhr.open('POST', 'AdminCertify');
    xhr.send(jsonData);
}

$(document).ready(function () {

    $('#user-details').hide();
    $('#user-notifications').hide();
    $('#logout').hide();

    isLoggedIn();

    function isLoggedIn() {
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const res = JSON.parse(xhr.responseText);
                setPageForLoggedUser(res);
            } else if (xhr.status !== 200) {
                // dont throw error plz
            }
        };
        xhr.open('GET', 'isLogged');
        xhr.send();
    }

    $('#login').click(function () {
        $('#main-container').load('html/login.html');
    });

    $('#logout').click(function () {
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                doLogout();
            } else if (xhr.status !== 200) {
                location.reload();
            }
        };
        xhr.open('POST', 'Logout');
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhr.send();
    });

    function doLogout() {
        $('#nav-left').html('<li class="nav-item"> ' +
            '<button id="available-doctors" class="navbar-btn btn btn-custom override-nav-btn" ' +
            'type="button" onclick="listDoctors()">Doctors</button> </li>')
        $('#nav-left').show();
        $('#user-details').hide();
        $('#user-notifications').hide();
        $('#logout').hide();
        $('#login').show();
        $('#sign-up').show();
        $('#main-container').empty();
    }

    $('#user-notifications').click(function () {
        updateNotifications();
    })

    $('.dropdown-menu').click(function (e) {
        e.stopPropagation();
    });

    $('#show-all-notifications-btn').click(function () {
        $('#main-container').load('jsp/Messages.jsp')
    })

});

