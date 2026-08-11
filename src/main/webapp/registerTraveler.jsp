<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Traveler Registration</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        :root {
            --auth-bg: #ffffff;
            --auth-surface: #ffffff;
            --auth-soft: #f7faf8;
            --auth-border: #d8e4de;
            --auth-text: #14241d;
            --auth-muted: #607267;
            --auth-muted-strong: #405147;
            --auth-primary: #0e7c66;
            --auth-primary-strong: #075f4e;
            --auth-primary-soft: #e8f7ef;
            --auth-shadow: 0 18px 46px rgba(19, 35, 28, 0.10);
        }

        * { box-sizing: border-box; }

        body.auth-page {
            margin: 0 !important;
            min-height: 100vh !important;
            color: var(--auth-text) !important;
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif !important;
            background: var(--auth-bg) !important;
            padding: 28px 18px !important;
        }

        .auth-shell {
            width: min(1040px, 100%);
            min-height: calc(100vh - 56px);
            margin: 0 auto;
            display: grid;
            place-items: center;
        }

        .auth-panel {
            width: min(660px, 100%);
            padding: 30px;
            border: 1px solid var(--auth-border);
            border-radius: 18px;
            background: var(--auth-surface);
            box-shadow: var(--auth-shadow);
        }

        .auth-topline {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 14px;
            margin-bottom: 24px;
        }

        .auth-brand {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            color: var(--auth-text);
            font-weight: 850;
            text-decoration: none;
        }

        .auth-brand-mark {
            width: 34px;
            height: 34px;
            display: grid;
            place-items: center;
            border-radius: 10px;
            color: var(--auth-primary-strong);
            background: var(--auth-primary-soft);
            border: 1px solid #bfe8cf;
            font-size: 0.85rem;
            font-weight: 900;
        }

        .auth-home-link {
            color: var(--auth-muted-strong) !important;
            font-size: 0.92rem;
            font-weight: 800;
            text-decoration: none;
        }

        .auth-home-link:hover {
            color: var(--auth-primary-strong) !important;
        }

        .auth-eyebrow {
            display: inline-flex;
            margin-bottom: 14px;
            color: var(--auth-primary) !important;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            font-size: 0.74rem;
            font-weight: 850;
        }

        .auth-panel h1 {
            margin: 0 0 10px;
            color: var(--auth-text) !important;
            font-size: clamp(1.85rem, 4vw, 2.35rem) !important;
            line-height: 1.05 !important;
            letter-spacing: 0 !important;
        }

        .auth-intro {
            margin: 0 0 24px;
            color: var(--auth-muted) !important;
            line-height: 1.7;
            font-size: 0.98rem;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 14px;
        }

        .field-group {
            margin-bottom: 14px;
        }

        .field-group.full {
            grid-column: 1 / -1;
        }

        .field-group label {
            display: block;
            margin-bottom: 8px;
            color: var(--auth-muted-strong) !important;
            font-size: 0.92rem;
            font-weight: 800;
        }

        .field-group input {
            width: 100% !important;
            height: 46px !important;
            padding: 11px 13px !important;
            border-radius: 10px !important;
            border: 1px solid var(--auth-border) !important;
            background: #ffffff !important;
            color: var(--auth-text) !important;
            font-size: 1rem;
            outline: none !important;
            transition: border-color 0.25s ease, box-shadow 0.25s ease;
        }

        .field-group input::placeholder {
            color: #8ba197;
        }

        .field-group input:focus {
            border-color: rgba(14, 124, 102, 0.58) !important;
            box-shadow: 0 0 0 4px rgba(14, 124, 102, 0.10) !important;
        }

        .checkbox-row {
            display: flex;
            align-items: center;
            gap: 10px;
            margin: 4px 0 20px;
            color: var(--auth-muted) !important;
            line-height: 1.45;
            font-size: 0.95rem;
        }

        .checkbox-row input {
            width: 18px !important;
            height: 18px !important;
            accent-color: var(--auth-primary);
        }

        .auth-actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 8px;
        }

        .auth-submit,
        .auth-secondary-button {
            min-height: 46px !important;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 18px !important;
            border-radius: 999px !important;
            font-weight: 850 !important;
            font-size: 0.98rem !important;
            line-height: 1.2;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.18s ease, box-shadow 0.18s ease, background 0.18s ease, border-color 0.18s ease;
        }

        .auth-submit {
            border: 1px solid var(--auth-primary) !important;
            background: linear-gradient(135deg, var(--auth-primary), #13a085) !important;
            color: #ffffff !important;
            box-shadow: 0 12px 26px rgba(14, 124, 102, 0.18) !important;
        }

        .auth-submit:hover {
            transform: translateY(-1px);
            background: linear-gradient(135deg, var(--auth-primary-strong), var(--auth-primary)) !important;
        }

        .auth-secondary-button {
            border: 1px solid var(--auth-border) !important;
            background: var(--auth-soft) !important;
            color: var(--auth-muted-strong) !important;
        }

        .auth-secondary-button:hover {
            transform: translateY(-1px);
            border-color: #c8e2d8 !important;
            background: #ffffff !important;
            color: var(--auth-text) !important;
        }

        @media (max-width: 640px) {
            body.auth-page {
                padding: 16px !important;
            }

            .auth-shell {
                min-height: calc(100vh - 32px);
            }

            .auth-panel {
                padding: 24px;
                border-radius: 16px;
            }

            .auth-topline,
            .auth-actions {
                align-items: stretch;
                flex-direction: column;
            }

            .grid {
                grid-template-columns: 1fr;
            }

            .auth-submit,
            .auth-secondary-button {
                width: 100%;
            }
        }
    </style>
</head>
<body class="auth-page">
<%@ include file="/header.jspf" %>
<main class="auth-shell">
    <section class="auth-panel" aria-labelledby="registerTitle">
        <div class="auth-topline">
            <a href="index.jsp" class="auth-brand">
                <span class="auth-brand-mark">SF</span>
                <span>Safe Flow</span>
            </a>
            <a href="index.jsp" class="auth-home-link">Home</a>
        </div>

        <span class="auth-eyebrow">Traveler access</span>
        <h1 id="registerTitle">Create your Safe Flow account.</h1>
        <p class="auth-intro">Open a traveler profile for reports, notifications, and reserved area access.</p>

        <form action="registerTraveler" method="post" accept-charset="UTF-8">
            <div class="grid">
                <div class="field-group">
                    <label for="firstName">First name</label>
                    <input id="firstName" type="text" name="firstName" placeholder="First name" autocomplete="given-name">
                </div>

                <div class="field-group">
                    <label for="lastName">Last name</label>
                    <input id="lastName" type="text" name="lastName" placeholder="Last name" autocomplete="family-name">
                </div>

                <div class="field-group full">
                    <label for="taxCode">Tax code</label>
                    <input id="taxCode" type="text" name="taxCode" maxlength="16" placeholder="16-character tax code" autocomplete="off">
                </div>

                <div class="field-group full">
                    <label for="email">Email</label>
                    <input id="email" type="email" name="email" placeholder="Email address" autocomplete="email">
                </div>

                <div class="field-group">
                    <label for="password">Password</label>
                    <input id="password" type="password" name="password" placeholder="Password" autocomplete="new-password">
                </div>

                <div class="field-group">
                    <label for="birthDate">Birth date</label>
                    <input id="birthDate" type="date" name="birthDate" autocomplete="bday">
                </div>
            </div>

            <label class="checkbox-row" for="disabled">
                <input id="disabled" type="checkbox" name="disabled">
                Accessibility support required
            </label>

            <div class="auth-actions">
                <button type="submit" class="auth-submit">Create account</button>
                <a href="login.jsp" class="auth-secondary-button">Back to login</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
