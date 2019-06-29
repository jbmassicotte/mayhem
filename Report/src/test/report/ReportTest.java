package test.report;

import com.alienvault.github.Issue;
import com.alienvault.report.Report;
import com.alienvault.report.TopDay;
import junit.framework.TestCase;
import test.java.IssueTestHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class ReportTest extends TestCase {
    private IssueTestHelper helper;

    public void setUp() throws Exception {
        super.setUp();
        helper = new IssueTestHelper();
    }

    /*
     * Check if Report returns exactly same list when provided a sorted list
     */
    public void testSortedList() {
        List<Issue> sortedList = helper.makeSortedList();

        try {
            Report report = new Report(sortedList);
            List<Issue> issues = report.getIssues();
            assertTrue(helper.areEquals(sortedList,issues));
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check if Report returns a sorted list when provided an unsorted list
     */
    public void testIssuesAreSorted() {
        List<Issue> unsortedList = helper.makeUnsortedList();
        // make sure list is not sorted
        assertFalse(Issue.isSorted(unsortedList));

        try {
            Report report = new Report(unsortedList);
            List<Issue> issues = report.getIssues();
            assertEquals(unsortedList.size(),issues.size());
            assertFalse(helper.areEquals(unsortedList,issues));
            assertTrue(Issue.isSorted(issues));
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check if topDay is day of last issue in sorted list,
     * knowing there is only 1 issue per day.
     */
    public  void testLastDayInSortedList() {
        List<Issue> unsortedList = helper.makeUnsortedList();

        try {
            Report report = new Report(unsortedList);
            List<Issue> issues = report.getIssues();

            String topDay = report.getTop_day().getDay();
            Issue lastIssue = issues.get(issues.size()-1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String lastDay = dateFormat.format(lastIssue.getCreated_at());
            assertEquals(topDay, lastDay);
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check if Report identifies the right TopDay given a list with a multi-issue day.
     * Date of the multi-issue day is generated randomly by IssueTestHelper.
     * numIssuesPerRepo specifies the number of issues for various repos,
     * for that TopDay.
     */
    public void testHighDay() {
        int[] numIssuesPerRepo = new int[]{3, 5, 2}; // <-- 3 issues for repo X, 5 for repo y, and 2 for repo z
        List<Issue> unsortedList = helper.makeListWithHighDay(numIssuesPerRepo);
        String expectedHighDay = helper.getHighDay1();

        try {
            Report report = new Report(unsortedList);
            TopDay topDay = report.getTop_day();
            assertEquals(topDay.getDay(), expectedHighDay);
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check if Report identifies the right TopDay given 2 TopDays, ie,
     * 2 days with same high number of occurrences.
     * numIssuesPerRepo 1 & 2 specify the number of issues for various repos,
     * for the 2 TopDays
     */
    public void testTwoHighDays() {
        int[] numIssuesPerRepo1 = new int[]{3, 5, 2};    // sum = 10
        int[] numIssuesPerRepo2 = new int[]{2, 1, 2, 5}; // sum = 10
        List<Issue> unsortedList = helper.makeListWithTwoHighDays(numIssuesPerRepo1,numIssuesPerRepo2);
        String expectedHighDay = helper.getMostRecentHighDay();

        try {
            Report report = new Report(unsortedList);
            TopDay topDay = report.getTop_day();
            assertEquals(topDay.getDay(), expectedHighDay);
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check if Report identifies the right occurrences on TopDay.
     */
    public void testOccurrences() {
        int[] numIssuesPerRepo = new int[]{3, 5, 2};
        List<Issue> unsortedList = helper.makeListWithHighDay(numIssuesPerRepo);
        HashMap<String,Integer> expectedOccurrences = helper.getHighDayOccurrences();

        try {
            Report report = new Report(unsortedList);
            TopDay topDay = report.getTop_day();
            HashMap<String,Integer> occurrences = topDay.getOccurrences();
            assertEquals(expectedOccurrences,occurrences);
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /*
     * Check if Report identifies the right occurrences on TopDay given
     * 2 TopDays
     */
    public void testOccurrencesWithTwoHighDays() {
        int[] numOccurrences1 = new int[]{3, 5, 2};    // sum = 10
        int[] numOccurrences2 = new int[]{2, 1, 2, 5}; // sum = 10
        List<Issue> unsortedList = helper.makeListWithTwoHighDays(numOccurrences1,numOccurrences2);
        HashMap<String,Integer> expectedOccurrences = helper.getHighDayOccurrences();

        try {
            Report report = new Report(unsortedList);
            TopDay topDay = report.getTop_day();
            HashMap<String,Integer> occurrences = topDay.getOccurrences();
            assertEquals(expectedOccurrences,occurrences);
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }
}