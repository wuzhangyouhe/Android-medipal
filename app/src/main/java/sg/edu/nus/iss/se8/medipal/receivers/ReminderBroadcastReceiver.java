package sg.edu.nus.iss.se8.medipal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.helpers.NotificationHelper;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;
import sg.edu.nus.iss.se8.medipal.services.ReminderService;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "receiver";
    private Context context;
    private Reminder reminder;
    private MediaPlayer mediaPlayer;
    private Appointment appointment;
    private String notificationTitle;
    private String notificationText;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.UK);
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.UK);
    private MedicinePrescription medicinePrescription;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Bundle bundle = intent.getBundleExtra(ReminderService.EXTRA_BUNDLE);
        if (bundle != null) {
            reminder = (Reminder) bundle.getSerializable(ReminderService.EXTRA_REMINDER);

            prepareNotificationTexts();
            playNotificationSound();
            showToast();
            showLocalNotification();
        }

        for (Reminder reminder : ReminderService.getAll()) {
            Log.d(TAG, "onReceive: " + reminder.toString());
        }

        reminder.setReminded(true);
        ReminderService.update(reminder);

        for (Reminder reminder : ReminderService.getAll()) {
            Log.d(TAG, "onReceive: " + reminder.toString());
        }
    }

    private void prepareNotificationTexts() {
        if (reminder.getReferenceObject() instanceof Appointment) {
            appointment = (Appointment) reminder.getReferenceObject();

            if (reminder.getType().name().equals("Appointment")) {
                notificationTitle = "Appointment at " + String.valueOf(appointment.getLocation());
                notificationText = "on " + dateFormatter.format(appointment.getDateAndTime()) + " at " + timeFormatter.format(appointment.getDateAndTime());
            } else {
                notificationTitle = "Appointment Pre Task";
                notificationText = String.valueOf(appointment.getAppointmentTask().getDescription());
            }
        } else {
            medicinePrescription = ((MedicinePrescription) reminder.getReferenceObject());

            if (reminder.getType().name().equals("MedicinePrescriptionConsumption")) {
                notificationTitle = "Time to consume: " + String.valueOf(medicinePrescription.getMedicine().getName());
            } else if (reminder.getType().name().equals("MedicinePrescriptionReplenish")) {
                notificationTitle = "Time to replenish: " + String.valueOf(medicinePrescription.getMedicine().getName());
            } else {
                notificationTitle = "Expiring tomorrow: " + String.valueOf(medicinePrescription.getMedicine().getName());
            }
            notificationText = dateFormatter.format(reminder.getWhenToRemind()) + ", " + timeFormatter.format(reminder.getWhenToRemind());
        }
    }

    private void playNotificationSound() {
        mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();
    }

    private void showToast() {
        Log.d("receiver", "reminder: " + reminder.toString());
        Log.d("receiver", "reminder: " + Calendar.getInstance().getTime().toString());
        Toast.makeText(context, "reminder triggered at: " + Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();
    }

    private void showLocalNotification() {
        NotificationHelper.makeNotification(context, notificationTitle, notificationText, reminder.getId());
    }


}
