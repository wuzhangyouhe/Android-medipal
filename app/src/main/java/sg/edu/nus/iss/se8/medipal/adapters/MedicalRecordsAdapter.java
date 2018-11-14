package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class MedicalRecordsAdapter extends RecyclerView.Adapter<MedicalRecordsAdapter.ViewHolder> {
    private List<MedicalRecord> medicalRecords;

    public MedicalRecordsAdapter() {
        medicalRecords = new ArrayList<>();
    }

    @Override
    public MedicalRecordsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_medical_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MedicalRecord medicalRecord = medicalRecords.get(position);

        viewHolder.textViewMedicalConditionName.setText(medicalRecord.getNameOfAilment());
        viewHolder.textViewMedicalConditionType.setText(medicalRecord.getType().toString());
        viewHolder.textViewMedicalRecordDate.setText("Started on " + DateUtils.toFriendlyDateString(medicalRecord.getStartDate()));
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMedicalConditionName;
        private TextView textViewMedicalConditionType;
        private TextView textViewMedicalRecordDate;

        private ViewHolder(View view) {
            super(view);
            textViewMedicalConditionName = (TextView) view.findViewById(R.id.textViewMedicalConditionName);
            textViewMedicalConditionType = (TextView) view.findViewById(R.id.textViewMedicalConditionType);
            textViewMedicalRecordDate = (TextView) view.findViewById(R.id.textViewMedicalRecordDate);
        }
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
}