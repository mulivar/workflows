package neota.workflow.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import neota.workflow.elements.Session;


/**
 * Session validation rule processes sessions.
 * @author leto
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class SessionRule implements ValidationRule
{
	/** The session to process during validation. */
	protected Session session;
}
