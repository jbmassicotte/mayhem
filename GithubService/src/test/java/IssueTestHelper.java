package test.java;

import com.alienvault.github.Issue;
import java.text.SimpleDateFormat;
import java.util.*;

public class IssueTestHelper {
    private SimpleDateFormat dateformat;
    private int issueIndex;
    private int repoIndex;
    private int stateIndex;
    private HashMap<String,Integer> highDayOccurrences;
    private String highDay1;
    private String highDay2;
    private Random random;

    public IssueTestHelper() {
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        issueIndex = 1;
        repoIndex = 1;
        stateIndex = 0;
        highDayOccurrences = new HashMap<>();
        random = new Random();
        highDay1 = makeRandomDate();
        highDay2 = makeRandomDate();
    }

    /*
     * Check 2 lists of issues are equal.
     */
    public Boolean areEquals(List<Issue> list1, List<Issue> list2) {
        if (list1.size() == list2.size()) {
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).isEqual(list2.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * Make a list composed of 1 issue.
     */
    public  List<Issue> makeOneItemList() {
        List<Issue> issues = new ArrayList<>();
        issues.add(makeIssue(repoIndex++, "2018-01-31 10:30:59"));
        return issues;
    }

    /*
     * Make a sorted list composed of 3 issues.
     */
    public List<Issue> makeSortedList() {
        List<Issue> issues = new ArrayList<>();
        issues.add(makeIssue(repoIndex++, "2018-01-31 10:30:59"));
        issues.add(makeIssue(repoIndex++, "2019-02-01 11:30:59"));
        issues.add(makeIssue(repoIndex++, "2019-04-11 12:30:59"));
        return issues;
    }

    /*
     * Make an unsorted list composed of 3 issues.
     */
    public  List<Issue> makeUnsortedList() {
        List<Issue> issues = new ArrayList<>();
        issues.add(makeIssue(repoIndex++, "2019-04-11 12:30:59"));
        issues.add(makeIssue(repoIndex++, "2018-01-31 10:30:59"));
        issues.add(makeIssue(repoIndex++, "2019-02-01 11:30:59"));
        return issues;
    }

    /*
     * Generate an unsorted list of num issues for a specific repo, for a specific day.
     */
    private List<Issue> makeIssuesForRepo(int repoIdx, String day, int numIssues) {
        // create num issues for a specific repo, on a specific day
        List<Issue> issues = new ArrayList<>();
        Issue issue = null;
        for (int j = 0; j < numIssues; j++) {
            issue = makeIssue(repoIdx, day + makeRandomTime());
            issues.add(issue);
        }
        if (issue != null) {
            highDayOccurrences.put(issue.getRepository(), numIssues);
        }
        return issues;
    }

    /*
     * Make an unsorted list of num issues, for various repos, for a specific day.
     */
    private List<Issue> makeListWithHighDay(String highDay, int[] numIssuesPerRepo) {
        List<Issue> issues = new ArrayList<>();
        for (int i=0; i<numIssuesPerRepo.length; i++) {
            int num = numIssuesPerRepo[i];
            issues.addAll(makeIssuesForRepo(repoIndex, highDay, num));
            repoIndex++;
        }
        return issues;
    }

    /*
     * Create an unsorted list with a High Day, ie, a day with several issue occurrences.
     * The High Day is generated randomly.
     * numIssuesPerRepo represents the number of issues for various repos on the High Day.
     */
    public List<Issue> makeListWithHighDay(int[] numIssuesPerRepo) {
        highDayOccurrences.clear();
        List<Issue> issues = makeUnsortedList();
        issues.addAll(makeListWithHighDay(highDay1, numIssuesPerRepo));
        return issues;
    }

    /*
     * Create unsorted list with 2 High Days.
     * The 2 High Days are generated randomly.
     * numOccurrences 1 and 2 represent the number of issues for various repos
     * on the 2 High Days.
     * Apology to the reader: handling of highDayOccurrences for the 2 HighDay case
     * not the cleanest, but the programmer is on a tight schedule...
     */
    public  List<Issue> makeListWithTwoHighDays(
            int[] numIssuesPerRepo1,
            int[] numIssuesPerRepo2) {
        highDayOccurrences.clear();
        List<Issue> issues = makeUnsortedList();
        issues.addAll(makeListWithHighDay(highDay1, numIssuesPerRepo1));
        HashMap<String,Integer> highDayOccurrences1 = new HashMap<>(highDayOccurrences);

        highDayOccurrences.clear();
        issues.addAll(makeListWithHighDay(highDay2, numIssuesPerRepo2));
        HashMap<String,Integer> highDayOccurrences2 = new HashMap<>(highDayOccurrences);
        
        // Keep occurrences for the most recent HighDay.
        if (highDay1.equals(getMostRecentHighDay()))
            highDayOccurrences = new HashMap<>(highDayOccurrences1);
        else
            highDayOccurrences = new HashMap<>(highDayOccurrences2);

        return issues;
    }

    /*
     * Return the occurrences for the high day.
     */
    public HashMap<String,Integer> getHighDayOccurrences() {
        return highDayOccurrences;
    }

    /*
     * Create an issue for a specific repo, and a specific creation time.
     * Use issueIndex to ensure all issues are unique.
     */
    public Issue makeIssue(int repoId, String strCreationDate)  {
        Issue issue = new Issue();
        try {
            issue.setId("id" + issueIndex);
            issue.setRepository("user" + repoId + "/" + "repo" + repoId);
            issue.setState(makeState());
            issue.setTitle("title" + issueIndex);
            Date creationDate = dateformat.parse(strCreationDate);
            issue.setCreatedAt(creationDate);
            issueIndex++;
        }
        catch (Exception ex) {
            System.out.println("Cannot create issue: " + ex.getMessage());
        }
        return issue;
    }

    public String getHighDay1() {
        return highDay1;
    }

    public String getHighDay2() {
        return highDay2;
    }
    
    public String getMostRecentHighDay() {
        return highDay1.compareTo(highDay2) > 0 ? highDay1 : highDay2;
    }

    /*
     * Alternate between 'close' and 'open' whenever creating an issue
     */
    private String makeState() {
        stateIndex = (++stateIndex % 2);
        return (stateIndex == 0 ? "close" : "open");
    }

    /*
     * Generate a random time between 00:00:00 and 23:59:59, with leading space
     */
    private String makeRandomTime() {
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        return String.format(" %02d:%02d:%02d",hour,minute,second);
    }

    /*
     * Generate a random date in 2018
     */
    private String makeRandomDate() {
        int year = 2018;
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(30);
        return String.format("%d-%02d-%02d", year, month, day);
    }
}
