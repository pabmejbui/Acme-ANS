<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <h3 class="page-header"><acme:print code="customer.recommendation.list.title"/></h3>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <acme:list>
                <acme:list-column code="customer.recommendation.list.label.name" path="name" width="30%"/>
                <acme:list-column code="customer.recommendation.list.label.category" path="category" width="20%"/>
                <acme:list-column code="customer.recommendation.list.label.city" path="city" width="20%"/>
                <acme:list-column code="customer.recommendation.list.label.website" path="website" width="30%"/>
            </acme:list>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title"><acme:print code="customer.recommendation.list.info.title"/></h3>
                </div>
                <div class="panel-body">
                    <acme:print code="customer.recommendation.list.explanation"/>
                </div>
            </div>
        </div>
    </div>
</div>