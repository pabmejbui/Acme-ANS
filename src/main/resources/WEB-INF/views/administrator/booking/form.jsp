
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.booking.form.label.locatorCode"
		path="locatorCode" readonly="true" />
	<acme:input-textbox code="administrator.booking.form.label.travelClass"
		path="travelClass" readonly="true" />
	<acme:input-textbox
		code="administrator.booking.form.label.lastCardNibble"
		path="lastCardNibble" readonly="true" />
	<acme:input-textbox
		code="administrator.booking.form.label.purchaseMoment"
		path="purchaseMoment" readonly="true" />
	<acme:input-textbox code="administrator.booking.form.label.passengers"
		path="passengers" readonly="true" />
	<acme:input-money code="administrator.booking.form.label.price"
		path="bookingCost" readonly="true" />

</acme:form>