<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.attendance.db.DBConnection"%>
<%@ page session="true"%>

<%
String teacherName = (String) session.getAttribute("user");
if (teacherName == null) {
	response.sendRedirect("login.html");
	return;
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Teacher Dashboard - Attendance</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<script>
        function confirmAction(message, event) {
            if (!confirm(message)) {
                event.preventDefault();
            }
        }

        setTimeout(() => {
            document.querySelectorAll('.alert').forEach(alert => alert.style.display = 'none');
        }, 5000);
    </script>

<style>
body {
	font-family: Arial, sans-serif;
	background-color: #6CB4EE;
}

.container {
	margin-top: 30px;
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
}

h2, h4 {
	color: #007BFF;
	text-align: center;
}

.form-section {
	padding: 15px;
	border-radius: 8px;
	box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
	background: white;
	margin-bottom: 20px;
}

.form-inline .form-group {
	margin-right: 10px;
}

.btn-block {
	font-size: 14px;
	padding: 8px;
}

.row {
	display: flex;
	flex-wrap: wrap;
}
</style>
</head>

<body>
	<div class="container">
		<h2>
			Welcome,
			<%=teacherName%>!
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
			<!-- Mark Attendance -->
			<div class="col-md-6">
				<div class="form-section">
					<h4>Mark Attendance</h4>
					<form action="MarkAttendanceServlet" method="post">
						<label>Student ID:</label> <input type="text" name="studentId"
							class="form-control mb-2" required> <label>Subject:</label>
						<select name="subjectId" class="form-control mb-2">
							<%
							try (Connection conn = DBConnection.getConnection()) {
								PreparedStatement ps = conn.prepareStatement("SELECT subject_id, name FROM subjects");
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
						</select> <label>Date:</label> <input type="date" name="attendanceDate"
							class="form-control mb-2" required> <label>Status:</label>
						<select name="status" class="form-control mb-2">
							<option value="Present">Present</option>
							<option value="Absent">Absent</option>
						</select>

						<button type="submit" class="btn btn-success btn-block"
							onclick="confirmAction('Mark attendance?', event);">Mark
							Attendance</button>
					</form>
				</div>
			</div>

			<!-- View & Download Attendance -->
			<div class="col-md-6">
				<div class="form-section">
					<h4>View Attendance</h4>
					<form action="ViewAttendanceServlet" method="get"
						class="form-inline">
						<div class="form-group">
							<label class="mr-2">From:</label> <input type="date"
								name="fromDate" class="form-control mr-2" required>
						</div>
						<div class="form-group">
							<label class="mr-2">To:</label> <input type="date" name="toDate"
								class="form-control mr-2" required>
						</div>
						<br>
						<br> <br>
						<br>
						<button type="submit" class="btn btn-info btn-block">View</button>
					</form>
				</div>

				<div class="form-section">
					<h4>Download Report</h4>
					<form action="ExportAttendanceServlet" method="get"
						class="form-inline">
						<div class="form-group">
							<label class="mr-2">From:</label> <input type="date"
								name="fromDate" class="form-control mr-2" required>
						</div>
						<div class="form-group">
							<label class="mr-2">To:</label> <input type="date" name="toDate"
								class="form-control mr-2" required>
						</div>
						<br>
						<br> <br>
						<br>
						<button type="submit" class="btn btn-warning btn-block">Download</button>
					</form>
				</div>
			</div>
		</div>

		<div class="text-center mt-2">
			<a href="login.html" class="btn btn-danger btn-lg"
				onclick="confirmAction('Are you sure you want to logout?', event)">Logout</a>
		</div>
	</div>
</body>
</html>
