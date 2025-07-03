<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code ="technician.maintenance-record.list.label.moment" path ="moment" width ="20%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.status" path ="status" width ="20%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.estimatedCost" path ="estimatedCost" width ="20%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.nextInspectionDate" path ="nextInspectionDate" width ="20%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.published" path ="draftMode" width ="20%"/>	
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>
	