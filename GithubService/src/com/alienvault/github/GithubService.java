package com.alienvault.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class GithubService   {
    private static final String BASE_URL = "https://api.github.com/";
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String UNKNOWN_ERROR = "Unknown error";

    private GithubRepoInterface service;

    public GithubService() {
        Gson gson = new GsonBuilder()
                .setDateFormat(ISO_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(GithubRepoInterface.class);
    }

    /*
     * Get all github issues for 'owner/repo', in ascending order by creation time
     */
    public List<Issue> getIssuesForRepo(
            String owner,
            String reponame)
            throws IOException {
        Call<List<Issue>> callissues = service.getIssues(owner,reponame);

        Response<List<Issue>> response = callissues.execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : UNKNOWN_ERROR);
        }
        return response.body();
    }

    /*
     * Get all public github repositories
     */
    public List<Repository> getPublicRepos() throws IOException {
        Call<List<Repository>> callrepos = service.getRepositories();

        Response<List<Repository>> response = callrepos.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : UNKNOWN_ERROR);
        }
        return response.body();
    }
}
