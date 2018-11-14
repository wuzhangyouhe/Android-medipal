package sg.edu.nus.iss.se8.medipal.activities.biodata;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.activities.MainActivity;
import sg.edu.nus.iss.se8.medipal.dao.UsersDao;
import sg.edu.nus.iss.se8.medipal.models.User;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class BiodataActivity extends BaseActivity {

    private String name ;
    private Date birthDate;
    private String identityNumber;
    private String address;
    private String postalCode;
    private Double height;
    private String bloodType;
    private String ID;
    private User user;
    private Boolean isNew = true;
    private DatePickerDialog datePickerDialog;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
    private Calendar userSelectedDateTime;
    private Calendar now;

    private TextInputEditText edittextusernric;
    private TextInputEditText edittextusername;
    private TextInputEditText edittextuseraddress;
    private TextInputEditText edittextuserpostalcode;
    private TextInputEditText edittextuserheight;
    private TextInputEditText edittextuserdateofbirth;
    private Spinner spinnerbloodtype;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getBirthdate() {
        return birthDate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthDate = birthdate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Debug","In Oncreate..");
        setContentView(R.layout.activity_biodata);
        initToolbar();
        user = UsersDao.getUser();
        initUserInterface();
        setDataInUserInterface(user);
    }

    public void saveUserDetails(View v) {
        Log.d("User", "saveUserDetails: ");
        if (edittextusernric.getText().toString().isEmpty()) {
            edittextusernric.setError("Enter your NRIC?");
        }  else if (edittextusername.getText().toString().isEmpty()) {
            edittextusername.setError("Enter your Full Name");
        }else if (edittextuseraddress.getText().toString().isEmpty()) {
            edittextuseraddress.setError("Enter your Residential Address");
        }else if (edittextuserpostalcode.getText().toString().isEmpty()) {
            edittextuserpostalcode.setError("Enter your home postal code number");
        }else if (edittextuserheight.getText().toString().isEmpty() ) {
            edittextuserheight.setError("Enter your height");
        } else if (edittextuserdateofbirth.getText().toString().isEmpty() ) {
            edittextuserdateofbirth.setError("Enter your date of birth");
        }
        else{
            getDataFromUserInterface();
            updateUserDetails();
            String message;
            if (isNew) {

                try {
                    UsersDao.save(user);

                } catch (Exception me) {
                    message = me.getMessage();
                }
            }
            else{
                try {
                    UsersDao.update(user);

                } catch (Exception me) {
                    message = me.getMessage();
                }
            }
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

    public void runIntent() {
        Intent returnToFragment = new Intent(this, MainActivity.class);
        returnToFragment.putExtra("viewpager_position", 0);
        startActivity(returnToFragment);
        finish();
    }

    public void setDataInUserInterface(User user) {

        if (user != null) {
            edittextusernric.setText(user.getIdentityNumber());
            edittextusername.setText(user.getName());
            edittextuserdateofbirth.setText(DateUtils.toFriendlyDateString(user.getBirthDate()));
            edittextuseraddress.setText(user.getAddress());
            edittextuserpostalcode.setText(user.getPostalCode());
            for (int i = 0; i < spinnerbloodtype.getCount(); i++) {
                if (spinnerbloodtype.getItemAtPosition(i).equals(user.getBloodType())) {
                    spinnerbloodtype.setSelection(i);
                }
            }
            edittextuserheight.setText(user.getHeight().toString());
            isNew = false;
        }

    }

    public void initUserInterface() {

        now = Calendar.getInstance();
        userSelectedDateTime = now;
        edittextusernric = (TextInputEditText) findViewById(R.id.edittextusernric);
        edittextusername = (TextInputEditText) findViewById(R.id.edittextusername);
        edittextuseraddress = (TextInputEditText) findViewById(R.id.edittextuseraddress);
        edittextuserpostalcode = (TextInputEditText) findViewById(R.id.edittextuserpostalcode);
        edittextuserheight = (TextInputEditText) findViewById(R.id.edittextuserheight);
        spinnerbloodtype = (Spinner)  findViewById(R.id.spinnerbloodtype);
        edittextuserdateofbirth = (TextInputEditText) findViewById(R.id.edittextuserdateofbirth);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodgroup, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerbloodtype.setAdapter(adapter);
        initDatePickerTextView();
        initDatePickerDialog();
    }

    public void getDataFromUserInterface() {
        Log.d(""," In getDataFromUserInterface()");
        if(!edittextusernric.getText().toString().isEmpty()) identityNumber = edittextusernric.getText().toString();
        if(!edittextusername.getText().toString().isEmpty()) name = edittextusername.getText().toString();
        if(!edittextuseraddress.getText().toString().isEmpty()) address = edittextuseraddress.getText().toString();
        if(!edittextuserpostalcode.getText().toString().isEmpty()) postalCode = edittextuserpostalcode.getText().toString();
        if(!edittextuserheight.getText().toString().isEmpty()) height = Double.valueOf(edittextuserheight.getText().toString());
        bloodType = spinnerbloodtype.getSelectedItem().toString();
        if(!edittextuserdateofbirth.getText().toString().isEmpty()) birthDate = new Date(edittextuserdateofbirth.getText().toString());

    }

    public void setDataInUserInterface() {
        edittextusernric.setText(user.getIdentityNumber());
        edittextusername.setText(user.getName());
        edittextuseraddress.setText(user.getAddress());
        edittextuserpostalcode.setText(user.getPostalCode());
        edittextuserheight.setText(user.getHeight().toString());
    }

    public void updateUserDetails() {
        if (user == null) user = new User();
        user.setIdentityNumber(identityNumber);
        user.setName(name);
        user.setAddress(address);
        user.setPostalCode(postalCode);
        user.setHeight((Double.valueOf(height)));
        user.setBloodType(bloodType);
        user.setBirthDate(birthDate);
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Bio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initDatePickerTextView() {
        edittextuserdateofbirth.setOnClickListener(new View.OnClickListener() {
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
                userSelectedDateTime = Calendar.getInstance();
                userSelectedDateTime.set(year, monthOfYear, dayOfMonth);
                edittextuserdateofbirth.setText(dateFormatter.format(userSelectedDateTime.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
    }
}
