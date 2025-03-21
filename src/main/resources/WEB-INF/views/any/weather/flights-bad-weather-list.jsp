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
<%@taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
    <acme:list-column path="tag" code="any.flight.list.tag" />
    <acme:list-column path="scheduledDeparture" code="any.flight.list.scheduledDeparture" />
    <acme:list-column path="scheduledArrival" code="any.flight.list.scheduledArrival" />
    <acme:list-column path="originCity" code="any.flight.list.originCity" />
    <acme:list-column path="destinationCity" code="any.flight.list.destinationCity" />
</acme:list>

