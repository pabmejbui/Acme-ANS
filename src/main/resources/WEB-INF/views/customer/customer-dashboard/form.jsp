<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="customer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.spentMoneyLastYear"/></th>
		<td><acme:print value="${spentMoneyLastYear}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.lastFiveDestinations"/></th>
		<td>
			<ul>
				<jstl:forEach var="destination" items="${lastFiveDestinations}">
					<li><jstl:out value="${destination}"/></li>
				</jstl:forEach>
			</ul>
		</td>
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
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingAverageCost"/></th>
		<td><acme:print value="${bookingAverageCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingMinimumCost"/></th>
		<td><acme:print value="${bookingMinimumCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingMaximumCost"/></th>
		<td><acme:print value="${bookingMaximumCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingDeviationCost"/></th>
		<td><acme:print value="${bookingDeviationCost}"/></td>
	</tr>
</table>

<h2><acme:print code="customer.dashboard.form.title.passenger-statistics"/></h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingCountPassengers"/></th>
		<td><acme:print value="${bookingCountPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingAveragePassengers"/></th>
		<td><acme:print value="${bookingAveragePassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingMinimumPassengers"/></th>
		<td><acme:print value="${bookingMinimumPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingMaximumPassengers"/></th>
		<td><acme:print value="${bookingMaximumPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="customer.dashboard.form.label.bookingDeviationPassengers"/></th>
		<td><acme:print value="${bookingDeviationPassengers}"/></td>
	</tr>
</table>

<h2><acme:print code="customer.dashboard.form.title.booking-class-ratio"/></h2>

<div>
	<canvas id="bookingClassChart"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		const economy = ${economyBookings};
		const business = ${businessBookings};

		const data = {
			labels: ["ECONOMY", "BUSINESS"],
			datasets: [{
				data: [economy, business],
				backgroundColor: ["#4e73df", "#1cc88a"]
			}]
		};

		const options = {
			scales: {
				y: {
					beginAtZero: true,
					suggestedMax: Math.max(economy, business) + 1
				}
			},
			plugins: {
				legend: { display: false }
			}
		};

		new Chart(document.getElementById("bookingClassChart").getContext("2d"), {
			type: "bar",
			data: data,
			options: options
		});
	});
</script>

<acme:return/>
