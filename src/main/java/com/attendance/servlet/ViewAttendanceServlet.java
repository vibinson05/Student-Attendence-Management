package com.attendance.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewAttendanceServlet") // ✅ Ensure this exact mapping
public class ViewAttendanceServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");

		if (fromDate == null || toDate == null || fromDate.isEmpty() || toDate.isEmpty()) {
			response.sendRedirect("teacherDashboard.jsp?error=Please select a valid date range.");
			return;
		}

		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);

		// ✅ Forward request to viewAttendance.jsp
		request.getRequestDispatcher("viewAttendance.jsp").forward(request, response);
	}
}
