package com.org.jira;

import com.org.jira.enums.Component;
import com.org.jira.enums.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.lang.System.lineSeparator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {
    private String issueKey;
    private IssueType type;
    private Component component;
    private String customerName;
    private String time;
    private String summary;
    private String description;

    public String format() {

        return  "Description : "            + this.description  + lineSeparator() +
                "Customer Name : "          + this.customerName + lineSeparator() +
                "Time : "                   + this.time;
    }
}
