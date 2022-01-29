$(document).ready(function () {

    function doDone(data) {
        if (data.status !== 'selected')
            return;
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                table.ajax.reload(null, false);
            } else if (xhr.status !== 200) {
                alert(xhr.status)
            }
        };
        xhr.open('POST', 'DoneRendezvous');
        xhr.send(JSON.stringify(data));
    }

    function doCancel(data) {
        // set status to cancelled
        if (data.status !== 'selected' && data.status !== 'free')
            return;
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                table.ajax.reload(null, false);
            } else if (xhr.status !== 200) {
                alert(xhr.status)
            }
        };
        xhr.open('POST', 'CancelRendezvous');
        xhr.send(JSON.stringify(data));
    }

    const table = $('#example').DataTable({
        "responsive": true,
        "dom": 'Blfrtip',
        "lengthChange": false,
        select: true,
        "bProcessing": true,
        buttons: [
            {
                text: '<i class="fas fa-plus"></i> New',
                name: 'new',
                className: 'btn btn-custom btn-custom-override',
                action: function () {
                    $('#Modal').css('display', 'block');
                }
            }, {
                extend: 'selected',
                text: '<i class="fas fa-check"></i> Done',
                name: 'done',
                className: 'btn btn-custom btn-custom-override',
                action: function () {
                    const data = table.row({selected: true}).data();
                    doDone(data)
                }
            }, {
                extend: 'selected',
                text: '<i class="fas fa-times"></i> Cancel',
                name: 'cancel',
                className: 'btn btn-custom btn-custom-delete btn-custom-override',
                action: function () {
                    const data = table.row({selected: true}).data();
                    doCancel(data)
                }
            }, {
                extend: 'pdf',
                text: '<i class="far fa-file-pdf"></i> Export',
                name: 'pdf',
                className: 'btn btn-custom btn-custom-override'
            }
        ],
        ajax: 'DoctorRendezvous',
        order: [[2, 'asc']],

        columns: [
            {
                data: null,
                defaultContent: '',
                className: 'select-checkbox',
                orderable: false
            },
            {"data": "user_name"},
            {"data": "date"},
            {"data": "price", render: $.fn.dataTable.render.number(',', '.', 0, '€')},
            {"data": "user_info"},
            {"data": "status"}
        ]
    });


    $('.close').click(function () {
        $('#Modal').css('display', 'none');
    });

    $(window).click(function (event) {
        if (event.target.id === 'Modal') {
            $('#Modal').css('display', 'none');
        }
    });

    $(function () {
        const dtToday = new Date();

        let month = dtToday.getMonth() + 1;
        let day = dtToday.getDate();
        const year = dtToday.getFullYear();

        if (month < 10)
            month = '0' + month.toString();
        if (day < 10)
            day = '0' + day.toString();

        const maxDate = year + '-' + month + '-' + day;
        $('#date').attr('min', maxDate);
    });

    $('#rendezvous-form').submit(function () {
        let myForm = document.getElementById('rendezvous-form');
        let formData = new FormData(myForm);
        const data = {};
        formData.forEach((value, key) => data[key] = value);
        let jsonData = JSON.stringify(data);

        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                $('#Modal').css('display', 'none');
                table.ajax.reload(null, false);
            } else if (xhr.status !== 200) {
                alert('Request failed. Returned status of ' + xhr.status);
            }
        };
        xhr.open('POST', 'NewRendezvous');
        xhr.setRequestHeader("Content-type", "application/json")
        xhr.send(jsonData);
        return false;
    });
});
