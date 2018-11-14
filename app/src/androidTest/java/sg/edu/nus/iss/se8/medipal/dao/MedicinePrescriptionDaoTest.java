package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MedicinePrescriptionDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {
        Medicine md = new Medicine(null, "medicine name panadol", "for fever", new Category(1, null, null, Category.ReminderApplicableOption.Y, null));
        MedicineDao.save(md);
        MedicinePrescription mp = new MedicinePrescription(null, md, 34, 3333, 43433434, new Date(), new Date(), false, 88);
        MedicinePrescriptionDao.save(mp);

        MedicinePrescription mp2 = new MedicinePrescription(mp.getId(), md, 99, 999, 9999999, new Date(), new Date(), false, 88);
        MedicinePrescriptionDao.update(mp2);

        List<MedicinePrescription> listMP = MedicinePrescriptionDao.getAll();
        MedicinePrescriptionDao.delete(mp2.getId());
        assertEquals(ConsumptionDao.getAllByPrescription(mp2).size(), 0);
    }
}