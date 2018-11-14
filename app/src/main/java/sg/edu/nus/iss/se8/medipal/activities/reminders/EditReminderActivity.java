package sg.edu.nus.iss.se8.medipal.activities.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.ReminderDao;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.RemindersFragment;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class EditReminderActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Edit Reminder";

    private Reminder receivedReminder = null;

    private TextView textViewReminderWhenToRemindDate;
    private TextView textViewReminderWhenToRemindTime;
    private Switch switchReminderOn;
    private TextInputEditText textInputEditTextReminderDescription;
    private Button buttonEditReminder;
    private DatePickerDialog datePickerDialogReminderWhenToRemind;
    private TimePickerDialog timePickerDialogReminderWhenToRemind;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
    private final Calendar now = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        initToolbar(TOOLBAR_TITLE, true);
        initHandles();
        initDateAndTimePickerDialog();
        initDatePickerTextViews();
        initEditButton();
        unpackIntent();
    }

    private void initHandles() {
        textViewReminderWhenToRemindDate = (TextView) findViewById(R.id.textViewReminderWhenToRemindDate);
        textViewReminderWhenToRemindTime = (TextView) findViewById(R.id.textViewReminderWhenToRemindTime);
        switchReminderOn = (Switch) findViewById(R.id.switchReminderOn);
        textInputEditTextReminderDescription = (TextInputEditText) findViewById(R.id.textInputEditTextReminderDescription);
        buttonEditReminder = (Button) findViewById(R.id.buttonEditReminder);
    }

    private void initDatePickerTextViews() {
        textViewReminderWhenToRemindDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogReminderWhenToRemind.show();
            }
        });

        textViewReminderWhenToRemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogReminderWhenToRemind.show();
            }
        });
    }

    private void initDateAndTimePickerDialog() {
        datePickerDialogReminderWhenToRemind = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);

                textViewReminderWhenToRemindDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        timePickerDialogReminderWhenToRemind = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newTime = Calendar.getInstance();

                newTime.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                textViewReminderWhenToRemindTime.setText(DateUtils.toFriendlyTimeString(newTime.getTime()));
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
    }

    private void initEditButton() {
        buttonEditReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptEdit();
            }

            private void attemptEdit() {
                getFormDataAndSaveReminder();
                Toast.makeText(EditReminderActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);

                finish();
            }

            private void getFormDataAndSaveReminder() {
                Date whenToRemindDate = null;
                Date whenToRemindTime = null;

                try {
                    whenToRemindDate = DateUtils.fromFriendlyDateString(textViewReminderWhenToRemindDate.getText().toString());
                    whenToRemindTime = DateUtils.fromFriendlyTimeString(textViewReminderWhenToRemindTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                boolean reminderOn = switchReminderOn.isChecked();

                String reminderDescription = textInputEditTextReminderDescription.getText().toString();

                Date whenToRemind = (Date) whenToRemindDate.clone();
                Calendar calendarWhenToRemind = DateUtils.toCalendar(whenToRemind);
                Calendar calendarWhenToRemindTime = DateUtils.toCalendar(whenToRemindTime);
                calendarWhenToRemind.set(Calendar.HOUR_OF_DAY, calendarWhenToRemindTime.get(Calendar.HOUR_OF_DAY));
                calendarWhenToRemind.set(Calendar.MINUTE, calendarWhenToRemindTime.get(Calendar.MINUTE));

                receivedReminder.setWhenToRemind(calendarWhenToRemind.getTime());
                receivedReminder.setReminderOn(reminderOn);
                receivedReminder.setDescription(reminderDescription);

                ReminderDao.update(receivedReminder);
            }
        });
    }

    private void unpackIntent() {
        Intent intent = getIntent();

        receivedReminder = (Reminder) intent.getSerializableExtra(RemindersFragment.EXTRA_REMINDER);

        textViewReminderWhenToRemindDate.setText(DateUtils.toFriendlyDateString(receivedReminder.getWhenToRemind()));
        textViewReminderWhenToRemindTime.setText(DateUtils.toFriendlyTimeString(receivedReminder.getWhenToRemind()));
        switchReminderOn.setChecked(receivedReminder.isReminderOn());
        textInputEditTextReminderDescription.setText(receivedReminder.getDescription());

        Calendar whenToRemind = DateUtils.toCalendar(receivedReminder.getWhenToRemind());
        datePickerDialogReminderWhenToRemind.updateDate(whenToRemind.get(Calendar.YEAR), whenToRemind.get(Calendar.MONTH), whenToRemind.get(Calendar.DATE));
        timePickerDialogReminderWhenToRemind.updateTime(whenToRemind.get(Calendar.HOUR_OF_DAY), whenToRemind.get(Calendar.MINUTE));
    }
}
