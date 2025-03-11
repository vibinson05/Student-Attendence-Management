<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.attendance.db.DBConnection"%>
<%@ page session="true"%>

<%
String adminName = (String) session.getAttribute("user");
if (adminName == null) {
	response.sendRedirect("login.html");
	return;
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<style>
body {
	font-family: Arial, sans-serif;
	background-color: #6CB4EE;
}

.container {
	margin-top: 50px;
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

h2, h4 {
	color: #007BFF;
	text-align: center;
}

.section {
	padding: 10px;
	border-radius: 8px;
	background: white;
	margin-bottom: 10px;
}

.form-group, .btn {
	margin-bottom: 5px;
}

.form-inline .form-group {
	margin-right: 5px;
}
</style>

<script>
	function confirmAction(message, event) {
		if (!confirm(message)) {
			event.preventDefault();
		}
	}
</script>

</head>
<body>

	<div class="container">
		<h2>
			Welcome, Admin
			<%=adminName%>!
		</h2>

		<%
		if (request.getParameter("success") != null) {
		%>
		<div class="alert alert-success text-center"><%=request.getParameter("success")%></div>
		<%
		}
		%>
		<%
		if (request.getParameter("error") != null) {
		%>
		<div class="alert alert-danger text-center"><%=request.getParameter("error")%></div>
		<%
		}
		%>

		<div class="row">
			<!-- Left Column: Manage Students & Assign Subjects -->
			<div class="col-md-6">

				<!-- Manage Students -->
				<div class="section">
					<h4>Manage Students</h4>
					<form action="ManageStudentsServlet" method="post">
						<input type="text" name="name" class="form-control mb-1"
							placeholder="Student Name"> <input type="email"
							name="email" class="form-control mb-1" placeholder="Email"
							required> <input type="password" name="password"
							class="form-control mb-1" placeholder="Password">
						<div class="btn-group d-flex">
							<button type="submit" name="action" value="add"
								class="btn btn-success flex-fill"
								onclick="confirmAction('Add?', event)">Add</button>
							<button type="submit" name="action" value="remove"
								class="btn btn-danger flex-fill"
								onclick="confirmAction('Remove?', event)">Remove</button>
						</div>
					</form>
				</div>

				<!-- Assign Subjects to Teachers -->
				<div class="section">
					<h4>Assign Subjects</h4>
					<form action="AssignSubjectsServlet" method="post">
						<select name="teacherId" class="form-control mb-1">
							<option value="">Select Teacher</option>
							<%
							try (Connection conn = DBConnection.getConnection();
									PreparedStatement ps = conn.prepareStatement("SELECT teacher_id, name FROM teachers")) {
								ResultSet rs = ps.executeQuery();
								while (rs.next()) {
							%>
							<option value="<%=rs.getInt("teacher_id")%>"><%=rs.getString("name")%></option>
							<%
							}
							} catch (Exception e) {
							e.printStackTrace();
							}
							%>
						</select> <select name="subjectId" class="form-control mb-1">
							<option value="">Select Subject</option>
							<%
							try (Connection conn = DBConnection.getConnection();
									PreparedStatement ps = conn.prepareStatement("SELECT subject_id, name FROM subjects")) {
								ResultSet rs = ps.executeQuery();
								while (rs.next()) {
							%>
							<option value="<%=rs.getInt("subject_id")%>"><%=rs.getString("name")%></option>
							<%
							}
							} catch (Exception e) {
							e.printStackTrace();
							}
							%>
						</select>

						<button type="submit" class="btn btn-primary btn-block"
							onclick="confirmAction('Assign?', event)">Assign</button>
					</form>
				</div>

			</div>

			<!-- Right Column: View Reports, Download Reports & Manage Subjects -->
			<div class="col-md-6">

				<!-- View Attendance Reports -->
				<div class="section">
					<h4>View Reports</h4>
					<form action="ViewAttendanceServlet" method="get"
						class="form-inline">
						<input type="date" name="fromDate" class="form-control mr-1"
							required> <input type="date" name="toDate"
							class="form-control mr-1" required>
						<button type="submit" class="btn btn-info">View</button>
					</form>
				</div>

				<br>

				<!-- Download Attendance Report -->
				<div class="section">
					<h4>Download Report</h4>
					<form action="ExportAttendanceServlet" method="get"
						class="form-inline">
						<input type="date" name="fromDate" class="form-control mr-1"
							required> <input type="date" name="toDate"
							class="form-control mr-1" required>
						<button type="submit" class="btn btn-warning">Download</button>
					</form>
				</div>

				<br>

				<!-- Manage Subjects -->
				<div class="section">
					<h4>Manage Subjects</h4>
					<form action="ManageSubjectsServlet" method="post">
						<input type="text" name="subjectName" class="form-control mb-1"
							placeholder="Subject Name" required>
						<div class="btn-group d-flex">
							<button type="submit" name="action" value="add"
								class="btn btn-success flex-fill"
								onclick="confirmAction('Add?', event)">Add</button>
							<button type="submit" name="action" value="remove"
								class="btn btn-danger flex-fill"
								onclick="confirmAction('Remove?', event)">Remove</button>
						</div>
					</form>
				</div>

			</div>
		</div>

		<div class="text-center mt-2">
			<a href="login.html" class="btn btn-danger btn-lg"
				onclick="confirmAction('Logout?', event)">Logout</a>
		</div>
	</div>

</body>
</html>
