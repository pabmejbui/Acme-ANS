<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link"
				action="http://www.example.com/" />
			<acme:menu-suboption code="master.menu.anonymous.pablo-mejias"
				action="http://www.sevillafc.es/" />
			<acme:menu-suboption code="master.menu.anonymous.francisco-perez"
				action="https://www.chollometro.com/" />
			<acme:menu-suboption code="master.menu.anonymous.andres-garcia"
				action="https://chatgpt.com/" />
			<acme:menu-suboption code="master.menu.anonymous.alejandro-corral"
				action="https://www.youtube.com/" />
			<acme:menu-suboption code="master.menu.anonymous.jesus-oria"
				action="https://www.philadelphiaeagles.com/" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator"
			access="hasRealm('Administrator')">
			<acme:menu-suboption
				code="master.menu.administrator.list-user-accounts"
				action="/administrator/user-account/list" />
			<acme:menu-separator />
			<acme:menu-suboption 
				code="master.menu.administrator.list-aircrafts"
				action="/administrator/aircraft/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-services"
				action="/administrator/service/list" />
			<acme:menu-separator />
			<acme:menu-suboption code="master.menu.administrator.list-airlines"
				action="/administrator/airline/list" />
			<acme:menu-separator />
			<acme:menu-suboption code="master.menu.administrator.list-airports"
				action="/administrator/airport/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-bookings"
				action="/administrator/booking/list" />
			<acme:menu-separator />
			<acme:menu-suboption
				code="master.menu.administrator.initialise-recommendation"
				action="/administrator/system/initialise-recommendation" />
			<acme:menu-suboption
				code="master.menu.administrator.populate-db-initial"
				action="/administrator/system/populate-initial" />
			<acme:menu-suboption
				code="master.menu.administrator.populate-db-sample"
				action="/administrator/system/populate-sample" />
			<acme:menu-separator />
			<acme:menu-suboption
				code="master.menu.administrator.shut-system-down"
				action="/administrator/system/shut-down" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider"
			access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link"
				action="http://www.example.com/" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer"
			access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link"
				action="http://www.example.com/" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.customer"
			access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.list-bookings"
				action="/customer/booking/list" />
			<acme:menu-suboption code="master.menu.customer.list-passengers"
				action="/customer/passenger/list" />				
			<acme:menu-suboption code="master.menu.customer.dashboard"
				action="/customer/customer-dashboard/show" />
			<acme:menu-suboption code="master.menu.customer.recommendations"
			 action="/customer/recommendation/list"/>
		</acme:menu-option>


		<%-- <acme:menu-option code="master.menu.flightCrewMember" access="hasRealm('FlightCrewMember')">
 			<acme:menu-suboption code="master.menu.flightCrewMember.flightAssignment" action="/flight-crew-member/flight-assignment/list"/>
 		</acme:menu-option> --%>

		<acme:menu-option code="master.menu.flightCrewMember"
			access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption
				code="master.menu.flightCrewMember.flightAssignmentCompleted"
				action="/flight-crew-member/flight-assignment/list-completed" />
			<acme:menu-suboption
				code="master.menu.flightCrewMember.flightAssignmentPlanned"
				action="/flight-crew-member/flight-assignment/list-planned" />
			<acme:menu-suboption code="master.menu.flightCrewMember.activityLogs"
				action="/flight-crew-member/activity-log/list" />
		</acme:menu-option>
		
		
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.list-my-maintenance-records" action="/technician/maintenance-record/list?mine=true"/>			
			<acme:menu-suboption code="master.menu.technician.list-my-tasks" action="/technician/task/list?mine=true"/>
		</acme:menu-option>
	
		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list-flights" action="/manager/flight/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.manager.dashboard" action="/manager/manager-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.assistance-agent.claims" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistance-agent.claims.claims-list" action="/assistance-agent/claim/list"/>
			<acme:menu-suboption code="master.menu.assistance-agent.claims.list-claims-pending" action="/assistance-agent/claim/pending"/>	
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any" access="isAuthenticated() or isAnonymous()">
    		<acme:menu-suboption code="master.menu.any.flight.list" action="/any/flight/list"/>
    		<acme:menu-separator/>
    		<acme:menu-suboption code="master.menu.any.weather.list" action="/any/weather-condition/list"/>
			<acme:menu-suboption code="master.menu.any.flight-under-bad-weather.list" action="/any/flight-under-bad-weather/list-under-bad-weather"/>
		</acme:menu-option>	

	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.user-account"
			access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile"
				action="/authenticated/user-account/update" />

			<acme:menu-suboption code="master.menu.user-account.become-provider"
				action="/authenticated/provider/create"
				access="!hasRealm('Provider')" />
			<acme:menu-suboption code="master.menu.user-account.provider-profile"
				action="/authenticated/provider/update"
				access="hasRealm('Provider')" />
			<acme:menu-suboption code="master.menu.user-account.become-consumer"
				action="/authenticated/consumer/create"
				access="!hasRealm('Consumer')" />
			<acme:menu-suboption code="master.menu.user-account.consumer-profile"
				action="/authenticated/consumer/update"
				access="hasRealm('Consumer')" />
			<acme:menu-suboption code="master.menu.user-account.become-customer"
				action="/authenticated/customer/create"
				access="!hasRealm('Customer')" />
			<acme:menu-suboption code="master.menu.user-account.customer-profile"
				action="/authenticated/customer/update"
				access="hasRealm('Customer')" />
			<acme:menu-suboption code="master.menu.user-account.become-manager"
				action="/authenticated/manager/create" access="!hasRealm('Manager')" />
			<acme:menu-suboption code="master.menu.user-account.manager-profile"
				action="/authenticated/manager/update" access="hasRealm('Manager')" />
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>