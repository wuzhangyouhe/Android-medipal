package sg.edu.nus.iss.se8.medipal.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.medicalrecords.AddMedicalRecordActivity;
import sg.edu.nus.iss.se8.medipal.adapters.MedicalRecordsAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.MedicalRecordComparator;
import sg.edu.nus.iss.se8.medipal.dao.MedicalRecordDao;
import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;

import static android.app.Activity.RESULT_OK;

public class MedicalRecordsFragment extends Fragment {
    private static final int REQUEST_CODE_ADD_MEDICAL_RECORD = 1;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_records, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFab();
        initRecyclerView();
        populateListFromDatabase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_MEDICAL_RECORD) {
            if (resultCode == RESULT_OK) {
                populateListFromDatabase();
            }
        }
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(view.getContext(), AddMedicalRecordActivity.class), REQUEST_CODE_ADD_MEDICAL_RECORD);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewMedicalRecords);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MedicalRecordsAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void populateListFromDatabase() {
        List<MedicalRecord> medicalRecords = MedicalRecordDao.getAll();

        Collections.sort(medicalRecords, new MedicalRecordComparator());

        ((MedicalRecordsAdapter) adapter).setMedicalRecords(medicalRecords);
        adapter.notifyDataSetChanged();
    }
}
