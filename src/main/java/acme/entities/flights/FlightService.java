
package acme.entities.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acme.client.services.AbstractService;

@Service
@Transactional
public class FlightService extends AbstractService {

	@Autowired
	private FlightRepository flightRepository;


	public Flight loadFlightWithDerivedAttributes(final int flightId) {
		Flight flight = this.flightRepository.findOneFlightById(flightId);

		flight.setScheduledDeparture(this.flightRepository.findScheduledDeparture(flightId));
		flight.setScheduledArrival(this.flightRepository.findScheduledArrival(flightId));
		flight.setOriginCity(this.flightRepository.findOriginCity(flightId));
		flight.setDestinationCity(this.flightRepository.findDestinationCity(flightId));

		Integer layovers = this.flightRepository.countLayovers(flightId);
		flight.setNumberOfLayovers(layovers != null && layovers >= 0 ? layovers : 0);

		return flight;
	}
}
