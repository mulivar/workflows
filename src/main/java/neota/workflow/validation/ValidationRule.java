package neota.workflow.validation;


public interface ValidationRule
{
	boolean validate();
	String getDescription();
}
