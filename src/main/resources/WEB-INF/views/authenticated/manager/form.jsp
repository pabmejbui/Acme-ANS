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

<%@page %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="authenticated.manager.form.label.identifier" path="identifier"/>
    <acme:input-integer code="authenticated.manager.form.label.experience" path="experience"/>
    <acme:input-moment code="authenticated.manager.form.label.birthDate" path="birthDate"/>
    <acme:input-url code="authenticated.manager.form.label.pictureLink" path="pictureLink"/>

    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="authenticated.manager.form.button.create" action="/authenticated/manager/create"/>
        </jstl:when>
        <jstl:when test="${_command == 'update'}">
            <acme:submit code="authenticated.manager.form.button.update" action="/authenticated/manager/update"/>
        </jstl:when>
    </jstl:choose>
</acme:form>

