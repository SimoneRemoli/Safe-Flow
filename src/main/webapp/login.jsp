<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>RouteX - Login</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.82);
            --line: rgba(111, 247, 255, 0.2);
            --text: #ecf7ff;
            --muted: #8ba5be;
            --accent: #6ff7ff;
            --accent-2: #89ffd1;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 20% 20%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 82% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
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
            animation: drift 18s linear infinite;
        }

        .home-link {
            position: absolute;
            top: 24px;
            right: 24px;
            text-decoration: none;
            color: var(--text);
            padding: 10px 16px;
            border-radius: 999px;
            border: 1px solid rgba(255,255,255,0.12);
            background: rgba(255,255,255,0.06);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .home-link:hover {
            transform: translateY(-2px);
            border-color: rgba(111,247,255,0.4);
        }

        .login-panel {
            width: min(430px, calc(100% - 24px));
            padding: 28px;
            border-radius: 28px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.88), rgba(4, 12, 23, 0.94));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.42);
            backdrop-filter: blur(16px);
            position: relative;
            z-index: 1;
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
            margin: 18px 0 10px;
            font-size: 2.5rem;
            line-height: 0.95;
        }

        p {
            margin: 0 0 22px;
            color: var(--muted);
            line-height: 1.7;
        }

        .field-group {
            margin-bottom: 14px;
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
            padding: 15px 16px;
            border-radius: 16px;
            border: 1px solid rgba(255,255,255,0.12);
            background: rgba(255,255,255,0.05);
            color: var(--text);
            font-size: 1rem;
            outline: none;
            transition: border-color 0.25s ease, box-shadow 0.25s ease;
        }

        .field-group input::placeholder {
            color: #6f87a1;
        }

        .field-group input:focus {
            border-color: rgba(111,247,255,0.55);
            box-shadow: 0 0 0 4px rgba(111,247,255,0.08);
        }

        .submit-btn {
            width: 100%;
            margin-top: 8px;
            padding: 16px 20px;
            border: none;
            border-radius: 999px;
            color: #04111f;
            font-weight: 700;
            font-size: 1rem;
            letter-spacing: 0.08em;
            text-transform: uppercase;
            background: linear-gradient(90deg, var(--accent), var(--accent-2), #8dd8ff);
            cursor: pointer;
            box-shadow: 0 20px 34px rgba(111,247,255,0.22);
            transition: transform 0.25s ease, box-shadow 0.25s ease;
        }

        .submit-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 24px 42px rgba(111,247,255,0.28);
        }

        @keyframes drift {
            0% { transform: translateY(0); }
            50% { transform: translateY(-14px); }
            100% { transform: translateY(0); }
        }
    </style>
</head>
<body>
    <a href="index.jsp" class="home-link">Home</a>

    <div class="login-panel">
        <div class="eyebrow">RouteX Access</div>
        <h1>Minimal login.<br>Future ready.</h1>
        <p>Accedi alla control room di RouteX con un’interfaccia essenziale, pulita e coerente con il progetto.</p>

        <form action="login" method="post">
            <div class="field-group">
                <label for="email">Email</label>
                <input id="email" type="text" name="Email" placeholder="Email Address">
            </div>

            <div class="field-group">
                <label for="password">Password</label>
                <input id="password" type="password" name="Password" placeholder="Password">
            </div>

            <button type="submit" class="submit-btn">Login</button>
        </form>
    </div>
</body>
</html>
