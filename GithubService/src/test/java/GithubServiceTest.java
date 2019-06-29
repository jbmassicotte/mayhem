package test.java;

import com.alienvault.github.GithubService;
import com.alienvault.github.Issue;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class GithubServiceTest extends TestCase {
    private GithubService githubService;
    private IssueTestHelper helper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        githubService = new GithubService();
        helper = new IssueTestHelper();
    }

    @Test
    /*
     * Check that service throws exception when given an invalid repo name
     */
    public void testGetIssuesForBadRepo() {
        String owner = "bad";
        String repo = "unknown";
        try {
            List<Issue> repoIssues = githubService.getIssuesForRepo(owner, repo);
            fail("Should throw exception for invalid repo");
        } catch (Exception ex) {
            assertThat(ex, instanceOf(IOException.class));
        }
    }

    @Test
    /*
     * Check that service returns a non-empty sorted list of issues, when given
     * a valid repo name that has linked issues.
     */
    public void testGetIssuesForGoodRepo() {
        List<Issue> repoIssues;

        // we picked a repo we know has issues
        String owner = "wycats";
        String repo = "merb-core";
        try {
            repoIssues = githubService.getIssuesForRepo(owner, repo);
            assertFalse(repoIssues.isEmpty());
            assertTrue(Issue.isSorted(repoIssues));
        } catch (Exception ex) {
            fail("Caught exception: " + ex.getMessage());
        }
    }

}