package sg.edu.nus.iss.se8.medipal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import sg.edu.nus.iss.se8.medipal.Constants;
import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.biodata.BiodataActivity;
import sg.edu.nus.iss.se8.medipal.activities.emergencycontacts.PrioritiseEmergencyContactsActivity;
import sg.edu.nus.iss.se8.medipal.activities.healthmeasurements.HealthMeasurementActivity;
import sg.edu.nus.iss.se8.medipal.activities.medicine.MedicineActivity;
import sg.edu.nus.iss.se8.medipal.activities.report.HelpActivity;
import sg.edu.nus.iss.se8.medipal.activities.report.ReportActivity;
import sg.edu.nus.iss.se8.medipal.adapters.PagerAdapter;
import sg.edu.nus.iss.se8.medipal.dao.DBDAO;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.helpers.OnboardingHelper;
import sg.edu.nus.iss.se8.medipal.seeders.DatabaseSeeder;
import sg.edu.nus.iss.se8.medipal.services.ReminderService;

public class MainActivity extends AppCompatActivity {
    private static final String TOOLBAR_TITLE = "Medicine Prescriptions";
    private static final String TAG = "main activity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    int viewPagerPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBDAO.init(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewPagerPosition = extras.getInt("viewpager_position");
        }

        initToolbar();
        initTabLayout();
        initPagerView();

        OnboardingHelper onboardingHelper = new OnboardingHelper(this);
        if (onboardingHelper.shouldShowOnboardingWizard()) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        } else {
            try {
                DatabaseSeeder.seedMandatoryTablesIfEmpty();
            } catch (MedipalException e) {
                e.printStackTrace();
            }

            if (Constants.DEBUG_MODE) {
                try {
                    DatabaseSeeder.seedEmptyTablesOnlyIfAllTablesAreEmpty(this);
                    Toast.makeText(this, "Seeding sample data for Mr Darryl...", Toast.LENGTH_SHORT).show();
                } catch (MedipalException e) {
                    e.printStackTrace();
                }
            }
            ReminderService reminderService = new ReminderService(this);
            reminderService.setAlarmsForReminders();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(TOOLBAR_TITLE);
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.mainActivityTabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_medicine_prescriptions_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_medical_records_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_reminders_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_today_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_contacts_white_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void initPagerView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        toolbar.setTitle(TOOLBAR_TITLE);
                        break;
                    case 1:
                        toolbar.setTitle("Medical Records");
                        break;
                    case 2:
                        toolbar.setTitle("Reminders");
                        break;
                    case 3:
                        toolbar.setTitle("Appointments");
                        break;
                    case 4:
                        toolbar.setTitle("Emergency Contacts");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (viewPagerPosition != -1) {
            viewPager.setCurrentItem(viewPagerPosition);
            viewPagerPosition = -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;

        switch (id) {
            case R.id.action_sort:
                intent = new Intent(this, PrioritiseEmergencyContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_medicine:
                intent = new Intent(this, MedicineActivity.class);
                startActivity(intent);
                break;
            case R.id.action_biodata:
                intent = new Intent(this, BiodataActivity.class);
                startActivity(intent);
                break;
            case R.id.action_health_measurement:
                intent = new Intent(this, HealthMeasurementActivity.class);
                startActivity(intent);
                break;
            case R.id.action_report:
                intent = new Intent(this, ReportActivity.class);
                startActivity(intent);
                break;
            case R.id.action_help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
