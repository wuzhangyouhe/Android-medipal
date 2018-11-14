package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.AppointmentTask;

public class AppointmentDao {
    protected static Appointment save(Appointment appointment) throws MedipalException {
        if (appointment.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            appointment.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("location", appointment.getLocation());
            values.put("date_and_time", appointment.getDateAndTime().getTime());
            values.put("reminder_on", appointment.isReminderOn());
            values.put("description", appointment.getDescription());
            if (appointment.getAppointmentTask() != null) {
                values.put("task_date_and_time", appointment.getAppointmentTask().getDateTime() == null ? null : appointment.getAppointmentTask().getDateTime().getTime());
                values.put("task_description", appointment.getAppointmentTask().getDescription());
            }
            long id = DBDAO.getDatabase().insert("appointments", null, values);
            appointment.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return appointment;
    }

    public static Appointment get(Integer id) {
        String sql = "SELECT id, location, date_and_time, reminder_on, description, task_date_and_time, task_description FROM appointments where id = ?";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{"" + id});
        cursor.moveToFirst();
        Appointment a = new Appointment(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), cursor.getInt(3) == 1, cursor.getString(4));
        a.setAppointmentTask(new AppointmentTask(cursor.getString(6), new Date(cursor.getLong(5))));
        return a;
    }

    protected static List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<Appointment>();
        String sql = "SELECT id, location, date_and_time, reminder_on, description, task_date_and_time, task_description FROM appointments";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Appointment a = new Appointment(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), cursor.getInt(3) == 1, cursor.getString(4));
            if (cursor.getString(6) != null && cursor.getLong(5) != 0) {
                a.setAppointmentTask(new AppointmentTask(cursor.getString(6), new Date(cursor.getLong(5))));
            }
            list.add(a);
        }
        return list;
    }

    protected static Appointment update(Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put("location", appointment.getLocation());
        values.put("date_and_time", appointment.getDateAndTime().getTime());
        values.put("reminder_on", appointment.isReminderOn());
        values.put("description", appointment.getDescription());
        if (appointment.getAppointmentTask() != null) {
            values.put("task_date_and_time", appointment.getAppointmentTask().getDateTime() == null ? null : appointment.getAppointmentTask().getDateTime().getTime());
            values.put("task_description", appointment.getAppointmentTask().getDescription());
        }
        int count = DBDAO.getDatabase().update("appointments", values, "id = ?", new String[]{appointment.getId() + ""});
        return appointment;
    }

    protected static void delete(Integer id) {
        DBDAO.getDatabase().delete("appointments", "id = ?", new String[]{id + ""});
    }
}
