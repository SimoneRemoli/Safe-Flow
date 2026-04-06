<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Error</title>

    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

    <style>
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 24px;
            background: linear-gradient(180deg, #f8fbff 0%, #f3f7fb 100%);
            color: #0f172a;
            font-family: "Segoe UI", sans-serif;
        }

        .error-container {
            width: min(560px, 100%);
            background: rgba(255, 255, 255, 0.97);
            padding: 38px 34px;
            border-radius: 24px;
            border: 1px solid rgba(15, 23, 42, 0.08);
            box-shadow: 0 22px 54px rgba(15, 23, 42, 0.08);
            text-align: center;
        }

        .error-icon {
            font-size: 50px;
            color: #dc2626;
            margin-bottom: 18px;
        }

        h1 {
            color: #0f172a;
            font-size: 1.9rem;
            margin: 0 0 10px;
            letter-spacing: -0.03em;
        }

        p {
            font-size: 1rem;
            margin: 0 0 26px;
            color: #475569;
            line-height: 1.7;
        }

        .btn-home {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            background: linear-gradient(90deg, #2563eb, #1d4ed8);
            color: #ffffff;
            border: none;
            padding: 12px 18px;
            border-radius: 999px;
            font-size: 0.98rem;
            font-weight: 700;
            text-decoration: none;
            box-shadow: 0 14px 28px rgba(37, 99, 235, 0.16);
        }

        .details {
            background: #f8fafc;
            border: 1px solid #dbe2ea;
            border-radius: 16px;
            padding: 14px 16px;
            font-size: 0.95rem;
            color: #334155;
            text-align: left;
            margin-bottom: 22px;
            white-space: pre-wrap;
            line-height: 1.6;
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>

<body>
<%@ include file="header.jspf" %>

<div class="error-container">
    <i class="fas fa-exclamation-triangle error-icon"></i>
    <h1>Something went wrong!</h1>

    <%
        String errore = (String) request.getAttribute("errore");
    %>

    <% if (errore != null) { %>
        <p>We encountered an unexpected error:</p>

        <div class="details">
            <strong>Details:</strong><br>
            <%= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(errore) %>
        </div>
    <% } else { %>
        <p>We encountered an unexpected error. Please try again later.</p>
    <% } %>

    <a href="indexLogged.jsp" class="btn-home"><i class="fas fa-home"></i> Back to Home</a>
</div>

</body>
</html>
