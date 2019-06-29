package com.alienvault.report;

import com.alienvault.github.Issue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class Report {
    private List<Issue> issues;
    private TopDay top_day;

    public List<Issue> getIssues() {
        return issues;
    }
    public TopDay getTop_day() {
        return top_day;
    }

    /**
     * Sort the input list by creation date, and identify the TopDay.
     * @param issues A list of unsorted issues
     */
    public Report(List<Issue> issues) throws Exception {
        this.issues = new ArrayList<>(issues);
        sortByCreateDate(this.issues);
        try {
            this.top_day = new TopDay(this.issues);
        }
        catch (Exception ex) {
            throw new Exception("Cannot create report: " + ex.getMessage());
        }
    }

    public String toString() {
        return "<issues = " + issues + ", top_day = " + top_day + ">";
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /*
     * Sort Issues by Created_at date, from old to new.
     * See discussion below regarding the sorting strategy.
     */
    private void sortByCreateDate(List<Issue> issuesToSort) {
        Comparator<Issue> compareByDate = new Comparator<Issue>() {
            @Override
            public int compare(Issue i1, Issue i2) {
                Date d1 = i1.getCreated_at();
                Date d2 = i2.getCreated_at();
                return d1.compareTo(d2);
            }
        };
        Collections.sort(issuesToSort, compareByDate);
    }
}

/************* Note to the reader regarding sorting strategy ********************

The current implementation appends each repo list to one global list,
and perform 1 sort on the global list. Assuming we have M repo lists, and (for
the sake of the discussion) all lists have N elements, the computational
complexity is O(MNlog(MN)), or O(MNlog(M) + MMlog(N)).

In the case where M is small and N is large, this becomes O(MNlog(N))
With M large and N small, the result is O(MNlog(M)).

Both cases are upper bound because these estimates do not consider the fact
that the sublists are presorted.

The alternate strategy consists of merging the sorted lists as they are retrieved
from github. Here is an example of how such a merge could be performed:
https://www.programcreek.com/2012/12/leetcode-merge-two-sorted-lists-java/

The computional complexity of one such merge is X+Y, where X and Y are the number of
items in each lists. The overall complexity can be evaluated as follow, again
assuming M sublists with N elements per sublist:

SUM i:1->M of ((i-1)N+N)
    = SUM i:1->M of iN
    = N(SUM i:1->M of i)
    = N(M(M+1)/2)   <-- using https://cseweb.ucsd.edu/groups/tatami/kumo/exs/sum/
    = N(MM/2 + M/2)
    ~ O(NMM)

In the case where M is small and N is large, the merge strategy wins as:
NMM < NMlog(N), or M < log(N).

In the case where M is large and N is small (essentially losing the benefits
of the presorted sublists), the 1 sort strategy wins as:
NMM > NMlog(M), or M > log(M).

Bottom line, without knowing in advance how M compares to N, we can't
predict which strategy is best, hence the reason we picked the 1 sort
strategy, which requires less code on our part and is easier to implement.

*****************************************************************************/