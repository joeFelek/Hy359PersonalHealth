/**
 *
 */

$(document).ready(function () {

    function doCancel(data) {
        if (data.status !== 'selected')
            return;
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                rendezvousTable.ajax.reload(null, false);
            } else if (xhr.status !== 200) {
                alert(xhr.status)
            }
        };
        xhr.open('POST', 'CancelRendezvous');
        xhr.send(JSON.stringify(data));
    }

    function doMessage(data) {
        if (data.status !== 'done')
            return;
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                $('#main-container').load('jsp/Messages.jsp')
            } else if (xhr.status !== 200) {
                alert(xhr.status)
            }
        };
        xhr.open('POST', 'StartConversation');
        xhr.send(JSON.stringify(data));
    }

    let rendezvousTable = $('#user-rendezvous-table').DataTable({
        "responsive": true,
        "dom": 'Blfrtip',
        "lengthChange": false,
        buttons: [{
            extend: 'selected',
            text: '<i class="far fa-comment-dots"></i> Send message',
            name: 'message',
            className: 'btn btn-custom btn-custom-user-rendezvous',
            action: function () {
                const data = rendezvousTable.row({selected: true}).data();
                doMessage(data)
            }
        }, {
            extend: 'selected',
            text: '<i class="fas fa-times"></i> Cancel',
            name: 'cancel',
            className: 'btn btn-custom btn-custom-delete btn-custom-override',
            action: function () {
                const data = rendezvousTable.row({selected: true}).data();
                doCancel(data)
            }
        }],
        select: true,
        order: [[3, 'asc']],
        "bProcessing": true,
        "ajax": "UserListRendezvous",
        columns: [
            {"data": "name"},
            {"data": "address"},
            {"data": "email"},
            {"data": "date"},
            {"data": "price", render: $.fn.dataTable.render.number(',', '.', 0, '€')},
            {"data": "status"}
        ]
    })
});