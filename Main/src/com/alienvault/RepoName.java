package com.alienvault;

import java.util.ArrayList;
import java.util.List;

public class RepoName {
    String owner;
    String repo;

    /**
     * Retrieves owner and repository from input string.
     * Throws error is string format does not match "owner/repository".
     * @param string A github repository of format "owner/repository"
     */
    public RepoName(String string) throws Exception {
        final String delimiters = "/";
        String[] tokens = string.split(delimiters);
        if (tokens.length != 2) {
            throw new Exception("invalid argument: " + string);
        }
        this.owner = tokens[0];
        this.repo = tokens[1];
    }
    public String getOwner() {
        return owner;
    }
    public String getRepo() {
        return repo;
    }
    public String toString() {
        return "<owner = " + owner + ", repo = " + repo + ">";
    }

    /**
     * Given a list of strings of format "owner/repository", return
     * a list of RepoNames.
     * Throws error if string list is emptyh.
     * @param strings A list of github repository of format "owner/repository"
     */
    public static List<RepoName> makeRepoNames(String[] strings) throws Exception {
        if (strings.length == 0) {
            throw new Exception("Empty list");
        }
        List<RepoName> names = new ArrayList<>();
        for (String string : strings) {
            names.add(new RepoName(string));
        }
        return names;
    }
}
