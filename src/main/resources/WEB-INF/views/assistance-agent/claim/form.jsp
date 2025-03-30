

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>

	<jstl:set var="isShowOrUpdate" value="${_command eq 'show' or _command eq 'update'}"/>
	
	<acme:input-textbox code ="assistance-agent.claim.form.label.registrationMoment" path = "registrationMoment"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.passengerEmail" path ="passengerEmail"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.description" path ="description"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.type" path ="type"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.indicator" path ="indicator" readonly="${isShowOrUpdate}"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.assistanceAgent" path ="assistanceAgent" readonly="${isShowOrUpdate}"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.leg" path ="leg" readonly="${isShowOrUpdate}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update')}">
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update/"/>	
		</jstl:when>
		<jstl:when  test="${acme:anyOf(_command,'create')}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>
	</jstl:choose>
	
	<acme:button code="assistance-agent.claim.form.button.tracking-log" action="/assistance-agent/tracking-log/list"/>
</acme:form>