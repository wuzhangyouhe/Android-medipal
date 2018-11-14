/*
package sg.edu.nus.iss.se8.medipal.helpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.reminders.AppointmentReminder;
import sg.edu.nus.iss.se8.medipal.utils.RandomUtils;

import static org.junit.Assert.assertEquals;

public class ReminderHelperTest {
    private ReminderHelper reminderHelper;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAppointmentReminders() throws Exception {
        Appointment appointment1 = new Appointment(1, "TTSH", RandomUtils.date(),true,  "See eye specialist");
        AppointmentReminder appointmentReminder1 = new AppointmentReminder(appointment1);

        Appointment appointment2 = new Appointment(3, "SGH", RandomUtils.date(), true, "See heart specialist");
        AppointmentReminder appointmentReminder2 = new AppointmentReminder(appointment2);

        reminderHelper = new ReminderHelper();
        reminderHelper.addReminder(appointmentReminder1);
        reminderHelper.addReminder(appointmentReminder2);
        List<AppointmentReminder> retrievedAppointmentReminders = reminderHelper.getAppointmentReminders();

        assertEquals("Incorrect size of retrieved appointment reminders list.", 2, retrievedAppointmentReminders.size());

        AppointmentReminder retrievedReminder;
        retrievedReminder = retrievedAppointmentReminders.get(0);
        assertEquals("TTSH", retrievedReminder.getReferenceObject().getLocation());

        retrievedReminder = retrievedAppointmentReminders.get(1);
        assertEquals("SGH", retrievedReminder.getReferenceObject().getLocation());
    }
}*/
