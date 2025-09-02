<%-- <%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.activity-log.list.label.registration-moment" path="registrationMoment" width="34%"/>
	<acme:list-column code="flight-crew-member.activity-log.form.label.type-of-incident" path="incidentType" width="33%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.severity-level" path="severityLevel" width="33%"/>
	<acme:list-payload path="payload"/>	
	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create"/>
</jstl:if>	 --%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.activity-log.list.label.registration-moment" path="registrationMoment" width="25%"/>
	<acme:list-column code="flight-crew-member.activity-log.form.label.incident-type" path="incidentType" width="25%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.severity" path="severity" width="25%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.flight-assignment" path="flightAssignment" width="25%"/>
	
	<acme:list-payload path="payload"/>	
	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create"/>
</jstl:if>
