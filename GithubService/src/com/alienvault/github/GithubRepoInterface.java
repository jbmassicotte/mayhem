package com.alienvault.github;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface GithubRepoInterface {
    /*
     * Get all github issues for 'owner/repo', in ascending order by creation time
     */
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("repos/{owner}/{reponame}/issues?state=all&sort=created&direction=asc")
    public Call<List<Issue>> getIssues(
            @Path("owner") String owner,
            @Path("reponame") String reponame);

    /*
     * Get all public github repositories
     */
    @GET("repositories")
    public Call<List<Repository>> getRepositories();
}
