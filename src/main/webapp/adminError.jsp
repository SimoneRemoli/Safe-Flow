<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Admin Error</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        :root {
            --line: #dbe7e1;
            --text: #17231d;
            --muted: #607267;
            --danger: #b4233f;
            --danger-soft: #fff2f4;
            --accent: #1f6b4d;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
            background: #ffffff !important;
            padding: 94px 18px 32px !important;
        }

        .error-container {
            width: min(760px, 100%);
            margin: 0 auto;
            background: #ffffff !important;
            border: 1px solid var(--line);
            border-radius: 14px;
            padding: 30px;
            box-shadow: none !important;
            text-align: left;
        }

        .error-icon {
            display: inline-grid;
            place-items: center;
            width: 58px;
            height: 58px;
            border-radius: 14px;
            background: var(--danger-soft);
            border: 1px solid #ffd5dc;
            font-size: 26px;
            color: var(--danger);
            margin-bottom: 20px;
        }

        h1 {
            margin: 0 0 14px;
            font-size: clamp(1.85rem, 3vw, 2.35rem);
            line-height: 1.05;
            color: var(--text);
        }

        p {
            color: var(--muted);
            line-height: 1.7;
        }

        .details {
            background: #fbfdfc;
            border: 1px solid var(--line);
            border-radius: 10px;
            padding: 18px;
            color: var(--muted);
            text-align: left;
            line-height: 1.7;
            margin: 20px 0 24px;
            white-space: pre-wrap;
        }

        .btn-home {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            padding: 12px 16px;
            border-radius: 8px;
            font-weight: 700;
            color: #ffffff;
            background: var(--accent);
            border: 1px solid var(--accent);
            box-shadow: none;
            transition: transform 0.2s ease, background 0.2s ease;
        }

        .btn-home:hover {
            transform: translateY(-2px);
            background: #18583f;
        }
    </style>
</head>
<body class="admin-error-page">
<%@ include file="/header.jspf" %>
<%
    String errore = (String) request.getAttribute("errore");
%>
<div class="error-container">
    <i class="fas fa-exclamation-triangle error-icon"></i>
    <h1>Admin operation failed</h1>

    <% if (errore != null) { %>
        <p>An error occurred during an administrative operation.</p>
        <div class="details">
            <strong>Details:</strong><br>
            <%= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(errore) %>
        </div>
    <% } else { %>
        <p>An unexpected administrative error occurred. Please try again later.</p>
    <% } %>

    <a href="adminHub" class="btn-home">
        <i class="fas fa-home"></i> Back to Admin Home
    </a>
</div>
</body>
</html>
