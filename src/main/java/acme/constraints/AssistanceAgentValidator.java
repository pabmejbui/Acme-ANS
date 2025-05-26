
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.realms.assistanceAgent.AssistanceAgent;
import acme.realms.assistanceAgent.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


	@Override
	protected void initialise(final ValidAssistanceAgent constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueAssistanceAgent;
				AssistanceAgent existingAssistanceAgent;

				existingAssistanceAgent = this.repository.findAssistanceAgentByEmployeeCode(assistanceAgent.getEmployeeCode());
				uniqueAssistanceAgent = existingAssistanceAgent == null || existingAssistanceAgent.equals(assistanceAgent);

				super.state(context, uniqueAssistanceAgent, "assistanceAgent", "acme.validation.assistance-agent.employee-code-duplicated.message");
			}
			{
				String employeeCode = assistanceAgent.getEmployeeCode();
				boolean matchesPattern = employeeCode.matches("^[A-Z]{2,3}\\d{6}$");

				super.state(context, matchesPattern, "employeeCode", "{acme.validation.assistance-agent.employee-code-invalid-format.message}");

			}
			{
				DefaultUserIdentity identity = assistanceAgent.getUserAccount().getIdentity();
				String employeeCode = assistanceAgent.getEmployeeCode();

				String nameInitial = StringHelper.capitaliseInitial(StringHelper.smallInitial(identity.getName())).substring(0, 1);
				String firstNameInitial = StringHelper.capitaliseInitial(StringHelper.smallInitial(identity.getSurname())).substring(0, 1);
				String expectedInitials = nameInitial + firstNameInitial;

				String employeeCodeInitials = employeeCode.substring(0, 2);

				if (!StringHelper.isEqual(expectedInitials, employeeCodeInitials, false))
					super.state(context, false, "employeeCode", "{acme.validation.assistance-agent.employee-code-not-matching-initials.message}");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
