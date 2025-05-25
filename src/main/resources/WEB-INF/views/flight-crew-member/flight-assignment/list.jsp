<%--
- list.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.leg"  path="leg" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.duty" path="duty" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.lastUpdate" path="lastUpdate" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.status"  path="status" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.remarks"  path="remarks" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.draftMode"  path="draftMode" width="10%"/>
	
	<acme:list-payload path="payload"/>
</acme:list>
	
<acme:button code="flight-crew-member.flight-assignment.list.label.create" action="/flight-crew-member/flight-assignment/create"/>