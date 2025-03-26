<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.booking.form.label.locatorCode"
		path="locatorCode" />
	<acme:input-select code="customer.booking.form.label.travelClass"
		path="travelClass" choices="${travelClasses}" />
	<acme:input-money code="customer.booking.form.label.price" path="price" />
	<acme:input-textbox code="customer.booking.form.label.lastCardNibble"
		path="lastCardNibble" />

	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="customer.booking.form.label.confirmation"
				path="confirmation" />
			<acme:submit code="customer.booking.form.button.create"
				action="/customer/booking/create" />
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="customer.booking.form.button.update"
				action="/customer/booking/update" />
		</jstl:when>
	</jstl:choose>
</acme:form>