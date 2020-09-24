package neota.workflow.validation;

import java.util.Collection;

import neota.workflow.exceptions.ValidationException;


/**
 * Validates an object available for inspection based on the set of rules.
 */
public class Validator
{
	/**
	 * Validate all the provided rules by iterating through all of them.
	 * @param rules the rules to validate
	 * @throws ValidationException in case there's a rule that isn't satisfied; the exception is thrown at the first
	 * such rule encountered, no other rules are being processed afterwards
	 */
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
