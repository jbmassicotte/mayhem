package test.java;

import com.alienvault.github.Issue;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class IssueTest extends TestCase {
    private IssueTestHelper helper;

    public void setUp() throws Exception {
        super.setUp();
        helper = new IssueTestHelper();
    }

    /*
     * Verify isEqual returns true
     */
    public void testIsEqual() {
        Issue issue = helper.makeIssue(1, "2019-02-01 11:30:59");
        assertTrue(issue.isEqual(issue));
    }

    /*
     * Verify isEqual returns false
     */
    public void testIsNotEqual() {
        Issue issue1 = helper.makeIssue(1, "2019-02-01 11:30:59");
        Issue issue2 = helper.makeIssue(2, "2019-02-01 11:30:59");
        Issue issue3 = helper.makeIssue(1, "2019-02-01 11:30:58");
        assertFalse(issue1.isEqual(issue2));
        assertFalse(issue1.isEqual(issue3));
    }

    /*
     * Check that isSorted returns true given an empty list.
     */
    public void testEmptyListIsSorted() {
        List<Issue> issues = new ArrayList<>();
        assertTrue(Issue.isSorted(issues));
    }

    /*
     * Check that isSorted returns true on a one item list
     */
    public void testOneItemListIsSorted() {
        List<Issue> issues = helper.makeOneItemList();
        assertTrue(Issue.isSorted(issues));
    }

    /*
     * Check that isSorted returns true given a 3 items sorted list
     */
    public void testIsSorted() {
        List<Issue> issues = helper.makeSortedList();
        assertTrue(Issue.isSorted(issues));
    }

    /*
     * Check that isSorted returns false given a 3 items unsorted list
     */
    public void testIsNotSorted() {
        List<Issue> issues = helper.makeUnsortedList();
        assertFalse(Issue.isSorted(issues));
    }
}