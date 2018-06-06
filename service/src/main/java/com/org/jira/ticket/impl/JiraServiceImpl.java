package com.org.jira.ticket.impl;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.ProgressMonitor;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.jira.rest.client.domain.*;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.org.jira.ticket.client.RestClient;
import com.org.jira.ticket.JiraService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
public class JiraServiceImpl implements JiraService {

    private static final String USER_URI_PREFIX = "/rest/servicedesk/1/pages/people/customers/pagination/XS/invite/organisation";

    @Value("${jira.project}")
    protected String project;

    @Value("${jira.host}")
    protected String host;

    @Value("${jira.user}")
    protected String username;

    @Value("${jira.password}")
    protected String password;

    @Value("${jira.organisation}")
    protected String organisation;

    @Autowired
    private JiraRestClient jiraConnection;

    @Autowired
    private RestClient restClient;

    @Resource(name = "progressMonitor")
    private ProgressMonitor progressMonitor;

    @Override
    public IssueType getIssueType(String issueType) {
        List<IssueType> issueTypeList = (List<IssueType>) jiraConnection.getMetadataClient().getIssueTypes(progressMonitor);
        log.debug("List of available issue type [ {} ]", issueTypeList);
        for (IssueType issue : issueTypeList) {
            log.info("Issue type [ {} ]" + issue.getName());
            if (issue.getName().equalsIgnoreCase(issueType)) {
                return issue;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllField() {
        return (List<Field>) restClient.getMetadataRestClient().getFields(progressMonitor);
    }

    @Override
    public Field getField(String fieldName){
        List<Field> fields = getAllField();
        log.debug("List of available fields [ {} ]", fields);
        for (Field field : fields) {
            if(field.getName().equalsIgnoreCase(fieldName)){
                return field;
            }
        }
        return null;
    }

    @Override
    public List<BasicProject> getAllProject(){
        return (List<BasicProject>) jiraConnection.getProjectClient().getAllProjects(progressMonitor);
    }

    @Override
    public Project getProjectByName(String projectName) {
        return jiraConnection.getProjectClient().getProject(projectName, progressMonitor);
    }

    @Override
    public BasicIssue createIssueInJira(com.org.jira.Issue issue) {
        IssueInputBuilder issueInputBuilder = new IssueInputBuilder(getProjectByName(project).getKey(), getIssueType(issue.getType()).getId());
        User user = getUserByName(issue.getReporter());

        // create user
        if(user == null) {
            createUser(organisation, issue.getReporter());
            user = getUserByName(issue.getReporter());
        }

        IssueInput issueInput = issueInputBuilder.setSummary(issue.getSummary())
                        .setDescription(issue.getDescription())
                        .setReporter(user)
//                      .setFieldValue(getField("Request Type").getId(), "Technical Support")
                        .build();
        BasicIssue issueCreated = jiraConnection.getIssueClient().createIssue(issueInput, progressMonitor);
        log.info("New issue has been created with key [ {} ]", issueCreated);
        return issueCreated;
    }

    @Override
    public Issue updateIssueInJira(Issue issue, Iterable<FieldInput> newFieldList) {
        jiraConnection.getIssueClient().update(issue, newFieldList, progressMonitor);
        return issue;
    }

    @Override
    public Issue getIssue(String issueKey) {
        Issue issue = jiraConnection.getIssueClient().getIssue(issueKey, progressMonitor);
        log.info("Issue from jira [ {} ]", issue);
        return issue;
    }

    @Override
    public boolean isUserExist(String username) {
        return getUserByName(username) != null;
    }

    @Override
    public User getUserByName(String username) {
        User user = null;
        try {
            user = jiraConnection.getUserClient().getUser(username, progressMonitor);
            log.info("Jira User [ {} ]", user);
        } catch (RestClientException exception) {
            log.error("Unable to retrieve user from jira [ {} ]", exception.getMessage());
        }
        return user;
    }

    @Override
    public void createUser(String organisation, String user){
        try {
            invokePostMethod(host + USER_URI_PREFIX + "?name=" + organisation, "{\"emails\":[\""+ user +"\"]}");
        }catch (Exception exception) {
            log.error("Exception during user creation", exception);
        }
    }

    private String invokePostMethod(String url, String data){
        String auth = new String(Base64.encode(username + ":" + password));
        Client client = Client.create();
        WebResource webResource = client.resource(url);

        ClientResponse response = webResource
                .header(AUTHORIZATION, "Basic " + auth)
                .accept(APPLICATION_JSON_VALUE)
                .type(APPLICATION_JSON_VALUE)
                .post(ClientResponse.class, data);
        log.info("Response from jira [ {} ] ", response);
        return response.getEntity(String.class);
    }
}
