package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;
import sg.edu.nus.iss.se8.medipal.reminders.ReminderReferenceObjectType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReminderDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {
        Appointment a = AppointmentDao.save(new Appointment(null, "loc", new Date(), false, "apppointment"));
        int initialCount = ReminderDao.getAll().size();
        Reminder r = ReminderDao.save(new Reminder(null, a, ReminderReferenceObjectType.Appointment, false, new Date(), true, "description of reminder"));
        assertTrue(r.getId() > 0);
        assertTrue(ReminderDao.getAll().size() == initialCount + 1);
        r.setDescription("newly description");
        ReminderDao.update(r);
        assertTrue(ReminderDao.getAll().get(ReminderDao.getAll().size() - 1).getDescription().equals("newly description"));
        Category c = new Category(null, "hello", "asdf", Category.ReminderApplicableOption.Y, "asdfdasfaf");

        CategoryDao.save(c);
        Medicine md = new Medicine(null, "medicine name panadol", "for fever", c);
        MedicineDao.save(md);
        MedicinePrescription mp = new MedicinePrescription(null, md, 34, 3333, 43433434, new Date(), new Date(), false, 88);
        MedicinePrescriptionDao.save(mp);
        Reminder r2 = ReminderDao.save(new Reminder(null, mp, ReminderReferenceObjectType.MedicinePrescriptionConsumption, false, new Date(), true, "description of reminder"));
        assertTrue(ReminderDao.getAll().size() == initialCount + 2);
    }
}