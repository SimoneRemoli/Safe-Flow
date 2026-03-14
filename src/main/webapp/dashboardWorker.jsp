<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="it.web.routex.domain.SessionAuthUtil" %>
<%
    HttpSession currentSession = request.getSession(false);
    String nomeWorker = null;
    String cognomeWorker = null;
    String ruoloWorker = null;

    if (SessionAuthUtil.isLoggedIn(currentSession)) {
        nomeWorker = (String) currentSession.getAttribute("nome");
        cognomeWorker = (String) currentSession.getAttribute("cognome");
        Object ruoloObj = currentSession.getAttribute("ruolo");
        ruoloWorker = ruoloObj != null ? ruoloObj.toString() : null;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Worker Hub</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
            --panel-soft: rgba(255, 255, 255, 0.05);
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
            --accent-2: #8dd8ff;
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
        .status-grid {
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
            grid-template-columns: 1.1fr 0.9fr;
            gap: 28px;
            align-items: stretch;
        }

        .hero-copy,
        .hero-panel,
        .action-card,
        .status-card {
            border-radius: 28px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            background: rgba(255, 255, 255, 0.04);
            backdrop-filter: blur(12px);
        }

        .hero-copy {
            padding: 28px;
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

        .hero-copy p {
            margin: 0;
            color: var(--muted);
            line-height: 1.8;
            font-size: 1.02rem;
            max-width: 680px;
        }

        .worker-pill {
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
            padding: 24px;
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.04), transparent),
                rgba(8, 24, 43, 0.9);
            border-color: rgba(111, 247, 255, 0.18);
            position: relative;
            overflow: hidden;
        }

        .hero-panel::before {
            content: "";
            position: absolute;
            inset: 18px;
            border-radius: 24px;
            border: 1px solid rgba(111, 247, 255, 0.15);
            pointer-events: none;
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

        .hero-panel h2 {
            margin: 0 0 12px;
            font-size: 1.6rem;
        }

        .hero-panel p {
            margin: 0 0 18px;
            color: var(--muted);
            line-height: 1.7;
        }

        .signal-list {
            margin: 0;
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

        .action-card,
        .status-card {
            padding: 22px;
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

        .action-card p {
            margin: 0 0 18px;
            color: var(--muted);
            line-height: 1.7;
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

        .status-grid {
            grid-template-columns: repeat(3, minmax(0, 1fr));
        }

        .status-card strong {
            display: block;
            font-size: 1.45rem;
            margin-bottom: 6px;
        }

        .status-card span {
            color: var(--muted);
            line-height: 1.65;
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
                <strong>RouteX Worker Hub</strong>
                <span>Operativita' in tempo reale per il presidio della rete</span>
            </div>
        </div>

        <nav class="nav-actions">
            <a href="dashboardWorker.jsp"><i class="fas fa-home"></i> Home</a>
            <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-copy">
            <span class="eyebrow">Worker Access</span>
            <h1>Control room per il personale RouteX</h1>
            <p>
                L'accesso worker ora usa lo stesso linguaggio visuale della versione futuristica:
                pannelli operativi, stato della sessione e ingressi rapidi alle funzioni essenziali.
            </p>

            <div class="worker-pill">
                <i class="fas fa-user-astronaut"></i>
                <span>
                    <%= nomeWorker != null ? nomeWorker : "Operatore" %>
                    <%= cognomeWorker != null ? cognomeWorker : "" %>
                    <% if (ruoloWorker != null) { %>
                        | <%= ruoloWorker %>
                    <% } %>
                </span>
            </div>
        </div>

        <aside class="hero-panel">
            <span class="status-label">Mission status</span>
            <h2>Nodo operativo sincronizzato</h2>
            <p>
                Dal pannello puoi controllare notifiche, monitorare il turno attivo e
                rientrare rapidamente nella dashboard senza passare da layout legacy.
            </p>

            <ul class="signal-list">
                <li>
                    <i class="fas fa-bell"></i>
                    <div>
                        <strong>Alert management</strong>
                        <div>Consulta e aggiorna le segnalazioni ricevute dal sistema.</div>
                    </div>
                </li>
                <li>
                    <i class="fas fa-clock"></i>
                    <div>
                        <strong>Shift overview</strong>
                        <div>Visualizza orario di inizio, fine turno e sede assegnata.</div>
                    </div>
                </li>
                <li>
                    <i class="fas fa-shield-alt"></i>
                    <div>
                        <strong>Secure session</strong>
                        <div>Sessione autenticata pronta per operazioni worker dedicate.</div>
                    </div>
                </li>
            </ul>
        </aside>
    </section>

    <section class="quick-actions">
        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-bell"></i>
            </div>
            <h3>Notifiche di sistema</h3>
            <p>
                Apri la coda delle notifiche, marca quelle risolte e mantieni allineato il flusso operativo.
            </p>
            <a href="viewNotifications">
                Apri notifiche <i class="fas fa-arrow-right"></i>
            </a>
        </article>

        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-business-time"></i>
            </div>
            <h3>Orario di lavoro</h3>
            <p>
                Controlla rapidamente fascia oraria, durata del turno e luogo di servizio assegnato.
            </p>
            <a href="viewWorkSchedule">
                Apri orario <i class="fas fa-arrow-right"></i>
            </a>
        </article>
    </section>

    <section class="status-grid">
        <article class="status-card">
            <strong>Realtime UI</strong>
            <span>Il worker autenticato resta nello stesso ecosistema visuale glass e neon della nuova esperienza RouteX.</span>
        </article>
        <article class="status-card">
            <strong>Fast access</strong>
            <span>Le azioni principali sono esposte in prima vista, senza menu intermedi o schermate datate.</span>
        </article>
        <article class="status-card">
            <strong>Operational focus</strong>
            <span>Il layout privilegia leggibilita', stato della sessione e priorita' delle attivita' di presidio.</span>
        </article>
    </section>
</div>
</body>
</html>
