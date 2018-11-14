package sg.edu.nus.iss.se8.medipal.activities.medicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.BaseActivity;
import sg.edu.nus.iss.se8.medipal.adapters.MedicineAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.MedicineComparator;
import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Medicine;

public class MedicineActivity extends BaseActivity {
    private static final String TOOLBAR_TITLE = "Medicine";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Medicine> medicines;
    public static final String MEDICINE = "MEDICINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

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
                Intent intent = new Intent(MedicineActivity.this, AddMedicineActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewMedicine);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MedicineAdapter(medicines, new MedicineAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View v, int position) {
                Medicine medicine = ((MedicineAdapter) recyclerView.getAdapter()).getItem(position);

                Intent goToEditActivity = new Intent(MedicineActivity.this, EditMedicineActivity.class);
                goToEditActivity.putExtra("MEDICINE", medicine);
                startActivity(goToEditActivity);
            }

            @Override
            public boolean layoutOnLongClick(View v, int position) {
                final Medicine medicine = ((MedicineAdapter) recyclerView.getAdapter()).getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setMessage("Are you sure you want to delete this Medicine?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            MedicineDao.delete(medicine.getId());
                        } catch (MedipalException me){
                            Snackbar.make(recyclerView, me.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("DISMISS", null)
                                    .show();
                        }

                        adapter.notifyDataSetChanged();
                        populateListFromDatabase();

                        Snackbar.make(recyclerView, "Medicine deleted", Snackbar.LENGTH_LONG)
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
        medicines = new ArrayList<>();

        medicines = MedicineDao.getAll();

        Collections.sort(medicines, new MedicineComparator());

        ((MedicineAdapter) adapter).setMedicines(medicines);

        adapter.notifyDataSetChanged();
    }
}
