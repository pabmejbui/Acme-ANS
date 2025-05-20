<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${!draftMode}"> 
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flightChoices}" readonly="${!draftMode}"/>
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-textbox code="customer.booking.form.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>	
	<jstl:if test="${_command != 'create'}">
	<acme:input-money code="customer.booking.form.label.price" path="bookingCost" readonly="true"/>
	</jstl:if>
	<acme:input-integer code="customer.booking.form.label.lastCardNibble" path="lastCardNibble"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode}">
		<jstl:if test="${draftMode}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
			<acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete?bookingId=${id}"/>
			<jstl:if test="${_command != 'create'}">
				<acme:button code="customer.booking.form.button.addPassenger" action="/customer/booking-record/create?bookingId=${id}"/>
				<acme:button code="customer.booking.form.button.deletePassenger" action="/customer/booking-record/delete?bookingId=${id}"/>
			</jstl:if>
		</jstl:if>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="customer.booking.form.button.listPassenger" action="/customer/passenger/list?bookingId=${id}"/>
	</jstl:if>
</acme:form>




