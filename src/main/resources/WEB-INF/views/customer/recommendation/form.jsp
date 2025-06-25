<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-textbox code="customer.recommendation.form.label.name" path="name"/>
    <acme:input-textbox code="customer.recommendation.form.label.category" path="category"/>
    <acme:input-textbox code="customer.recommendation.form.label.city" path="city"/>
    <acme:input-textbox code="customer.recommendation.form.label.airport" path="airport.name"/>
    <acme:input-url code="customer.recommendation.form.label.website" path="website"/>
    <acme:input-textarea code="customer.recommendation.form.label.description" path="description"/>
    
</acme:form>