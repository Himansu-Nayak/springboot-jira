package com.org.jira.enums;

/**
 * Component Type
 *
 */
public enum Component {

    IOS_APP("IOS App"),
    WEB_APPLICATION("Web Application");

    private String component;

    Component(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }
}
