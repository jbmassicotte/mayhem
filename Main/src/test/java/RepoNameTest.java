package test.java;

import com.alienvault.RepoName;
import junit.framework.TestCase;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class RepoNameTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testMakeRepoNames() {
        String[] strings = {"hello/world", "foo/bar"};
        try {
            List<RepoName> names = RepoName.makeRepoNames(strings);
            assertEquals(names.get(0).getOwner(), "hello");
            assertEquals(names.get(0).getRepo(), "world");
            assertEquals(names.get(1).getOwner(),"foo");
            assertEquals(names.get(1).getRepo(), "bar");
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    public void testInvalidRepoName() {
        String[] strings = {"helloworld", "foo/bar"};
        try {
            List<RepoName> names = RepoName.makeRepoNames(strings);
            fail("Should throw exception for invalid repo");
        } catch (Exception ex) {
            assertThat(ex, instanceOf(Exception.class));
        }
    }

    public void testEmptyNameList() {
        String[] strings = {};
        try {
            List<RepoName> names = RepoName.makeRepoNames(strings);
            fail("Should throw exception for empty list");
        } catch (Exception ex) {
            assertThat(ex, instanceOf(Exception.class));
        }
    }
}