package com.org.jira.ticket;

import com.atlassian.jira.rest.client.domain.*;
import com.atlassian.jira.rest.client.domain.input.FieldInput;

import java.net.URISyntaxException;
import java.util.List;

/**
 * The interface for Jira Service
 */
public interface JiraService {

    /**
     * Returns issue type.
     *
     * @param issueType the issue type
     * @return the issue type from jira
     */
    IssueType getIssueType(String issueType);

    /**
     * Returns all projects on jira.
     *
     * @return the list of project.
     */
    List<BasicProject> getAllProject();

    /**
     * Returns all projects on jira.
     *
     * @return the list of project.
     */
    Project getProjectByName(String projectName);

    /**
     * Create support jira on JIRA.
     *
     * @param issue the issue to be created
     * @return the issue with the id
     */
    BasicIssue createIssueInJira(com.org.jira.Issue issue) throws URISyntaxException;

    /**
     * Return all supported fields on jira.
     *
     * @return jira supported fields
     */
    List<Field> getAllField();

    /**
     * Return jira field.
     *
     * @param fieldName the jira field name
     * @return jira field
     */
    Field getField(String fieldName);

    /**
     * Update a issue on JIRA.
     *
     * @param issue the issue.
     * @param newFieldList the list of field to be updated.
     * @return the issue with the id
     */
    Issue updateIssueInJira(Issue issue, Iterable<FieldInput> newFieldList);

    /**
     * Returns a issue from JIRA.
     *
     * @param issueKey the issue key
     * @return the issue with the id
     */
    Issue getIssue(String issueKey);

    /**
     * create user under the provided organisation.
     *
     * @param user the jira user
     * @return the jira user
     */
    void createUser(String organisation, String user);

    /**
     * Returns a JIRA user.
     *
     * @param username the jira user
     * @return the user
     */
    User getUserByName(String username);

    /**
     * validate if an user exist on JIRA.
     *
     * @param username the jira user
     * @return true if exist else false
     */
    boolean isUserExist(String username);
}
