package io.vertigo.addons.comment;

import io.vertigo.dynamo.domain.model.KeyConcept;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.lang.Component;

import java.util.List;

/**
 * @author pchretien
 */
public interface CommentManager extends Component {

	/**
	 * Publish a comment on a key concept.
	 * @param comment Comment
	 * @param keyConceptUri keyConcept's uri
	 */
	void publish(Comment comment, URI<? extends KeyConcept> keyConceptUri);

	/**
	 * Get ordered comments list published on this keyConcept.
	 * @param keyConceptUri keyConcept's uri
	 * @return ordered comments list
	 */
	List<Comment> getComments(URI<? extends KeyConcept> keyConceptUri);
}
