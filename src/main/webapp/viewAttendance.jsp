<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.attendance.db.DBConnection"%>
<%@ page session="true"%>

<%
String fromDate = (String) request.getAttribute("fromDate");
String toDate = (String) request.getAttribute("toDate");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>View Attendance</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="container mt-4">

	<h2 class="text-center">
		Attendance Records from
		<%=fromDate%>
		to
		<%=toDate%></h2>

	<table class="table table-bordered mt-4">
		<thead>
			<tr>
				<th>Student</th>
				<th>Subject</th>
				<th>Date</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
			<%
			try (Connection conn = DBConnection.getConnection()) {
				String query = "SELECT s.name AS student_name, sub.name AS subject_name, a.date_time, a.status "
				+ "FROM attendance a " + "JOIN students s ON a.student_id = s.student_id "
				+ "JOIN subjects sub ON a.subject_id = sub.subject_id " + "WHERE a.date_time BETWEEN ? AND ? "
				+ "ORDER BY a.date_time DESC";

				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
				ResultSet rs = ps.executeQuery();

				boolean hasRecords = false;
				while (rs.next()) {
					hasRecords = true;
			%>
			<tr>
				<td><%=rs.getString("student_name")%></td>
				<td><%=rs.getString("subject_name")%></td>
				<td><%=rs.getDate("date_time")%></td>
				<td><%=rs.getString("status")%></td>
			</tr>
			<%
			}
			if (!hasRecords) {
			%>
			<tr>
				<td colspan="4" class="text-danger text-center">No attendance
					records found for the selected date range.</td>
			</tr>
			<%
			}
			} catch (Exception e) {
			e.printStackTrace();
			%>
			<tr>
				<td colspan="4" class="text-danger">Error loading attendance
					records.</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>

	<a href="teacherDashboard.jsp" class="btn btn-primary">Back to
		Dashboard</a>

</body>
</html>
