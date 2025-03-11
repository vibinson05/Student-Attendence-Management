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
import javax.servlet.http.HttpSession;

import com.attendance.db.DBConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String role = request.getParameter("role");

		if (email == null || password == null || role == null) {
			response.sendRedirect("login.html?error=Missing Fields");
			return;
		}

		String query = "";
		if ("student".equals(role)) {
			query = "SELECT * FROM students WHERE email=? AND password=?";
		} else if ("teacher".equals(role)) {
			query = "SELECT * FROM teachers WHERE email=? AND password=?";
		} else if ("admin".equals(role)) { // ✅ Fixed Admin Role
			query = "SELECT * FROM admins WHERE email=? AND password=?";
		} else {
			response.sendRedirect("login.html?error=Invalid Role");
			return;
		}

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				HttpSession session = request.getSession();
				session.setAttribute("user", rs.getString("name"));
				session.setAttribute("role", role);

				if ("student".equals(role)) {
					session.setAttribute("userId", rs.getInt("student_id"));
					response.sendRedirect("studentDashboard.jsp");
				} else if ("teacher".equals(role)) {
					session.setAttribute("userId", rs.getInt("teacher_id"));
					response.sendRedirect("teacherDashboard.jsp");
				} else if ("admin".equals(role)) { // ✅ Redirect to Admin Dashboard
					session.setAttribute("userId", rs.getInt("id"));
					response.sendRedirect("adminDashboard.jsp");
				}
			} else {
				response.sendRedirect("login.html?error=Invalid Credentials");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("login.html?error=Database Error");
		}
	}
}
