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
            display: block;
        }

        .hero-copy,
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
<%@ include file="header.jspf" %>
<div class="shell">
    <header class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX logo">
            <div>
                <strong>RouteX Worker Hub</strong>
                <span>Real-time operations for metro network support</span>
            </div>
        </div>

        <nav class="nav-actions">
            <a href="dashboardWorker.jsp"><i class="fas fa-home"></i> Home</a>
            <a href="viewNotifications"><i class="fas fa-bell"></i> Notifications</a>
            <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </header>

    <section class="hero">
        <div class="hero-copy">
            <span class="eyebrow">Worker Access</span>
            <h1>Control room for RouteX staff<br>and anti-pickpocket operations</h1>
            <p>
                RouteX stands against pickpockets. If you notice any pickpocket activity, it should be reported immediately.
                The worker access now uses the same visual language as the updated RouteX experience, with operational panels,
                session awareness, and quick entry points to essential tools.
            </p>
        </div>
    </section>

    <section class="quick-actions">
        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-bell"></i>
            </div>
            <h3>System notifications</h3>
            <p>
                Open the notification queue, mark resolved items, and keep the operational flow aligned.
            </p>
            <a href="viewNotifications">
                Open notifications <i class="fas fa-arrow-right"></i>
            </a>
        </article>

        <article class="action-card">
            <div class="action-icon">
                <i class="fas fa-business-time"></i>
            </div>
            <h3>Work schedule</h3>
            <p>
                Quickly check your time slot, shift duration, and assigned work location.
            </p>
            <a href="viewWorkSchedule">
                Open schedule <i class="fas fa-arrow-right"></i>
            </a>
        </article>
    </section>

    <section class="status-grid">
        <article class="status-card">
            <strong>Realtime UI</strong>
            <span>The authenticated worker stays inside the same clean RouteX interface used across the updated experience.</span>
        </article>
        <article class="status-card">
            <strong>Fast access</strong>
            <span>The main actions are visible at first glance, without intermediate menus or outdated screens.</span>
        </article>
        <article class="status-card">
            <strong>Operational focus</strong>
            <span>The layout prioritizes readability, session awareness, and the most important operational tasks.</span>
        </article>
    </section>
</div>
</body>
</html>
