package neota.workflow.validation;

import neota.workflow.elements.Workflow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Workflow validation rule processes workflows.
 * @author leto
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class WorkflowRule implements ValidationRule
{
	/** The workflow to process during validation. */
	protected Workflow workflow;
}
