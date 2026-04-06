<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer oraInizio = (Integer) request.getAttribute("oraInizio");
    Integer oraFine = (Integer) request.getAttribute("oraFine");
    String luogoDiLavoro = (String) request.getAttribute("luogoDiLavoro");
    Integer durataTurno = (Integer) request.getAttribute("durataTurno");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Orario di lavoro</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
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

        .panel {
            width: min(780px, 100%);
            padding: 30px;
            border-radius: 30px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
            backdrop-filter: blur(16px);
        }

        .eyebrow {
            display: inline-flex;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: rgba(111, 247, 255, 0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        h1 {
            margin: 16px 0 10px;
            font-size: clamp(2.2rem, 4vw, 3.8rem);
            line-height: 0.96;
        }

        .subtitle {
            margin: 0 0 24px;
            color: var(--muted);
            line-height: 1.75;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px;
        }

        .metric {
            padding: 20px;
            border-radius: 24px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .metric-label {
            color: var(--muted);
            font-size: 0.88rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            margin-bottom: 10px;
        }

        .metric-value {
            font-size: 1.9rem;
            font-weight: 700;
        }

        .metric-wide {
            grid-column: 1 / -1;
        }

        .actions {
            margin-top: 24px;
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .actions a {
            text-decoration: none;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            transition: transform 0.25s ease;
        }

        .primary {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
        }

        .secondary {
            color: var(--text);
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }

        .actions a:hover {
            transform: translateY(-2px);
        }

        @media (max-width: 640px) {
            .panel {
                padding: 22px;
                border-radius: 24px;
            }

            .grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<div class="panel">
    <span class="eyebrow">Shift overview</span>
    <h1>Orario di lavoro</h1>
    <p class="subtitle">
        Visualizza i dettagli del turno assegnato all'interno dello stesso ambiente visuale futuristico del worker hub.
    </p>

    <div class="grid">
        <div class="metric">
            <div class="metric-label">Ora di inizio</div>
            <div class="metric-value"><%= oraInizio %>:00</div>
        </div>

        <div class="metric">
            <div class="metric-label">Ora di fine</div>
            <div class="metric-value"><%= oraFine %>:00</div>
        </div>

        <div class="metric metric-wide">
            <div class="metric-label">Luogo di lavoro</div>
            <div class="metric-value"><%= luogoDiLavoro %></div>
        </div>

        <div class="metric metric-wide">
            <div class="metric-label">Durata del turno</div>
            <div class="metric-value"><%= durataTurno %> ore</div>
        </div>
    </div>

    <div class="actions">
        <a href="<%= request.getContextPath() %>/workerHub" class="primary">Torna alla dashboard</a>
        <a href="<%= request.getContextPath() %>/logout" class="secondary">Logout</a>
    </div>
</div>
</body>
</html>
