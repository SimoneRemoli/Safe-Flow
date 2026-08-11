<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Login</title>
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
            --auth-blue: #2563eb;
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
            width: min(460px, 100%);
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

        .field-group {
            margin-bottom: 15px;
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

        .auth-submit {
            width: 100% !important;
            min-height: 46px !important;
            margin-top: 6px;
            padding: 12px 18px !important;
            border: 1px solid var(--auth-primary) !important;
            border-radius: 999px !important;
            color: #ffffff !important;
            font-weight: 850 !important;
            font-size: 0.98rem !important;
            letter-spacing: 0 !important;
            background: linear-gradient(135deg, var(--auth-primary), #13a085) !important;
            cursor: pointer;
            box-shadow: 0 12px 26px rgba(14, 124, 102, 0.18) !important;
            transition: transform 0.18s ease, box-shadow 0.18s ease, background 0.18s ease;
        }

        .auth-submit:hover {
            transform: translateY(-1px);
            background: linear-gradient(135deg, var(--auth-primary-strong), var(--auth-primary)) !important;
        }

        .auth-secondary-link {
            display: flex;
            justify-content: center;
            width: 100%;
            margin-top: 16px;
            color: var(--auth-muted) !important;
            text-decoration: none;
            font-size: 0.95rem;
            line-height: 1.4;
        }

        .auth-secondary-link strong {
            color: var(--auth-primary-strong) !important;
            margin-left: 6px;
        }

        @media (max-width: 620px) {
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

            .auth-topline {
                align-items: flex-start;
                flex-direction: column;
            }
        }
    </style>
</head>
<body class="auth-page">
    <%@ include file="/header.jspf" %>
    <main class="auth-shell">
        <section class="auth-panel" aria-labelledby="loginTitle">
            <div class="auth-topline">
                <a href="index.jsp" class="auth-brand">
                    <span class="auth-brand-mark">SF</span>
                    <span>Safe Flow</span>
                </a>
                <a href="index.jsp" class="auth-home-link">Home</a>
            </div>

            <span class="auth-eyebrow">Reserved area</span>
            <h1 id="loginTitle">Access the safety desk.</h1>
            <p class="auth-intro">Manage traveler reports, public alerts, and administrative review flows.</p>

            <form action="login" method="post" accept-charset="UTF-8">
                <div class="field-group">
                    <label for="email">Email</label>
                    <input id="email" type="email" name="Email" placeholder="Email address" autocomplete="email">
                </div>

                <div class="field-group">
                    <label for="password">Password</label>
                    <input id="password" type="password" name="Password" placeholder="Password" autocomplete="current-password">
                </div>

                <button type="submit" class="auth-submit">Login</button>
            </form>

            <a href="registerTraveler" class="auth-secondary-link">Need an account?<strong>Register as traveler</strong></a>
        </section>
    </main>
</body>
</html>
