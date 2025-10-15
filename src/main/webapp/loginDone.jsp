<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>RouteX - Login Success</title>
    <style>
        /* Stile di base per la pagina */
        body {
            background: linear-gradient(to bottom, #e0f7fa, #80deea);
            font-family: 'Arial Rounded MT Bold', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            position: relative;
        }

        /* Container per il messaggio di successo */
        .login-success-container {
            background: rgba(255, 255, 255, 0.9);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
            max-width: 500px;
            width: 100%;
            text-align: center;
            animation: slideIn 1s ease-out;
        }

        /* Stile per il testo del titolo */
        h2 {
            margin-bottom: 20px;
            font-size: 28px;
            color: #007bff;
        }

        /* Stile per il messaggio */
        p {
            font-size: 18px;
            color: #333;
            margin-bottom: 20px;
        }

        /* Stile per i pulsanti */
        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 15px;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        button:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }

        /* Animazione di slide in */
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Stile per i pulsanti registrati e home */
        .button-container-right {
            position: absolute;
            top: 20px;
            right: 40px;
            display: flex;
            gap: 10px;
        }

        .button-container-right a {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            font-size: 16px;
            border: none;
            cursor: pointer;
            border-radius: 10px;
            text-decoration: none;
            text-align: center;
        }

        .button-container-right a:hover {
            background-color: #0056b3;
        }

    </style>
</head>
<body>

    <!-- Contenitore dei pulsanti Home e Register -->
    <div class="button-container-right">
        <a href="index.jsp">Home</a>
        <a href="logout">Logout</a>
    </div>

    <!-- Contenitore del messaggio di successo -->
    <div class="login-success-container">
        <h2>Welcome to RouteX!</h2>
        <form action="index.jsp" method="post">
            <button type="submit">Go to Home</button>
        </form>
    </div>

</body>
</html>
