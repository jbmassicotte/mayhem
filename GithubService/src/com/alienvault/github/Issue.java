package com.alienvault.github;

import java.util.Date;
import java.util.List;

/*
 * A github issue
 */
public class Issue {
    private String id;
    private String state;
    private String title;
    private String repository;
    Date created_at;

    public String getId() {
        return id;
    }
    public String getState() {
        return state;
    }
    public String getTitle() {
        return title;
    }
    public String getRepository() { return repository; }
    public Date getCreated_at() {
        return created_at;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setRepository(String repository) { this.repository = repository; }
    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }
    public String toString() {
        return "<id = " + id + ", state = " + state + ", title = " + title +
                ", repository = " + repository + ", created_at = " + created_at + ">";
    }

    /*
     * Checks if issue is same as this.
     */
    public boolean isEqual(Issue issue) {
        return  this.id.equals(issue.id) &&
                this.state.equals(issue.state) &&
                this.title.equals(issue.title) &&
                this.repository.equals(issue.repository) &&
                this.created_at.compareTo(issue.created_at) == 0;
    }

    /*
     * Helper function checks if list of issues is sorted based on creation date,
     * in ascending order.
     */
    public static  Boolean isSorted(List<Issue> issues) {
        for (int i=1; i<issues.size(); i++) {
            Date d1 = issues.get(i-1).getCreated_at();
            Date d2 = issues.get(i).getCreated_at();
            if (d1.compareTo(d2) > 0) {
                return false;
            }
        }
        return true;
    }
}
