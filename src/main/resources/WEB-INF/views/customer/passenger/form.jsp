<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.passenger.form.label.fullName" path="fullName"/>
    <acme:input-textbox code="customer.passenger.form.label.email" path="email"/>
    <acme:input-textbox code="customer.passenger.form.label.passportNumber" path="passportNumber"/>
    <acme:input-textbox code="customer.passenger.form.label.dateOfBirth" path="dateOfBirth"/>
    <acme:input-textbox code="customer.passenger.form.label.specialNeeds" path="specialNeeds"/>

    <jstl:choose>
        <jstl:when test="${_command == 'update' && isDraftMode}">
            <acme:submit code="customer.passenger.form.button.save" action="/customer/passenger/update"/>
        </jstl:when>
    </jstl:choose>

    <jstl:if test="${_command == 'create'}">
        <acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
    </jstl:if>
</acme:form>
