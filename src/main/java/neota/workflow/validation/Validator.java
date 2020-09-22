package neota.workflow.validation;

import java.util.Collection;

import neota.workflow.exceptions.ValidationException;


/**
 * Validates an object available for inspection based on the set of rules.
 * @author leto
 */
public class Validator
{
	public void validate(Collection<ValidationRule> rules) throws ValidationException
	{
		for (ValidationRule rule : rules)
		{
			if (!rule.validate())
			{
				throw new ValidationException("Failed to validate the following rule: " + rule.getDescription());
			}
		}
	}
}
