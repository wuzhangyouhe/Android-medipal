package sg.edu.nus.iss.se8.medipal.fragments.tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.consumptionhistory.ConsumptionHistoryActivity;
import sg.edu.nus.iss.se8.medipal.activities.medicineprescriptions.AddEditMedicinePrescriptionActivity;
import sg.edu.nus.iss.se8.medipal.adapters.MedicinePrescriptionsAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.MedicinePrescriptionComparator;
import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;

import static android.app.Activity.RESULT_OK;

public class MedicinePrescriptionsFragment extends Fragment {
    public static final int REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION = 1;
    public static final int REQUEST_CODE_EDIT_MEDICINE_PRESCRIPTION = 2;

    public static final String EXTRA_MEDICINE_PRESCRIPTION_MODE = "MEDICINE_PRESCRIPTION_MODE";
    public static final String EXTRA_MEDICINE_PRESCRIPTION = "MEDICINE_PRESCRIPTION";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<MedicinePrescription> medicinePrescriptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medicine_prescriptions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFab();
        initRecyclerView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION) {
            if (resultCode == RESULT_OK) {
                populateListFromDatabase();
            }
        }
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewMedicinePrescriptions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MedicinePrescriptionsAdapter(medicinePrescriptions, new MedicinePrescriptionsAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View view, int position) {
                MedicinePrescription medicinePrescription = ((MedicinePrescriptionsAdapter) recyclerView.getAdapter()).getItem(position);

                Intent intent = new Intent(view.getContext(), AddEditMedicinePrescriptionActivity.class);
                intent.putExtra(EXTRA_MEDICINE_PRESCRIPTION_MODE, REQUEST_CODE_EDIT_MEDICINE_PRESCRIPTION);
                intent.putExtra(EXTRA_MEDICINE_PRESCRIPTION, medicinePrescription);
                startActivityForResult(intent, REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION);
            }

            @Override
            public boolean layoutOnLongClick(View view, int position) {
                final MedicinePrescription medicinePrescription = ((MedicinePrescriptionsAdapter) recyclerView.getAdapter()).getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this prescription?\n\nAll consumptions for this prescription will also get deleted.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MedicinePrescriptionService medicinePrescriptionService = new MedicinePrescriptionService(MedicinePrescriptionsFragment.this.getActivity());
                        medicinePrescriptionService.deleteMedicinePrescription(medicinePrescription.getId());
                        adapter.notifyDataSetChanged();
                        populateListFromDatabase();

                        Snackbar.make(recyclerView, "Prescription deleted", Snackbar.LENGTH_LONG)
                                .setAction("DISMISS", null)
                                .show();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();

                return true;
            }

            @Override
            public void imageViewOnClick(View v, int position) {

                final MedicinePrescription medicinePrescription = ((MedicinePrescriptionsAdapter) recyclerView.getAdapter()).getItem(position);

                Intent intent = new Intent(v.getContext(), ConsumptionHistoryActivity.class);
                intent.putExtra("MEDICINE_CONSUMPTION", medicinePrescription);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        populateListFromDatabase();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MedicineDao.getAll().size() == 0) {
                    Toast.makeText(MedicinePrescriptionsFragment.this.getContext(), "Please add a medicine first.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), AddEditMedicinePrescriptionActivity.class);
                    intent.putExtra(EXTRA_MEDICINE_PRESCRIPTION_MODE, REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION);
                    startActivityForResult(intent, REQUEST_CODE_ADD_MEDICINE_PRESCRIPTION);
                }
            }
        });
    }

    private void populateListFromDatabase() {
        medicinePrescriptions = new ArrayList<>();

        medicinePrescriptions.addAll(MedicinePrescriptionService.getAll());

        Collections.sort(medicinePrescriptions, new MedicinePrescriptionComparator());

        ((MedicinePrescriptionsAdapter) adapter).setMedicinePrescriptions(medicinePrescriptions);

        adapter.notifyDataSetChanged();
    }
}
