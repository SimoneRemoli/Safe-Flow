<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>RouteX - Launch Mode</title>
    <style>
        :root {
            --bg: #f3f5f8;
            --surface: #ffffff;
            --surface-soft: #f8fafc;
            --border: #d8e0ea;
            --text: #16202a;
            --muted: #64748b;
            --accent: #0f6dff;
            --accent-dark: #0b5fe0;
            --shadow: 0 18px 40px rgba(15, 23, 42, 0.06);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            background: var(--bg);
            color: var(--text);
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif;
        }

        .page {
            width: min(980px, calc(100% - 32px));
            margin: 24px auto;
            min-height: calc(100vh - 48px);
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 24px;
            box-shadow: var(--shadow);
            padding: 28px;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .brand img {
            width: 64px;
            height: auto;
        }

        .brand strong {
            display: block;
            font-size: 1.18rem;
        }

        .brand span {
            display: block;
            margin-top: 2px;
            font-size: 0.94rem;
            color: var(--muted);
        }

        .hero {
            margin-top: 36px;
            display: grid;
            gap: 28px;
        }

        .eyebrow {
            display: inline-block;
            padding: 7px 12px;
            border-radius: 999px;
            border: 1px solid #cfe0ff;
            background: #eaf1ff;
            color: var(--accent);
            font-size: 0.75rem;
            font-weight: 700;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        h1 {
            margin: 18px 0 14px;
            font-size: clamp(2.2rem, 4vw, 3.6rem);
            line-height: 1.02;
            letter-spacing: -0.03em;
        }

        .hero p {
            margin: 0;
            max-width: 680px;
            color: var(--muted);
            font-size: 1rem;
            line-height: 1.7;
        }

        .summary {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 14px;
        }

        .summary-card {
            padding: 18px;
            border-radius: 18px;
            border: 1px solid var(--border);
            background: var(--surface-soft);
        }

        .summary-card strong {
            display: block;
            margin-bottom: 6px;
            font-size: 1rem;
        }

        .summary-card span {
            color: var(--muted);
            line-height: 1.6;
            font-size: 0.94rem;
        }

        .mode-section {
            display: grid;
            gap: 16px;
        }

        .mode-head strong {
            display: block;
            margin-bottom: 6px;
            font-size: 1.15rem;
        }

        .mode-head span {
            color: var(--muted);
            line-height: 1.6;
        }

        .mode-form {
            display: grid;
            gap: 14px;
        }

        .mode-button {
            width: 100%;
            text-align: left;
            padding: 20px;
            border-radius: 18px;
            border: 1px solid var(--border);
            background: var(--surface-soft);
            color: var(--text);
            cursor: pointer;
        }

        .mode-button:hover {
            border-color: #bfd1e4;
            background: #f1f5f9;
        }

        .mode-button strong {
            display: block;
            margin-bottom: 6px;
            font-size: 1.02rem;
        }

        .mode-button span {
            display: block;
            color: var(--muted);
            line-height: 1.6;
            font-size: 0.94rem;
        }

        .mode-button.primary {
            background: var(--accent);
            border-color: var(--accent);
            color: #ffffff;
        }

        .mode-button.primary:hover {
            background: var(--accent-dark);
            border-color: var(--accent-dark);
        }

        .mode-button.primary span,
        .mode-button.primary strong {
            color: #ffffff;
        }

        .footer-note {
            padding: 16px 18px;
            border-radius: 16px;
            border: 1px solid var(--border);
            background: var(--surface-soft);
            color: var(--muted);
            line-height: 1.6;
            font-size: 0.94rem;
        }

        @media (max-width: 760px) {
            .page {
                width: min(100% - 16px, 980px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 18px;
            }

            .summary {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<main class="page">
    <div class="brand">
        <img src="images/logo-no-background.png" alt="RouteX">
        <div>
            <strong>RouteX</strong>
            <span>Choose how to start the application.</span>
        </div>
    </div>

    <section class="hero">
        <div>
            <div class="eyebrow">System Launch</div>
            <h1>Start RouteX in the mode you need.</h1>
            <p>
                Seleziona la modalità di esecuzione dell'applicazione.
                L'interfaccia rimane semplice e il comportamento applicativo non cambia.
            </p>
        </div>

        <div class="summary">
            <div class="summary-card">
                <strong>Demo</strong>
                <span>Per presentazioni, test rapidi e navigazione simulata.</span>
            </div>
            <div class="summary-card">
                <strong>Full</strong>
                <span>Per usare l'applicazione con persistenza e configurazione completa.</span>
            </div>
            <div class="summary-card">
                <strong>Clean UI</strong>
                <span>Schermata iniziale più minimale, uniforme e leggibile.</span>
            </div>
        </div>

        <section class="mode-section">
            <div class="mode-head">
                <strong>Select execution mode</strong>
                <span>La modalità scelta resta attiva per tutta l'esecuzione dell'applicazione.</span>
            </div>

            <form action="selectMode" method="post" class="mode-form">
                <button class="mode-button" type="submit" name="mode" value="DEMO">
                    <strong>Demo Version</strong>
                    <span>Ambiente rapido per testare il flusso dell'applicazione e la navigazione urbana simulata.</span>
                </button>

                <button class="mode-button primary" type="submit" name="mode" value="FULL">
                    <strong>Full Version</strong>
                    <span>Esperienza completa con configurazione estesa e comportamento operativo pieno.</span>
                </button>
            </form>

            <div class="footer-note">
                Puoi cambiare lo stile delle altre pagine nello stesso modo, un passo alla volta,
                senza toccare la logica applicativa.
            </div>
        </section>
    </section>
</main>
</body>
</html>
