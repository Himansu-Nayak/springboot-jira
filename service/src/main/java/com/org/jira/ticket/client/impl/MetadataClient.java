package com.org.jira.ticket.client.impl;

import com.atlassian.jira.rest.client.ProgressMonitor;
import com.atlassian.jira.rest.client.domain.Field;
import com.atlassian.jira.rest.client.internal.jersey.JerseyMetadataRestClient;
import com.atlassian.jira.rest.client.internal.json.GenericJsonArrayParser;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class MetadataClient extends JerseyMetadataRestClient implements com.org.jira.ticket.client.MetadataRestClient {

	private final FieldJsonParser fieldJsonParser = new FieldJsonParser();
	private final GenericJsonArrayParser<Field> fieldsJsonParser = GenericJsonArrayParser.create(fieldJsonParser);

	/**
	 * Default constructor
	 * @param baseUri
	 * @param client
	 */
	public MetadataClient(URI baseUri, ApacheHttpClient client) {
		super(baseUri, client);
	}

	@Override
	public Iterable<Field> getFields(ProgressMonitor progressMonitor) {
		final URI uri = UriBuilder.fromUri(baseUri).path("field").build();
		return getAndParse(uri, fieldsJsonParser, progressMonitor);
	}

	private class FieldJsonParser implements JsonObjectParser<Field> {

		@Override
		public Field parse(JSONObject json) throws JSONException {
			final String name = json.getString("name");
			final String id = json.getString("id");
			return new Field(id, name, null, null);
		}

	}

}
