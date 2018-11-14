package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;

import static org.junit.Assert.assertTrue;

public class MedicineDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {



        Medicine md = new Medicine(null, "medicine name panadol", "for fever", new Category(1, null, null, Category.ReminderApplicableOption.Y, null));
        MedicineDao.save(md);
        Medicine md2 = new Medicine (md.getId(), "updated name", "still for fever", new Category(2, null, null, Category.ReminderApplicableOption.Y, null));
        MedicineDao.update(md2);
        MedicineDao.getAll();
        MedicineDao.delete(md.getId());
    }
}