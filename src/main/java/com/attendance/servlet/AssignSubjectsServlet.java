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

@WebServlet("/AssignSubjectsServlet") // ✅ Ensure this annotation is correct
public class AssignSubjectsServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String teacherId = request.getParameter("teacherId");
		String subjectId = request.getParameter("subjectId");

		if (teacherId == null || subjectId == null || teacherId.isEmpty() || subjectId.isEmpty()) {
			response.sendRedirect("adminDashboard.jsp?error=Please select both teacher and subject.");
			return;
		}

		try (Connection conn = DBConnection.getConnection()) {
			if (conn == null) {
				response.sendRedirect("adminDashboard.jsp?error=Database connection failed.");
				return;
			}

			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO teacher_subjects (teacher_id, subject_id) VALUES (?, ?)");
			ps.setInt(1, Integer.parseInt(teacherId));
			ps.setInt(2, Integer.parseInt(subjectId));
			int rows = ps.executeUpdate();

			if (rows > 0) {
				response.sendRedirect("adminDashboard.jsp?success=Subject assigned successfully");
			} else {
				response.sendRedirect("adminDashboard.jsp?error=Failed to assign subject.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("adminDashboard.jsp?error=Database error occurred.");
		}
	}
}
