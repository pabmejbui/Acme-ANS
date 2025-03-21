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

<acme:form readonly="true">
    <acme:input-textbox path="city" code="any.weather.show.city" />
    <acme:input-textbox path="country" code="any.weather.show.country" />
    <acme:input-double path="temperature" code="any.weather.show.temperature" />
        <acme:input-double path="humidity" code="any.weather.show.humidity" />
    <acme:input-double path="windSpeed" code="any.weather.show.windSpeed" />
    <acme:input-textbox path="description" code="any.weather.show.description" />
    <acme:input-moment path="reportTime" code="any.weather.show.reportTime" />
</acme:form>

