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

<acme:form>
    <acme:input-textbox code="any.weather-condition.form.label.city" path="city" readonly="true"/>
    <acme:input-textbox code="any.weather-condition.form.label.country" path="country" readonly="true"/>
    <acme:input-textbox code="any.weather-condition.form.label.temperature" path="temperature" readonly="true"/>
    <acme:input-textbox code="any.weather-condition.form.label.humidity" path="humidity" readonly="true"/>
    <acme:input-textbox code="any.weather-condition.form.label.windSpeed" path="windSpeed" readonly="true"/>
    <acme:input-textbox code="any.weather-condition.form.label.description" path="description" readonly="true"/>
    <acme:input-moment code="any.weather-condition.form.label.reportTime" path="reportTime" readonly="true"/>
</acme:form>
