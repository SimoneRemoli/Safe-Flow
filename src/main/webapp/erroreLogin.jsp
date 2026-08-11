<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String errorTitle = (String) request.getAttribute("titoloErrore");
    String errorMessage = (String) request.getAttribute("messaggioErrore");

    if (errorTitle == null || errorTitle.isBlank()) {
        errorTitle = "Login failed";
    }
    if (errorMessage == null || errorMessage.isBlank()) {
        errorMessage = "Safe Flow could not complete your login request. Please check your credentials and try again.";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Login Error</title>
    <style>
        :root {
            --sf-page: #f4f7f5;
            --sf-surface: #ffffff;
            --sf-surface-soft: #f8fbf9;
            --sf-border: #d8e4de;
            --sf-text: #13231c;
            --sf-muted: #607267;
            --sf-muted-strong: #405147;
            --sf-primary: #0e7c66;
            --sf-primary-strong: #075f4e;
            --sf-danger: #b42318;
            --sf-danger-soft: #fff0ee;
            --sf-danger-border: #fac7c2;
            --sf-shadow: 0 20px 54px rgba(19, 35, 28, 0.12);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0 !important;
            min-height: 100vh !important;
            color: var(--sf-text) !important;
            font-family: "Inter", "Segoe UI", "Helvetica Neue", Arial, sans-serif !important;
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.78), rgba(244, 247, 245, 0.96)),
                var(--sf-page) !important;
            display: grid;
            place-items: center;
            padding: 32px 18px !important;
        }

        .login-error-shell {
            width: min(760px, 100%);
        }

        .login-error-panel {
            background: var(--sf-surface);
            border: 1px solid var(--sf-border);
            border-radius: 18px;
            box-shadow: var(--sf-shadow);
            overflow: hidden;
        }

        .login-error-status {
            display: grid;
            grid-template-columns: auto 1fr;
            gap: 18px;
            align-items: center;
            padding: 30px;
            border-bottom: 1px solid var(--sf-border);
            background: var(--sf-surface-soft);
        }

        .login-error-icon {
            width: 52px;
            height: 52px;
            border-radius: 14px;
            display: grid;
            place-items: center;
            color: var(--sf-danger);
            background: var(--sf-danger-soft);
            border: 1px solid var(--sf-danger-border);
            font-size: 1.6rem;
            font-weight: 900;
        }

        .login-error-eyebrow {
            margin: 0 0 8px;
            color: var(--sf-danger);
            font-size: 0.76rem;
            font-weight: 850;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        .login-error-status h1 {
            margin: 0 !important;
            font-size: clamp(1.8rem, 4vw, 2.55rem) !important;
            line-height: 1.05 !important;
            letter-spacing: 0 !important;
        }

        .login-error-body {
            padding: 30px;
        }

        .login-error-message {
            margin: 0;
            color: var(--sf-muted-strong);
            font-size: 1.04rem;
            line-height: 1.75;
        }

        .login-error-note {
            margin: 20px 0 0;
            padding: 14px 16px;
            border: 1px solid var(--sf-border);
            border-radius: 10px;
            background: var(--sf-surface-soft);
            color: var(--sf-muted);
            line-height: 1.6;
            font-size: 0.94rem;
        }

        .login-error-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 26px;
        }

        .login-error-actions a {
            min-height: 44px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 11px 16px;
            border-radius: 999px;
            border: 1px solid var(--sf-border);
            text-decoration: none;
            font-weight: 800;
            transition: transform 0.18s ease, border-color 0.18s ease, background 0.18s ease;
        }

        .login-error-actions a:hover {
            transform: translateY(-1px);
        }

        .login-error-primary {
            background: linear-gradient(135deg, var(--sf-primary), #13a085) !important;
            border-color: var(--sf-primary) !important;
            color: #ffffff !important;
            box-shadow: 0 12px 26px rgba(14, 124, 102, 0.18) !important;
        }

        .login-error-primary:hover {
            background: linear-gradient(135deg, var(--sf-primary-strong), var(--sf-primary)) !important;
            color: #ffffff !important;
        }

        .login-error-secondary {
            background: var(--sf-surface-soft) !important;
            color: var(--sf-muted-strong) !important;
        }

        .login-error-secondary:hover {
            border-color: #c8e2d8 !important;
            background: #ffffff !important;
            color: var(--sf-text) !important;
        }

        @media (max-width: 620px) {
            body {
                padding: 16px !important;
            }

            .login-error-status {
                grid-template-columns: 1fr;
                padding: 22px;
            }

            .login-error-body {
                padding: 22px;
            }

            .login-error-actions a {
                width: 100%;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="login-error-shell" aria-labelledby="loginErrorTitle">
    <section class="login-error-panel">
        <div class="login-error-status">
            <div class="login-error-icon" aria-hidden="true">!</div>
            <div>
                <p class="login-error-eyebrow">Reserved area access</p>
                <h1 id="loginErrorTitle"><%= StringEscapeUtils.escapeHtml4(errorTitle) %></h1>
            </div>
        </div>

        <div class="login-error-body">
            <p class="login-error-message"><%= StringEscapeUtils.escapeHtml4(errorMessage) %></p>
            <p class="login-error-note">
                For security reasons, Safe Flow cannot disclose sensitive authentication details.
                If the problem persists, contact the platform administrator.
            </p>

            <div class="login-error-actions" aria-label="Login error actions">
                <a class="login-error-primary" href="login.jsp">Back to login</a>
                <a class="login-error-secondary" href="index.jsp">Return to public home</a>
            </div>
        </div>
    </section>
</main>
</body>
</html>
