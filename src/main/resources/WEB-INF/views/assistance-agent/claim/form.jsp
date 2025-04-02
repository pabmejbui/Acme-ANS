
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	
	<acme:input-moment  code ="assistance-agent.claim.form.label.registrationMoment" path = "registrationMoment" readonly="true"/>
	<acme:input-email code ="assistance-agent.claim.form.label.passengerEmail" path ="passengerEmail"/>
	<acme:input-textarea  code ="assistance-agent.claim.form.label.description" path ="description"/>
	<acme:input-select  code ="assistance-agent.claim.form.label.type" path ="type" choices = "${types}"/>
	<acme:input-select code ="assistance-agent.claim.form.label.leg" path ="leg" choices = "${legs}"/>
	<acme:input-textbox code ="assistance-agent.claim.form.label.status" path ="indicator" readonly="true"/>
	<acme:input-checkbox code ="assistance-agent.claim.form.label.draftMode" path ="draftMode" readonly="true"/>

	
	<jstl:choose>    
        <jstl:when test="${_command == 'show'}">  
            <acme:button code="assistance-agent.claim.form.button.details" action="/assistance-agent/claim/show?id=${id}"/>  
        </jstl:when>  
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">  
            <acme:button code="assistance-agent.claim.form.button.details" action="/assistance-agent/claim/show?id=${id}"/>  
            <acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>  
            <acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>  
            <acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>  
        </jstl:when>  
        <jstl:when test="${_command == 'create'}">  
            <acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>  
        </jstl:when>    
    </jstl:choose>  
	
	
</acme:form>