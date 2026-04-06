<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Traveler Registration</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
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
            padding: 20px;
        }

        .panel {
            width: min(560px, 100%);
            padding: 30px;
            border-radius: 28px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.88), rgba(4, 12, 23, 0.94));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.42);
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
            font-size: 2.35rem;
            line-height: 1;
        }

        p {
            margin: 0 0 22px;
            color: var(--muted);
            line-height: 1.7;
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
        }

        .checkbox-row {
            display: flex;
            align-items: center;
            gap: 10px;
            margin: 6px 0 18px;
            color: var(--muted);
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 8px;
        }

        .btn,
        .btn-link {
            text-decoration: none;
            border: none;
            cursor: pointer;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
        }

        .btn {
            color: #04111f;
            background: linear-gradient(90deg, var(--accent), var(--accent-2), #8dd8ff);
        }

        .btn-link {
            color: var(--text);
            background: rgba(255,255,255,0.06);
            border: 1px solid rgba(255,255,255,0.12);
        }

        @media (max-width: 640px) {
            .grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<div class="panel">
    <span class="eyebrow">Traveler Registration</span>
    <h1>Create your RouteX account</h1>
    <p>Register as a traveler to access your reserved area, saved routes, purchased tickets, and platform notifications.</p>

    <form action="registerTraveler" method="post">
        <div class="grid">
            <div class="field-group">
                <label for="firstName">First name</label>
                <input id="firstName" type="text" name="firstName" placeholder="First name">
            </div>

            <div class="field-group">
                <label for="lastName">Last name</label>
                <input id="lastName" type="text" name="lastName" placeholder="Last name">
            </div>

            <div class="field-group full">
                <label for="taxCode">Tax code</label>
                <input id="taxCode" type="text" name="taxCode" maxlength="16" placeholder="16-character tax code">
            </div>

            <div class="field-group full">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" placeholder="Email address">
            </div>

            <div class="field-group">
                <label for="password">Password</label>
                <input id="password" type="password" name="password" placeholder="Password">
            </div>

            <div class="field-group">
                <label for="birthDate">Birth date</label>
                <input id="birthDate" type="date" name="birthDate">
            </div>
        </div>

        <label class="checkbox-row" for="disabled">
            <input id="disabled" type="checkbox" name="disabled">
            Accessibility support required
        </label>

        <div class="actions">
            <button type="submit" class="btn">Create account</button>
            <a href="login.jsp" class="btn-link">Back to login</a>
        </div>
    </form>
</div>
</body>
</html>
