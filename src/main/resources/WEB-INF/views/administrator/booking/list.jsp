<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="administrator.booking.list.label.locatorCode" path="locatorCode" width="30%"/>
    <acme:list-column code="administrator.booking.list.label.purchaseMoment" path="purchaseMoment" width="30%"/>
    <acme:list-column code="administrator.booking.list.label.price" path="bookingCost" width="30%"/>
</acme:list>
