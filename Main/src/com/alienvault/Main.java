package com.alienvault;

import com.alienvault.github.GithubService;
import com.alienvault.github.Issue;
import com.alienvault.report.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * GitHub Issues -------------
 *
 * Create a program that generates a report about the the Issues belonging to a
 * list of github repositories ordered by creation time, and information about
 * the day when most Issues were created.
 *
 * Input: ----- List of 1 to n Strings with Github repositories references with
 * the format "owner/repository"
 *
 *
 * Output: ------ String representation of a Json dictionary with the following
 * content:
 *
 * - "issues": List containing all the Issues related to all the repositories
 * provided. The list should be ordered by the Issue "created_at" field (From
 * oldest to newest) Each entry of the list will be a dictionary with basic
 * Issue information: "id", "state", "title", "repository" and "created_at"
 * fields. Issue entry example: { "id": 1, "state": "open", "title": "Found a
 * bug", "repository": "owner1/repository1", "created_at":
 * "2011-04-22T13:33:48Z" }
 *
 * - "top_day": Dictionary with the information of the day when most Issues were
 * created. It will contain the day and the number of Issues that were created
 * on each repository this day If there are more than one "top_day", the latest
 * one should be used. example: { "day": "2011-04-22", "occurrences": {
 * "owner1/repository1": 8, "owner2/repository2": 0, "owner3/repository3": 2 } }
 *
 *
 * Output example: --------------
 *
 * {
 * "issues": [ { "id": 38, "state": "open", "title": "Found a bug",
 * "repository": "owner1/repository1", "created_at": "2011-04-22T13:33:48Z" }, {
 * "id": 23, "state": "open", "title": "Found a bug 2", "repository":
 * "owner1/repository1", "created_at": "2011-04-22T18:24:32Z" }, { "id": 24,
 * "state": "closed", "title": "Feature request", "repository":
 * "owner2/repository2", "created_at": "2011-05-08T09:15:20Z" } ], "top_day": {
 * "day": "2011-04-22", "occurrences": { "owner1/repository1": 2,
 * "owner2/repository2": 0 } } }
 *
 * --------------------------------------------------------
 *
 * You can create the classes and methods you consider. You can use any library
 * you need. Good modularization, error control and code style will be taken
 * into account. Memory usage and execution time will be taken into account.
 *
 * Good Luck!
 */
public class Main {
    private static Boolean verboseMode = false;
    static final String VERBOSEVARNAME = "CODEEXERCISEVERBOSE";

    /**
     * @param args String array with Github repositories with the format
     * "owner/repository"
     */
    public static void main(String[] args) {
        String verboseVal = System.getenv(VERBOSEVARNAME);
        verboseMode = (verboseVal!=null && verboseVal.equals("ON"));
        if (verboseMode)
            System.out.println("verbose mode ON");

        /*
        get list of '<owner>/<repo>'
         */
        List<RepoName> repoNames;
        try {
            repoNames = RepoName.makeRepoNames(args);
            if (verboseMode) {
                System.out.println("Input repo names:");
                for (RepoName name : repoNames) {
                    System.out.println(name);
                }
            }
        } catch (Exception iex) {
            System.out.println("Bad input arguments: " + iex.toString());
            showHelp();
            return;
        }

        /*
         retrieve issues from github for '<owner>/<repo>'
         */
        System.out.println("Retrieving issues from github");
        List<Issue> issues = getIssues(repoNames);
        if (issues.isEmpty()) {
            System.out.println("No issues found for repositories " + repoNames);
            return;
        }

        /*
         produce report for retrieved issues
         */
        try {
            System.out.println("Producing report");
            Report report = new Report(issues);
            showReport(report);
        } catch (Exception rex) {
            System.out.println("Error generating report: " + rex.toString());
        }

    }

    private static void showHelp() {
        System.out.println("Expecting 1 or more strings of format <owner>/<repo>");
        System.out.println("Use env var '" + VERBOSEVARNAME + "=ON' to enable verbose mode");
    }

    /*
    Retrieve all issues for all repoNames
     */
    public static List<Issue> getIssues(List<RepoName> repoNames) {
        List<Issue> issues = new ArrayList<>();
        GithubService githubService = new GithubService();
        for (RepoName reponame : repoNames) {
            try {
                List<Issue> repoIssues = githubService.getIssuesForRepo(
                        reponame.getOwner(), reponame.getRepo());
                String fullname = reponame.getOwner() + "/" + reponame.getRepo();
                for (Issue issue : repoIssues) {
                    issue.setRepository(fullname);
                }
                if (verboseMode) {
                    System.out.println("Issues for " + fullname + ":");
                    for (Issue issue : repoIssues) {
                        System.out.println(issue);
                    }
                }
                // See discussion at the bottom of Report.java regarding
                // best strategy for sorting issues.
                issues.addAll(repoIssues);
            } catch (Exception ex) {
                // do not return; skip this reponame and try the others in the list
                System.out.println("Error retrieving issues for repo " + reponame + ": " + ex.toString());
            }
        }
        return  issues;
    }

    private static void showReport(Report report) {
        if (verboseMode) {
            System.out.println("Report:");
            System.out.println(report);
        }
        System.out.println("Report, json format: ");
        System.out.println(
                "============================================================================================");
        System.out.println(report.toJSON());
        System.out.println(
                "============================================================================================");
    }
}
