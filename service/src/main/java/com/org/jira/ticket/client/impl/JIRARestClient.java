package com.org.jira.ticket.client.impl;

import com.atlassian.jira.rest.client.*;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.jersey.*;
import com.org.jira.ticket.client.RestClient;
import com.sun.jersey.api.client.AsyncViewResource;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.ViewResource;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;

import static com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClient.createDefaultClientHander;

@Slf4j
@Component
public class JIRARestClient implements InitializingBean, RestClient {

    private ApacheHttpClient httpClient;

    private IssueRestClient issueRestClient;

    private SessionRestClient sessionRestClient;

    private UserRestClient userRestClient;

    private ProjectRestClient projectRestClient;

    private ComponentRestClient componentRestClient;

    private com.org.jira.ticket.client.MetadataRestClient metadataClient;

    private MetadataRestClient metadataRestClient;

    private SearchRestClient searchRestClient;

    private VersionRestClient versionRestClient;

    private ProjectRolesRestClient projectRolesRestClient;

    @Value("${jira.host}")
    protected String host;

    @Value("${jira.user}")
    protected String username;

    @Value("${jira.password}")
    protected String password;

    private URI baseUri;

    private URI serverUri;

    @Override
    public void afterPropertiesSet() throws URISyntaxException {
        init();
    }

    @Bean
    public ProgressMonitor progressMonitor() {
        return new NullProgressMonitor();
    }

    public void init() throws URISyntaxException {
        log.info("Initialize JIRA REST clients");

        this.serverUri = new URI(host);
        this.baseUri = UriBuilder.fromUri(serverUri).path("/rest/api/latest").build();

        AuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(username, password);
        DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
        authenticationHandler.configure(config);
        httpClient = initHttpClient(config, authenticationHandler);
        metadataClient = new MetadataClient(baseUri, httpClient);
        metadataRestClient = new JerseyMetadataRestClient(baseUri, httpClient);
        sessionRestClient = new JerseySessionRestClient(httpClient, serverUri);
        issueRestClient = new JerseyIssueRestClient(baseUri, httpClient, sessionRestClient, metadataRestClient);
        userRestClient = new JerseyUserRestClient(baseUri, httpClient);
        projectRestClient = new JerseyProjectRestClient(baseUri, httpClient);
        componentRestClient = new JerseyComponentRestClient(baseUri, httpClient);
        searchRestClient = new JerseySearchRestClient(baseUri, httpClient);
        versionRestClient = new JerseyVersionRestClient(baseUri, httpClient);
        projectRolesRestClient = new JerseyProjectRolesRestClient(baseUri, httpClient, serverUri);
    }

    public static ApacheHttpClient initHttpClient(DefaultApacheHttpClientConfig config,
                                                  final AuthenticationHandler authenticationHandler) {
        return new ApacheHttpClient(createDefaultClientHander(config)) {
            @Override
            public WebResource resource(URI u) {
                final WebResource resource = super.resource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }

            @Override
            public AsyncWebResource asyncResource(URI u) {
                final AsyncWebResource resource = super.asyncResource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }

            @Override
            public ViewResource viewResource(URI u) {
                final ViewResource resource = super.viewResource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }

            @Override
            public AsyncViewResource asyncViewResource(URI u) {
                final AsyncViewResource resource = super.asyncViewResource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }
        };
    }

    @Override
    public IssueRestClient getIssueClient() {
        return issueRestClient;
    }

    @Override
    public SessionRestClient getSessionClient() {
        return sessionRestClient;
    }

    @Override
    public UserRestClient getUserClient() {
        return userRestClient;
    }

    @Override
    public ProjectRestClient getProjectClient() {
        return projectRestClient;
    }

    @Override
    public ComponentRestClient getComponentClient() {
        return componentRestClient;
    }

    @Override
    public com.org.jira.ticket.client.MetadataRestClient getMetadataRestClient() {
        return metadataClient;
    }

    @Override
    public com.atlassian.jira.rest.client.MetadataRestClient getMetadataClient() {
        return metadataRestClient;
    }

    @Override
    public SearchRestClient getSearchClient() {
        return searchRestClient;
    }

    @Override
    public VersionRestClient getVersionRestClient() {
        return versionRestClient;
    }

    @Override
    public ProjectRolesRestClient getProjectRolesRestClient() {
        return projectRolesRestClient;
    }

    @Override
    public ApacheHttpClient getTransportClient() {
        return httpClient;
    }
}
