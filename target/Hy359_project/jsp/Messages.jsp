<%@ page import="database.tables.EditDoctorTable" %>
<%@ page import="database.tables.EditSimpleUserTable" %>
<%@ page import="database.tables.EditMessageTable" %>
<%@ page import="database.tables.EditConversationTable" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Conversation" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="model.Message" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %>
<%--
  Created by IntelliJ IDEA.
  User: joe
  Date: 1/25/2022
  Time: 9:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Messages</title>
</head>
<body>
<%
    EditDoctorTable edt = new EditDoctorTable();
    EditSimpleUserTable eut = new EditSimpleUserTable();
    EditConversationTable ect = new EditConversationTable();
    EditMessageTable emt = new EditMessageTable();

    String currentUserName = request.getSession().getAttribute("loggedIn").toString();
    String currentUserType = request.getSession().getAttribute("userType").toString();
    int currentUserId = 0;
    int receiver_id;
    ArrayList<Conversation> conversations;
    ArrayList<String> convNames = new ArrayList<>();
    ArrayList<ArrayList<Message>> messages = new ArrayList<>();
    try {
        if (currentUserType.equals("user"))
            currentUserId = eut.databaseGetSimpleUserId(currentUserName);
        else
            currentUserId = edt.databaseGetDoctorId(currentUserName);

        conversations = ect.databaseGetConversations(currentUserType + "_id", currentUserId);
        System.out.println("hello");
        convNames.add("System");
        messages.add(emt.databaseGetSystemMessages(currentUserType, currentUserId));
        for (Conversation conv : conversations) {
            if (currentUserType.equals("user")) {
                JSONObject jo = new JSONObject(edt.databaseGetDoctor(conv.getDoctor_id()));
                convNames.add(jo.getString("firstname") + " " + jo.getString("lastname"));
            } else {
                String name = eut.databaseGetSimpleUserName(conv.getUser_id());
                convNames.add(name);
            }
            messages.add(emt.databaseToMessage(conv.getUser_id(), conv.getDoctor_id()));
        }

    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }


%>

<div class="container">
    <div class="row">
        <div id="conversations-container" class="col-md-4">
            <div class="list-group" id="list-tab" role="tablist">
                <% for (int i = 0; i < convNames.size(); i++) { %>
                <a class="list-group-item list-group-item-action show-message" id="list-<%=i%>-list" data-toggle="list"
                   href="#list-<%=i%>"
                   role="tab" aria-controls="<%=i%>">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><%=convNames.get(i)%>
                        </h5>
                        <%
                            Message lastMessage = messages.get(i).get(messages.get(i).size() - 1);
                            String time = null;
                            try {
                                time = getTimeAgo(lastMessage.getDate_time());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String seen = "";
                            String bold = "";
                            if (!lastMessage.getSender().equals(currentUserType)) {
                                seen = (lastMessage.getSeen() == 0) ? "new messages" : "";
                                bold = (lastMessage.getSeen() == 0) ? "font-weight-bold" : "text-muted";
                            }
                            int id = (currentUserType.equals("user")) ? lastMessage.getDoctor_id() : lastMessage.getUser_id();
                        %>
                        <small><%=time%>
                        </small>
                    </div>
                    <p class="mb-1 <%=bold%>">"<%=lastMessage.getMessage()%>"</p>
                    <span class="badge badge-primary badge-pill"><%=seen%></span>
                    <p id="other" hidden><%=id%>
                    </p>
                </a>
                <%}%>
            </div>
        </div>
        <div id="messages-container" class="col-md-8">
            <div class="tab-content" id="nav-tabContent">
                <% for (int i = 0; i < convNames.size(); i++) { %>
                <div class="tab-pane fade show" id="list-<%=i%>" role="tabpanel" aria-labelledby="list-<%=i%>-list">
                    <h4 style="font-weight: bold"><%=convNames.get(i)%>
                    </h4>
                    <%
                        for (int j = 0; j < messages.get(i).size(); j++) {
                            Message message = messages.get(i).get(j);
                            String position = (message.getSender().equals(currentUserType)) ? "right" : "left";
                            String color = (message.getSender().equals(currentUserType)) ? "" : "darker";
                            String time = null;
                            try {
                                time = getTimeAgo(message.getDate_time());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                    %>
                    <div class="chat-container container <%=color%>">
                        <p style="float: <%=position%>"><%=messages.get(i).get(j).getMessage()%>
                        </p>
                        <span class="time-<%=position%>">sent</span>
                        <span class="time-<%=position%>"><%=time%></span>
                    </div>
                    <%
                        }
                        if (!convNames.get(i).equals("System")) {

                            if (currentUserType.equals("user")) {
                                receiver_id = messages.get(i).get(0).getDoctor_id();
                            } else {
                                receiver_id = messages.get(i).get(0).getUser_id();
                            }
                    %>
                    <div class="chat-wrapper">
                        <div class="message-wrapper" style="width: 100%">
                            <div id="message-text-<%=receiver_id%>" class="message-text" contentEditable
                                 style="width: 90%"></div>
                            <button class="btn btn-custom btn-custom-send" type="button"><i class="far fa-paper-plane"
                                                                                            onclick="sendMessage(<%=currentUserId%>,<%=receiver_id%>)"></i>
                            </button>
                        </div>
                    </div>
                    <%}%>
                </div>
                <%}%>
            </div>
        </div>
    </div>
</div>
<script>

    function sendMessage(sender_id, receiver_id) {
        const data = {
            'sender_id': sender_id,
            'receiver_id': receiver_id,
            'message': $('#message-text-' + receiver_id).html()
        }
        const xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                $('#main-container').load('jsp/Messages.jsp')
            } else if (xhr.status !== 200) {

            }
        };
        xhr.open('POST', 'SendMessage');
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhr.send(JSON.stringify(data));
    }

    $(document).ready(function () {

        $('#messages-container').hide();
        $('.show-message').click(function () {
            $('#messages-container').show();
            const badge = $(this).children()[2]
            const latestMessage = $(this).children()[1]
            const id = $(this).children()[3]
            if ($(badge).html() === 'new messages') {
                const data = {'id': $(id).html()}
                const xhr = new XMLHttpRequest();
                xhr.onload = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        $(badge).empty()
                        $(latestMessage).removeClass('font-weight-bold')
                        $(latestMessage).addClass('text-muted')
                        updateNotifications();
                    } else if (xhr.status !== 200) {

                    }
                };
                xhr.open('POST', 'MessageSeen');
                xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhr.send(JSON.stringify(data));
            }
        })
    });
</script>

</body>
</html>
<%!
    private String formatTime(long time, String str) {
        if (time > 1) {
            return time + " " + str + "s ago";
        } else
            return time + " " + str + " ago";
    }

    private String getTimeAgo(String date_time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date past = format.parse(date_time);
        Date now = new Date();
        double time = (double) ((now.getTime() - past.getTime()) / 1000);
        double interval = time / 31536000;
        if (interval > 1) {
            return formatTime(Math.round(interval), "year");
        }
        interval = time / 2592000;
        if (interval > 1) {
            return formatTime(Math.round(interval), "month");
        }
        interval = time / 86400;
        if (interval > 1) {
            return formatTime(Math.round(interval), "day");
        }
        interval = time / 3600;
        if (interval > 1) {
            return formatTime(Math.round(interval), "hour");
        }
        interval = time / 60;
        if (interval > 1) {
            return formatTime(Math.round(interval), "minute");
        }
        return formatTime(Math.round(interval), "second");
    }
%>