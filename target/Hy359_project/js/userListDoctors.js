function bookRendezvous(id, date) {
    let user_info = $('#user_info').val()
    if (user_info === '')
        user_info = 'null'
    let req = {'id': id, 'date': date, 'user_info': user_info}
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#rendezvous-msg').show();
            $('#rendezvous-btn').prop("disabled", true);
            setTimeout(function () {
                closeModal();
            }, 2000);
        } else if (xhr.status !== 200) {
            alert(xhr.status)
        }
    };
    xhr.open('POST', 'BookRendezvous');
    xhr.send(JSON.stringify(req));
}


function closeModal() {
    $('#Modal').css('display', 'none');
    $('#book').empty()
    $('#rendezvous-table').DataTable().destroy();
}


$(document).ready(function () {

    let rendezvous_table;

    function showAvailableRendezvous(data) {
        rendezvous_table = $('#rendezvous-table').DataTable({
            "responsive": true,
            "dom": 'Blfrtip',
            "lengthChange": false,
            buttons: [],
            select: true,
            order: [[1, 'asc']],
            "bProcessing": true,
            "ajax": {
                "url": 'ShowAvailableRendezvous',
                "type": 'POST',
                "data": function () {
                    return JSON.stringify(data);
                }
            },
            columns: [
                {
                    data: null,
                    defaultContent: '',
                    className: 'select-checkbox',
                    orderable: false
                },
                {"data": "date"},
                {"data": "price", render: $.fn.dataTable.render.number(',', '.', 0, '€')}
            ]
        })
        $('#Modal').css('display', 'block');
    }

    $('#rendezvous-table').on('select.dt', function (e, dt, type, indexes) {
        let data = dt.rows(indexes).data()[0];
        let newHtml = '<div class="container" style="margin-top: 50px">' +
            '<label for="user_info">Add comment to doctor:</label><br>' +
            '<textarea id="user_info" name="user_info" cols="70" rows="5"></textarea><br>' +
            '<button id="rendezvous-btn" type="button" class="btn btn-primary" onclick="bookRendezvous(' + data.doctor_id + ',\'' + data.date + '\');">Book</button> ' +
            '<small id="rendezvous-msg" style="color: #51b27e">Rendezvous booked successfully</small>' +
            '</div>'
        $('#book').html(newHtml);
        $('#rendezvous-msg').hide();
    });

    $('.close').click(function () {
        closeModal()
    });

    $(window).click(function (event) {
        if (event.target.id === 'Modal') {
            closeModal()
        }
    });

    const table = $('#doctor-table').DataTable({
        "responsive": true,
        "dom": 'Blfrtip',
        "lengthChange": false,
        select: true,
        "bProcessing": true,
        ajax: 'DoctorListForUser',
        order: [[7, 'asc']],
        "columnDefs": [
            {"type": "distance", targets: [7, 8]}
        ],
        buttons: [
            {
                extend: 'selected',
                text: 'Book Rendezvous <i class="fas fa-book-medical"></i>',
                name: 'rendezvous',
                className: 'btn btn-custom btn-custom-override btn-custom-user-rendezvous',
                action: function () {
                    const data = table.row({selected: true}).data();
                    showAvailableRendezvous(data)
                }
            }
        ],
        columns: [
            {"data": "specialty"},
            {"data": "name"},
            {"data": "info"},
            {"data": "city"},
            {"data": "address"},
            {"data": "telephone"},
            {"data": "email"},
            {
                "data": "dist", render: function (data) {
                    if (data === 'null') return data
                    return data + ' km'
                }
            },
            {
                "data": "dur", render: function (data) {
                    if (data === 'null') return data
                    return data + ' min'
                }
            }
        ]
    });

    $.fn.dataTableExt.oSort["distance-desc"] = function (x, y) {
        return customSort(x, y, false)
    };

    $.fn.dataTableExt.oSort["distance-asc"] = function (x, y) {
        return customSort(x, y, true)
    }

    function customSort(x, y, ascending) {
        let n1 = convert(x)
        let n2 = convert(y);

        if (n1 === n2) {
            return 0;
        } else if (isNaN(n1)) {
            return 1;
        } else if (isNaN(n2)) {
            return -1;
        } else if (ascending) {
            return n1 < n2 ? -1 : 1;
        } else {
            return n1 < n2 ? 1 : -1;
        }
    }

    function convert(s) {
        if (s.includes('km'))
            return parseFloat(s)
        else
            return parseInt(s)
    }

});

