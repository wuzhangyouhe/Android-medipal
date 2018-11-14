package sg.edu.nus.iss.se8.medipal.activities.medicalrecords;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.MedicalRecordDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;
import sg.edu.nus.iss.se8.medipal.widgets.CustomRadioGroup;

public class AddMedicalRecordActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Add Medical Record";
    private EditText editTextMedicalRecordName;
    private CustomRadioGroup radioGroupMedicalRecordType;
    private TextView textViewMedicalRecordDate;
    private DatePickerDialog datePickerDialog;
    private static final int TOOLBAR_ACTION_SAVE = 0;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        initToolbar(TOOLBAR_TITLE, true);
        initMedicalRecordNameEditText();
        initDatePickerTextView();
        initDatePickerDialog();
        initMedicalConditionTypeRadioGroup();
        initSaveButton();
    }

    private void initMedicalRecordNameEditText() {
        editTextMedicalRecordName = (EditText) findViewById(R.id.editTextMedicalRecordName);
    }

    private void initDatePickerTextView() {
        textViewMedicalRecordDate = (TextView) findViewById(R.id.textViewMedicalRecordDate);

        Calendar calendar = Calendar.getInstance();
        textViewMedicalRecordDate.setText(dateFormatter.format(calendar.getTime()));

        textViewMedicalRecordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private void initDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);

                textViewMedicalRecordDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void initMedicalConditionTypeRadioGroup() {
        radioGroupMedicalRecordType = (CustomRadioGroup) findViewById(R.id.radioGroupMedicalRecordType);

        for (MedicalRecord.Type medicalRecordType : MedicalRecord.Type.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(medicalRecordType.toString());
            radioButton.setTag(medicalRecordType);
            radioGroupMedicalRecordType.addView(radioButton);
        }
    }

    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSaveMedicalRecord);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    attemptSave();
                } catch (MedipalException me) {
                    me.printStackTrace();
                }
            }

            private void attemptSave() throws MedipalException{
                boolean isFormComplete = false;
                try {
                    isFormComplete = validateForm();
                } catch (Exception e) {
                    Toast.makeText(AddMedicalRecordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (isFormComplete) {
                    try {
                        getFormDataAndSaveMedicalRecord();
                    } catch (MedipalException me) {
                        throw me;
                    }catch (SQLException se) {
                        throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
                    } catch (Exception e) {
                        throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
                    }
                    Toast.makeText(AddMedicalRecordActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("isRecordSaved", true);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }

            private boolean validateForm() throws Exception {
                if (TextUtils.isEmpty(editTextMedicalRecordName.getText())) {
                    throw new Exception("No medical record name entered. Please enter a name.");
                }

                if (!radioGroupMedicalRecordType.hasCheckedRadioButton()) {
                    throw new Exception("No type selected. Please select a type.");
                }

                return true;
            }

            private void getFormDataAndSaveMedicalRecord() throws MedipalException{
                editTextMedicalRecordName = (EditText) findViewById(R.id.editTextMedicalRecordName);
                String medicalRecordName = editTextMedicalRecordName.getText().toString();

                RadioButton selectedRadioButton = (RadioButton) radioGroupMedicalRecordType.findViewById(radioGroupMedicalRecordType.getCheckedRadioButtonId());
                MedicalRecord.Type medicalRecordType = (MedicalRecord.Type) selectedRadioButton.getTag();

                Date date = null;
                try {
                    date = dateFormatter.parse(textViewMedicalRecordDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                MedicalRecord medicalRecord = new MedicalRecord(null, medicalRecordName, date, medicalRecordType);

                try {
                    MedicalRecordDao.save(medicalRecord);
                } catch (MedipalException me) {
                    throw me;
                }catch (SQLException se) {
                    throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
                } catch (Exception e) {
                    throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
