package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EmergencyContactDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {
        EmergencyContact ec = new EmergencyContact(null, "test", "+65-98336472", EmergencyContact.Type.EN, "My Favorite Hospital", 4);
        int before = EmergencyContactDao.getAll().size();
        EmergencyContactDao.save(ec);
        assertNotNull(ec.getId());
        assertTrue(ec.getId() >= 1);
        int during = EmergencyContactDao.getAll().size();
        assertEquals(before + 1, during);
        EmergencyContact ec2 = new EmergencyContact(ec.getId(), "MODIFIED NAME", "+65-98336472", EmergencyContact.Type.EN, "My Favorite Hospital", 6);
        EmergencyContactDao.update(ec2);
        assertEquals(before + 1, during);
        assertEquals(ec2.getName(), "MODIFIED NAME");
        EmergencyContactDao.delete(ec.getId());
        int after = EmergencyContactDao.getAll().size();
        assertEquals(before, after);
    }
}