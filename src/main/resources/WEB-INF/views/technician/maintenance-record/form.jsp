


<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	
	<acme:input-select code = "technician.maintenance-record.form.label.maintenanceStatus" path = "maintenanceStatus" choices="${maintenanceStatus}"/>
	<acme:input-moment code = "technician.maintenance-record.form.label.nextInspectionDate" path = "nextInspectionDate"/>
	<acme:input-money code = "technician.maintenance-record.form.label.estimatedCost" path = "estimatedCost"/>
	<acme:input-textbox code = "technician.maintenance-record.form.label.notes" path = "notes"/>
	<acme:input-select code = "technician.maintenance-record.form.label.aircraft" path = "aircraft" choices="${aircraft}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete') }">
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>	
			<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>	
		</jstl:when>
		<jstl:when  test="${acme:anyOf(_command,'create')}">
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>