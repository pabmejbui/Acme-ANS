<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="manager.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.ranking"/></th>
		<td><acme:print value="${rankingBasedOneExperience}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.yearsToRetire"/></th>
		<td><acme:print value="${yearsToRetire}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.avgCost"/></th>
		<td><acme:print value="${avgDeviationOfCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.minCost"/></th>
		<td><acme:print value="${minDeviationOfCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.maxCost"/></th>
		<td><acme:print value="${maxDeviationOfCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.stdCost"/></th>
		<td><acme:print value="${standardDeviationOfCost}"/></td>
	</tr>
</table>

<h2><acme:print code="manager.dashboard.form.title.leg-ratios"/></h2>

<div>
	<canvas id="ratiosChart"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [ "ON_TIME", "DELAYED" ],
			datasets : [{
				data : [
					<jstl:out value="${ratioOnTimeLegs}" />, 
					<jstl:out value="${ratioDelayedLegs}" />
				]
			}]
		};
		var options = {
			scales : {
				yAxes : [{
					ticks : {
						suggestedMin : 0.0,
						suggestedMax : 1.0
					}
				}]
			},
			legend : {
				display : false
			}
		};

		var canvas = document.getElementById("ratiosChart");
		var context = canvas.getContext("2d");

		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>

