package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import sg.edu.nus.iss.se8.medipal.models.User;

public class UserDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {
        User u = new User(null, "New User Name",new Date(), "","","",3.0, "");
        UsersDao.save(u);
        u.setName("ola");
        u.setAddress("new address");
        UsersDao.update(u);
        UsersDao.getAll();
        UsersDao.delete(u.getId());
    }
}