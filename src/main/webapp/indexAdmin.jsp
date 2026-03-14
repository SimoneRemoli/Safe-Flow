<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.web.routex.domain.SessionAuthUtil" %>
<%
    HttpSession currentSession = request.getSession(false);
    String nomeAdmin = null;
    String cognomeAdmin = null;
    String ruoloAdmin = null;

    if (SessionAuthUtil.isLoggedIn(currentSession)) {
        nomeAdmin = (String) currentSession.getAttribute("nome");
        cognomeAdmin = (String) currentSession.getAttribute("cognome");
        Object ruoloObj = currentSession.getAttribute("ruolo");
        ruoloAdmin = ruoloObj != null ? ruoloObj.toString() : null;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Admin Hub</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
            --accent-2: #8dd8ff;
            --success: #89ffd1;
            --warning: #ffd38d;
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
                radial-gradient(circle at bottom center, rgba(67, 112, 255, 0.16), transparent 32%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            overflow-x: hidden;
            position: relative;
        }

        body::before {
            content: "";
            position: fixed;
            inset: 0;
            background-image:
                radial-gradient(circle, rgba(255, 255, 255, 0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(111, 247, 255, 0.42) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.16;
            pointer-events: none;
            animation: drift 18s linear infinite;
        }

        .shell {
            width: min(1240px, calc(100% - 32px));
            margin: 24px auto;
            min-height: calc(100vh - 48px);
            padding: 28px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            box-shadow: 0 32px 84px rgba(0, 0, 0, 0.4);
            backdrop-filter: blur(16px);
            position: relative;
            overflow: hidden;
        }

        .shell::after {
            content: "";
            position: absolute;
            width: 420px;
            height: 420px;
            right: -120px;
            top: -160px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.14), transparent 66%);
            filter: blur(12px);
        }

        .topbar,
        .hero,
        .quick-actions,
        .status-grid,
        .alert-panel {
            position: relative;
            z-index: 1;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .brand img {
            width: 72px;
        }

        .brand strong {
            display: block;
            font-size: 1.35rem;
        }

        .brand span {
            color: var(--muted);
            font-size: 0.94rem;
        }

        .nav-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .nav-actions a {
            text-decoration: none;
            color: var(--text);
            padding: 10px 16px;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.06);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .nav-actions a:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.42);
        }

        .hero {
            margin-top: 28px;
            display: grid;
            grid-template-columns: 1.08fr 0.92fr;
            gap: 28px;
            align-items: stretch;
        }

        .hero-copy,
        .hero-panel,
        .action-card,
        .status-card,
        .alert-panel {
            border-radius: 28px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            background: rgba(255, 255, 255, 0.04);
            backdrop-filter: blur(12px);
        }

        .hero-copy,
        .hero-panel,
        .action-card,
        .status-card,
        .alert-panel {
            padding: 24px;
        }

        .eyebrow {
            display: inline-flex;
            width: fit-content;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: rgba(111, 247, 255, 0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        .hero-copy h1 {
            margin: 18px 0 14px;
            font-size: clamp(2.8rem, 5vw, 5rem);
            line-height: 0.95;
        }

        .hero-copy p,
        .hero-panel p,
        .action-card p,
        .status-card span,
        .alert-panel {
            color: var(--muted);
            line-height: 1.75;
        }

        .admin-pill {
            margin-top: 22px;
            display: inline-flex;
            gap: 10px;
            align-items: center;
            padding: 12px 18px;
            border-radius: 999px;
            background: rgba(111, 247, 255, 0.08);
            border: 1px solid rgba(111, 247, 255, 0.16);
            color: #dffbff;
        }

        .hero-panel {
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.04), transparent),
                rgba(8, 24, 43, 0.9);
            border-color: rgba(111, 247, 255, 0.18);
        }

        .status-label {
            display: inline-flex;
            margin-bottom: 14px;
            padding: 8px 12px;
            border-radius: 999px;
            background: rgba(111, 247, 255, 0.08);
            border: 1px solid rgba(111, 247, 255, 0.16);
            color: var(--accent);
            font-size: 0.84rem;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        .signal-list {
            margin: 18px 0 0;
            padding: 0;
            list-style: none;
            display: grid;
            gap: 14px;
        }

        .signal-list li {
            display: flex;
            gap: 14px;
            align-items: flex-start;
            padding: 14px 16px;
            border-radius: 18px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .signal-list i {
            color: var(--accent);
            margin-top: 2px;
        }

        .quick-actions,
        .status-grid {
            display: grid;
            gap: 18px;
            margin-top: 22px;
        }

        .quick-actions {
            grid-template-columns: repeat(2, minmax(0, 1fr));
        }

        .status-grid {
            grid-template-columns: repeat(3, minmax(0, 1fr));
        }

        .action-card {
            position: relative;
            overflow: hidden;
        }

        .action-card::after {
            content: "";
            position: absolute;
            inset: auto -60px -70px auto;
            width: 180px;
            height: 180px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.14), transparent 70%);
        }

        .action-icon {
            width: 58px;
            height: 58px;
            border-radius: 18px;
            display: grid;
            place-items: center;
            font-size: 1.4rem;
            color: #04111f;
            background: linear-gradient(135deg, var(--accent), var(--success));
            box-shadow: 0 18px 36px rgba(111, 247, 255, 0.18);
        }

        .action-card h3 {
            margin: 18px 0 10px;
            font-size: 1.4rem;
        }

        .action-card a {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            color: #04111f;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.05em;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 20px 36px rgba(111, 247, 255, 0.2);
            transition: transform 0.25s ease, box-shadow 0.25s ease;
            position: relative;
            z-index: 1;
        }

        .action-card a:hover {
            transform: translateY(-3px);
            box-shadow: 0 24px 42px rgba(111, 247, 255, 0.28);
        }

        .status-card strong {
            display: block;
            font-size: 1.45rem;
            margin-bottom: 6px;
        }

        .alert-panel {
            margin-top: 22px;
            border-color: rgba(255, 211, 141, 0.18);
            background: rgba(255, 211, 141, 0.06);
            color: #f7e6c4;
        }

        .alert-panel i {
            color: var(--warning);
            margin-right: 10px;
        }

        @keyframes drift {
            from { transform: translateY(0); }
            to { transform: translateY(-150px); }
        }

        @media (max-width: 980px) {
            .hero,
            .quick-actions,
            .status-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 640px) {
            .shell {
                width: min(100% - 20px, 100%);
                margin: 10px auto;
                min-height: calc(100vh - 20px);
                padding: 18px;
                border-radius: 24px;
            }

            .hero-copy h1 {
                font-size: 2.4rem;
            }

            .nav-actions {
                width: 100%;
            }

            .nav-actions a {
                flex: 1;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<div class="shell">
    <header class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX logo">
            <div>
                <strong>RouteX Admin Hub</strong>
                <span>Governance, comunicazioni e osservabilita' della piattaforma</span>
            </div>
        </div>

        <nav class="nav-actions">
            <a href="indexAdmin.jsp"><i class="fas fa-home"></i> Home</a>
            <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-copy">
            <span class="eyebrow">Admin Access</span>
            <h1>Control deck per l'area amministrativa</h1>
            <p>
                L'admin ora usa lo stesso layout futuristico dell'area worker:
                accessi rapidi, pannelli glass e priorita' operative leggibili in una singola vista.
            </p>

            <div class="admin-pill">
                <i class="fas fa-user-shield"></i>
                <span>
                    <%= nomeAdmin != null ? nomeAdmin : "Admin" %>
                    <%= cognomeAdmin != null ? cognomeAdmin : "" %>
                    <% if (ruoloAdmin != null) { %>
                        | <%= ruoloAdmin %>
                    <% } %>
                </span>
            </div>
        </div>

        <aside class="hero-panel">
            <span class="status-label">Mission status</span>
            <h2>Centro di coordinamento sincronizzato</h2>
            <p>
                Da qui puoi distribuire comunicazioni ai worker e monitorare report, trend d'uso
                e statistiche senza uscire dal nuovo impianto visivo RouteX.
            </p>

            <ul class="signal-list">
                <li>
                    <i class="fas fa-bullhorn"></i>
                    <div>
                        <strong>Broadcast operativo</strong>
                        <div>Invia messaggi di coordinamento verso il personale della piattaforma.</div>
                    </div>
                </li>
                <li>
                    <i class="fas fa-chart-line"></i>
                    <div>
                        <strong>Analytics panel</strong>
                        <div>Analizza viaggi, distanza complessiva e tempi aggregati di utilizzo.</div>
                    </div>
                </li>
                <li>
                    <i class="fas fa-shield-alt"></i>
                    <div>
                        <strong>Secure governance</strong>
                        <div>Sessione amministrativa pronta per operazioni ad alto privilegio.</div>
                    </div>
                </li>
            </ul>
        </aside>
    </section>

    <section class="quick-actions">
        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-bullhorn"></i>
            </div>
            <h3>Invia comunicazione</h3>
            <p>
                Apri il composer per distribuire rapidamente aggiornamenti e istruzioni ai worker.
            </p>
            <a href="sendCommunicationn.jsp">
                Apri composer <i class="fas fa-arrow-right"></i>
            </a>
        </article>

        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-chart-area"></i>
            </div>
            <h3>Reports & statistics</h3>
            <p>
                Consulta i percorsi tracciati, i dati aggregati e la fotografia operativa della piattaforma.
            </p>
            <a href="PathInfoRAS">
                Apri report <i class="fas fa-arrow-right"></i>
            </a>
        </article>
    </section>

    <section class="status-grid">
        <article class="status-card">
            <strong>Unified UI</strong>
            <span>L'area admin condivide lo stesso linguaggio visuale del nuovo hub worker, senza pagine scollegate.</span>
        </article>
        <article class="status-card">
            <strong>Decision speed</strong>
            <span>Le azioni amministrative principali sono accessibili subito, senza passaggi intermedi superflui.</span>
        </article>
        <article class="status-card">
            <strong>Operational clarity</strong>
            <span>Metriche, messaggi e strumenti di coordinamento sono esposti con priorita' e gerarchia piu' nette.</span>
        </article>
    </section>

    <%
        String msg = (String) session.getAttribute("alertMessage");
        String successRegister = (String) session.getAttribute("successRegister");
        String infoMessage = msg != null && !msg.isEmpty() ? msg : successRegister;
        if (infoMessage != null && !infoMessage.isEmpty()) {
    %>
        <div class="alert-panel">
            <i class="fas fa-satellite-dish"></i>
            <%= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(infoMessage) %>
        </div>
    <%
            session.removeAttribute("alertMessage");
            session.removeAttribute("successRegister");
        }
    %>
</div>
</body>
</html>
