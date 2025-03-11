<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.attendance.db.DBConnection"%>
<%@ page session="true"%>

<%
String studentName = (String) session.getAttribute("user");
Integer studentId = (Integer) session.getAttribute("userId");

if (studentName == null) {
	response.sendRedirect("login.html");
	return;
}

String startDate = request.getParameter("startDate");
String endDate = request.getParameter("endDate");
Connection conn = DBConnection.getConnection();
PreparedStatement ps;

if ((startDate == null || startDate.isEmpty()) || (endDate == null || endDate.isEmpty())) {
	ps = conn.prepareStatement("SELECT s.name AS subject_name, a.date_time, a.status "
	+ "FROM attendance a JOIN subjects s ON a.subject_id = s.subject_id "
	+ "WHERE a.student_id = ? ORDER BY a.date_time DESC");
	ps.setInt(1, studentId);
} else {
	ps = conn.prepareStatement("SELECT s.name AS subject_name, a.date_time, a.status "
	+ "FROM attendance a JOIN subjects s ON a.subject_id = s.subject_id "
	+ "WHERE a.student_id = ? AND DATE(a.date_time) BETWEEN ? AND ? ORDER BY a.date_time DESC");
	ps.setInt(1, studentId);
	ps.setString(2, startDate);
	ps.setString(3, endDate);
}
ResultSet rs = ps.executeQuery();
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Student Dashboard</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #FFE53B;
	text-align: center;
	margin: 0;
	padding: 0;
}

.container {
	width: 80%;
	margin: auto;
	background: white;
	padding: 20px;
	margin-top: 50px;
	border-radius: 8px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

h2 {
	color: #333;
}

table {
	width: 100%;
	margin-top: 20px;
	border-collapse: collapse;
}

th, td {
	padding: 12px;
	border: 1px solid #ddd;
	text-align: center;
}

th {
	background-color: #007BFF;
	color: white;
}

tr:nth-child(even) {
	background-color: #f9f9f9;
}

.status-present {
	color: green;
	font-weight: bold;
}

.status-absent {
	color: red;
	font-weight: bold;
}

.logout-btn {
	display: inline-block;
	padding: 10px 20px;
	margin-top: 20px;
	font-size: 16px;
	color: white;
	background-color: #dc3545;
	text-decoration: none;
	border-radius: 5px;
	transition: 0.3s;
}

.logout-btn:hover {
	background-color: #c82333;
}

.calendar-container {
	margin: 20px 0;
}
</style>
</head>
<body>

	<div class="container">
		<h2>
			Welcome,
			<%=studentName%>!
		</h2>

		<div class="calendar-container">
			<form method="GET" action="studentDashboard.jsp">
				<label for="startDate">Start Date:</label> <input type="date"
					id="startDate" name="startDate" required> <label
					for="endDate">End Date:</label> <input type="date" id="endDate"
					name="endDate" required>
				<button type="submit">Filter</button>
			</form>
		</div>

		<h3>Your Attendance Records</h3>
		<table>
			<tr>
				<th>Subject</th>
				<th>Date & Time</th>
				<th>Status</th>
			</tr>
			<%
			while (rs.next()) {
				String status = rs.getString("status");
				String statusClass = status.equals("Present") ? "status-present" : "status-absent";
			%>
			<tr>
				<td><%=rs.getString("subject_name")%></td>
				<td><%=rs.getTimestamp("date_time")%></td>
				<td class="<%=statusClass%>"><%=status%></td>
			</tr>
			<%
			}
			%>
		</table>

		<a href="login.html" class="logout-btn">Logout</a>
	</div>

</body>
</html>
