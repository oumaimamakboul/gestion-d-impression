package com.uniquedeveloper.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uname = request.getParameter("name");
        String uemail = request.getParameter("email");
        String upswd = request.getParameter("pass");
        String umob = request.getParameter("contact");
        RequestDispatcher dispatcher = null;

        try {
            // Utilisation de try-with-resources pour gérer automatiquement la fermeture des ressources
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/company", "root", "123456");
                 PreparedStatement pst = con.prepareStatement("INSERT INTO users(uname, upswd, uemail, umob) VALUES (?, ?, ?, ?)")) {

                // Paramètres de la requête préparée
                pst.setString(1, uname);
                pst.setString(2, upswd);
                pst.setString(3, uemail);
                pst.setString(4, umob);

                // Exécution de la requête
                int rowCount = pst.executeUpdate();

                dispatcher = request.getRequestDispatcher("registration.jsp");

                // Définition de l'attribut de statut en fonction du résultat de l'exécution de la requête
                if (rowCount > 0) {
                    request.setAttribute("status", "success");
                } else {
                    request.setAttribute("status", "failed");
                }

                // Redirection vers la page de réponse
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
