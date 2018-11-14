package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;
import sg.edu.nus.iss.se8.medipal.reminders.ReminderReferenceObjectType;

public class ReminderDao {
    public static Reminder save(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(reminder.getType().getReferenceTable() + "_id", reminder.getReferenceObjectId());
        values.put("type", reminder.getType().name());
        values.put("reminder_on", reminder.isReminderOn());
        values.put("reminder_date_and_time", reminder.getWhenToRemind().getTime());
        values.put("reminded", reminder.hasReminded());
        values.put("description", reminder.getDescription());
        long id = DBDAO.getDatabase().insert("reminders", null, values);
        reminder.setId((int) id);
        return reminder;
    }

    public static Reminder get(Appointment appointment, ReminderReferenceObjectType type) throws MedipalException {
        String sql = "SELECT id, prescription_id, appointment_id, type, reminder_on, reminder_date_and_time, reminded, description FROM reminders where appointment_id = ? and type = ?";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{appointment.getId() + "", type.name()});
        try {
            cursor.moveToNext();
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.NOT_FOUND, MedipalException.Level.MESSAGE, e);
        }
        Reminder reminder = populateReminder(cursor);
        return reminder;
    }


    public static Reminder get(MedicinePrescription medicinePrescription, ReminderReferenceObjectType type) {
        String sql = "SELECT id, prescription_id, appointment_id, type, reminder_on, reminder_date_and_time, reminded, description FROM reminders where prescription_id = ? and type = ?";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{medicinePrescription.getId() + "", type.name()});
        cursor.moveToNext();
        Reminder reminder = populateReminder(cursor);
        return reminder;
    }

    public static List<Reminder> getAll(Appointment appointment) {
        List<Reminder> list = new ArrayList<Reminder>();
        String sql = "SELECT id, prescription_id, appointment_id, type, reminder_on, reminder_date_and_time, reminded, description FROM reminders where appointment_id = ?";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{appointment.getId() + ""});
        while (cursor.moveToNext()) {
            list.add(populateReminder(cursor));
        }
        return list;
    }


    public static List<Reminder> getAll(MedicinePrescription medicinePrescription) {
        List<Reminder> list = new ArrayList<Reminder>();
        String sql = "SELECT id, prescription_id, appointment_id, type, reminder_on, reminder_date_and_time, reminded, description FROM reminders where prescription_id = ?";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{medicinePrescription.getId() + ""});
        while (cursor.moveToNext()) {
            list.add(populateReminder(cursor));
        }
        return list;
    }


    public static List<Reminder> getAll() {
        List<Reminder> list = new ArrayList<Reminder>();
        String sql = "SELECT id, prescription_id, appointment_id, type, reminder_on, reminder_date_and_time, reminded, description FROM reminders";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(populateReminder(cursor));
        }
        return list;
    }

    private static Reminder populateReminder(Cursor cursor) {
        Reminder reminder = null;
        switch (ReminderReferenceObjectType.valueOf(cursor.getString(3))) {
            case Appointment:
            case AppointmentTask:
                reminder = new Reminder(cursor.getInt(0), AppointmentDao.get(cursor.getInt(2)), ReminderReferenceObjectType.valueOf(cursor.getString(3)), cursor.getInt(4) == 1, new Date(cursor.getLong(5)), cursor.getInt(6) == 1, cursor.getString(7));
                break;

            case MedicinePrescriptionConsumption:
            case MedicinePrescriptionExpiry:
            case MedicinePrescriptionReplenish:
                reminder = new Reminder(cursor.getInt(0), MedicinePrescriptionDao.get(cursor.getInt(1)), ReminderReferenceObjectType.valueOf(cursor.getString(3)), cursor.getInt(4) == 1, new Date(cursor.getLong(5)), cursor.getInt(6) == 1, cursor.getString(7));
                break;
        }
        return reminder;
    }

    private static Reminder get(String type, String referenceId) {
        return null;
    }

    public static Reminder update(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(reminder.getType().getReferenceTable() + "_id", reminder.getReferenceObjectId());
        values.put("type", reminder.getType().name());
        values.put("reminder_on", reminder.isReminderOn());
        values.put("reminder_date_and_time", reminder.getWhenToRemind().getTime());
        values.put("reminded", reminder.hasReminded());
        values.put("description", reminder.getDescription());
        int count = DBDAO.getDatabase().update("reminders", values, "id = ?", new String[]{reminder.getId() + ""});
        return reminder;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("reminders", "id = ?", new String[]{id + ""});
    }
}
