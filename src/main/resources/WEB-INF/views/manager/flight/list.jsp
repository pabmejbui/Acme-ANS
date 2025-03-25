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
    <acme:list-column code="manager.flight.list.label.tag" path="tag" width="15%"/>
    <acme:list-column code="manager.flight.list.label.origin" path="originCity" width="20%"/>
    <acme:list-column code="manager.flight.list.label.destination" path="destinationCity" width="20%"/>
    <acme:list-column code="manager.flight.list.label.departure" path="scheduledDeparture" width="15%"/>
    <acme:list-column code="manager.flight.list.label.arrival" path="scheduledArrival" width="15%"/>
    <acme:list-column code="manager.flight.list.label.draftMode" path="draftMode" width="10%"/>
    <acme:list-payload path="payload"/>
</acme:list>

<acme:button code="manager.flight.list.button.create" action="/manager/flight/create"/>
