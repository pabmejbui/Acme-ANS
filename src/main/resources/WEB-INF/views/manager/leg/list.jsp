<%--
- list.jsp
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

<acme:list>
	<acme:list-column code="manager.leg.list.label.flight-number" path="flightNumber" width="15%"/>
	<acme:list-column code="manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="15%"/>
	<acme:list-column code="manager.leg.list.label.scheduledArrival" path="scheduledArrival" width="15%"/>
	<acme:list-column code="manager.leg.list.label.status" path="status" width="10%"/>
	<acme:list-column code="manager.leg.list.label.duration" path="duration" width="10%"/>	
	<acme:list-column code="manager.leg.list.label.originAirport" path="originAirport" width="15%"/>	
	<acme:list-column code="manager.leg.list.label.destinationAirport" path="destinationAirport" width="15%"/>	
	<acme:list-column code="manager.leg.list.label.draftMode" path="draftMode" width="5%"/>	
</acme:list>


	
