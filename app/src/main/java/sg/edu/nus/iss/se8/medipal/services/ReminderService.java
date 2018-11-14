package sg.edu.nus.iss.se8.medipal.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.dao.ConsumptionDao;
import sg.edu.nus.iss.se8.medipal.dao.ReminderDao;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.receivers.ReminderBroadcastReceiver;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;
import sg.edu.nus.iss.se8.medipal.reminders.ReminderReferenceObjectType;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

import static android.content.Context.ALARM_SERVICE;

public class ReminderService extends ReminderDao {
    public static final String EXTRA_BUNDLE = "BUNDLE";
    public static final String EXTRA_REMINDER = "REMINDER";
    private static final String TAG = "ReminderService";
    private Context context;

    public ReminderService(Context context) {
        this.context = context;
    }

    void createRemindersFor(Appointment appointment) {
        Reminder appointmentReminder = new Reminder(
                null,
                appointment,
                ReminderReferenceObjectType.Appointment,
                appointment.isReminderOn(),
                DateUtils.twentyFourHoursBefore(appointment.getDateAndTime()),
                false,
                null
        );
        appointmentReminder = ReminderDao.save(appointmentReminder);

        if (appointment.getAppointmentTask() != null) {
            Reminder appointmentTaskReminder = new Reminder(
                    null,
                    appointment,
                    ReminderReferenceObjectType.AppointmentTask,
                    appointment.isReminderOn(),
                    DateUtils.twentyFourHoursBefore(appointment.getAppointmentTask().getDateTime()),
                    false,
                    appointment.getAppointmentTask().getDescription()
            );
            appointmentTaskReminder = ReminderDao.save(appointmentTaskReminder);
            setAlarmForReminder(appointmentTaskReminder);
        }

        setAlarmForReminder(appointmentReminder);
    }

    void createRemindersFor(MedicinePrescription medicinePrescription) {
        Reminder expiryReminder = new Reminder(
                null,
                medicinePrescription,
                ReminderReferenceObjectType.MedicinePrescriptionExpiry,
                medicinePrescription.isReminderOn(),
                DateUtils.twentyFourHoursBefore(medicinePrescription.getExpiryDate()),
                false,
                null
        );
        ReminderDao.save(expiryReminder);
        setAlarmForReminder(expiryReminder);

        Reminder replenishReminder = new Reminder(
                null,
                medicinePrescription,
                ReminderReferenceObjectType.MedicinePrescriptionReplenish,
                medicinePrescription.isReminderOn(),
                DateUtils.twentyFourHoursBefore(medicinePrescription.getDepletionDate()),
                false,
                null
        );
        ReminderDao.save(replenishReminder);
        setAlarmForReminder(replenishReminder);

        createConsumptionReminderFor(medicinePrescription);
    }

    public void createConsumptionReminderFor(MedicinePrescription medicinePrescription) {
        if (ConsumptionDao.getAllByPrescription(medicinePrescription).size() > 0) {
            Reminder nextConsumptionReminder = new Reminder(
                    null,
                    medicinePrescription,
                    ReminderReferenceObjectType.MedicinePrescriptionConsumption,
                    medicinePrescription.isReminderOn(),
                    medicinePrescription.getDateTimeOfNextDose(),
                    false,
                    null
            );
            ReminderDao.save(nextConsumptionReminder);
            setAlarmForReminder(nextConsumptionReminder);
//            Log.d(TAG, "createConsumptionReminderFor: " + nextConsumptionReminder.toString());
//            Log.d(TAG, "createConsumptionReminderFor: getWhenToRemind is: " + nextConsumptionReminder.getWhenToRemind());
        }
    }

    public static void updateRemindersFor(Appointment appointment) {

    }

    public static void updateRemindersFor(MedicinePrescription medicinePrescription) {

    }

    void deleteRemindersFor(Appointment appointment) {
        deleteReminders(ReminderDao.getAll(appointment));
    }

    void deleteRemindersFor(MedicinePrescription medicinePrescription) {
        deleteReminders(ReminderDao.getAll(medicinePrescription));
    }

    private void deleteReminders(List<Reminder> reminders) {
        for (Reminder reminder : reminders) {
            ReminderDao.delete(reminder.getId());
            Log.d("deleted", "deleteReminders: " + reminder.getId());
        }

        setAlarmsForReminders();
    }

    public void setAlarmsForReminders() {
        for (Reminder reminder : ReminderService.getAll()) {
            cancelAlarmForReminder(reminder);
//            Log.d("setAlarmsForReminders", "cancelled alarm: " + reminder.getId());

            if (!DateUtils.dateIsInThePast(reminder.getWhenToRemind())) {
                setAlarmForReminder(reminder);
            }
        }
    }

    private void cancelAlarmForReminder(Reminder reminder) {
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
    }

    private void setAlarmForReminder(Reminder reminder) {
        if (reminder.hasReminded() || (!reminder.isReminderOn())) {
            return;
        }

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_REMINDER, reminder);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar reminderTime = Calendar.getInstance();
        reminderTime.setTime(reminder.getWhenToRemind());
        ((AlarmManager) context.getSystemService(ALARM_SERVICE)).setExact(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), pendingIntent);

        Log.d("setAlarmForReminder", reminder.getType().name() + ": " + reminderTime.getTime().toString());
    }
}
