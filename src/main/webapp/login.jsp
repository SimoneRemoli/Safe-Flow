<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>RouteX - Login</title>
    <style>
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
            font-family: 'Arial Rounded MT Bold', sans-serif;
            transition: all 0.3s ease;
        }

        input:focus {
            outline: none;
            border-color: #0056b3;
            box-shadow: 0 0 8px rgba(0, 91, 187, 0.5);
        }

        .register-container,
        .login-container {
            background: rgba(255, 255, 255, 0.9);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
            max-width: 600px;
            width: 100%;
            text-align: center;
            animation: slideIn 1s ease-out;
        }

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

        h2 {
            margin-bottom: 20px;
            font-size: 28px;
            color: #007bff;
        }

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
        <a href="register.jsp">Register</a>
    </div>

    <!-- Contenitore del form di login -->
    <div class="login-container">
        <h2>RouteX - Login</h2>
        <div class="row">
            <form action="login" method="post">
                <div class="tab-content bg-white" id="myTabContent2">
                    <div class="tab-pane shadow p-5 fade show active" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
                        <div class="form-group my-4">
                            <input type="email" name="Email" placeholder="Email Address" required><br>
                        </div>
                        <div class="form-group my-4">
                            <input type="password" name="Password" placeholder="Password" required><br>
                        </div>
                        <div class="mb-4">
                            <button class="btn btn-primary btn-block rounded" type="submit">Login</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>


</body>
</html>
