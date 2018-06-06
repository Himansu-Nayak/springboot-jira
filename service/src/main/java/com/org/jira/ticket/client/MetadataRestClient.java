package com.org.jira.ticket.client;

import com.atlassian.jira.rest.client.ProgressMonitor;
import com.atlassian.jira.rest.client.domain.Field;

/**
 * The interface for MetadataRestClient
 */
public interface MetadataRestClient extends com.atlassian.jira.rest.client.MetadataRestClient {

	/**
	 * Retrieves from the server complete list of available issue type
	 *
	 * @param progressMonitor progress monitor
	 * @return complete information about issue type resource
	 */
	Iterable<Field> getFields(ProgressMonitor progressMonitor);
}
