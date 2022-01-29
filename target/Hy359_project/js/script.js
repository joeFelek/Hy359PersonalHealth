$(document).ready(function() {
    
    // password's visibility swap
    $('.showhide').click(function() {
        let e = $(this).find('.in-eye')
        $(e).toggleClass('fa-eye')
        $(e).toggleClass('fa-eye-slash')     
        
        let pwd = $(this).parent().find('input')

        if (pwd.attr('type') === 'password') 
            pwd.attr('type', 'text')
        else 
            pwd.attr('type','password') 
    });
    
    // check if passwords match
    $('#password,#cpassword').change(function() {          
        if (pwd_match()) 
            $('#no-match-pwd').hide()
        else 
            $('#no-match-pwd').show() 
    });

    function pwd_match() {
        let pwd = $('#password').val()
        let cpwd = $('#cpassword').val()
            
        return !cpwd || pwd === cpwd
    }

    // password safety
    $('#password').keyup(function() { 

        let pwd = $('#password').val()
        if (!pwd) {
            $('#pass-test').empty();
            return
        }

        let count_of_numbers = pwd.replace(/[^0-9]/g,"").length
        let stats = count(pwd) 
        let count_of_most_occurring = stats.max 
        let count_of_different = stats.diff

        let weak = (count_of_numbers/pwd.length >= 0.5) || 
                   (count_of_most_occurring/pwd.length >= 0.5)
        
        let strong = count_of_different/pwd.length >= 0.8

        if (weak) {
            $('#pass-test').text('Weak');
            $('#pass-test').css('color','red');
        } else if (strong) { 
            $('#pass-test').text('Strong');
            $('#pass-test').css('color','green');
        } else {
            $('#pass-test').text('Medium');
            $('#pass-test').css('color','orange');
        }    

    });

    // Return the number of occurrences of the most occurring char in a string
    // and the number of different chars
    function count(str) {
        let count = [];
        let max = 0
        let same = 0

        if (!str)
            return
        
        for (let i = 0; i < str.length; i++) {
            count[i] = 0
            for (let j = 0; j < str.length; j++) {
                if (str[i] === str[j] && i>j)
                    break
                if (str[i] === str[j])
                    count[i]++
            }
            if (count[i] > max) 
                max = count[i]
            
            if (count[i] === 0)
                same++
        }

        // find differences
        let diff =  str.length - same
        
        return {
            max,
            diff
        }
    }

    // doctor extra handling
    $('#doctor').click(function() { 
        $('#DoctorExtra').show();
        $('#address').attr('placeholder', 'Clinic\'s address');
        $('#address-label').text('Clinic\'s Address');
    });
    $('#normal').click(function() { 
        $('#DoctorExtra').hide();
        $('#address').attr('placeholder', 'Address');
        $('#address-label').text('Address');
    });  

    // amka handling
    $('#birth, #amka').change(() => amka_check());

    function amka_check() {
        if (valid_amka())
            $('#amka-error').hide()
        else
            $('#amka-error').show()
    }

    function valid_amka() {
        let amka = $('#amka').val().substring(0,6)
        let date = new Date($('#birth').val());

        let year = date.getYear()
        let month = (date.getMonth()+1).toString()
        let day = date.getDate().toString()

        if (month.length === 1)
            month = '0' + month
        if (day.length === 1)
            day = '0' + day

        date = day + month + year;

        return amka === date
    }

    // conditions checkbox handling
    function agreed_to_conditions() {
        return $('#termCheckbox').is(":checked") 
    }

    $('#submit').click(function() { 
        if (!agreed_to_conditions())
            $('#conditions-msg').show()
    });

    $('#termCheckbox').click(function() { 
        $('#conditions-msg').hide()
    });
    
    // prevent submit
    function can_submit() {
        return agreed_to_conditions() && valid_amka() && pwd_match()
    }
    
    $('#Form').submit(function(e) {
        if (can_submit()) {
            sendAjaxPOST();
            return false;
        }
        e.preventDefault()
        console.log('submit intercepted')
    });

    function sendAjaxPOST() {
        let myForm = document.getElementById('Form');
        let formData = new FormData(myForm);
        const data = {};
        formData.forEach((value, key) => {
            if(key === "doctor_info" && value === "")
                value = "empty"
            else if(value === "")
                value = "0"
            data[key] = value
        });
        let jsonData = JSON.stringify(data);

        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                $('#form-container').hide(1000, "linear", () => printSuccess(xhr.responseText));
            } else if (xhr.status !== 200) {
                alert('Request failed. Returned status of ' + xhr.status);
            }
        };
        xhr.open('POST', '../Submit');
        xhr.setRequestHeader("Content-type", "application/json")
        xhr.send(jsonData);
    }

    function printSuccess(responseText) {
        $('#Map').hide()
        $('#title1').hide()
        const jsonRes = JSON.parse(responseText)
        $('#map-container').html(detailsHtml(jsonRes))
    }

    function detailsHtml(res) {
        let info = "null"
        let specialty = "null"
        let certified = "null"
        let blooddonor;
        if(res.userType === 'doctor') {
            info = res.doctor_info
            specialty = res.specialty
            if(res.certified === '1')
                certified = 'yes'
            else
                certified = 'no'
        }
        if(res.bloodonor === '1') {
            blooddonor = 'yes'
        }else
            blooddonor = 'no'

        var htmlString = '' + 
        '<div class="container-fluid" id="details-container">' + 
        '    <div id="details-msg-container" class="container">' + 
        '        <h1 id="details-msg">'+res.message+'</h1>' + 
        '    </div>' + 
        '    <div id="details-user-type-container" class="row">' + 
        '        <div id="details-user-container" class="col-md-6">' + 
        '            <span id="details-user-title">Username:</span>' + 
        '            <span id="details-user">'+res.username+'</span>' + 
        '        </div>' + 
        '        <div id="details-type-container" class="col-md-6">' + 
        '            <span id="details-type-title">User Type:</span>' + 
        '            <span id="details-type">'+res.userType+'</span>' + 
        '        </div>' + 
        '    </div>' + 
        '    <div id="details-email-tel-container" class="row">' +
        '        <div id="details-email-container" class="col-md-6">' + 
        '            <span id="details-email-title">Email:</span>' + 
        '            <span id="details-email">'+res.email+'</span>' + 
        '        </div>' +
        '        <div id="details-tel-container" class="col-md-6">' + 
        '            <span id="details-tel-title">Telephone:</span>' + 
        '            <span id="details-tel">'+res.telephone+'</span>' + 
        '        </div>' + 
        '    </div>' + 
        '    <div id="details-Password-container" class="row">' +
        '        <div id="details-password-container" class="col-md-12">' + 
        '            <span id="details-password-title">Password:</span>' + 
        '            <span id="details-password">'+res.password+'</span>  ' + 
        '        </div>' +
        '    </div>' + 
        '    <div id="details-Fullname-container" class="row">' +
        '       <div id="details-fullname-container" class="col-md-12">' + 
        '           <span id="details-fullname-title">Full Name:</span>' + 
        '           <span id="details-fullname">'+res.firstname+' '+res.lastname+'</span>' + 
        '       </div>' + 
        '    </div>' +
        '    <div id="details-birth-amka-gender-container" class="row">' + 
        '        <div id="details-birth-container" class="col-md-6">' + 
        '            <span id="details-birth-title">Birth Date:</span>' + 
        '            <span id="details-birth">'+res.birthdate+'</span>' + 
        '        </div>' + 
        '        <div id="details-amka-container" class="col-md-3">' + 
        '            <span id="details-amka-title">AMKA:</span>' + 
        '            <span id="details-amka">'+res.amka+'</span>' + 
        '        </div>' + 
        '        <div id="details-gender-container" class="col-md-3">' + 
        '            <span id="details-gender-title">Gender:</span>' + 
        '            <span id="details-gender">'+res.gender+'</span>' + 
        '        </div>' + 
        '    </div>' + 
        '    <div id="details-country-city-container" class="row">' + 
        '        <div id="details-country-container" class="col-md-6">' + 
        '            <span id="details-country-title">Country:</span>' + 
        '            <span id="details-country">'+res.country+'</span>' + 
        '        </div>' + 
        '        <div id="details-city-container" class="col-md-6">' + 
        '            <span id="details-city-title">City:</span>' + 
        '            <span id="details-city">'+res.city+'</span>' + 
        '        </div>' + 
        '    </div>' + 
        '    <div id="details-address-lat-lon-container" class="row">' + 
        '        <div id="details-address-container" class="col-md-6">' + 
        '            <span id="details-address-title">Address:</span>' + 
        '            <span id="details-address">'+res.address+'</span>' + 
        '        </div>' + 
        '        <div id="details-lat-container" class="col-md-3">' + 
        '            <span id="details-lat-title">Lat:</span>' + 
        '            <span id="details-lat">'+res.lat+'</span>' + 
        '        </div>' + 
        '        <div id="details-lon-container" class="col-md-3">' + 
        '            <span id="details-lon-title">Lon:</span>' + 
        '            <span id="details-lon">'+res.lon+'</span>' + 
        '        </div>' + 
        '    </div>' + 
        '    <div id="details-heigth-weigth-donor-blood-container" class="row">' + 
        '        <div id="details-heigth-container" class="col-md-3">' + 
        '            <span id="details-heigth-title">Heigth:</span>' + 
        '            <span id="details-heigth">'+res.height+'</span>' + 
        '        </div>' + 
        '        <div id="details-weigth-container" class="col-md-3">' + 
        '            <span id="details-weigth-title">Weigth:</span>' + 
        '            <span id="details-weigth">'+res.weight+'</span>' + 
        '        </div>' + 
        '        <div id="details-donor-container" class="col-md-3">' + 
        '            <span id="details-donor-title">Blood Donor:</span>' + 
        '            <span id="details-donor">'+blooddonor+'</span>' + 
        '        </div>' + 
        '        <div id="details-blood-container" class="col-md-3">' + 
        '            <span id="details-blood-title">Blood Type:</span>' + 
        '            <span id="details-blood">'+res.bloodtype+'</span>' + 
        '        </div>' + 
        '    </div>' + 
        '    <div id="details-Specialty-container" class="row">' +
        '       <div id="details-specialty-container" class="col-md-12">' + 
        '           <span id="details-specialty-title">Specialty:</span>' + 
        '           <span id="details-specialty">'+specialty+'</span>' + 
        '       </div>' +
        '    </div>' + 
        '    <div id="details-Info-container" class="row">' +
        '       <div id="details-info-container" class="col-md-12">' + 
        '           <span id="details-info-title">Doctor Info:</span>' + 
        '           <span id="details-info">'+info+'</span>' + 
        '       </div>' + 
        '    </div>' +
        '    <div id="details-Certified-container" class="row">' +
        '       <div id="details-certified-container" class="col-md-12">' + 
        '           <span id="details-certified-title">Certified:</span>' + 
        '           <span id="details-certified">'+certified+'</span>' + 
        '       </div>' +
        '    </div>' + 
        '</div>' + 
        '';

        if(res.userType === 'normal') {
            var index = htmlString.indexOf('<div id="details-Specialty-container"')
            return htmlString.substring(0,index)+'</div>';
        }
        return htmlString
    }

});
