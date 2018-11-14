package sg.edu.nus.iss.se8.medipal.activities.healthmeasurements;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.dao.MeasurementDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Measurement;
import sg.edu.nus.iss.se8.medipal.utils.RandomUtils;

public class AddEditHealthMeasurementActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Health Measurements";


    private Integer systolic =0;
    private Integer diastolic =0;
    private Double temperature =0.0;
    private Integer pulse= 0;
    private Double weight=  0.0;
    private Date measurementDate;
    private Double sugar =0.0;
    private Double cholesterol = 0.0;

    private TextInputEditText edittextusersystolic;
    private TextInputEditText edittextuserdiastolic;
    private TextInputEditText edittextuserpulse;
    private TextInputEditText edittextusertemperature;
    private TextInputEditText edittextuserweight;
    private TextInputEditText edittextuserglucose;
    private TextInputEditText edittextusercholosteral;
    private Button buttonSaveMeasurements;

    private Measurement measurement;
    Boolean isSave = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_measurement);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            measurement = (Measurement) getIntent().getSerializableExtra("MEASUREMENT");
        }
        initToolbar(TOOLBAR_TITLE, true);
        initUserInterface();
        initButtonSaveMeasurements();
    }

    private void initButtonSaveMeasurements() {
        buttonSaveMeasurements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserMeasurements();
            }
        });
    }

    public void saveUserMeasurements() {
        Log.d("Measurement", "Saving measurement.. ");
        getDataFromUserInterface();
        Boolean bool1 = edittextusersystolic.getText().toString().isEmpty();
        Boolean bool2 = edittextuserdiastolic.getText().toString().isEmpty();
        String tempString1 = edittextusersystolic.getText().toString();
        String tempString2 = edittextusersystolic.getText().toString();

        Boolean blank = true;

        if (edittextusersystolic.getText().toString().isEmpty() &
                edittextuserdiastolic.getText().toString().isEmpty() &
                edittextuserpulse.getText().toString().isEmpty() &
                edittextusertemperature.getText().toString().isEmpty() &
                edittextuserweight.getText().toString().isEmpty() &
                edittextuserglucose.getText().toString().isEmpty() &
                edittextusercholosteral.getText().toString().isEmpty()) {
            blank = false;
        }



        if (!(edittextusersystolic.getText().toString().isEmpty()) & edittextuserdiastolic.getText().toString().isEmpty()) {
            edittextuserdiastolic.setError("Diastolic cannot be blank");
        } else if (!(edittextuserdiastolic.getText().toString().isEmpty()) & edittextusersystolic.getText().toString().isEmpty()) {
            edittextusersystolic.setError("Systolic cannot be blank");
        }
        else if (!blank) {
            edittextusersystolic.setError("Enter at least one health measurement");
        }
        else {
            Log.d("Measurement" ,"Saving measurement to db..") ;
            Measurement measurement = new Measurement();

            if (systolic != null ) measurement.setSystolic(systolic);
            if (diastolic !=null )measurement.setDiastolic((diastolic));
            if (pulse !=null ) measurement.setPulse(pulse);
            if (temperature !=null ) measurement.setTemperature(temperature);
            if (weight !=null ) measurement.setWeight(weight);
            if (sugar !=null ) measurement.setSugar(sugar);
            if (cholesterol !=null )  measurement.setCholesterol(cholesterol);
            measurement.setMeasurementDate(Calendar.getInstance().getTime());
            if (isSave) {
                String message;
                try {
                    message = "";
                    MeasurementDao.save(measurement);
                }
                catch(MedipalException me){
                    message = me.getMessage();

                }
            }else{
                measurement.setId(this.measurement.getId());
                MeasurementDao.update(measurement);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Measurements saved!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    runIntent();
                }
            });
            builder.show();
        }
    }
    public void runIntent() {
        Intent intent = new Intent(AddEditHealthMeasurementActivity.this, HealthMeasurementActivity.class);
        startActivity(intent);
    }

    public void initUserInterface() {
        edittextusersystolic = (TextInputEditText) findViewById(R.id.edittextusersystolic);
        edittextuserdiastolic = (TextInputEditText) findViewById(R.id.edittextuserdiastolic);
        edittextuserpulse = (TextInputEditText) findViewById(R.id.edittextuserpulse);
        edittextusertemperature = (TextInputEditText) findViewById(R.id.edittextusertemperature);
        edittextuserweight = (TextInputEditText) findViewById(R.id.edittextuserweight);
        edittextuserglucose = (TextInputEditText) findViewById(R.id.edittextuserglucose);
        edittextusercholosteral = (TextInputEditText) findViewById(R.id.edittextusercholestrol);
        buttonSaveMeasurements = (Button) findViewById(R.id.buttonSaveMeasurements);
        if (measurement !=null) {
            setDataInUserInterface();
            isSave = false;
        }
    }
    public void setDataInUserInterface() {
        edittextusersystolic.setText(measurement.getSystolic().toString());
        edittextuserdiastolic.setText(measurement.getDiastolic().toString());
        edittextuserpulse.setText(measurement.getPulse().toString());
        edittextusertemperature.setText(measurement.getTemperature().toString());
        edittextuserweight.setText(measurement.getWeight().toString());
        edittextuserglucose.setText(measurement.getSugar().toString());
        edittextusercholosteral.setText(measurement.getCholesterol().toString());
    }

    public void getDataFromUserInterface() {
        Log.d("measurements","Setting the value to measurement model from activity before save");
        if (!edittextusersystolic.getText().toString().isEmpty() ){
            systolic = Integer.valueOf(edittextusersystolic.getText().toString());}
        if (!edittextuserdiastolic.getText().toString().isEmpty() ){
            diastolic = Integer.valueOf(edittextuserdiastolic.getText().toString());}
        if (!edittextusertemperature.getText().toString().isEmpty() ){
            temperature = Double.valueOf(edittextusertemperature.getText().toString());}
        if (!edittextuserpulse.getText().toString().isEmpty() ){
            pulse = Integer.valueOf(edittextuserpulse.getText().toString());}
        if (!edittextuserweight.getText().toString().isEmpty() ){
            weight = Double.valueOf(edittextuserweight.getText().toString());}

        measurementDate = RandomUtils.date();
        if (!edittextuserglucose.getText().toString().isEmpty() ){
            sugar = Double.valueOf(edittextuserglucose.getText().toString());}
        if (!edittextusercholosteral.getText().toString().isEmpty() ){
            cholesterol = Double.valueOf(edittextusercholosteral.getText().toString());}
    }
}
