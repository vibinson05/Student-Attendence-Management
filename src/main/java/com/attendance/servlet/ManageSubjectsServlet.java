package com.attendance.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.attendance.db.DBConnection;

@WebServlet("/ManageSubjectsServlet")  
public class ManageSubjectsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String subjectName = request.getParameter("subjectName");

        if (subjectName == null || subjectName.trim().isEmpty()) {
            response.sendRedirect("adminDashboard.jsp?error=Please enter a subject name.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if ("add".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO subjects (name) VALUES (?)");
                ps.setString(1, subjectName);
                ps.executeUpdate();
                response.sendRedirect("adminDashboard.jsp?success=Subject added successfully.");
            } else if ("remove".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM subjects WHERE name=?");
                ps.setString(1, subjectName);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    response.sendRedirect("adminDashboard.jsp?success=Subject removed successfully.");
                } else {
                    response.sendRedirect("adminDashboard.jsp?error=Subject not found.");
                }
            } else {
                response.sendRedirect("adminDashboard.jsp?error=Invalid action.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard.jsp?error=Database error occurred.");
        }
    }
}