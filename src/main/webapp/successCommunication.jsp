<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Success</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --page-bg: linear-gradient(180deg, #f8fbff 0%, #f3f7fb 100%);
            --surface: rgba(255, 255, 255, 0.96);
            --line: rgba(15, 23, 42, 0.08);
            --text: #0f172a;
            --muted: #475569;
            --accent: #2563eb;
            --success: #16a34a;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background: var(--page-bg);
            display: grid;
            place-items: center;
            padding: 24px;
        }

        .success-container {
            width: min(680px, 100%);
            background: var(--surface);
            border: 1px solid var(--line);
            border-radius: 28px;
            padding: 38px 34px;
            box-shadow: 0 22px 54px rgba(15, 23, 42, 0.08);
            text-align: center;
        }

        .success-icon {
            font-size: 52px;
            color: var(--success);
            margin-bottom: 18px;
        }

        h1 {
            margin: 0 0 12px;
            color: #0f172a;
            font-size: 2rem;
            letter-spacing: -0.03em;
        }

        p {
            color: var(--muted);
            line-height: 1.7;
            margin: 0 auto 28px;
            max-width: 520px;
        }

        .btn-home {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            color: #ffffff;
            background: linear-gradient(90deg, #2563eb, #1d4ed8);
            box-shadow: 0 14px 28px rgba(37, 99, 235, 0.16);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .btn-home:hover {
            transform: translateY(-2px);
            box-shadow: 0 18px 32px rgba(37, 99, 235, 0.22);
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%
    String successTitle = (String) request.getAttribute("successTitle");
    String successMessage = (String) request.getAttribute("successMessage");
    String successHomeTargetAttr = (String) request.getAttribute("successHomeTarget");
    String successHomeLabelAttr = (String) request.getAttribute("successHomeLabel");
    HttpSession successSession = request.getSession(false);
    String successRole = successSession != null && successSession.getAttribute("ruolo") != null
            ? successSession.getAttribute("ruolo").toString()
            : "";
    String successHomeTarget = successHomeTargetAttr != null ? successHomeTargetAttr : "index.jsp";
    String successHomeLabel = successHomeLabelAttr != null ? successHomeLabelAttr : "Back to Home";

    if (successHomeTargetAttr == null && "ADMIN".equalsIgnoreCase(successRole)) {
        successHomeTarget = "adminHub";
    } else if (successHomeTargetAttr == null && "TRAVELER".equalsIgnoreCase(successRole)) {
        successHomeTarget = "travelerHome";
    }
%>
<div class="success-container">
    <i class="fas fa-check-circle success-icon"></i>
    <h1><%= successTitle %></h1>
    <p><%= successMessage %></p>
    <a href="<%= successHomeTarget %>" class="btn-home">
        <i class="fas fa-home"></i> <%= successHomeLabel %>
    </a>
</div>
</body>
</html>
