package sg.edu.nus.iss.se8.medipal.activities.emergencycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.activities.MainActivity;
import sg.edu.nus.iss.se8.medipal.dao.EmergencyContactDao;
import sg.edu.nus.iss.se8.medipal.itemtouchhelper.OnStartDragListener;
import sg.edu.nus.iss.se8.medipal.itemtouchhelper.SimpleItemTouchHelperCallback;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;


public class PrioritiseEmergencyContactsActivity extends BaseActivity implements OnStartDragListener {
    private static final String TOOLBAR_TITLE = "Prioritise Emergency Contacts";

    private PrioritiseEmergencyContactsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<EmergencyContact> emergencyContacts;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prioritise_emergency_contacts);

        initToolbar(TOOLBAR_TITLE, false);
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_prioritise_emergency_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent returnToFragment = new Intent(PrioritiseEmergencyContactsActivity.this, MainActivity.class);
        returnToFragment.putExtra("viewpager_position", 4);
        startActivity(returnToFragment);

        return super.onOptionsItemSelected(menuItem);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewEmergencyContacts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        populateViews();
    }

    public void populateViews() {
        emergencyContacts = new ArrayList<>();

        List<EmergencyContact> list = EmergencyContactDao.getAll();
        for (EmergencyContact ec : list) {
            emergencyContacts.add(ec);
        }

        initAdapter();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void initAdapter() {
        adapter = new PrioritiseEmergencyContactsAdapter(emergencyContacts, new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
