<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <title>RouteX - Metro Finder</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Arial Rounded MT Bold', sans-serif;
            height: 100vh;
            display: flex;
            align-items: center;
            background: url('images/light.jpg') no-repeat center center/cover;
            overflow: hidden;
            position: relative;
        }

        /* Sfocatura per il background */
        .background-blur {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            backdrop-filter: blur(6px); /* Sfocatura leggera */
            z-index: -1;
        }

        /* Contenitore principale */
        .main-container {
            display: flex;
            flex-direction: row;
            align-items: center;
            width: 100%;
            height: 100%;
            color: white;
        }

        /* Box a sinistra per logo e motto */
        .left-box {
            flex: 1;
            padding: 30px;
            background: rgba(0, 0, 0, 0.6);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100%;
            box-shadow: 2px 0 10px rgba(0, 0, 0, 0.5);
        }

        .left-box img {
            width: 150px;
            margin-bottom: 20px;
        }

        .left-box h1 {
            font-size: 24px;
            text-align: center;
            margin-bottom: 10px;
        }

        .left-box p {
            font-size: 18px;
            text-align: center;
            line-height: 1.5;
        }

        /* Contenitore centrale per pulsanti e contenuto */
        .right-content {
            flex: 3;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 40px;
        }

        .right-content .welcome-container {
            text-align: center;
            background: rgba(0, 0, 0, 0.7);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
        }

        .right-content .welcome-container img {
            animation: fadeIn 2s ease-in-out;
            width: 200px;
            margin-bottom: 20px;
        }

        .right-content .welcome-container h1 {
            color: white;
            font-size: 26px;
            margin-bottom: 20px;
        }

        .welcome-button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 15px;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .welcome-button:hover {
            background-color: #0056b3;
        }

        /* Pulsanti di Login e Registrati */
        .button-container {
            position: absolute;
            top: 20px;
            right: 40px;
            display: flex;
            gap: 10px;
        }

        .button-container a {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            font-size: 16px;
            border: none;
            cursor: pointer;
            border-radius: 10px;
            text-decoration: none;
            text-align: center;
            transition: background-color 0.3s ease;
        }

        .button-container a:hover {
            background-color: #0056b3;
        }

        /* Sezione per loghi della metropolitana */
        .metro-logos {
            position: absolute;
            right: 20px;
            bottom: 20px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 15px;
        }

        .metro-logos img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
            transition: transform 0.3s ease;
        }

        .metro-logos img:hover {
            transform: scale(1.2);
        }
    </style>
</head>
<body>
    <!-- Sfocatura del background -->
    <div class="background-blur"></div>

    <!-- Contenitore principale -->
    <div class="main-container">
        <!-- Box a sinistra -->
        <div class="left-box">
            <img src="images/logo-no-background.png" alt="Logo">
            <h1>RouteX</h1>
            <p>Navigating the Future,<br>One Stop at a Time</p>
        </div>

        <!-- Contenuto centrale -->
        <div class="right-content">
            <!-- Pulsanti Login, Register o Logout -->
            <div class="button-container">
                <%
                    session = request.getSession(false);
                    String nome = null;
                    if (session != null) {
                        nome = (String) session.getAttribute("nome");
                    }

                    if (nome == null) {
                %>
                        <a href="register.jsp">Register</a>
                        <a href="login.jsp">Login</a>
                <%
                    } else {
                %>
                        <a href="index.jsp">Home</a>
                        <a href="logout" class="logout-link">Logout</a>
                        <a href="areaRiservata" method="get">Area riservata</a>
                <%
                    }
                %>

            </div>

            <div class="welcome-container">
                <img src="images/logo-no-background.png" alt="Logo">
                <h1>Welcome to RouteX!</h1>
                <form action="search.jsp" method="post">
                    <button class="welcome-button" type="submit">Start Exploring</button>
                </form>
            </div>
        </div>
    </div>

    <!-- Sezione loghi della metropolitana -->
    <div class="metro-logos">
        <i class="fas fa-subway" style="font-size: 50px; color: white;"></i>
        <i class="fas fa-train" style="font-size: 50px; color: white;"></i>
        <i class="fas fa-bus" style="font-size: 50px; color: white;"></i>
        <i class="fas fa-map-marker-alt" style="font-size: 50px; color: white;"></i>
    </div>

    <script>
    window.onbeforeunload = function () {
        // Invia logout in modo sincrono prima che la pagina si chiuda o ricarichi
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "logout", false);  // false = sincrono (blockante)
        try {
            xhr.send(null);
        } catch(e) {
            // Ignora errori
        }
        // Non mostrare messaggi di conferma
        return undefined;
    };
    </script>


</body>
</html>
