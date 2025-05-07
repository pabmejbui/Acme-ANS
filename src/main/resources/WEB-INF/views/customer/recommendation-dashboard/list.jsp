<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	<acme:list-column code="customer.recommendation-dashboard.list.label.city" path="city" width="30%"/>
	<acme:list-column code="customer.recommendation-dashboard.list.label.name" path="name" width="30%"/>
	<acme:list-column code="customer.recommendation-dashboard.list.label.recommendationType" path="type" width="30%"/>
	<acme:list-column code="customer.recommendation-dashboard.list.label.rating" path="rating" width="10%"/>
</acme:list>