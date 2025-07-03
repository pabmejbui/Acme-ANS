<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="technician.maintenance-record-task.form.label.aircraft"
		path="aircraftRegistrationNumber" readonly="true"/>
	<acme:input-select code="technician.maintenance-record-task.form.label.task"
		path="task" choices="${tasks}" />
	<acme:hidden-data path="maintenanceRecordId"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.maintenance-record-task.form.button.link"
				action="/technician/maintenance-record-task/create" />
		</jstl:when>
		<jstl:when test="${_command == 'delete'}">
			<acme:submit code="technician.maintenance-record-task.form.button.unlink"
				action="/technician/maintenance-record-task/delete" />
		</jstl:when>
	</jstl:choose>
</acme:form>