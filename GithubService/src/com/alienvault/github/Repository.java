package com.alienvault.github;

/*
 * Helper class used in conjunction with tool GetPublicRepos, for the sole purpose
 * of identifying a list of valid public repo name.
 */
public class Repository {
    private String full_name;

    public  String getFull_name() {
        return this.full_name;
    }
    public  void setFull_name(String name) {
        this.full_name = name;
    }
    public String toString() {
        return "<full_name = " + full_name + ">";
    }
}
