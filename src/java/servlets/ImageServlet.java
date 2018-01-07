/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author EDU
 */
public class ImageServlet extends HttpServlet {

   
    private static final long serialVersionUID = -6449908958106497977L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get last uploaded image
        try {
            
            // Image bytes
            byte[] imageBytes = null;
             
//            // Get username from current HttpSession
//            HttpSession session = SessionUtils.getSession();
//            String name = session.getAttribute("username").toString();

            String name = req.getParameter("name");
            
            // Connect to the database
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/photofiles", "app", "app");
             
            // Create the statement
            // This query is specific to SQL, it returns only one row (using 'LIMIT 1') - the last uploaded file
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM photo WHERE username = '" + name + "'");
             
            ResultSet rs = statement.executeQuery();
             
            while (rs.next()) { // This will run only once
                imageBytes = rs.getBytes("file");
            }
 
            // Close the connection
            connection.close();
             

            resp.getOutputStream().write(imageBytes);
            resp.getOutputStream().close();
             
        } catch (Exception e) {
            // Display error message
            resp.getWriter().write(e.getMessage());
            resp.getWriter().close();
        }
         
    }   
}