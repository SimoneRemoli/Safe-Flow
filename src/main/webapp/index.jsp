<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Metro Finder</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.8);
            --line: rgba(111, 247, 255, 0.22);
            --text: #ecf7ff;
            --muted: #8ba7c0;
            --accent: #6ff7ff;
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
                radial-gradient(circle, rgba(111, 247, 255, 0.45) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.18;
            pointer-events: none;
            animation: drift 18s linear infinite;
        }

        .shell {
            width: min(1280px, calc(100% - 32px));
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

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            position: relative;
            z-index: 1;
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
            border-color: rgba(111, 247, 255, 0.4);
        }

        .hero {
            margin-top: 28px;
            display: grid;
            grid-template-columns: 1.08fr 0.92fr;
            gap: 28px;
            align-items: stretch;
        }

        .hero-copy {
            padding: 20px 8px 20px 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: relative;
            z-index: 1;
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
            margin: 18px 0 16px;
            font-size: clamp(3rem, 5vw, 5.5rem);
            line-height: 0.92;
        }

        .hero-copy p {
            margin: 0;
            color: var(--muted);
            font-size: 1.05rem;
            line-height: 1.8;
            max-width: 700px;
        }

        .cta-row {
            display: flex;
            gap: 14px;
            margin-top: 26px;
            flex-wrap: wrap;
        }

        .cta-row button,
        .cta-row a {
            border: none;
            text-decoration: none;
            cursor: pointer;
            padding: 16px 24px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.06em;
            transition: transform 0.25s ease, box-shadow 0.25s ease;
        }

        .primary-cta {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 20px 36px rgba(111, 247, 255, 0.24);
        }

        .secondary-cta {
            color: var(--text);
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }

        .cta-row button:hover,
        .cta-row a:hover {
            transform: translateY(-3px);
        }

        .info-grid {
            margin-top: 28px;
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 16px;
        }

        .info-card {
            padding: 18px;
            border-radius: 24px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .info-card strong {
            display: block;
            font-size: 1.4rem;
            margin-bottom: 6px;
        }

        .info-card span {
            color: var(--muted);
            line-height: 1.6;
        }

        .visual-panel {
            border-radius: 30px;
            padding: 22px;
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.04), transparent),
                rgba(8, 24, 43, 0.9);
            border: 1px solid rgba(111, 247, 255, 0.18);
            position: relative;
            overflow: hidden;
            z-index: 1;
        }

        .visual-panel::before {
            content: "";
            position: absolute;
            inset: auto -60px -90px auto;
            width: 260px;
            height: 260px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(83, 169, 255, 0.2), transparent 70%);
        }

        .network-stage {
            min-height: 460px;
            border-radius: 26px;
            padding: 20px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            background:
                radial-gradient(circle at 20% 18%, rgba(111, 247, 255, 0.14), transparent 30%),
                linear-gradient(180deg, rgba(8, 18, 32, 0.86), rgba(6, 14, 25, 0.96));
            position: relative;
            overflow: hidden;
        }

        .ring {
            position: absolute;
            border-radius: 50%;
            border: 1px solid rgba(255, 255, 255, 0.14);
            animation: rotate 18s linear infinite;
        }

        .ring.one {
            width: 260px;
            height: 260px;
            top: 62px;
            right: 60px;
        }

        .ring.two {
            width: 150px;
            height: 150px;
            top: 116px;
            right: 115px;
            animation-direction: reverse;
            animation-duration: 12s;
        }

        .planet {
            position: absolute;
            border-radius: 50%;
            box-shadow: 0 0 30px rgba(111, 247, 255, 0.32);
            animation: float 5s ease-in-out infinite;
        }

        .planet.core {
            width: 104px;
            height: 104px;
            top: 138px;
            right: 136px;
            background: radial-gradient(circle at 30% 30%, #ffffff, #7cf5ff 34%, #0a93ff);
        }

        .planet.small-a {
            width: 24px;
            height: 24px;
            top: 102px;
            right: 172px;
            background: radial-gradient(circle at 30% 30%, #fff8bf, #ffd66b 42%, #ff8f3c);
            animation-delay: 0.4s;
        }

        .planet.small-b {
            width: 20px;
            height: 20px;
            top: 264px;
            right: 220px;
            background: radial-gradient(circle at 30% 30%, #fbe0ff, #c794ff 42%, #7354ff);
            animation-delay: 1.2s;
        }

        .network-lines {
            position: absolute;
            inset: 0;
        }

        .network-lines span {
            position: absolute;
            display: block;
            height: 2px;
            border-radius: 999px;
            background: linear-gradient(90deg, transparent, rgba(111, 247, 255, 0.55), transparent);
        }

        .network-lines span:nth-child(1) {
            width: 180px;
            left: 42px;
            top: 110px;
        }

        .network-lines span:nth-child(2) {
            width: 210px;
            left: 92px;
            top: 206px;
            transform: rotate(-18deg);
        }

        .network-lines span:nth-child(3) {
            width: 160px;
            left: 84px;
            bottom: 104px;
            transform: rotate(24deg);
        }

        .stage-copy {
            position: absolute;
            left: 24px;
            bottom: 24px;
            max-width: 260px;
        }

        .stage-copy strong {
            display: block;
            font-size: 1.2rem;
            margin-bottom: 6px;
        }

        .stage-copy span {
            color: var(--muted);
            line-height: 1.6;
        }

        @keyframes drift {
            0% { transform: translateY(0); }
            50% { transform: translateY(-14px); }
            100% { transform: translateY(0); }
        }

        @keyframes rotate {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }

        @keyframes float {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-10px); }
        }

        @media (max-width: 1024px) {
            .hero {
                grid-template-columns: 1fr;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .shell {
                width: min(100% - 16px, 1280px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 24px;
            }

            .topbar {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
<%@ include file="header.jspf" %>
<div class="shell">
    <div class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX">
            <div>
                <strong>RouteX</strong>
                <span>Metro intelligence for urban exploration</span>
            </div>
        </div>

        <div class="nav-actions">
            <a href="login.jsp">Login</a>
        </div>
    </div>

    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">Urban Mobility Interface</div>
            <h1>Explore metro systems<br>like a future city console.</h1>
            <p>
                RouteX trasforma la ricerca di percorsi in un'esperienza urbana immersiva:
                scegli la rete, entra nelle citta disponibili e naviga il sistema metropolitano con un'interfaccia
                piu scenografica, tecnica e coerente con il progetto.
            </p>

            <div class="cta-row">
                <form action="PathControllerGrafico" method="get" style="margin:0;">
                    <button class="primary-cta" type="submit">Start Exploring</button>
                </form>
                <a class="secondary-cta" href="login.jsp">Access Control Center</a>
            </div>

            <div class="info-grid">
                <div class="info-card">
                    <strong>4 Networks</strong>
                    <span>Citta metropolitane pronte per essere esplorate con una UI dinamica.</span>
                </div>
                <div class="info-card">
                    <strong>Animated Flow</strong>
                    <span>Interfaccia coerente con il linguaggio visivo futuristico del modulo percorsi.</span>
                </div>
                <div class="info-card">
                    <strong>Fast Access</strong>
                    <span>Ingresso diretto alla navigazione urbana e al motore di ricerca tratte.</span>
                </div>
            </div>
        </div>

        <aside class="visual-panel">
            <div class="network-stage">
                <div class="ring one"></div>
                <div class="ring two"></div>
                <div class="planet core"></div>
                <div class="planet small-a"></div>
                <div class="planet small-b"></div>

                <div class="network-lines">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>

                <div class="stage-copy">
                    <strong>Metro constellation online</strong>
                    <span>Avvia l'esplorazione e passa alla selezione città con pianeti interattivi e preview della rete.</span>
                </div>
            </div>
        </aside>
    </section>
</div>
</body>
</html>
