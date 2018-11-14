package sg.edu.nus.iss.se8.medipal.activities.medicineprescriptions;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.MedicinePrescriptionsFragment;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;
import sg.edu.nus.iss.se8.medipal.utils.NumberUtils;

public class AddEditMedicinePrescriptionActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE_ADD = "Add Medicine Prescription";
    private static final String TOOLBAR_TITLE_EDIT = "Edit Medicine Prescription";

    private MedicinePrescription receivedMedicinePrescription = null;

    private Spinner spinnerMedicine;
    private TextInputEditText editTextMedicinePrescriptionQuantityIssued;
    private TextInputEditText editTextMedicinePrescriptionDoseQuantity;
    private TextInputEditText editTextMedicinePrescriptionDoseFrequency;
    private TextView textViewMedicinePrescriptionIssueDate;
    private TextView textViewMedicinePrescriptionExpiryDate;
    private Switch switchMedicinePrescriptionReminder;
    private TextInputEditText editTextMedicinePrescriptionReminderThreshold;
    private Button buttonAddEditMedicinePrescription;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
    private DatePickerDialog datePickerDialogMedicinePrescriptionIssueDate;
    private DatePickerDialog datePickerDialogMedicinePrescriptionExpiryDate;
    private final Calendar now = Calendar.getInstance();
    private static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_medicine_prescription);

        initHandles();
        initSpinnerMedicine();
        initDatePickerDialogs();
        initDatePickerTextViews();
        unpackIntent();
    }

    private void initHandles() {
        spinnerMedicine = (Spinner) findViewById(R.id.spinnerMedicine);
        editTextMedicinePrescriptionQuantityIssued = (TextInputEditText) findViewById(R.id.editTextMedicinePrescriptionQuantityIssued);
        editTextMedicinePrescriptionDoseQuantity = (TextInputEditText) findViewById(R.id.editTextMedicinePrescriptionDoseQuantity);
        editTextMedicinePrescriptionDoseFrequency = (TextInputEditText) findViewById(R.id.editTextMedicinePrescriptionDoseFrequency);
        textViewMedicinePrescriptionIssueDate = (TextView) findViewById(R.id.textViewMedicinePrescriptionIssueDate);
        textViewMedicinePrescriptionExpiryDate = (TextView) findViewById(R.id.textViewMedicinePrescriptionExpiryDate);
        switchMedicinePrescriptionReminder = (Switch) findViewById(R.id.switchMedicinePrescriptionReminder);
        editTextMedicinePrescriptionReminderThreshold = (TextInputEditText) findViewById(R.id.editTextMedicinePrescriptionReminderThreshold);
        buttonAddEditMedicinePrescription = (Button) findViewById(R.id.buttonAddEditMedicinePrescription);
    }

    private void initSpinnerMedicine() {
        spinnerMedicine.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, MedicineDao.getAll()));
    }

    private void initDatePickerDialogs() {
        datePickerDialogMedicinePrescriptionIssueDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);

                textViewMedicinePrescriptionIssueDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        datePickerDialogMedicinePrescriptionExpiryDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);

                textViewMedicinePrescriptionExpiryDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
    }

    private void initDatePickerTextViews() {
        textViewMedicinePrescriptionIssueDate.setText(dateFormatter.format(now.getTime()));
        textViewMedicinePrescriptionExpiryDate.setText(dateFormatter.format(now.getTime()));

        textViewMedicinePrescriptionIssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogMedicinePrescriptionIssueDate.show();
            }
        });

        textViewMedicinePrescriptionExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogMedicinePrescriptionExpiryDate.show();
            }
        });
    }

    private void initAddButton() {
        buttonAddEditMedicinePrescription.setText("Add");

        buttonAddEditMedicinePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptAdd();
                } catch (MedipalException e) {
                    e.printStackTrace();
                }
            }

            private void attemptAdd() throws MedipalException {
                boolean isFormComplete = false;

                try {
                    isFormComplete = validateForm();
                } catch (Exception e) {
                    Toast.makeText(AddEditMedicinePrescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (isFormComplete) {
                    try {
                        getFormDataAndSaveMedicalRecord();
                    } catch (MedipalException me) {
                        throw me;
                    } catch (SQLException se) {
                        throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
                    } catch (Exception e) {
                        throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
                    }
                    Toast.makeText(AddEditMedicinePrescriptionActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("isRecordSaved", true);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private void initEditButton() {
        buttonAddEditMedicinePrescription.setText("Save");

        buttonAddEditMedicinePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptEdit();
                } catch (MedipalException e) {
                    e.printStackTrace();
                }
            }

            private void attemptEdit() throws MedipalException {
                boolean isFormComplete = false;

                try {
                    isFormComplete = validateForm();
                } catch (Exception e) {
                    Toast.makeText(AddEditMedicinePrescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (isFormComplete) {
                    try {
                        getFormDataAndSaveMedicalRecord();
                    } catch (MedipalException me) {
                        throw me;
                    } catch (SQLException se) {
                        throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
                    } catch (Exception e) {
                        throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
                    }
                    Toast.makeText(AddEditMedicinePrescriptionActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("isRecordSaved", true);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private void unpackIntent() {
        Intent intent = getIntent();

        mode = intent.getIntExtra(MedicinePrescriptionsFragment.EXTRA_MEDICINE_PRESCRIPTION_MODE, 0);

        switch (mode) {
            case MedicinePrescriptionsFragment.REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION:
                initToolbar(TOOLBAR_TITLE_ADD, true);
                initAddButton();
                break;
            case MedicinePrescriptionsFragment.REQUEST_CODE_EDIT_MEDICINE_PRESCRIPTION:
                initToolbar(TOOLBAR_TITLE_EDIT, true);
                initEditButton();

                receivedMedicinePrescription = (MedicinePrescription) intent.getSerializableExtra(MedicinePrescriptionsFragment.EXTRA_MEDICINE_PRESCRIPTION);

                for (int i = 0; i < spinnerMedicine.getCount(); i++) {
                    if (spinnerMedicine.getItemAtPosition(i).toString().equalsIgnoreCase(receivedMedicinePrescription.getMedicine().getName())) {
                        spinnerMedicine.setSelection(i);
                    }
                }

                editTextMedicinePrescriptionQuantityIssued.setText(String.valueOf(receivedMedicinePrescription.getQuantityIssued()));
                editTextMedicinePrescriptionDoseQuantity.setText(String.valueOf(receivedMedicinePrescription.getDoseQuantity()));
                editTextMedicinePrescriptionDoseFrequency.setText(String.valueOf(receivedMedicinePrescription.getDoseFrequency()));
                textViewMedicinePrescriptionIssueDate.setText(DateUtils.toFriendlyDateString(receivedMedicinePrescription.getIssueDate()));
                textViewMedicinePrescriptionExpiryDate.setText(DateUtils.toFriendlyDateString(receivedMedicinePrescription.getExpiryDate()));
                switchMedicinePrescriptionReminder.setChecked(receivedMedicinePrescription.isReminderOn());
                editTextMedicinePrescriptionReminderThreshold.setText(String.valueOf(receivedMedicinePrescription.getThresholdQuantity()));

                Calendar issueDate = DateUtils.toCalendar(receivedMedicinePrescription.getIssueDate());
                datePickerDialogMedicinePrescriptionIssueDate.updateDate(issueDate.get(Calendar.YEAR), issueDate.get(Calendar.MONTH), issueDate.get(Calendar.DATE));

                Calendar expiryDate = DateUtils.toCalendar(receivedMedicinePrescription.getExpiryDate());
                datePickerDialogMedicinePrescriptionExpiryDate.updateDate(expiryDate.get(Calendar.YEAR), expiryDate.get(Calendar.MONTH), expiryDate.get(Calendar.DATE));

                break;
        }
    }

    private boolean validateForm() throws Exception {
        if (TextUtils.isEmpty(editTextMedicinePrescriptionQuantityIssued.getText())) {
            throw new Exception("Please enter a quantity issued.");
        }
        if (!NumberUtils.isStringAPositiveNonZeroInteger(editTextMedicinePrescriptionQuantityIssued.getText().toString())) {
            throw new Exception("Please enter a non-zero number for quantity issued.");
        }

        if (TextUtils.isEmpty(editTextMedicinePrescriptionDoseQuantity.getText())) {
            throw new Exception("Please enter dose quantity.");
        }
        if (!NumberUtils.isStringAPositiveNonZeroInteger(editTextMedicinePrescriptionDoseQuantity.getText().toString())) {
            throw new Exception("Please enter a non-zero number for dose quantity.");
        }
        int quantityIssued = Integer.parseInt(editTextMedicinePrescriptionQuantityIssued.getText().toString());
        int doseQuantity = Integer.parseInt(editTextMedicinePrescriptionDoseQuantity.getText().toString());
        if (doseQuantity > quantityIssued) {
            throw new Exception("Dose quantity cannot exceed quantity issued.");
        }

        if (TextUtils.isEmpty(editTextMedicinePrescriptionDoseFrequency.getText())) {
            throw new Exception("Please enter dose frequency.");
        }
        if (!NumberUtils.isStringAPositiveNonZeroInteger(editTextMedicinePrescriptionDoseFrequency.getText().toString())) {
            throw new Exception("Please enter a non-zero number for dose frequency.");
        }

        if (TextUtils.isEmpty(editTextMedicinePrescriptionReminderThreshold.getText())) {
            throw new Exception("Please enter reminder threshold.");
        }
        if (!NumberUtils.isStringAPositiveNonZeroInteger(editTextMedicinePrescriptionReminderThreshold.getText().toString())) {
            throw new Exception("Please enter a non-zero number for reminder threshold.");
        }

        return true;
    }

    private void getFormDataAndSaveMedicalRecord() throws MedipalException {
        Medicine medicine = (Medicine) spinnerMedicine.getSelectedItem();

        int quantityIssued = Integer.parseInt(editTextMedicinePrescriptionQuantityIssued.getText().toString());
        int doseQuantity = Integer.parseInt(editTextMedicinePrescriptionDoseQuantity.getText().toString());
        int doseFrequency = Integer.parseInt(editTextMedicinePrescriptionDoseFrequency.getText().toString());

        Date issueDate = null;
        Date expiryDate = null;

        try {
            issueDate = DateUtils.fromFriendlyDateString(textViewMedicinePrescriptionIssueDate.getText().toString());
            expiryDate = DateUtils.fromFriendlyDateString(textViewMedicinePrescriptionExpiryDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean isReminderOn = switchMedicinePrescriptionReminder.isChecked();
        int reminderThreshold = Integer.parseInt(editTextMedicinePrescriptionReminderThreshold.getText().toString());

        MedicinePrescription newMedicinePrescription = new MedicinePrescription(
                null,
                medicine,
                quantityIssued,
                doseQuantity,
                doseFrequency,
                issueDate,
                expiryDate,
                isReminderOn,
                reminderThreshold
        );

        switch (mode) {
            case MedicinePrescriptionsFragment.REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION:
                try {
                    MedicinePrescriptionService medicinePrescriptionService = new MedicinePrescriptionService(this);
                    medicinePrescriptionService.createMedicinePrescription(newMedicinePrescription);
                } catch (MedipalException me) {
                    throw me;
                } catch (SQLException se) {
                    throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
                } catch (Exception e) {
                    throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
                }
                break;
            case MedicinePrescriptionsFragment.REQUEST_CODE_EDIT_MEDICINE_PRESCRIPTION:
                newMedicinePrescription.setId(receivedMedicinePrescription.getId());
                MedicinePrescriptionService.update(newMedicinePrescription);
                break;
        }
    }
}
