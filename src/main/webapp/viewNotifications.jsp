<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>RouteX - Notifiche</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/bootstrap/dist/css/bootstrap.css">

    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">

    <style>
        body { background-color: #f8f9fa; color: #212529; font-family: "Segoe UI", sans-serif; }
        .main-container { display: flex; width: 100%; min-height: 100vh; }
        .right-content { flex: 1; padding: 40px; background-color: #ffffff; }
        .button-container { display: flex; justify-content: flex-end; gap: 10px; margin-bottom: 20px; }
        .button-container a { background-color: #007bff; color: white; padding: 10px 15px; font-size: 16px; border-radius: 10px; text-decoration: none; transition: background-color 0.3s ease; }
        .button-container a:hover { background-color: #0056b3; }
        .table-responsive { background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); margin-bottom: 30px; }
        table.dataTable thead { background-color: #e9ecef; color: #212529; }
        table.dataTable tbody tr:nth-child(even) { background-color: #f1f1f1; }
        .dataTables_wrapper .dataTables_paginate { display: flex !important; justify-content: center; margin-top: 15px; }
        .dataTables_wrapper .dataTables_paginate a,
        .dataTables_wrapper .dataTables_paginate span { color: #212529 !important; background-color: #e2e6ea !important; border: 1px solid #ced4da !important; padding: 6px 12px; margin: 0 3px; border-radius: 5px; text-decoration: none; font-weight: bold; }
        .dataTables_wrapper .dataTables_paginate .current { background-color: #007bff !important; color: white !important; }
        .dataTables_wrapper .dataTables_paginate a:hover { background-color: #0056b3 !important; color: white !important; }
        .dataTables_wrapper .dataTables_filter input { background-color: #ffffff; border: 1px solid #ced4da; color: #212529; padding: 5px; border-radius: 4px; }
        .metro-logos { position: fixed; right: 20px; bottom: 20px; display: flex; flex-direction: column; align-items: center; gap: 15px; }
        .metro-logos i { font-size: 40px; color: #6c757d; }
    </style>
</head>

<body>
<div class="main-container">
    <div class="right-content">

        <!-- LOGIN / HOME -->
        <div class="button-container">
            <a href="dashboardWorker.jsp">Home</a>
            <a href="logout">Logout</a>
            <a href="addCommunicationWorker.jsp">Aggiungi una segnalazione</a>
        </div>

        <!-- TABELLA NOTIFICHE -->
        <div class="table-responsive">
            <h3>Lista Notifiche</h3>

            <form action="updateNotifications" method="post">

                <table id="GridNotifications" class="display table table-striped">
                    <thead>
                    <tr>
                        <th>Messaggio</th>
                        <th>Data</th>
                        <th>Risolta</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="r" items="${notifiche}">
                        <tr>
                            <td>${r.message}</td>
                            <td>
                                <fmt:formatDate value="${r.date}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td class="text-center">
                                <input type="checkbox"
                                       name="risolte"
                                       value="${r.date.time}|${fn:escapeXml(r.message)}"
                                       <c:if test="${r.risolto}">checked</c:if> />
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>

                <button type="submit" class="btn btn-success mt-3">
                    Salva modifiche
                </button>

            </form>
        </div>
    </div>
</div>

<!-- Icone metro -->
<div class="metro-logos">
    <i class="fas fa-subway"></i>
    <i class="fas fa-train"></i>
    <i class="fas fa-bus"></i>
    <i class="fas fa-map-marker-alt"></i>
</div>

<!-- jQuery + DataTables -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>

<script>
    $(document).ready(function () {
        $('#GridNotifications').DataTable({
            pageLength: 10,
            lengthChange: false,
            language: {
                paginate: { previous: "Precedente", next: "Successiva" },
                info: "Pagina _PAGE_ di _PAGES_",
                search: "Cerca:",
                zeroRecords: "Nessuna notifica trovata.",
                emptyTable: "Nessuna notifica trovata."
            }
        });
    });


</script>
<c:if test="${not empty sessionScope.solved}">
    <script>
        window.onload = function () {
            alert("${fn:escapeXml(sessionScope.solved)}");
        };
    </script>

    <c:remove var="solved" scope="session"/>
</c:if>

<%
    String msg = (String) session.getAttribute("alertMessage");
    if (msg != null && !msg.isEmpty()) {
%>

    <script>
        window.onload = function() {
            alert("<%= msg %>");
        };
    </script>

<%
    session.removeAttribute("alertMessage");
    }
%>

</body>
</html>