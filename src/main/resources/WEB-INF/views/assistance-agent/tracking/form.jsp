<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="assistance-agent.tracking.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	<acme:input-textbox code="assistance-agent.tracking.form.label.step" path="step"/>
	<acme:input-double code="assistance-agent.tracking.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-select code="assistance-agent.tracking.form.label.indicator" path="indicator" choices="${indicator}"/>
	<acme:input-textarea code="assistance-agent.tracking.form.label.resolution" path="resolution"/>

	<jstl:choose>        
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">    
			<acme:submit code="assistance-agent.tracking.form.button.update" action="/assistance-agent/tracking/update"/>
			<acme:submit code="assistance-agent.tracking.form.button.delete" action="/assistance-agent/tracking/delete"/>
			<acme:submit code="assistance-agent.tracking.form.button.publish" action="/assistance-agent/tracking/publish"/>
		</jstl:when>
        
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.tracking.form.button.create" action="/assistance-agent/tracking/create?claimId=${claimId}"/>
		</jstl:when>    
	</jstl:choose>

</acme:form>