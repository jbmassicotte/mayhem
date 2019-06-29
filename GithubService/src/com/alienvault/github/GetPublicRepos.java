package com.alienvault.github;

/*
 * Helper tool used for retrieving list of public repositories.
 * Not intended as part of the final product.
 */

import java.io.IOException;
import java.util.List;

public class GetPublicRepos {
    public static void main(String[] args) {
        System.out.println("Getting public repos from github");

        GithubService githubService = new GithubService();
        try {
            List<Repository> repos = githubService.getPublicRepos();
            System.out.println(repos);
            for (Repository repo : repos) {
                System.out.println(repo.getFull_name());
            }
        } catch (IOException exc) {
            System.out.println("Error retrieving repo: " + exc);
        }
    }
}

