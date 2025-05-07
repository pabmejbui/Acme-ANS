
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecommendationDashboard extends AbstractForm {

	// Serialisation identifier----------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes-------------------------------------------------

	private String				city;
	private String				name;
	private Double				rating;
	private String				type;
}
