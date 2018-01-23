package avicit.plm.core.accesscontrol.dispatcher.path;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

// reference: undertow classe
public class PathTemplateTestCase {

    @Test
    public void testMatches() {
        // test normal use
        testMatch("/docs/mydoc", "/docs/mydoc");
        testMatch("/docs/{docId}", "/docs/mydoc", "docId", "mydoc");
        testMatch("/docs/{docId}/{op}", "/docs/mydoc/read", "docId", "mydoc", "op", "read");
        testMatch("/docs/{docId}/{op}/{allowed}", "/docs/mydoc/read/true", "docId", "mydoc", "op", "read", "allowed", "true");
        testMatch("/docs/{docId}/operation/{op}", "/docs/mydoc/operation/read", "docId", "mydoc", "op", "read");
        testMatch("/docs/{docId}/read", "/docs/mydoc/read", "docId", "mydoc");
        testMatch("/docs/{docId}/read", "/docs/mydoc/read?myQueryParam", "docId", "mydoc");

        // test no leading slash
        testMatch("docs/mydoc", "/docs/mydoc");
        testMatch("docs/{docId}", "/docs/mydoc", "docId", "mydoc");
        testMatch("docs/{docId}/{op}", "/docs/mydoc/read", "docId", "mydoc", "op", "read");
        testMatch("docs/{docId}/{op}/{allowed}", "/docs/mydoc/read/true", "docId", "mydoc", "op", "read", "allowed", "true");
        testMatch("docs/{docId}/operation/{op}", "/docs/mydoc/operation/read", "docId", "mydoc", "op", "read");
        testMatch("docs/{docId}/read", "/docs/mydoc/read", "docId", "mydoc");
        testMatch("docs/{docId}/read", "/docs/mydoc/read?myQueryParam", "docId", "mydoc");

        // test trailing slashes
        testMatch("/docs/mydoc/", "/docs/mydoc/");
        testMatch("/docs/{docId}/", "/docs/mydoc/", "docId", "mydoc");
        testMatch("/docs/{docId}/{op}/", "/docs/mydoc/read/", "docId", "mydoc", "op", "read");
        testMatch("/docs/{docId}/{op}/{allowed}/", "/docs/mydoc/read/true/", "docId", "mydoc", "op", "read", "allowed", "true");
        testMatch("/docs/{docId}/operation/{op}/", "/docs/mydoc/operation/read/", "docId", "mydoc", "op", "read");
        testMatch("/docs/{docId}/read/", "/docs/mydoc/read/", "docId", "mydoc");

        // test straight replacement of template
        testMatch("/{foo}", "/bob", "foo", "bob");
        testMatch("{foo}", "/bob", "foo", "bob");
        testMatch("/{foo}/", "/bob/", "foo", "bob");

        // test that brackets (and the possibility of recursive templates) don't mess up the matching
        testMatch("/{value}", "/{value}", "value", "{value}");
    }

    @Test
    public void wildCardTests() {
        // wildcard matches
        testMatch("/*", "/docs/mydoc/test","*","docs/mydoc/test");
        testMatch("/docs/*", "/docs/mydoc/test","*","mydoc/test");
        testMatch("/docs*", "/docs/mydoc/test","*","/mydoc/test");
        testMatch("/docs/*", "/docs/mydoc/test/test2","*","mydoc/test/test2");
        testMatch("/docs/{docId}/*", "/docs/mydoc/test", "docId", "mydoc", "*","test");
        testMatch("/docs/{docId}/*", "/docs/mydoc/", "docId", "mydoc", "*","");
        testMatch("/docs/{docId}/*", "/docs/mydoc/test/test2/test3/test4", "docId", "mydoc", "*","test/test2/test3/test4");
        testMatch("/docs/{docId}/{docId2}/*", "/docs/mydoc/test/test2/test3/test4", "docId", "mydoc","docId2", "test", "*","test2/test3/test4");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullPath() {
        PathTemplate.create(null);
    }

    @Test
    public void testDetectDuplicates() {
        final TreeSet<PathTemplate> seen = new TreeSet<>();
        seen.add(PathTemplate.create("/bob/{foo}"));
        Assert.assertTrue(seen.contains(PathTemplate.create("/bob/{ak}")));
        Assert.assertFalse(seen.contains(PathTemplate.create("/bob/{ak}/other")));
    }

    @Test
    public void testTrailingSlash() {
        PathTemplate template = PathTemplate.create("/bob/");
        Assert.assertFalse(template.matches("/bob", new HashMap<>()));
        Assert.assertTrue(template.matches("/bob/", new HashMap<>()));

        template = PathTemplate.create("/bob/{id}/");
        Assert.assertFalse(template.matches("/bob/1", new HashMap<>()));
        Assert.assertTrue(template.matches("/bob/1/", new HashMap<>()));

    }

    private void testMatch(final String template, final String path, final String ... pathParams)  {
        Assert.assertEquals(0, pathParams.length % 2);
        final Map<String, String> expected = new HashMap<>();
        for(int i = 0; i < pathParams.length; i+=2) {
            expected.put(pathParams[i], pathParams[i+1]);
        }
        final Map<String, String> params = new HashMap<>();

        PathTemplate pathTemplate = PathTemplate.create(template);
        Assert.assertTrue("Failed. Template: " + pathTemplate, pathTemplate.matches(path, params));
        Assert.assertEquals(expected, params);
        if(template.endsWith("*") && ! template.contains("{")){
            Assert.assertEquals("Failed. Template: "+pathTemplate+"Must have a part representing the wildcard",1,new PathTemplateFriend(pathTemplate).getPartAmount());
        }

    }

    static class PathTemplateFriend {
        private final PathTemplate template;

        PathTemplateFriend(PathTemplate template) {
            this.template = template;
        }

        int getPartAmount() {
            return template.parts.size();
        }
    }

}
