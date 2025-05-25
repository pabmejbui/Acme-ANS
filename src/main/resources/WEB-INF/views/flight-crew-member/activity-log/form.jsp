<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.incidentType" path="incidentType" readonly="draftMode" placeholder = "acme.placeholders.form.activityLog.incidentType"/>
	<acme:input-textarea code="flight-crew-member.activity-log.form.label.description" path="description" readonly="draftMode" placeholder = "acme.placeholders.form.activityLog.description"/>
	<acme:input-integer code="flight-crew-member.activity-log.form.label.severity" path="severity" readonly="draftMode" placeholder = "acme.placeholders.form.activityLog.severity"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create' && showAction}">
			<acme:submit code="flight-crew-member.activity-log.form.button.create" action="/flight-crew-member/activity-log/create?assignmentId=${assignmentId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update') && showAction && draftMode == true}">
			<acme:submit code="flight-crew-member.activity-log.form.button.update" action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
		</jstl:when>
	</jstl:choose>
		<jstl:if test="${acme:anyOf(_command, 'show|update') && showAction && draftMode == true && draftModeFlightAssignment == false}">
			<acme:submit code="flight-crew-member.activity-log.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
		</jstl:if>
</acme:form>