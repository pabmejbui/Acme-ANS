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

<acme:form readonly="${readonly}">
    <acme:input-textbox code="any.weather.form.label.city" path="city"/>
    <acme:input-textbox code="any.weather.form.label.country" path="country"/>
    <acme:input-double code="any.weather.form.label.temperature" path="temperature"/>
    <acme:input-double code="any.weather.form.label.humidity" path="humidity"/>
    <acme:input-double code="any.weather.form.label.windSpeed" path="windSpeed"/>
    <acme:input-textarea code="any.weather.form.label.description" path="description"/>
    <acme:input-textbox code="any.weather.form.label.reportTime" path="reportTime"/>
</acme:form>

