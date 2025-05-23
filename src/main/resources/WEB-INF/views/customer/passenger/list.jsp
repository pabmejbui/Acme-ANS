<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.dateOfBirth" path="dateOfBirth" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.specialNeeds" path="specialNeeds" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.draftMode" path="draftMode" width="20%"/>
	<acme:list-payload path="payload"/>	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
</jstl:if>	


<style>
    td {
        max-width: 200px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
</style>