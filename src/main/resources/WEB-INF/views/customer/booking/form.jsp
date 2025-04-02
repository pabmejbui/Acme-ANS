<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.booking.form.label.locatorCode"
		path="locatorCode" />
	<acme:input-select code="customer.booking.form.label.travelClass"
		path="travelClass" choices="${travelClasses}" />

	<acme:input-textbox code="customer.booking.form.label.lastCardNibble"
		path="lastCardNibble" />
	<acme:input-textbox code="customer.booking.form.label.purchaseMoment" path="purchaseMoment" readonly="true" />
    <acme:input-textbox code="customer.booking.form.label.passengers" path="passengers" readonly="true" />
	<acme:input-money code="customer.booking.form.label.price" path="bookingCost" readonly="true"/>

	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="customer.booking.form.button.passengers" action="/customer/passenger/list?masterId=${id}"/>
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>



