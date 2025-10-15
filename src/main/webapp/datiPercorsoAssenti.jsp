<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Prendo i parametri della richiesta (nomi coerenti con il tuo)
    String citta = request.getParameter("city");
    String partenza = request.getParameter("startStation");
    String arrivo = request.getParameter("endStation");

    boolean mancaCitta = (citta == null || citta.trim().isEmpty());
    boolean mancaPartenza = (partenza == null || partenza.trim().isEmpty());
    boolean mancaArrivo = (arrivo == null || arrivo.trim().isEmpty());

    StringBuilder messaggio = new StringBuilder();

    if(mancaCitta || mancaPartenza || mancaArrivo) {
        messaggio.append("<h1 style='color:#ff4d4d; margin-bottom:20px;'>Errore di compilazione</h1>");
        messaggio.append("<ul style='text-align:left; display:inline-block; max-width:400px; color:white;'>");
        if(mancaCitta) {
            messaggio.append("<li>Manca la selezione della città.</li>");
            messaggio.append(request.getParameter("city"));
        }
        if(mancaPartenza) {
            messaggio.append("<li>Manca la stazione di partenza.</li>");
        }
        if(mancaArrivo) {
            messaggio.append("<li>Manca la stazione di arrivo.</li>");
        }
        messaggio.append("</ul>");
    } else {
        messaggio.append("<p style='color:white;'>Nessun errore rilevato.</p>");
    }
%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
<title>RouteX - Errore</title>
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
    .background-blur {
        position: absolute; top: 0; left: 0; width: 100%; height: 100%; backdrop-filter: blur(6px); z-index: -1;
    }
    .main-container {
        display: flex; flex-direction: row; align-items: center; width: 100%; height: 100%; color: white;
    }
    .left-box {
        flex: 1; padding: 30px; background: rgba(0, 0, 0, 0.6); display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; box-shadow: 2px 0 10px rgba(0, 0, 0, 0.5);
    }
    .left-box img { width: 150px; margin-bottom: 20px; }
    .left-box h1 { font-size: 24px; text-align: center; margin-bottom: 10px; }
    .left-box p { font-size: 18px; text-align: center; line-height: 1.5; }
    .right-content {
        flex: 3; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 40px;
    }
    .right-content .welcome-container {
        text-align: center; background: rgba(0, 0, 0, 0.7); padding: 30px; border-radius: 15px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
        max-width: 600px;
    }
    .right-content .welcome-container img {
        width: 120px; margin-bottom: 20px;
    }
    .welcome-button {
        background-color: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 15px; font-size: 18px; cursor: pointer; text-decoration: none;
        display: inline-block;
        margin-top: 25px;
    }
    .welcome-button:hover { background-color: #0056b3; }
    .button-container {
        position: absolute; top: 20px; right: 40px; display: flex; gap: 10px;
        z-index: 10;
    }
    .button-container a {
        background-color: #007bff; color: white; padding: 10px 15px; font-size: 16px; border: none; cursor: pointer; border-radius: 10px; text-decoration: none; text-align: center;
    }
    .button-container a:hover { background-color: #0056b3; }
    .metro-logos {
        position: absolute; right: 20px; bottom: 20px; display: flex; flex-direction: column; align-items: center; gap: 15px;
    }
    .metro-logos i {
        font-size: 50px; color: white;
    }
</style>
</head>
<body>
    <div class="background-blur"></div>

    <div class="main-container">
        <div class="left-box">
            <img src="images/logo-no-background.png" alt="Logo">
            <h1>RouteX</h1>
            <p>Navigating the Future,<br>One Stop at a Time</p>
        </div>

        <div class="right-content">
            <div class="button-container">
                <%
                    String nome = null;
                    if (session != null) {
                        nome = (String) session.getAttribute("nome");
                    }

                    if (nome == null) {
                %>
                    <a href="index.jsp">Home</a>
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
                <%= messaggio.toString() %>
            </div>
        </div>
    </div>

    <div class="metro-logos">
        <i class="fas fa-subway"></i>
        <i class="fas fa-train"></i>
        <i class="fas fa-bus"></i>
        <i class="fas fa-map-marker-alt"></i>
    </div>
</body>
</html>
