package sg.edu.nus.iss.se8.medipal.activities.consumptionhistory;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.adapters.PrescriptionConsumptionHistoryAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.ConsumptionComparator;
import sg.edu.nus.iss.se8.medipal.dao.ConsumptionDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.services.ReminderService;


public class ConsumptionHistoryActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Consumption History";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Consumption> consumptions;

    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.UK);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
    private final SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM yyyy", Locale.UK);
    private final Calendar now = Calendar.getInstance();
    private Calendar userSelectedDateTime;
    private Calendar userSelectedFilterStartDate;
    private Calendar userSelectedFilterEndDate;

    private MedicinePrescription medicinePrescription;
    private Consumption consumption;
    private TextView textViewConsumptionDate;
    private TextView textViewConsumptionTime;
    private TextView textViewFilterStartDate;
    private TextView textViewFilterEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_history);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            medicinePrescription = (MedicinePrescription) getIntent().getSerializableExtra("MEDICINE_CONSUMPTION");
        }

        initToolbar();
        initFab();
        initRecyclerView();
        populateListFromDatabase();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSaveConsumptionDialog();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TOOLBAR_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            Drawable newIcon = getDrawable(R.drawable.ic_filter);
            toolbar.setOverflowIcon(newIcon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter_consumption_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.action_filter_today:
                filteredViews("TODAY");
                break;
            case R.id.action_filter_this_week:
                filteredViews("THIS_WEEK");
                break;
            case R.id.action_filter_this_month:
                filteredViews("THIS_MONTH");
                break;
            case R.id.action_filter_custom_range:
                displayAlertDialogForCustomRange();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewConsumptionHistory);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PrescriptionConsumptionHistoryAdapter(new PrescriptionConsumptionHistoryAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View v, int position) {
                consumption = ((PrescriptionConsumptionHistoryAdapter) recyclerView.getAdapter()).getItem(position);

                medicinePrescription = consumption.getPrescription();
                initSaveConsumptionDialog();
            }

            @Override
            public boolean layoutOnLongClick(View v, int position) {
                consumption = ((PrescriptionConsumptionHistoryAdapter) recyclerView.getAdapter()).getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setMessage("Are you sure you want to delete this consumption detail?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ConsumptionDao.delete(consumption.getId());
                        adapter.notifyDataSetChanged();
                        populateListFromDatabase();

                        Snackbar.make(recyclerView, "Consumption deleted", Snackbar.LENGTH_LONG)
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

    public void populateListFromDatabase() {
        consumptions = ConsumptionDao.getAllByPrescription(medicinePrescription);

        Collections.sort(consumptions, new ConsumptionComparator());

        ((PrescriptionConsumptionHistoryAdapter) adapter).setConsumptions(consumptions);
        adapter.notifyDataSetChanged();
    }

    public void initSaveConsumptionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_add_consumption_history, null);
        dialogBuilder.setView(dialogView);

        userSelectedDateTime = Calendar.getInstance();

        textViewConsumptionDate = (TextView) dialogView.findViewById(R.id.editTextConsumptionDate);
        textViewConsumptionTime = (TextView) dialogView.findViewById(R.id.editTextConsumptionTime);

        initConsumptionDateTimeTextViews();

        dialogBuilder.setMessage(medicinePrescription.getDoseQuantity() + " " + medicinePrescription.getMedicine().getName() + " pills consumed on");
        dialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    saveConsumptionDetails();
                } catch (MedipalException me) {
                    me.printStackTrace();
                }
                populateListFromDatabase();
            }
        });

        dialogBuilder.setNegativeButton("CANCEL", null);
        AlertDialog builder = dialogBuilder.create();
        builder.show();
    }

    private void initConsumptionDateTimeTextViews() {

        if (consumption != null) {
            textViewConsumptionDate.setText(dateFormatter.format(consumption.getConsumptionDate()));
            textViewConsumptionTime.setText(timeFormatter.format(consumption.getConsumptionDate()));
        } else {
            textViewConsumptionDate.setText(dateFormatter.format(now.getTime()));
            textViewConsumptionTime.setText(timeFormatter.format(now.getTime()));
        }

        textViewConsumptionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConsumptionDatePickerDialog();
            }
        });

        textViewConsumptionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConsumptionTimePickerDialog();
            }
        });
    }

    private void initConsumptionDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userSelectedDateTime.set(year, monthOfYear, dayOfMonth);
                        textViewConsumptionDate.setText(dateFormatter.format(userSelectedDateTime.getTime()));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void initConsumptionTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        userSelectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        userSelectedDateTime.set(Calendar.MINUTE, minute);
                        userSelectedDateTime.set(Calendar.AM_PM, userSelectedDateTime.get(Calendar.AM_PM));
                        textViewConsumptionTime.setText(timeFormatter.format(userSelectedDateTime.getTime()));
                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void saveConsumptionDetails() throws MedipalException {
        if (consumption != null) {
            consumption.setConsumptionDate(userSelectedDateTime.getTime());
            ConsumptionDao.update(consumption);
            consumption = null;
        } else {
            Consumption consumption = new Consumption(null, medicinePrescription, medicinePrescription.getDoseQuantity(), userSelectedDateTime.getTime());
            try {
                Consumption savedConsumption = ConsumptionDao.save(consumption);
                MedicinePrescription medicinePrescription = savedConsumption.getPrescription();

                ReminderService reminderService = new ReminderService(this);
                reminderService.createConsumptionReminderFor(medicinePrescription);
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }
        }
    }

    public void filteredViews(String filterType) {
        consumptions.clear();
        List<Consumption> list = ConsumptionDao.getAllByPrescription(medicinePrescription);
        Calendar thisWeek = Calendar.getInstance();

        for (Consumption consumption : list) {

            switch (filterType) {
                case "TODAY":
                    if (inSameDay(consumption.getConsumptionDate(), now.getTime())) {
                        consumptions.add(consumption);
                    }
                    break;
                case "THIS_WEEK":
                    if (inSameWeek(consumption.getConsumptionDate(), thisWeek.getTime())) {
                        consumptions.add(consumption);
                    }
                    break;
                case "THIS_MONTH":
                    if (monthFormatter.format(consumption.getConsumptionDate()).equals(monthFormatter.format(now.getTime()))) {
                        consumptions.add(consumption);
                    }
                    break;
                case "CUSTOM_RANGE":
                    if (consumption.getConsumptionDate().after(userSelectedFilterStartDate.getTime()) && consumption.getConsumptionDate().before(userSelectedFilterEndDate.getTime())) {
                        consumptions.add(consumption);
                    }
                    break;
            }
        }

        adapter.notifyDataSetChanged();
    }

    private boolean validateFilterRangeDates() {
        return userSelectedFilterStartDate.getTime().before(userSelectedFilterEndDate.getTime()) && userSelectedFilterEndDate.getTime().after(userSelectedFilterStartDate.getTime());
    }

    private void displayAlertDialogForCustomRange() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_filter_date_range_consumption_history, null);
        dialogBuilder.setView(dialogView);

        userSelectedFilterStartDate = Calendar.getInstance();
        userSelectedFilterEndDate = Calendar.getInstance();

        textViewFilterStartDate = (TextView) dialogView.findViewById(R.id.textViewFilterStartDate);
        textViewFilterEndDate = (TextView) dialogView.findViewById(R.id.textViewFilterEndDate);

        initFilterDatesTextViews();

        dialogBuilder.setMessage("Enter a range of dates to filter");
        dialogBuilder.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (validateFilterRangeDates()) {
                    filteredViews("CUSTOM_RANGE");
                } else {
                    Toast.makeText(ConsumptionHistoryActivity.this, "Invalid range", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("CANCEL", null);
        AlertDialog builder = dialogBuilder.create();
        builder.show();
    }

    private void initFilterDatesTextViews() {

        textViewFilterStartDate.setText(dateFormatter.format(now.getTime()));
        textViewFilterEndDate.setText(dateFormatter.format(now.getTime()));

        textViewFilterStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFilterStartDatePickerDialog();
            }
        });

        textViewFilterEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFilterEndDatePickerDialog();
            }
        });
    }

    private void initFilterStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userSelectedFilterStartDate.set(year, monthOfYear, dayOfMonth);
                        textViewFilterStartDate.setText(dateFormatter.format(userSelectedFilterStartDate.getTime()));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void initFilterEndDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userSelectedFilterEndDate.set(year, monthOfYear, dayOfMonth);
                        textViewFilterEndDate.setText(dateFormatter.format(userSelectedFilterEndDate.getTime()));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public boolean inSameDay(Date date1, Date date2) {
        if (dateFormatter.format(date1).equals(dateFormatter.format(date2))) {
            return true;
        } else if (null == date1 || null == date2) {
            return false;
        } else {
            return false;
        }
    }

    public boolean inSameWeek(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return false;
        }

        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (date1.before(date2)) {
            earlier.setTime(date1);
            later.setTime(date2);
        } else {
            earlier.setTime(date2);
            later.setTime(date1);
        }
        if (inSameYear(date1, date2)) {
            int week1 = earlier.get(Calendar.WEEK_OF_YEAR);
            int week2 = later.get(Calendar.WEEK_OF_YEAR);
            if (week1 == week2) {
                return true;
            }
        } else {
            int dayOfWeek = earlier.get(Calendar.DAY_OF_WEEK);
            earlier.add(Calendar.DATE, 7 - dayOfWeek);
            if (inSameYear(earlier.getTime(), later.getTime())) {
                int week1 = earlier.get(Calendar.WEEK_OF_YEAR);
                int week2 = later.get(Calendar.WEEK_OF_YEAR);
                if (week1 == week2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean inSameYear(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int year1 = cal1.get(Calendar.YEAR);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int year2 = cal2.get(Calendar.YEAR);
        return year1 == year2;

    }
}
