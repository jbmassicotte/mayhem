package com.alienvault.report;

import com.alienvault.github.Issue;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopDay {
    private String day;
    private HashMap<String,Integer> occurrences;

    public String getDay() {
        return day;
    }

    public HashMap<String,Integer> getOccurrences() {
        return occurrences;
    }

    /**
     * Go through list of issues, identify 'high day', and identify issues
     * that have occurred on that day.
     * @param issues A list of sorted issues
     */
    public TopDay(List<Issue> issues) throws Exception {
        if (issues.isEmpty()) {
            throw new Exception("Issue list is empty; cannot create TopDay");
        }
        // Count number of issues per day. Result is in a map of
        // type '<day> : <count>', where day is in format 'yyyy-MM-dd'.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String,Integer>	dailyCounts = generateDailyCounts(issues,formatter);

        // Identify 'top day'
        this.day = findHighCountDay(dailyCounts);

        // Identify all issues occurring during 'top day'
        this.occurrences = findOccurrences(issues, this.day, formatter);
    }

    public String toString() {
        return "<day = " + day + ", occurrences = " + occurrences + ">";
    }

    /*
     Count number of issues per day.
     Return map of <datestring> : <issuecount>
    */
    private HashMap<String, Integer> generateDailyCounts(
            List<Issue> issues,
            SimpleDateFormat formatter) {
        HashMap<String, Integer> dailyCounts = new HashMap<>();
        for (Issue issue : issues) {
            String issueDay = formatter.format(issue.getCreated_at());
            Integer count = dailyCounts.getOrDefault(issueDay, 0);
            dailyCounts.put(issueDay, ++count);
        }
        return dailyCounts;
    }

    /*
     Iterate through dailyCounts; identify day with highest count
     Return most recent day in case there are 2+ highest counts
    */
    private String findHighCountDay(
            HashMap<String,Integer> dailyCounts)
            throws Exception {
        String highDay = "undef";
        Integer highCount = 0;

        for (Map.Entry<String,Integer> entry : dailyCounts.entrySet()) {
            String currentDay = entry.getKey();
            Integer currentCount = entry.getValue();

            // look for count higher than highCount, or
            // count equal to highCount but with day greater than highDay
            if (currentCount > highCount ||
                    (currentCount == highCount && currentDay.compareTo(highDay) > 0)) {
                highDay = currentDay;
                highCount = currentCount;
            }
        }
        if (highCount == 0) {
            throw new Exception("All daily counts <= 0; cannot identify TopDay");
        }
        return highDay;
    }

    /*
     Identify all issues occurring on highDay
    */
    private HashMap<String,Integer> findOccurrences(
            List<Issue> issues,
            String highDay,
            SimpleDateFormat formatter) {
        /*
         Iterate through list of issues, identify those that fall on highDay.
         Use fact that issues are chronologically sorted to reduce
         number of iterations.
        */
        HashMap<String,Integer> instances = new HashMap<>();
        for (Issue issue : issues) {
            String issueDay = formatter.format(issue.getCreated_at());
            if (issueDay.compareTo(highDay) == 0) {
                // issue falls on highDay
                String key = issue.getRepository();
                Integer val = instances.getOrDefault(key,0);
                instances.put(key,++val);
            }
            else if (issueDay.compareTo(highDay) > 0)
                break;
        }
        return instances;
    }
}
