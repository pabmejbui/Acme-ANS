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
    <acme:list-column path="city" code="any.weather.list.city" />
    <acme:list-column path="country" code="any.weather.list.country" />
    <acme:list-column path="temperature" code="any.weather.list.temperature" />
    <acme:list-column path="humidity" code="any.weather.list.humidity" />
    <acme:list-column path="windSpeed" code="any.weather.list.windSpeed" />
    <acme:list-column path="description" code="any.weather.list.description" />
    <acme:list-column path="reportTime" code="any.weather.list.reportTime" />
</acme:list>
