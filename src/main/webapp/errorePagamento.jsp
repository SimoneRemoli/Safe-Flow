<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>RouteX - Pagamento non riuscito</title>

    <!-- Bootstrap + FontAwesome -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/lib/bootstrap/dist/css/bootstrap.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
            font-family: "Segoe UI", sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        .card {
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            text-align: center;
            padding: 40px;
            max-width: 600px;
        }

        .card i {
            font-size: 70px;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .card h2 {
            color: #dc3545;
            margin-bottom: 15px;
        }

        .card p {
            color: #555;
            font-size: 18px;
            margin-bottom: 30px;
        }

        .btn-home {
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 10px;
            padding: 10px 25px;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }

        .btn-home:hover {
            background-color: #0056b3;
        }

        .btn-retry {
            background-color: #6c757d;
            color: white;
            border: none;
            border-radius: 10px;
            padding: 10px 25px;
            font-size: 16px;
            margin-left: 10px;
            transition: background-color 0.3s ease;
        }

        .btn-retry:hover {
            background-color: #5a6268;
        }
    </style>
</head>

<body>
    <div class="card">
        <i class="fas fa-times-circle"></i>
        <h2>Pagamento non riuscito</h2>
        <p>
            Si è verificato un errore durante l’elaborazione del pagamento.<br>
            Ti invitiamo a controllare i dati inseriti o a riprovare più tardi.
        </p>

        <div>
            <a href="index.jsp" class="btn-home">
                <i class="fas fa-home"></i> Torna alla Home
            </a>
            <a href="buyTicket" class="btn-retry">
                <i class="fas fa-redo"></i> Riprova Pagamento
            </a>
        </div>
    </div>
</body>
</html>
