<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

	<acme:input-textbox code="manager.leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-select code="manager.leg.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-double code="manager.leg.form.label.duration" path="duration" readonly="true"/>
	<acme:input-checkbox code="manager.leg.form.label.draftMode" path="draftMode" readonly="true"/>
	<acme:input-select code="manager.leg.form.label.originAirport" path="originAirport" choices="${originAirports}"/>
	<acme:input-select code="manager.leg.form.label.destinationAirport" path="destinationAirport" choices="${destinationAirports}"/>
	<acme:input-select code="manager.leg.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>

	<input type="hidden" name="masterId" value="${masterId}" />

	<jstl:choose>

		<jstl:when test="${_command == 'create' && allowCreate}">
			<acme:submit code="manager.leg.form.button.create" action="/manager/leg/create"/>
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="manager.leg.form.button.update" action="/manager/leg/update?id=${id}"/>
			<acme:submit code="manager.leg.form.button.delete" action="/manager/leg/delete?id=${id}"/>
			<acme:submit code="manager.leg.form.button.publish" action="/manager/leg/publish?id=${id}"/>
		</jstl:when>

	</jstl:choose>

</acme:form>
