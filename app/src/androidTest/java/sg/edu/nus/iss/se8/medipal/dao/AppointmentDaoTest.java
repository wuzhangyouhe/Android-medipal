package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.AppointmentTask;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AppointmentDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testCRUD() throws Exception {

        Appointment ec = new Appointment(null, "Woodlands Polyclinic", new Date(), false, "Full body checkup");
        int before = AppointmentDao.getAll().size();
        AppointmentDao.save(ec);
        assertNotNull(ec.getId());
        assertTrue(ec.getId() >= 1);

        ec.setAppointmentTask(new AppointmentTask("dummy description", new Date()));
        ec.setId(null);
        AppointmentDao.save(ec);

        int during = AppointmentDao.getAll().size();
        assertEquals(before + 2, during);
        Appointment ec2 = new Appointment(ec.getId(), "MODIFIED LOCATION", new Date(), false, "Full body checkup");
        AppointmentDao.update(ec2);
        assertEquals(before + 2, during);
        assertEquals(ec2.getLocation(), "MODIFIED LOCATION");
        AppointmentDao.delete(ec.getId());
        int after = AppointmentDao.getAll().size();
        assertEquals(before+1, after);
    }
}