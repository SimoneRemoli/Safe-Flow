<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Warning</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
            --success: #89ffd1;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 22%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 85% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            display: grid;
            place-items: center;
            padding: 18px;
        }

        .warning-container {
            width: min(680px, 100%);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            border: 1px solid var(--line);
            border-radius: 30px;
            padding: 34px;
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
            text-align: center;
        }

        .warning-icon {
            font-size: 56px;
            color: var(--success);
            margin-bottom: 18px;
        }

        h1 {
            margin: 0 0 14px;
            font-size: 2rem;
        }

        .details {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 18px;
            padding: 18px;
            color: var(--muted);
            text-align: left;
            line-height: 1.7;
            margin-bottom: 24px;
            white-space: pre-wrap;
        }

        .btn-home {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
            transition: transform 0.25s ease;
        }

        .btn-home:hover {
            transform: translateY(-2px);
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<div class="warning-container">
    <i class="fas fa-check-circle warning-icon"></i>
    <h1><%= request.getAttribute("warningTitle") %></h1>
    <div class="details">
        <strong>Dettagli:</strong><br>
        <%= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(
                (String) request.getAttribute("warningMessage")) %>
    </div>
    <a href="workerHub" class="btn-home">
        <i class="fas fa-home"></i> Torna alla Home
    </a>
</div>
</body>
</html>
