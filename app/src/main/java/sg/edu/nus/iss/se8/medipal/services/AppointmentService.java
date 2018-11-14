package sg.edu.nus.iss.se8.medipal.services;

import android.content.Context;
import android.database.SQLException;

import java.util.List;

import sg.edu.nus.iss.se8.medipal.dao.AppointmentDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Appointment;

public class AppointmentService extends AppointmentDao {
    private Context context;

    public AppointmentService(Context context) {
        this.context = context;
    }

    public void createAppointment(Appointment appointment) throws MedipalException {
        Appointment savedAppointment;
        try {
            savedAppointment = save(appointment);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }

        ReminderService reminderService = new ReminderService(context);
        reminderService.createRemindersFor(savedAppointment);
    }

    public static List<Appointment> getAll() {
        return AppointmentDao.getAll();
    }

    public static Appointment update(Appointment appointment) {
        return AppointmentDao.update(appointment);
    }

    public void deleteAppointment(Integer appointmentId) {
        Appointment appointment = AppointmentService.get(appointmentId);

        ReminderService reminderService = new ReminderService(context);
        reminderService.deleteRemindersFor(appointment);

        AppointmentDao.delete(appointmentId);
    }
}
