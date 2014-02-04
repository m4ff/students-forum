/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 *
 * @author Pier DAgostino
 */
@WebServlet(name = "QRServlet", urlPatterns = {"/qr-servlet"})
public class QRServlet extends HttpServlet {
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
        String text = (String) request.getParameter("qrtext");
        ByteArrayOutputStream out = QRCode.from(text).to(ImageType.PNG).stream();
        
        response.setContentType("image/png");
        response.setContentLength(out.size());
        OutputStream outStream = response.getOutputStream();
        outStream.write(out.toByteArray());
        outStream.flush();
        outStream.close();
    }
}
