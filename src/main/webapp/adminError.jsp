<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Admin Error</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(255, 128, 128, 0.22);
            --text: #ecf7ff;
            --muted: #c3ced8;
            --danger: #ff7f9f;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 18% 22%, rgba(255, 127, 159, 0.18), transparent 24%),
                radial-gradient(circle at 82% 18%, rgba(255, 166, 117, 0.16), transparent 22%),
                linear-gradient(135deg, #140912, #24111e 58%, #040913);
            display: grid;
            place-items: center;
            padding: 18px;
        }

        .error-container {
            width: min(700px, 100%);
            background: linear-gradient(180deg, rgba(29, 10, 19, 0.86), rgba(14, 8, 15, 0.94));
            border: 1px solid var(--line);
            border-radius: 30px;
            padding: 34px;
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
            text-align: center;
        }

        .error-icon {
            font-size: 56px;
            color: var(--danger);
            margin-bottom: 18px;
        }

        h1 {
            margin: 0 0 14px;
            font-size: 2rem;
            color: #ffd6df;
        }

        p {
            color: var(--muted);
            line-height: 1.7;
        }

        .details {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 18px;
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
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            color: #140912;
            background: linear-gradient(90deg, #ff7f9f, #ffc08d);
            box-shadow: 0 18px 32px rgba(255, 127, 159, 0.2);
            transition: transform 0.25s ease;
        }

        .btn-home:hover {
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
<%
    String errore = (String) request.getAttribute("errore");
%>
<div class="error-container">
    <i class="fas fa-exclamation-triangle error-icon"></i>
    <h1>Admin operation failed</h1>

    <% if (errore != null) { %>
        <p>Si e' verificato un errore durante un'operazione amministrativa.</p>
        <div class="details">
            <strong>Details:</strong><br>
            <%= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(errore) %>
        </div>
    <% } else { %>
        <p>Si e' verificato un errore amministrativo inatteso. Riprova piu' tardi.</p>
    <% } %>

    <a href="indexAdmin.jsp" class="btn-home">
        <i class="fas fa-home"></i> Back to Admin Home
    </a>
</div>
</body>
</html>
