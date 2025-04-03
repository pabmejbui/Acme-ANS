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

    <acme:list-column code="any.weather-condition.list.label.city" path="city"/>
    <acme:list-column code="any.weather-condition.list.label.country" path="country"/>
    <acme:list-column code="any.weather-condition.list.label.temperature" path="temperature"/>
    <acme:list-column code="any.weather-condition.list.label.windSpeed" path="windSpeed"/>
    <acme:list-column code="any.weather-condition.list.label.reportTime" path="reportTime"/>

    <acme:list-column code="any.weather-condition.list.label.actions" path="id"/>
    
</acme:list>

