package com.org.sb.jira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Jira application
 */
@EnableConfigurationProperties
@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class})
public class JiraApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(JiraApplication.class, args);
    }

}
