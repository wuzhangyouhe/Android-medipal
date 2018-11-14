package sg.edu.nus.iss.se8.medipal.activities.emergencycontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.activities.MainActivity;
import sg.edu.nus.iss.se8.medipal.dao.EmergencyContactDao;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

public class AddEmergencyContactActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Add Emergency Contact";

    private TextInputEditText editTextContactName;
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editTextDefaultMessage;
    private TextView messageCharCount;
    private Spinner spinnerContactType;
    private Button buttonSaveContactDetails;

    private String contactName;
    private String contactPhoneNumber;
    private String contactType;
    private String defaultMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_emergency_contact);

        initUserInterface();
        initToolbar(TOOLBAR_TITLE, true);
    }

    private void initUserInterface() {

        editTextContactName = (TextInputEditText) findViewById(R.id.editTextContactName);
        editTextPhoneNumber = (TextInputEditText) findViewById(R.id.editTextContactPhoneNumber);
        editTextDefaultMessage = (TextInputEditText) findViewById(R.id.editTextDefaultMessage);
        messageCharCount = (TextView) findViewById(R.id.textViewMessageCharCount);
        spinnerContactType = (Spinner) findViewById(R.id.spinnerContactType);
        buttonSaveContactDetails = (Button) findViewById(R.id.buttonSaveContactDetails);

        initMessageCharCount();
        initSpinnerArrayAdapter();
        initButtonSaveContactDetails();
    }

    private void initMessageCharCount() {
        editTextDefaultMessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                messageCharCount.setText(255 - s.toString().length() + "/255");
            }
        });
    }

    private void initSpinnerArrayAdapter() {
        spinnerContactType.setAdapter(new ArrayAdapter<EmergencyContact.Type>(this, android.R.layout.simple_spinner_dropdown_item, EmergencyContact.Type.values()));
    }

    private void initButtonSaveContactDetails() {
        buttonSaveContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactDetails();
            }
        });
    }

    private void saveContactDetails() {
        getDataFromUserInterface();

        if (validateData()) {

            EmergencyContact emergencyContact = new EmergencyContact(null, contactName, contactPhoneNumber, EmergencyContact.Type.valueOf(contactType), defaultMessage, null);

            String message = "New contact added!";
            try {
                EmergencyContactDao.save(emergencyContact);
            } catch (Exception e) {
                message = e.getMessage();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    runIntent();
                }
            });
            builder.show();
        }
    }

    private boolean validateData() {
        if (contactName.isEmpty()) {
            editTextContactName.setError("Enter a name");
            return false;
        } else if (contactPhoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Enter a phone number");
            return false;
        } else if (!contactPhoneNumber.matches("^(\\d{8})$")) {
            editTextPhoneNumber.setError("Mobile phone numbers should be 8 characters long");
            return false;
        } else if (!contactPhoneNumber.matches("[8-9].*")) {
            editTextPhoneNumber.setError("Mobile phone numbers should start with 8 or 9");
            return false;
        } else if (defaultMessage.isEmpty()) {
            editTextDefaultMessage.setError("Enter a message");
            return false;
        }else {
            return true;
        }
    }

    private void runIntent() {
        Intent returnToFragment = new Intent(AddEmergencyContactActivity.this, MainActivity.class);
        returnToFragment.putExtra("viewpager_position", 4);
        startActivity(returnToFragment);
    }

    private void getDataFromUserInterface() {
        contactName = editTextContactName.getText().toString();
        contactPhoneNumber = editTextPhoneNumber.getText().toString();
        contactType = ((EmergencyContact.Type) spinnerContactType.getSelectedItem()).name();
        defaultMessage = editTextDefaultMessage.getText().toString();
    }
}

