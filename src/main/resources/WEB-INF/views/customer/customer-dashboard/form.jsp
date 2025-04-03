<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="customer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.totalMoneySpentLastYear"/></th>
		<td><acme:print value="${totalMoneySpentBookingLastYear}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.lastFiveDestinations"/></th>
		<td>
			<ul>
				<jstl:forEach var="destination" items="${lastFiveDestinations}">
					<li><acme:print value="${destination}"/></li>
				</jstl:forEach>
			</ul>
		</td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.totalBookings"/></th>
		<td><acme:print value="${totalBookings}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.economyBookings"/></th>
		<td><acme:print value="${economyBookings}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.businessBookings"/></th>
		<td><acme:print value="${businessBookings}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.avgBookingCost"/></th>
		<td><acme:print value="${avgBookingCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.minBookingCost"/></th>
		<td><acme:print value="${minBookingCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.maxBookingCost"/></th>
		<td><acme:print value="${maxBookingCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.stdBookingCost"/></th>
		<td><acme:print value="${stdDevBookingCost}"/></td>
	</tr>
</table>

<h2><acme:print code="customer.dashboard.form.title.passenger-statistics"/></h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.totalPassengers"/></th>
		<td><acme:print value="${totalPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.avgPassengersPerBooking"/></th>
		<td><acme:print value="${avgPassengersPerBooking}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.minPassengersPerBooking"/></th>
		<td><acme:print value="${minPassengersPerBooking}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.maxPassengersPerBooking"/></th>
		<td><acme:print value="${maxPassengersPerBooking}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.stdPassengersPerBooking"/></th>
		<td><acme:print value="${stdDevPassengersPerBooking}"/></td>
	</tr>
</table>

<h2><acme:print code="customer.dashboard.form.title.booking-class-ratio"/></h2>

<div>
	<canvas id="bookingClassChart"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [ "ECONOMY", "BUSINESS" ],
			datasets : [{
				data : [
					<jstl:out value="${economyBookings}" />,
					<jstl:out value="${businessBookings}" />
				]
			}]
		};
		var options = {
			scales : {
				yAxes : [{
					ticks : {
						suggestedMin : 0,
						suggestedMax : Math.max(<jstl:out value="${economyBookings}" />, <jstl:out value="${businessBookings}" />)
					}
				}]
			},
			legend : {
				display : false
			}
		};
		var canvas = document.getElementById("bookingClassChart");
		var context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>
