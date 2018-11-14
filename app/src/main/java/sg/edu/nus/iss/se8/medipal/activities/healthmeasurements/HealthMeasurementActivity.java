package sg.edu.nus.iss.se8.medipal.activities.healthmeasurements;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Collections;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.adapters.HealthMeasurementAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.HealthMeasurementComparator;
import sg.edu.nus.iss.se8.medipal.dao.MeasurementDao;
import sg.edu.nus.iss.se8.medipal.models.Measurement;

public class HealthMeasurementActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Past Health Measurements";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Measurement> measurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_measurement);

        initToolbar(TOOLBAR_TITLE, true);
        initFab();
        initRecyclerView();
        populateListFromDatabase();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthMeasurementActivity.this, AddEditHealthMeasurementActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewHealthMeasurement);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HealthMeasurementAdapter(measurements, new HealthMeasurementAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View v, int position) {
                Measurement measurement = ((HealthMeasurementAdapter) recyclerView.getAdapter()).getItem(position);

                Intent goToEditActivity = new Intent(HealthMeasurementActivity.this, AddEditHealthMeasurementActivity.class);
                goToEditActivity.putExtra("MEASUREMENT", measurement);
                startActivity(goToEditActivity);
            }

            @Override
            public boolean layoutOnLongClick(View v, int position) {
                final Measurement measurement = ((HealthMeasurementAdapter) recyclerView.getAdapter()).getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setMessage("Are you sure you want to delete this Measurement?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MeasurementDao.delete(measurement.getId());
                        adapter.notifyDataSetChanged();
                        populateListFromDatabase();

                        Snackbar.make(recyclerView, "Measurement deleted", Snackbar.LENGTH_LONG)
                                .setAction("DISMISS", null)
                                .show();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();

                return true;
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void populateListFromDatabase() {
        measurements = MeasurementDao.getAll();

        Collections.sort(measurements, new HealthMeasurementComparator());

        ((HealthMeasurementAdapter) adapter).setMeasurements(measurements);
        adapter.notifyDataSetChanged();
    }
}
