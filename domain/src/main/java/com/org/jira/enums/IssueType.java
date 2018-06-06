package com.org.jira.enums;

/**
 * Issue Type
 *
 */
public enum IssueType {

    NEW_FEATURES("New Feature"),
    SUPPORT("Support"),
    BUG("Bug");

    private String issueType;

    IssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueType() {
        return issueType;
    }
}
