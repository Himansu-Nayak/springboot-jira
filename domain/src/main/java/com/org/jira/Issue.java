package com.org.jira;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Issue {
    private String issueKey;
    private String type;
    private String component;
    private String summary;
    private String description;
    private String reporter;
}
