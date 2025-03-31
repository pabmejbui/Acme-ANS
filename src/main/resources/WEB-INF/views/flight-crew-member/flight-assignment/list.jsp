<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.moment" path="moment" width="50%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.currentStatus" path="currentStatus" width="25%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.duty" path="duty" width="25%"/>
	<acme:list-payload path="payload"/>	
	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="flightCrewMember.flightAssignment.list.button.create" action="/flightCrewMember/flightAssignment/create"/>
</jstl:if>	