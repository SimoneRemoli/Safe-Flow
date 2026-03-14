<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Send Communication</title>
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

        .panel {
            width: min(820px, 100%);
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

        textarea {
            width: 100%;
            min-height: 190px;
            border-radius: 22px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.04);
            color: var(--text);
            padding: 18px;
            font: inherit;
            resize: vertical;
            outline: none;
        }

        textarea::placeholder {
            color: #7890a5;
        }

        textarea:focus {
            border-color: rgba(111, 247, 255, 0.4);
            box-shadow: 0 0 0 3px rgba(111, 247, 255, 0.08);
        }

        .counter {
            text-align: right;
            font-size: 13px;
            color: var(--muted);
            margin-top: 8px;
        }

        .actions {
            margin-top: 24px;
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn,
        .btn-link {
            text-decoration: none;
            border: none;
            cursor: pointer;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            transition: transform 0.25s ease;
        }

        .btn {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
        }

        .btn-link {
            color: var(--text);
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }

        .btn:hover,
        .btn-link:hover {
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
<div class="panel">
    <span class="eyebrow">Broadcast center</span>
    <h1>Invia comunicazione</h1>
    <p class="subtitle">
        Scrivi un messaggio operativo da distribuire ai worker della piattaforma mantenendo lo stesso layout admin futuristico.
    </p>

    <form action="confirmCommunication" method="post">
        <textarea id="message" name="message" maxlength="250" placeholder="Scrivi il messaggio da inviare ai worker..."></textarea>
        <div class="counter" id="counter">0 / 250</div>

        <div class="actions">
            <button type="submit" class="btn">Invia messaggio</button>
            <a href="indexAdmin.jsp" class="btn-link">Torna alla home</a>
        </div>
    </form>
</div>

<script>
    const textarea = document.getElementById("message");
    const counter = document.getElementById("counter");

    function updateCounter() {
        counter.textContent = textarea.value.length + " / 250";
    }

    textarea.addEventListener("input", updateCounter);
    updateCounter();
</script>
</body>
</html>
