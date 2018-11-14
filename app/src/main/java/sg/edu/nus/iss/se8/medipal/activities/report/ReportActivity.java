package sg.edu.nus.iss.se8.medipal.activities.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.adapters.ReportAdapter;
import sg.edu.nus.iss.se8.medipal.adapters.PrescriptionConsumptionReportAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.HealthMeasurementComparator;
import sg.edu.nus.iss.se8.medipal.dao.ConsumptionDao;
import sg.edu.nus.iss.se8.medipal.dao.MeasurementDao;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.Measurement;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;

public class ReportActivity extends BaseActivity {

    private DatePickerDialog datePickerDialogReportStartDate;
    private DatePickerDialog datePickerDialogReportEndDate;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
    private Calendar userSelectedStartDate;
    private Calendar userSelectedEndDate;
    private Calendar now;
    private String report;

    private Spinner spinnerReportType;
    private TextView textViewReportStartDate;
    private TextView textViewReportEndDate;

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Measurement> measurements;
    private List<Pair<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Debug", "In Oncreate..");
        setContentView(R.layout.activity_report);
        initToolbar("Medical Reports", true);
        initUserInterface();
    }

    public void initUserInterface() {
        now = Calendar.getInstance();
        userSelectedStartDate = Calendar.getInstance();
        userSelectedStartDate.add(Calendar.DATE, -1);
        userSelectedEndDate = Calendar.getInstance();

        spinnerReportType = (Spinner) findViewById(R.id.spinnerReportType);
        textViewReportStartDate = (TextView) findViewById(R.id.textViewReportStartDate);
        textViewReportEndDate = (TextView) findViewById(R.id.textViewReportEndDate);

        initSpinner();
        initDatePickerTextView();
        initDatePickerDialog();

        initRecyclerView();

        populateListFromDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_generate_medical_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        populateListFromDatabase();

        return super.onOptionsItemSelected(menuItem);
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.report, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerReportType.setAdapter(adapter);
    }

    private void initDatePickerTextView() {
        textViewReportStartDate.setText(dateFormatter.format(userSelectedStartDate.getTime()));
        textViewReportEndDate.setText(dateFormatter.format(userSelectedEndDate.getTime()));

        textViewReportStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogReportStartDate.show();
            }
        });
        textViewReportEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogReportEndDate.show();
            }
        });
    }

    private void initDatePickerDialog() {
        datePickerDialogReportStartDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                userSelectedStartDate.set(year, monthOfYear, dayOfMonth);
                textViewReportStartDate.setText(dateFormatter.format(userSelectedStartDate.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        datePickerDialogReportEndDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                userSelectedEndDate.set(year, monthOfYear, dayOfMonth);
                textViewReportEndDate.setText(dateFormatter.format(userSelectedEndDate.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewReportDetails);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }


    public void getDataFromUserInterface() {
        Log.d("", " In getDataFromUserInterface()");

        report = spinnerReportType.getSelectedItem().toString();
    }

    public void populateListFromDatabase() {

        getDataFromUserInterface();

        switch (report) {
            case "Medicine Consumption": {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        populateListWithMedicineConsumptions();
                    }
                }).run();
                break;
            }
            case "Health Measurement": {
                populateListWithHealthMeasurements();
                break;
            }
        }
    }

    private void populateListWithMedicineConsumptions() {

        try {
            list = MedicinePrescriptionService.getConsumptionChart(userSelectedStartDate.getTime(), userSelectedEndDate.getTime());
        } catch (Exception e) {
            Toast.makeText(ReportActivity.this, "Error fetching details of consumption", Toast.LENGTH_LONG).show();
            list = null;
        }
        adapter = new PrescriptionConsumptionReportAdapter(list);

        recyclerView.setAdapter(adapter);

        ((PrescriptionConsumptionReportAdapter) adapter).setConsumptions(list);

        adapter.notifyDataSetChanged();
    }

    private void populateListWithHealthMeasurements() {
        measurements = new ArrayList<>();

        measurements = MeasurementDao.getAll(userSelectedStartDate.getTime(), userSelectedEndDate.getTime());

        Collections.sort(measurements, new HealthMeasurementComparator());

        adapter = new ReportAdapter(measurements);

        recyclerView.setAdapter(adapter);

        ((ReportAdapter) adapter).setMeasurements(measurements);

        adapter.notifyDataSetChanged();
    }
}
