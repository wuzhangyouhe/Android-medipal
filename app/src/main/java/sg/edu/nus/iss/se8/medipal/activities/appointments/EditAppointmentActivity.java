package sg.edu.nus.iss.se8.medipal.activities.appointments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.activities.MainActivity;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.AppointmentsFragment;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.AppointmentTask;
import sg.edu.nus.iss.se8.medipal.services.AppointmentService;


public class EditAppointmentActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Edit Appointment Details";
    private TextInputEditText editLocation;
    private TextInputEditText editDate;
    private TextInputEditText editTime;
    private TextInputEditText editDescription;
    private Switch switchReminder;
    private Switch switchPreTask;
    private TextInputEditText editPreTaskDetails;
    private TextInputEditText editPreTaskDate;
    private TextInputEditText editPreTaskTime;
    private TextInputLayout inputLayoutPreTaskDetails;
    private TextInputLayout inputLayoutPreTaskDate;
    private TextInputLayout inputLayoutPreTaskTime;
    private Button buttonSaveAppointmentDetails;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialogPreTasks;
    private TimePickerDialog timePickerDialogPreTasks;
    private boolean reminder;
    private String location;
    private Date dateTime;
    private String description;
    private String preTaskDetails;
    private Date preTaskDateTime;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    private Calendar now;
    private Calendar userSelectedAppointmentDateTime;
    private Calendar userSelectedPreTaskDateTime;
    private Appointment appointment;
    private AppointmentTask appointmentTask;
    private Appointment newAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_appointment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment = (Appointment) getIntent().getSerializableExtra(AppointmentsFragment.APPOINTMENT);
        }

        initToolbar(TOOLBAR_TITLE, true);
        initUserInterface();
    }


    public void initUserInterface() {
        now = Calendar.getInstance();
        userSelectedAppointmentDateTime = Calendar.getInstance();
        userSelectedAppointmentDateTime.setTime(appointment.getDateAndTime());

        editLocation = (TextInputEditText) findViewById(R.id.editTextLocation);
        editDate = (TextInputEditText) findViewById(R.id.editTextDate);
        editTime = (TextInputEditText) findViewById(R.id.editTextTime);
        editDescription = (TextInputEditText) findViewById(R.id.editTextDescription);
        switchReminder = (Switch) findViewById(R.id.switchAppointmentReminder);
        switchPreTask = (Switch) findViewById(R.id.switchPretask);
        editPreTaskDetails = (TextInputEditText) findViewById(R.id.editTextPreTaskDetails);
        editPreTaskDate = (TextInputEditText) findViewById(R.id.editTextPreTaskDate);
        editPreTaskTime = (TextInputEditText) findViewById(R.id.editTextPreTaskTime);
        inputLayoutPreTaskDetails = (TextInputLayout) findViewById(R.id.inputLayoutPreTaskDetails);
        inputLayoutPreTaskDate = (TextInputLayout) findViewById(R.id.inputLayoutPreTaskDate);
        inputLayoutPreTaskTime = (TextInputLayout) findViewById(R.id.inputLayoutPreTaskTime);
        buttonSaveAppointmentDetails = (Button) findViewById(R.id.buttonAddAppointment);

        setDataInUserInterface();

        initDatePickerTextView();
        initDatePickerDialog();
        initTimePickerDialog();
        initTimePickerTextView();
        initReminderSwitch();
        initPreTasksSwitch();
        initPreTasksDatePickerTextView();
        initPreTasksTimePickerTextView();
        initPreTasksTimePickerDialog();
        initPreTasksDatePickerDialog();
        initButtonSaveAppointmentDetails();

        checkAndSetPreTask();
    }

    private void checkAndSetPreTask() {

        if (appointment.getAppointmentTask()!=null && appointment.getAppointmentTask().getDescription() != null) {
            appointmentTask = appointment.getAppointmentTask();
            userSelectedPreTaskDateTime = Calendar.getInstance();
            userSelectedPreTaskDateTime.setTime(appointmentTask.getDateTime());
            switchPreTask.setChecked(true);

            editPreTaskDetails.setText(appointmentTask.getDescription());
            editPreTaskDate.setText(dateFormatter.format(appointmentTask.getDateTime()));
            editPreTaskTime.setText(timeFormatter.format(appointmentTask.getDateTime()));
        } else {
            userSelectedPreTaskDateTime = Calendar.getInstance();
            appointmentTask = new AppointmentTask(null, null);
            switchPreTask.setChecked(false);
        }
    }

    public void setDataInUserInterface() {
        editLocation.setText(appointment.getLocation());

        editDate.setText(dateFormatter.format(appointment.getDateAndTime()));
        editTime.setText(timeFormatter.format(appointment.getDateAndTime()));
        editDescription.setText(appointment.getDescription());

        if (appointment.isReminderOn()) {
            switchReminder.setChecked(true);
            reminder = true;
        } else {
            switchReminder.setChecked(false);
            reminder = false;
        }
    }

    private void initDatePickerTextView() {
        editDate.setText(dateFormatter.format(appointment.getDateAndTime()));
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void initDatePickerDialog() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                userSelectedAppointmentDateTime = Calendar.getInstance();
                userSelectedAppointmentDateTime.set(year, monthOfYear, dayOfMonth);
                editDate.setText(dateFormatter.format(userSelectedAppointmentDateTime.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
    }

    private void initTimePickerTextView() {
        editTime.setText(timeFormatter.format(appointment.getDateAndTime()));
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }

    private void initTimePickerDialog() {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                userSelectedAppointmentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                userSelectedAppointmentDateTime.set(Calendar.MINUTE, minute);
                editTime.setText(timeFormatter.format(userSelectedAppointmentDateTime.getTime()));
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);

    }

    private void initReminderSwitch() {
        switchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reminder = isChecked;
            }
        });
    }

    public void initPreTasksSwitch() {
        switchPreTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(EditAppointmentActivity.this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    inputLayoutPreTaskDetails.setVisibility(View.VISIBLE);
                    inputLayoutPreTaskDate.setVisibility(View.VISIBLE);
                    inputLayoutPreTaskTime.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(EditAppointmentActivity.this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    inputLayoutPreTaskDetails.setVisibility(View.INVISIBLE);
                    inputLayoutPreTaskDate.setVisibility(View.INVISIBLE);
                    inputLayoutPreTaskTime.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initPreTasksDatePickerTextView() {
        editPreTaskDate.setText(dateFormatter.format(now.getTime()));
        editPreTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogPreTasks.show();
            }
        });
    }

    private void initPreTasksDatePickerDialog() {
        datePickerDialogPreTasks = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                userSelectedPreTaskDateTime = Calendar.getInstance();
                userSelectedPreTaskDateTime.set(year, monthOfYear, dayOfMonth);
                editPreTaskDate.setText(dateFormatter.format(userSelectedPreTaskDateTime.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

    }

    private void initPreTasksTimePickerTextView() {
        editPreTaskTime.setText(timeFormatter.format(now.getTime()));
        editPreTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogPreTasks.show();
            }
        });
    }

    private void initPreTasksTimePickerDialog() {
        timePickerDialogPreTasks = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                userSelectedPreTaskDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                userSelectedPreTaskDateTime.set(Calendar.MINUTE, minute);
                editPreTaskTime.setText(timeFormatter.format(userSelectedPreTaskDateTime.getTime()));
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);

    }

    private void initButtonSaveAppointmentDetails() {
        buttonSaveAppointmentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppointmentDetails(v);
            }
        });
    }

    public void saveAppointmentDetails(View v) {
        getAppointmentDataFromUserInterface();

        if (validateData()) {
            updateAppointmentPreTasksDetails();
            updateAppointmentDetails();

            AppointmentService.update(newAppointment);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Changes saved!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    runIntent();
                }
            });
            builder.show();
        }
    }

    public void updateAppointmentPreTasksDetails() {
        if (switchPreTask.isChecked()) {
            preTaskDetails = editPreTaskDetails.getText().toString();
            preTaskDateTime = userSelectedPreTaskDateTime.getTime();
            appointmentTask.setDescription(preTaskDetails);
            appointmentTask.setDateTime(preTaskDateTime);
        }
    }

    public void updateAppointmentDetails() {
        if(switchPreTask.isChecked()){
            newAppointment = new Appointment(null, location, dateTime, reminder, description, appointmentTask);
        } else {
            newAppointment = new Appointment(null, location, dateTime, reminder, description, new AppointmentTask(null, null));
        }

        newAppointment.setId(appointment.getId());
    }

    public boolean validateData() {
        if (location.isEmpty()) {
            editLocation.setError("You need to enter a location");
            return false;
        } else if (description.isEmpty()) {
            editDescription.setError("You need to enter a description");
            return false;
        } else if (switchPreTask.isChecked() && preTaskDetails.isEmpty()) {
            editPreTaskDetails.setError("You need to enter the pre-task details");
            return false;
        } else {
            return true;
        }
    }

    public void runIntent() {
        Intent returnToFragment = new Intent(this, MainActivity.class);
        returnToFragment.putExtra("viewpager_position", 3);
        startActivity(returnToFragment);
    }

    public void getAppointmentDataFromUserInterface() {
        location = editLocation.getText().toString();
        dateTime = userSelectedAppointmentDateTime.getTime();
        description = editDescription.getText().toString();

        if (switchPreTask.isChecked()) {
            preTaskDetails = editPreTaskDetails.getText().toString();
            preTaskDateTime = userSelectedPreTaskDateTime.getTime();
        }
    }
}
