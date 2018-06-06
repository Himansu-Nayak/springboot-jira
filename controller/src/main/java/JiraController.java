import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.codahale.metrics.annotation.Timed;
import com.org.jira.Issue;
import com.org.jira.SupportTicket;
import com.org.jira.ticket.JiraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.OK;

/**
 * The Jira controller.
 */
@Slf4j
@RestController
@RequestMapping(value = "/jira")
public class JiraController {

    private final JiraService jiraService;
    /**
     * Instantiates a new instance of the Jira Controller
     *
     * @param jiraService       the jira service
     */
    @Autowired
    public JiraController(JiraService jiraService) {
        this.jiraService = jiraService;
    }

    /**
     * Create support jira on JIRA
     *
     * @param supportTicket support jira
     * @return support jira with issue key
     */
    @PostMapping(value = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(name = "createSupportTicket", absolute = true)
    public ResponseEntity createSupportTicket(@RequestBody SupportTicket supportTicket) throws URISyntaxException {
        log.info("Creating support jira [ {} ].", supportTicket);
        Issue issue = createIssue(supportTicket);
        BasicIssue basicIssue = jiraService.createIssueInJira(issue);
        supportTicket.setIssueKey(issue.getIssueKey());
        log.info("Support jira [ {} ] created successfully.", supportTicket);
        return new ResponseEntity(basicIssue, OK);
    }

    private Issue createIssue(SupportTicket supportTicket) {
        return Issue.builder()
                .type(supportTicket.getType().name())
                .component(supportTicket.getComponent().name())
                .summary(supportTicket.getSummary())
                .description(supportTicket.format())
                .reporter("himansuxnayak@gmail.com")
                .build();
    }
}
