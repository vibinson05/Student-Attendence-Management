package com.attendance.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.attendance.db.DBConnection;

@WebServlet("/ManageStudentsServlet")
public class ManageStudentsServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		try (Connection conn = DBConnection.getConnection()) {
			if ("add".equals(action)) {
				addStudent(request, response, conn);
			} else if ("remove".equals(action)) {
				removeStudent(request, response, conn);
			} else {
				response.sendRedirect("adminDashboard.jsp?error=Invalid action");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("adminDashboard.jsp?error=Database error occurred.");
		}
	}

	// ✅ Add Student (WITHOUT BCrypt)
	private void addStudent(HttpServletRequest request, HttpServletResponse response, Connection conn)
			throws IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password"); // ✅ Plain Text Password

		try {
			// Check if student already exists
			PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE email = ?");
			checkStmt.setString(1, email);
			ResultSet rs = checkStmt.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				response.sendRedirect("adminDashboard.jsp?error=Student already exists");
				return;
			}

			// Insert student (NO BCrypt, Plain Password)
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO students (name, email, password) VALUES (?, ?, ?)");
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.executeUpdate();

			response.sendRedirect("adminDashboard.jsp?success=Student added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("adminDashboard.jsp?error=Error adding student");
		}
	}

	// ✅ Remove Student
	private void removeStudent(HttpServletRequest request, HttpServletResponse response, Connection conn)
			throws IOException {
		String email = request.getParameter("email");

		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE email=?");
			ps.setString(1, email);
			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				response.sendRedirect("adminDashboard.jsp?success=Student removed successfully");
			} else {
				response.sendRedirect("adminDashboard.jsp?error=Student not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("adminDashboard.jsp?error=Error removing student");
		}
	}
}
