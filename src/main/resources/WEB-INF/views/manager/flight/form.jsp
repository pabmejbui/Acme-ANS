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
    <acme:input-textbox code="manager.flight.form.label.tag" path="tag" readonly="${readonly}" />
    <acme:input-money code="manager.flight.form.label.cost" path="cost" readonly="${readonly}" />
    <acme:input-textarea code="manager.flight.form.label.description" path="description" readonly="${readonly}" />

    <jstl:choose>
        <jstl:when test="${readonly}">
            <acme:input-textbox code="manager.flight.form.label.indication" path="indication" readonly="true" />
        </jstl:when>
        <jstl:otherwise>
            <acme:input-select code="manager.flight.form.label.indication" path="indication" choices="${indications}" />
        </jstl:otherwise>
    </jstl:choose>

    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="manager.flight.form.button.save" action="/manager/flight/create" />
        </jstl:when>
        <jstl:when test="${_command == 'show'}">
            <acme:submit code="manager.flight.form.button.update" action="/manager/flight/update?id=${id}" />
            <acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete?id=${id}" />
            <acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish?id=${id}" />
        </jstl:when>
    </jstl:choose>
</acme:form>
