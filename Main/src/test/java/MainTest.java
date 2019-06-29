package test.java;

import com.alienvault.Main;
import com.alienvault.RepoName;
import com.alienvault.github.Issue;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class MainTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    /*
     * Check getIssues returns an empty list when given an
     * empty list of reponames
     */
    public void testGetIssuesWithNoReponames() {
        try {
            List<RepoName> repoNames = new ArrayList<>();
            List<Issue> issues = Main.getIssues(repoNames);
            assertTrue(issues.isEmpty());
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check getIssues returns an empty list when given a list
     * with an invalid reponame
     */
    public void testGetIssuesWithInvalidReponame() {
        try {
            List<RepoName> repoNames = new ArrayList<>();
            repoNames.add(new RepoName("hello/world"));
            List<Issue> issues = Main.getIssues(repoNames);
            assertTrue(issues.isEmpty());
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check getIssues returns a non empty list when given a list
     * with some valid reponames, and an invalid reponames.
     * We made sure to use repos that have issues.
     */
    public void testGetIssuesWithSomeValidReponames() {
        try {
            List<RepoName> repoNames = new ArrayList<>();
            repoNames.add(new RepoName("mojombo/grit"));
            repoNames.add(new RepoName("hello/world"));
            repoNames.add(new RepoName("rubinius/rubinius"));
            List<Issue> issues = Main.getIssues(repoNames);
            assertFalse(issues.isEmpty());
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }
}