package com.attendance.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.attendance.db.DBConnection;

@WebServlet("/MarkAttendanceServlet")
public class MarkAttendanceServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String studentId = request.getParameter("studentId");
		String subjectId = request.getParameter("subjectId");
		String attendanceDate = request.getParameter("attendanceDate");
		String status = request.getParameter("status"); // "Present" or "Absent"

		if (studentId == null || subjectId == null || attendanceDate == null || status == null) {
			response.sendRedirect("teacherDashboard.jsp?error=Please select all fields.");
			return;
		}

		try (Connection conn = DBConnection.getConnection()) {
			if (conn == null) {
				throw new SQLException("Database connection is null.");
			}

			// Convert date to java.sql.Date
			Date sqlDate = Date.valueOf(attendanceDate);

			// Insert attendance record
			PreparedStatement insertAttendance = conn.prepareStatement(
					"INSERT INTO attendance (student_id, subject_id, date_time, status) VALUES (?, ?, ?, ?)");
			insertAttendance.setInt(1, Integer.parseInt(studentId));
			insertAttendance.setInt(2, Integer.parseInt(subjectId));
			insertAttendance.setDate(3, sqlDate);
			insertAttendance.setString(4, status);
			insertAttendance.executeUpdate();

			response.sendRedirect("teacherDashboard.jsp?success=Attendance marked as " + status + " successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect("teacherDashboard.jsp?error=Failed to mark attendance.");
		}
	}
}
