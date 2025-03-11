package com.attendance.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.attendance.db.DBConnection;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String role = request.getParameter("role");

		if (name == null || email == null || password == null || role == null) {
			response.sendRedirect("register.html?error=Missing Fields");
			return;
		}

		String query = "";
		if ("student".equals(role)) {
			query = "INSERT INTO students (name, email, password) VALUES (?, ?, ?)";
		} else if ("teacher".equals(role)) {
			query = "INSERT INTO teachers (name, email, password) VALUES (?, ?, ?)";
		} else if ("admin".equals(role)) { // ✅ Fixed Admin Role
			query = "INSERT INTO admins (name, email, password) VALUES (?, ?, ?)";
		} else {
			response.sendRedirect("register.html?error=Invalid Role");
			return;
		}

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, password);
			int rowsInserted = ps.executeUpdate();

			if (rowsInserted > 0) {
				response.sendRedirect("login.html?success=Registered Successfully");
			} else {
				response.sendRedirect("register.html?error=Registration Failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect("register.html?error=SQL Error: " + e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("register.html");
	}
}
