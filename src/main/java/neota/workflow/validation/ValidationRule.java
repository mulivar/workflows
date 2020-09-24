package neota.workflow.validation;


/**
 * A validation rule is intended for verification of a single or multiple predicates.
 */
public interface ValidationRule
{
	/**
	 * Validates a rule, verifies if the object under validation satisfies a condition.
	 * @return true if the object under validation satisfies the coded condition, otherwise false
	 */
	boolean validate();
	
	/**
	 * Retrieves a description of the rule related to the object under validation.
	 * @return a string representing the description
	 */
	String getDescription();
}
