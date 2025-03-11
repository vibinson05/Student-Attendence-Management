package com.attendance.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.attendance.db.DBConnection;

@WebServlet("/ExportAttendanceServlet")
public class ExportAttendanceServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=attendance.xlsx");

		PrintWriter out = response.getWriter();
		out.println("Student Name,Subject,Date,Status");

		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");

		try (Connection conn = DBConnection.getConnection()) {
			PreparedStatement ps = conn
					.prepareStatement("SELECT s.name AS student_name, sub.name AS subject_name, a.date_time, a.status "
							+ "FROM attendance a " + "JOIN students s ON a.student_id = s.student_id "
							+ "JOIN subjects sub ON a.subject_id = sub.subject_id "
							+ "WHERE a.date_time BETWEEN ? AND ?");
			ps.setString(1, fromDate);
			ps.setString(2, toDate);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				out.println(rs.getString("student_name") + "," + rs.getString("subject_name") + ","
						+ rs.getString("date_time") + "," + rs.getString("status"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
