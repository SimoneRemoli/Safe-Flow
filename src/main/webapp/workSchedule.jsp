<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.web.routex.bean.WorkerScheduleBean" %>

<%
    WorkerScheduleBean schedule = (WorkerScheduleBean) request.getAttribute("workerSchedule");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <title>Orario di lavoro</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
        body {
            background-color: #f5f6f8;
        }
        .card-custom {
            max-width: 500px;
            margin: 80px auto;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0,0,0,0.1);
        }
        .title {
            font-weight: 600;
        }
        .label {
            color: #6c757d;
            font-size: 0.9rem;
        }
    </style>
</head>

<body>

<div class="card card-custom">
    <div class="card-body">

        <h4 class="card-title title mb-4 text-center">
            Orario di lavoro
        </h4>

        <% if (schedule != null) { %>

            <div class="mb-3">
                <div class="label">Ora di inizio</div>
                <div class="fs-5">
                    <%= schedule.getOraInizio() %>:00
                </div>
            </div>

            <div class="mb-3">
                <div class="label">Ora di fine</div>
                <div class="fs-5">
                    <%= schedule.getOraFine() %>:00
                </div>
            </div>

            <div class="mb-3">
                <div class="label">Luogo di lavoro</div>
                <div class="fs-5">
                    <%= schedule.getLuogoDiLavoro() %>
                </div>
            </div>

            <div class="mb-4">
                <div class="label">Durata turno</div>
                <div class="fs-5">
                    <%= schedule.getDurataTurno() %> ore
                </div>
            </div>

            <div class="text-center">
                <a href="<%= request.getContextPath() %>/dashboardWorker.jsp"
                   class="btn btn-primary">
                    Torna alla dashboard
                </a>
            </div>

        <% } else { %>

            <div class="alert alert-danger text-center">
                Impossibile recuperare l'orario di lavoro.
            </div>

        <% } %>

    </div>
</div>

</body>
</html>
