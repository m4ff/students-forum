/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author halfblood
 */
public class PasswordResetRequest extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/passwordResetRequest.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager manager = (DBManager) getServletContext().getAttribute("dbmanager");
        String error = null;
        String email = request.getParameter("email-field");
        if (manager.emailInDatabase(email)) {
            // Recipient's email ID needs to be mentioned.
            String to = email;
            // Sender's email ID needs to be mentioned
            final String from = "trovacontest@gmail.com";
            // Assuming you are sending email from localhost
            String host = "smtp.gmail.com";
            // Get system properties
            Properties properties = new Properties();

            // Setup mail server
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Get the default Session object.
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "nonrompere1234");
                }
            });

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);
                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));
                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));
                // Set Subject: header field
                message.setSubject("Password reset request");
                
                SecureRandom rand = new SecureRandom();
                byte[] bytes = new byte[8];
                rand.nextBytes(bytes);
                BigInteger bi = new BigInteger(bytes);
                if(bi.signum() == -1) {
                    bi = bi.negate();
                }
                String hexBytes = bi.toString(16);
                
                //Salvare nel DB sta roba con la data (aggiungere un campo) per il confronto
                manager.updateVerificationCodeAndTime(hexBytes,email);
                String link = "http://" + request.getLocalName() + ":" + request.getLocalPort() + "/password-reset-confirmation?email=" + email + "&x=" + hexBytes;
                
                // Now set the actual message
                message.setText("You requested a password reset. Please follow this link: " + link);
                // Send message
                Transport.send(message);
                error = "We just sent you an email, please check your inbox";
                
            } catch (MessagingException mex) {
                //error
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, mex);
                error = "An error occurred, please try again later";
            }
        } else {
            error = "Your email was not found";
        }
        request.setAttribute("error", error);
        doGet(request, response);
    }

}
