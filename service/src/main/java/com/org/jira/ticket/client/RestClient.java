package com.org.jira.ticket.client;

import com.atlassian.jira.rest.client.JiraRestClient;

/**
 * The interface for RestClient
 */
public interface RestClient extends JiraRestClient {

	/**
	 * Retrieves metadata rest client.
	 *
	 * @return metadata rest client
	 */
	MetadataRestClient getMetadataRestClient();
}
