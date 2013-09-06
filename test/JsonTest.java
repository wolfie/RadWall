import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.wolfie.radwall.rest.ChangeDAO;
import com.google.gson.GsonBuilder;

public class JsonTest {

    //@formatter:off
    private static String CHANGE_JSON =
    "{"+
        "'kind': 'gerritcodereview#change',"+
        "'id': 'vaadin~master~I750dff060469a656e199985984794f186365f121',"+
        "'project': 'vaadin',"+
        "'branch': 'master',"+
        "'change_id': 'I750dff060469a656e199985984794f186365f121',"+
        "'subject': 'Makes sure the aria-invalid attribute is removed when the caption is removed and not updated (#12517)',"+
        "'status': 'NEW',"+
        "'created': '2013-09-06 09:27:07.557000000',"+
        "'updated': '2013-09-06 09:27:07.557000000',"+
        "'mergeable': false,"+
        "'_sortkey': '002794d700000719',"+
        "'_number': 1817,"+
        "'owner': {"+
        "  'name': 'Michael Vogt'"+
        "}"+
    "}";
    
    private static String CHANGES_JSON = "["+CHANGE_JSON+"]";
    //@formatter:on

    @Test
    public void changeDAOSmokeTest() {
        ChangeDAO change = new GsonBuilder().create().fromJson(CHANGE_JSON,
                ChangeDAO.class);
        assertNotNull("kind", change.kind);
        assertNotNull("id", change.id);
        assertNotNull("project", change.project);
        assertNotNull("branch", change.branch);
        assertNotNull("changeId", change.changeId);
        assertNotNull("subject", change.subject);
        assertNotNull("status", change.status);
        assertNotNull("created", change.created);
        assertNotNull("updated", change.updated);
        assertNotNull("mergeable", change.mergeable);
        assertNotNull("_sortkey", change._sortkey);
        assertNotNull("_number", change._number);
        assertNotNull("owner", change.owner);
    }

    @Test
    public void changesDAOSmokeTest() {
        ChangeDAO[] changes = new GsonBuilder().create().fromJson(CHANGES_JSON,
                ChangeDAO[].class);
        assertNotNull("Changes should not be a null array", changes);
        assertEquals("Only one change should be passed", 1, changes.length);
    }

}
