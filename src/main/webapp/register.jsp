<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>RouteX - Register</title>
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

        input[type="text"],
        input[type="email"],
        input[type="date"],
        input[type="password"] {
            width: 80%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #007bff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            font-size: 16px;
            font-family: 'Arial Rounded MT Bold', sans-serif; /* assicurati che sia uguale per tutti */
            transition: all 0.3s ease;
        }

        /* Container per il form */
        .register-container {
            background: rgba(255, 255, 255, 0.9);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
            max-width: 600px;
            width: 100%;
            text-align: center;
            animation: slideIn 1s ease-out;
        }

        /* Stile per i campi input */
        input[type="text"], input[type="email"], input[type="date"] {
            width: 80%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #007bff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            font-size: 16px;
            transition: all 0.3s ease;
        }

        input[type="text"]:focus, input[type="email"]:focus, input[type="date"]:focus {
            outline: none;
            border-color: #0056b3;
            box-shadow: 0 0 8px rgba(0, 91, 187, 0.5);
        }

        /* Stile per la checkbox */
        .checkbox-container {
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 20px 0;
        }

        .checkbox-container label {
            font-size: 16px;
            margin-left: 10px;
        }

        /* Stile per il bottone Done */
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

        /* Stile per il testo del titolo */
        h2 {
            margin-bottom: 20px;
            font-size: 28px;
            color: #007bff;
        }

        /* Stile per i pulsanti registrati e login */
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

    <!-- Contenitore dei pulsanti Login e Home -->
    <div class="button-container-right">
        <a href="index.jsp">Home</a> <!-- cambio in home -->
        <a href="login.jsp">Login</a>
    </div>

    <!-- Contenitore del form di registrazione -->
    <div class="register-container">
        <h2>RouteX - Register</h2>
        <div class="row">
            <form action="register" method="post">
                <div class="tab-content bg-white" id="myTabContent">
                    <div class="tab-pane shadow p-5 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
                        <div class="form-group my-4">
                            <input type="text" name="firstName" placeholder="First Name" required><br>
                        </div>
                        <div class="form-group my-4">
                            <input type="text" name="lastName" placeholder="Last Name" required><br>
                        </div>
                        <div class="form-group my-4">
                            <input type="text" name="codicefiscale" placeholder="Codice Fiscale" required><br>
                        </div>
                        <div class="form-group my-4">
                            <input type="password" name="password" placeholder="Password" required><br>
                        </div>
                        <div class="form-group my-4">
                            <input type="email" name="email" placeholder="Email Address" required><br>
                        </div>
                        <div class="form-group my-4">
                            <input type="date" name="birthdate" placeholder="Birthdate" required><br>
                        </div>
                        <div class="checkbox-container">
                            <input type="checkbox" id="disabled" name="disabled">
                            <label for="disabled">I am disabled</label>
                        </div>
                        <div class="mb-4">
                            <button class="btn btn-primary btn-block rounded " type="submit" id="registrati" name="registrati">Registrati</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

</body>
</html>
