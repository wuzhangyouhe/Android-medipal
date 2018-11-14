package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;

import static org.junit.Assert.assertTrue;

public class ConsumptionDaoTest {

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

        Consumption c = new Consumption(null, mp, 4, new Date());
        ConsumptionDao.save(c);
        ConsumptionDao.getAll();
        ConsumptionDao.getAllByPrescription(mp);
        c.setQuantity(666);
        ConsumptionDao.update(c);
        ConsumptionDao.delete(c.getId());
        ConsumptionDao.getAll();

    }
}