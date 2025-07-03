<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.moment" path="moment" width="25%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="25%"/>
	<acme:list-column code="technician.maintenance-record.list.label.nextInspectionDate" path="nextInspectionDate" width="26%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>
	