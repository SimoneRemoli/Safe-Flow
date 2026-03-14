<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>RouteX - Launch Mode</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1e34;
            --panel: rgba(7, 20, 36, 0.8);
            --line: rgba(111, 247, 255, 0.22);
            --text: #ecf7ff;
            --muted: #8aa7c0;
            --accent: #6ff7ff;
            --accent-2: #53a9ff;
            --success: #95ffc5;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 20%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 85% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                radial-gradient(circle at bottom center, rgba(67, 112, 255, 0.16), transparent 30%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            overflow: hidden;
            position: relative;
        }

        body::before,
        body::after {
            content: "";
            position: fixed;
            inset: 0;
            pointer-events: none;
        }

        body::before {
            background-image:
                radial-gradient(circle, rgba(255, 255, 255, 0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(111, 247, 255, 0.45) 1px, transparent 1px);
            background-size: 160px 160px, 240px 240px;
            background-position: 0 0, 60px 90px;
            opacity: 0.2;
            animation: drift 18s linear infinite;
        }

        body::after {
            background: linear-gradient(180deg, rgba(255, 255, 255, 0.03), transparent 24%, transparent 76%, rgba(111, 247, 255, 0.05));
        }

        .shell {
            width: min(1240px, calc(100% - 32px));
            margin: 28px auto;
            min-height: calc(100vh - 56px);
            padding: 28px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.86), rgba(3, 10, 20, 0.9));
            box-shadow: 0 32px 80px rgba(0, 0, 0, 0.42);
            backdrop-filter: blur(16px);
            position: relative;
            overflow: hidden;
        }

        .shell::before {
            content: "";
            position: absolute;
            width: 420px;
            height: 420px;
            right: -140px;
            bottom: -160px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.14), transparent 64%);
            filter: blur(10px);
        }

        .hero {
            display: grid;
            grid-template-columns: 1.05fr 0.95fr;
            gap: 28px;
            align-items: stretch;
            min-height: calc(100vh - 112px);
        }

        .eyebrow {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: rgba(111, 247, 255, 0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        .hero-copy {
            padding: 18px 12px 18px 6px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: relative;
            z-index: 1;
        }

        .hero-copy h1 {
            margin: 18px 0 16px;
            font-size: clamp(3rem, 5vw, 5.4rem);
            line-height: 0.92;
            letter-spacing: 0.02em;
        }

        .hero-copy p {
            margin: 0;
            max-width: 680px;
            color: var(--muted);
            font-size: 1.05rem;
            line-height: 1.8;
        }

        .signal-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 16px;
            margin-top: 28px;
            max-width: 760px;
        }

        .signal {
            padding: 16px 18px;
            border-radius: 22px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .signal strong {
            display: block;
            font-size: 1.45rem;
            margin-bottom: 6px;
        }

        .signal span {
            color: var(--muted);
            font-size: 0.9rem;
        }

        .mode-panel {
            border-radius: 30px;
            padding: 24px;
            border: 1px solid rgba(111, 247, 255, 0.18);
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.04), transparent),
                rgba(8, 24, 43, 0.9);
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: relative;
            overflow: hidden;
            z-index: 1;
        }

        .mode-panel::before {
            content: "";
            position: absolute;
            width: 320px;
            height: 320px;
            left: 50%;
            top: -130px;
            transform: translateX(-50%);
            border-radius: 50%;
            background: radial-gradient(circle, rgba(83, 169, 255, 0.2), transparent 68%);
        }

        .panel-head {
            position: relative;
            z-index: 1;
            margin-bottom: 20px;
        }

        .panel-head strong {
            display: block;
            font-size: 1.35rem;
            margin-bottom: 6px;
        }

        .panel-head span {
            color: var(--muted);
            line-height: 1.6;
        }

        .mode-form {
            display: grid;
            gap: 16px;
            position: relative;
            z-index: 1;
        }

        .mode-button {
            width: 100%;
            padding: 22px;
            border-radius: 24px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            color: var(--text);
            text-align: left;
            cursor: pointer;
            transition: transform 0.25s ease, border-color 0.25s ease, box-shadow 0.25s ease;
            position: relative;
            overflow: hidden;
        }

        .mode-button:hover {
            transform: translateY(-4px);
            border-color: rgba(111, 247, 255, 0.38);
            box-shadow: 0 24px 44px rgba(0, 0, 0, 0.24);
        }

        .mode-button.demo {
            background: linear-gradient(135deg, rgba(111, 247, 255, 0.16), rgba(83, 169, 255, 0.12));
        }

        .mode-button.full {
            background: linear-gradient(135deg, rgba(149, 255, 197, 0.16), rgba(70, 187, 255, 0.12));
        }

        .mode-button strong {
            display: block;
            font-size: 1.18rem;
            margin-bottom: 6px;
        }

        .mode-button span {
            display: block;
            color: #d4ebff;
            opacity: 0.88;
            line-height: 1.55;
        }

        .panel-note {
            margin-top: 18px;
            color: var(--muted);
            font-size: 0.88rem;
            position: relative;
            z-index: 1;
        }

        .logo-band {
            margin-top: 20px;
            display: flex;
            align-items: center;
            gap: 16px;
            position: relative;
            z-index: 1;
        }

        .logo-band img {
            width: 86px;
        }

        .logo-band div {
            color: var(--muted);
            line-height: 1.6;
        }

        @keyframes drift {
            0% { transform: translateY(0); }
            50% { transform: translateY(-14px); }
            100% { transform: translateY(0); }
        }

        @media (max-width: 980px) {
            .hero {
                grid-template-columns: 1fr;
                min-height: auto;
            }

            .signal-grid {
                grid-template-columns: 1fr;
                max-width: none;
            }
        }

        @media (max-width: 720px) {
            .shell {
                width: min(100% - 16px, 1240px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 24px;
            }
        }
    </style>
</head>
<body>
<div class="shell">
    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">RouteX Network Boot</div>
            <h1>Urban transit.<br>Activated at launch.</h1>
            <p>
                RouteX apre l'esperienza come una control room metropolitana: scegli la modalita di esecuzione,
                avvia il sistema e accedi a un ecosistema pensato per esplorazione urbana, percorsi e mobilita intelligente.
            </p>

            <div class="signal-grid">
                <div class="signal">
                    <strong>4 Cities</strong>
                    <span>Reti metropolitane pronte per l'esplorazione</span>
                </div>
                <div class="signal">
                    <strong>Live Flow</strong>
                    <span>Interfaccia progettata come hub di navigazione urbana</span>
                </div>
                <div class="signal">
                    <strong>Route Ready</strong>
                    <span>Accesso rapido a ricerca percorsi e modalita operative</span>
                </div>
            </div>
        </div>

        <aside class="mode-panel">
            <div class="panel-head">
                <strong>Select execution mode</strong>
                <span>Scegli come inizializzare la sessione di RouteX. La modalita resta attiva per tutta l'esecuzione applicativa.</span>
            </div>

            <form action="selectMode" method="post" class="mode-form">
                <button class="mode-button demo" type="submit" name="mode" value="DEMO">
                    <strong>Demo Version</strong>
                    <span>Ambiente rapido per testare il flusso dell'applicazione e la navigazione urbana simulata.</span>
                </button>

                <button class="mode-button full" type="submit" name="mode" value="FULL">
                    <strong>Full Version</strong>
                    <span>Esperienza completa con la configurazione estesa del progetto e il comportamento operativo pieno.</span>
                </button>
            </form>

            <div class="panel-note">
                Consigliata se vuoi presentare il progetto: la scena iniziale ora e coerente con la nuova estetica futuristica del modulo metro.
            </div>

            <div class="logo-band">
                <img src="images/logo-no-background.png" alt="RouteX">
                <div>
                    <strong>RouteX</strong><br>
                    Navigating the Future, One Stop at a Time
                </div>
            </div>
        </aside>
    </section>
</div>
</body>
</html>
