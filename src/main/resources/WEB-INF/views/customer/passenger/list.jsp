<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="30%"/>
    <acme:list-column code="customer.passenger.list.label.email" path="email" width="30%"/>

    <acme:list-payload path="payload"/>

    <!-- Botón para publicar pasajeros en modo borrador -->
    <jstl:forEach var="passenger" items="${passengers}">
        <jstl:if test="${passenger.draftMode}">
            <acme:button code="customer.passenger.list.button.publish" action="/customer/passenger/publish?id=${passenger.id}"/>
        </jstl:if>
    </jstl:forEach>
</acme:list>

<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
