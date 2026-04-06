<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.web.routex.domain.SessionAuthUtil" %>
<%@ page import="it.web.routex.controller.applicativo.ReviewTravelerCommunicationsControllerApplicativo" %>
<%@ page import="it.web.routex.exception.DAOExceptionRemoli" %>
<%
    HttpSession currentSession = request.getSession(false);
    String nomeAdmin = null;
    String cognomeAdmin = null;
    String ruoloAdmin = null;
    int pendingTravelerReports = 0;

    if (SessionAuthUtil.isLoggedIn(currentSession)) {
        nomeAdmin = (String) currentSession.getAttribute("nome");
        cognomeAdmin = (String) currentSession.getAttribute("cognome");
        Object ruoloObj = currentSession.getAttribute("ruolo");
        ruoloAdmin = ruoloObj != null ? ruoloObj.toString() : null;

        try {
            ReviewTravelerCommunicationsControllerApplicativo reviewService =
                    new ReviewTravelerCommunicationsControllerApplicativo();
            pendingTravelerReports = reviewService.pendingMessages().size();
        } catch (DAOExceptionRemoli ignored) {
            pendingTravelerReports = 0;
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
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

        .notification-link {
            position: relative;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .notification-link.has-alert {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            border-color: transparent;
            box-shadow: 0 16px 28px rgba(111, 247, 255, 0.22);
        }

        .notification-badge {
            min-width: 22px;
            height: 22px;
            padding: 0 6px;
            border-radius: 999px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            background: rgba(4, 17, 31, 0.92);
            color: #f8fcff;
            font-size: 0.78rem;
            font-weight: 700;
            line-height: 1;
        }

        .hero {
            margin-top: 28px;
            display: block;
        }

        .hero-copy,
        .action-card,
        .status-card,
        .alert-panel {
            border-radius: 28px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            background: rgba(255, 255, 255, 0.04);
            backdrop-filter: blur(12px);
        }

        .hero-copy,
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

        .quick-actions,
        .status-grid {
            display: grid;
            gap: 18px;
            margin-top: 22px;
        }

        .quick-actions {
            grid-template-columns: repeat(3, minmax(0, 1fr));
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
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<div class="shell">
    <header class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX logo">
            <div>
                <strong>RouteX Admin Hub</strong>
                <span>Governance, communications, and platform observability</span>
            </div>
        </div>

        <nav class="nav-actions">
            <a href="adminHub"><i class="fas fa-home"></i> Home</a>
            <a href="reviewTravelerCommunications" class="notification-link <%= pendingTravelerReports > 0 ? "has-alert" : "" %>">
                <i class="fas fa-bell"></i> Notifications
                <% if (pendingTravelerReports > 0) { %>
                <span class="notification-badge"><%= pendingTravelerReports %></span>
                <% } %>
            </a>
            <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-copy">
            <span class="eyebrow">Admin Access</span>
            <h1>Control deck for the administrative area</h1>
            <p>
                The admin area now uses the same visual language as the updated worker hub:
                quick access, glass panels, and readable operational priorities in a single view.
            </p>
        </div>
    </section>

    <section class="quick-actions">
        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-bullhorn"></i>
            </div>
            <h3>Send communication</h3>
            <p>
                Open the composer to quickly distribute updates and instructions to workers.
            </p>
            <a href="adminReport">
                Open composer <i class="fas fa-arrow-right"></i>
            </a>
        </article>

        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-user-shield"></i>
            </div>
            <h3>Manage admins</h3>
            <p>
                Create new administrator accounts or remove existing ones directly from the platform control area.
            </p>
            <a href="manageAdmins">
                Open admin manager <i class="fas fa-arrow-right"></i>
            </a>
        </article>

        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-chart-area"></i>
            </div>
            <h3>Reports & statistics</h3>
            <p>
                Review tracked routes, aggregated data, and the operational picture of the platform.
            </p>
            <a href="PathInfoRAS">
                Open reports <i class="fas fa-arrow-right"></i>
            </a>
        </article>

        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-user-check"></i>
            </div>
            <h3>Approve traveler reports</h3>
            <p>
                Review pending traveler submissions and approve the messages that should become visible in the platform notifications.
            </p>
            <a href="reviewTravelerCommunications">
                Review reports <i class="fas fa-arrow-right"></i>
            </a>
        </article>
    </section>

    <section class="status-grid">
        <article class="status-card">
            <strong>Unified UI</strong>
            <span>The admin area shares the same visual language as the updated worker hub, without disconnected pages.</span>
        </article>
        <article class="status-card">
            <strong>Decision speed</strong>
            <span>The main administrative actions are immediately accessible, without unnecessary intermediate steps.</span>
        </article>
        <article class="status-card">
            <strong>Operational clarity</strong>
            <span>Metrics, messages, and coordination tools are exposed with clearer priority and hierarchy.</span>
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
