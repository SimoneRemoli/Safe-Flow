<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Conferma Pagamento</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
            --line: rgba(111, 247, 255, 0.2);
            --text: #ecf7ff;
            --muted: #8ba5be;
            --accent: #6ff7ff;
            --accent-2: #89ffd1;
            --soft: rgba(255,255,255,0.05);
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 18%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 82% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            overflow-x: hidden;
            position: relative;
        }

        body::before {
            content: "";
            position: fixed;
            inset: 0;
            background-image:
                radial-gradient(circle, rgba(255,255,255,0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(111,247,255,0.42) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.18;
            pointer-events: none;
        }

        .shell {
            width: min(940px, calc(100% - 32px));
            margin: 24px auto;
            padding: 28px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.88), rgba(4, 12, 23, 0.94));
            box-shadow: 0 32px 84px rgba(0, 0, 0, 0.42);
            backdrop-filter: blur(16px);
            position: relative;
            overflow: hidden;
        }

        .shell::after {
            content: "";
            position: absolute;
            width: 360px;
            height: 360px;
            right: -120px;
            top: -120px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.12), transparent 66%);
            filter: blur(12px);
        }

        .eyebrow {
            display: inline-flex;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111,247,255,0.2);
            background: rgba(111,247,255,0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        h1 {
            margin: 18px 0 8px;
            font-size: clamp(2.2rem, 4vw, 3.6rem);
            line-height: 0.95;
        }

        .subtitle {
            margin: 0 0 22px;
            color: var(--muted);
            line-height: 1.7;
        }

        .summary-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 14px;
            margin-bottom: 22px;
        }

        .summary-card {
            padding: 18px;
            border-radius: 22px;
            background: var(--soft);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .summary-card strong {
            display: block;
            font-size: 1.3rem;
            margin-bottom: 6px;
        }

        .summary-card span {
            color: var(--muted);
            font-size: 0.9rem;
        }

        .checkout-form {
            display: grid;
            gap: 18px;
        }

        .section {
            padding: 20px;
            border-radius: 24px;
            background: var(--soft);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .section h2 {
            margin: 0 0 12px;
            font-size: 1.15rem;
        }

        .section p {
            margin: 0 0 14px;
            color: var(--muted);
            line-height: 1.6;
        }

        .method-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 12px;
        }

        .method-card {
            position: relative;
            display: block;
            padding: 16px;
            border-radius: 18px;
            border: 1px solid rgba(255,255,255,0.08);
            background: rgba(255,255,255,0.03);
            cursor: pointer;
            transition: border-color 0.25s ease, transform 0.25s ease, background 0.25s ease;
        }

        .method-card:hover {
            transform: translateY(-2px);
            border-color: rgba(111,247,255,0.38);
        }

        .method-card input {
            position: absolute;
            opacity: 0;
            pointer-events: none;
        }

        .method-card.active {
            border-color: rgba(111,247,255,0.58);
            background: rgba(111,247,255,0.08);
        }

        .method-card strong {
            display: block;
            font-size: 1rem;
            margin-bottom: 6px;
        }

        .method-card span {
            color: var(--muted);
            font-size: 0.88rem;
        }

        .payment-details {
            display: none;
            margin-top: 14px;
        }

        .payment-details.visible {
            display: block;
        }

        .field-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 12px;
        }

        .field-group.full {
            grid-column: 1 / -1;
        }

        .field-group label {
            display: block;
            margin-bottom: 8px;
            color: #dff8ff;
            font-size: 0.92rem;
            letter-spacing: 0.04em;
        }

        .field-group input {
            width: 100%;
            padding: 14px 16px;
            border-radius: 16px;
            border: 1px solid rgba(255,255,255,0.12);
            background: rgba(255,255,255,0.05);
            color: var(--text);
            font-size: 1rem;
            outline: none;
            transition: border-color 0.25s ease, box-shadow 0.25s ease;
        }

        .field-group input:focus {
            border-color: rgba(111,247,255,0.55);
            box-shadow: 0 0 0 4px rgba(111,247,255,0.08);
        }

        .persist-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 12px;
        }

        .persist-card {
            position: relative;
            display: block;
            padding: 14px 16px;
            border-radius: 18px;
            border: 1px solid rgba(255,255,255,0.08);
            background: rgba(255,255,255,0.03);
            cursor: pointer;
        }

        .persist-card input {
            position: absolute;
            opacity: 0;
            pointer-events: none;
        }

        .persist-card.active {
            border-color: rgba(111,247,255,0.58);
            background: rgba(111,247,255,0.08);
        }

        .persist-card strong {
            display: block;
            margin-bottom: 4px;
        }

        .persist-card span {
            color: var(--muted);
            font-size: 0.88rem;
        }

        .actions {
            display: flex;
            gap: 12px;
            justify-content: space-between;
            flex-wrap: wrap;
            margin-top: 6px;
        }

        .btn-link,
        .btn-submit {
            text-decoration: none;
            border: none;
            cursor: pointer;
            padding: 15px 22px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.06em;
            transition: transform 0.25s ease, box-shadow 0.25s ease;
        }

        .btn-link {
            color: var(--text);
            background: rgba(255,255,255,0.06);
            border: 1px solid rgba(255,255,255,0.12);
        }

        .btn-submit {
            color: #04111f;
            background: linear-gradient(90deg, var(--accent), var(--accent-2), #8dd8ff);
            box-shadow: 0 20px 34px rgba(111,247,255,0.22);
        }

        .btn-link:hover,
        .btn-submit:hover {
            transform: translateY(-3px);
        }

        @media (max-width: 760px) {
            .summary-grid,
            .method-grid,
            .field-grid,
            .persist-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <script>
        function updatePaymentMethodUI() {
            const mastercard = document.getElementById("mastercard");
            const paypal = document.getElementById("paypal");
            const mastercardDetails = document.getElementById("mastercard-details");
            const paypalDetails = document.getElementById("paypal-details");
            const mastercardCard = document.getElementById("mastercard-card");
            const paypalCard = document.getElementById("paypal-card");

            const mastercardSelected = mastercard.checked;
            const paypalSelected = paypal.checked;

            mastercardDetails.classList.toggle("visible", mastercardSelected);
            paypalDetails.classList.toggle("visible", paypalSelected);
            mastercardCard.classList.toggle("active", mastercardSelected);
            paypalCard.classList.toggle("active", paypalSelected);
        }

        function updatePersistenceUI() {
            document.querySelectorAll(".persist-card").forEach((card) => {
                const input = card.querySelector("input");
                card.classList.toggle("active", input.checked);
            });
        }

        document.addEventListener("DOMContentLoaded", () => {
            updatePaymentMethodUI();
            updatePersistenceUI();
            document.querySelectorAll('input[name="metodoPagamento"]').forEach((input) => {
                input.addEventListener("change", updatePaymentMethodUI);
            });
            document.querySelectorAll('input[name="persistence"]').forEach((input) => {
                input.addEventListener("change", updatePersistenceUI);
            });
        });
    </script>
</head>
<body>
<%@ include file="header.jspf" %>
<%
    String city = (String) request.getAttribute("city");
    String quantity = (String) request.getAttribute("quantity");
    Double prezzo = (Double) request.getAttribute("prezzo");
%>

<div class="shell">
    <div class="eyebrow">Payment Gateway</div>
    <h1>Simple checkout.<br>Futuristic mood.</h1>
    <p class="subtitle">Una schermata più pulita per completare l’acquisto, con dettagli Mastercard ordinati e leggibili.</p>

    <div class="summary-grid">
        <div class="summary-card">
            <strong><%= city %></strong>
            <span>Città selezionata</span>
        </div>
        <div class="summary-card">
            <strong><%= quantity %></strong>
            <span>Numero di biglietti</span>
        </div>
        <div class="summary-card">
            <strong>€ <%= String.format("%.2f", prezzo) %></strong>
            <span>Totale da pagare</span>
        </div>
    </div>

    <form action="confermaPagamento" method="post" class="checkout-form">
        <input type="hidden" name="city" value="<%= city %>">
        <input type="hidden" name="quantity" value="<%= quantity %>">
        <input type="hidden" name="totale" value="<%= prezzo %>">

        <section class="section">
            <h2>Metodo di pagamento</h2>
            <p>Scegli il canale e inserisci solo i dati necessari.</p>

            <div class="method-grid">
                <label class="method-card" id="mastercard-card">
                    <input type="radio" name="metodoPagamento" id="mastercard" value="mastercard">
                    <strong>Mastercard</strong>
                    <span>Pagamento con carta in layout semplice e diretto.</span>
                </label>

                <label class="method-card" id="paypal-card">
                    <input type="radio" name="metodoPagamento" id="paypal" value="paypal">
                    <strong>PayPal</strong>
                    <span>Accesso rapido tramite email e codice transazione.</span>
                </label>
            </div>

            <div id="mastercard-details" class="payment-details">
                <div class="field-grid">
                    <div class="field-group full">
                        <label for="numeroCarta">Numero carta</label>
                        <input type="text" id="numeroCarta" name="numeroCarta" placeholder="1234 5678 9012 3456">
                    </div>
                    <div class="field-group">
                        <label for="scadenza">Scadenza</label>
                        <input type="text" id="scadenza" name="scadenza" placeholder="MM/AA">
                    </div>
                    <div class="field-group">
                        <label for="cvv">CVV</label>
                        <input type="password" id="cvv" name="cvv" placeholder="123">
                    </div>
                </div>
            </div>

            <div id="paypal-details" class="payment-details">
                <div class="field-grid">
                    <div class="field-group full">
                        <label for="emailPaypal">Email PayPal</label>
                        <input type="text" id="emailPaypal" name="emailPaypal" placeholder="name@example.com">
                    </div>
                    <div class="field-group full">
                        <label for="codiceTransazione">Codice transazione</label>
                        <input type="text" id="codiceTransazione" name="codiceTransazione" placeholder="PAYPAL123">
                    </div>
                </div>
            </div>
        </section>

        <section class="section">
            <h2>Persistenza</h2>
            <p>Scegli dove registrare l’operazione.</p>

            <div class="persist-grid">
                <label class="persist-card active">
                    <input type="radio" name="persistence" id="persistenceJDBC" value="JDBC" checked>
                    <strong>Database (JDBC)</strong>
                    <span>Salvataggio strutturato nel sistema principale.</span>
                </label>

                <label class="persist-card">
                    <input type="radio" name="persistence" id="persistenceFile" value="FileSystem">
                    <strong>File System (CSV)</strong>
                    <span>Persistenza locale in formato semplice.</span>
                </label>
            </div>
        </section>

        <div class="actions">
            <a href="buyTicket" class="btn-link">Annulla</a>
            <button type="submit" class="btn-submit">Conferma Pagamento</button>
        </div>
    </form>
</div>
</body>
</html>
