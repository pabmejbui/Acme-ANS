
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	
	<acme:input-moment  code ="assistance-agent.claim.form.label.registrationMoment" path = "registrationMoment"/>
	<acme:input-email code ="assistance-agent.claim.form.label.passengerEmail" path ="passengerEmail"/>
	<acme:input-textarea  code ="assistance-agent.claim.form.label.description" path ="description"/>
	<acme:input-select  code ="assistance-agent.claim.form.label.type" path ="type" choices = "${types}"/>
	<acme:input-select code ="assistance-agent.claim.form.label.leg" path ="leg" choices = "${legs}"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.status" path ="status" readonly="true"/>
	<acme:input-checkbox code ="assistance-agent.claim.form.label.draftMode" path ="DraftMode" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>		
	</jstl:choose>
	
</acme:form>