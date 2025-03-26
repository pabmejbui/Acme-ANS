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

<acme:form> 
	<acme:input-textbox code="manager.leg.form.label.flightNumber" path="flightNumber" readonly="true"/>
	<acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-select code="manager.leg.form.label.status" path="status" choices="${statuses}" readonly="true"/>
	<acme:input-double code="manager.leg.form.label.duration" path="duration" readonly="true"/>
	<acme:input-checkbox code="manager.leg.form.label.draftMode" path="draftMode" readonly="true"/>
	<acme:input-textbox code="manager.leg.form.label.originAirport" path="originAirport" readonly="true"/>
	<acme:input-textbox code="manager.leg.form.label.destinationAirport" path="destinationAirport" readonly="true"/>
	<acme:input-textbox code="manager.leg.form.label.aircraft" path="aircraft" readonly="true"/>
	<acme:input-textbox code="manager.leg.form.label.airline" path="airline" readonly="true"/>
	<acme:input-textbox code="manager.leg.form.label.flight" path="flight" readonly="true"/>
</acme:form>