package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class MedicinePrescriptionsAdapter extends RecyclerView.Adapter<MedicinePrescriptionsAdapter.ViewHolder> {
    private ArrayList<MedicinePrescription> medicinePrescriptions;
    public MyAdapterListener onClickListener;

    public MedicinePrescriptionsAdapter(ArrayList<MedicinePrescription> medicinePrescriptions, MyAdapterListener listener) {
        this.medicinePrescriptions = medicinePrescriptions;
        this.onClickListener = listener;
    }

    @Override
    public MedicinePrescriptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_medicine_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MedicinePrescription medicinePrescription = medicinePrescriptions.get(position);

        viewHolder.textViewMedicineName.setText(medicinePrescription.getMedicine().getName());
//        if (medicinePrescription.getRemainingNumDoses() != null) {
//            viewHolder.textViewMedicineDosageRemaining.setText(String.valueOf(medicinePrescription.getRemainingNumDoses()));
////            Log.d("medicinePrescription", "onBindViewHolder: " + medicinePrescription.toString());
////            Log.d("getRemainingNumDoses", "onBindViewHolder: " + medicinePrescription.getRemainingNumDoses());
//        }
        if (medicinePrescription.getDoseQuantity() != null) {
            viewHolder.textViewMedicineDoseQuantity.setText(String.valueOf(medicinePrescription.getDoseQuantity()));
        }
        if (medicinePrescription.getDoseFrequency() != null) {
            viewHolder.textViewMedicineDoseFrequency.setText(String.valueOf(medicinePrescription.getDoseFrequency()));
        }
        viewHolder.textViewMedicineExpiryDate.setText(DateUtils.toFriendlyDateString(medicinePrescription.getExpiryDate()));
        if (medicinePrescription.isReminderOn()) {
            viewHolder.imageViewMedicinePrescriptionReminder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageViewMedicinePrescriptionReminder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return medicinePrescriptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private TextView textViewMedicineName;
        //        private TextView textViewMedicineDosageRemaining;
        private TextView textViewMedicineDoseQuantity;
        private TextView textViewMedicineDoseFrequency;
        private TextView textViewMedicineExpiryDate;
        private ImageView imageViewMedicinePrescriptionReminder;
        private ImageView imageViewMedicineConsumption;

        private ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.leftLayout);
            textViewMedicineName = (TextView) view.findViewById(R.id.medicineName);
//            textViewMedicineDosageRemaining = (TextView) view.findViewById(R.id.medicineDosageRemaining);
            textViewMedicineDoseQuantity = (TextView) view.findViewById(R.id.medicineDoseQuantity);
            textViewMedicineDoseFrequency = (TextView) view.findViewById(R.id.medicineDoseFrequency);
            textViewMedicineExpiryDate = (TextView) view.findViewById(R.id.medicineExpiryDate);
            imageViewMedicinePrescriptionReminder = (ImageView) view.findViewById(R.id.imageViewMedicineReminder);
            imageViewMedicineConsumption = (ImageView) view.findViewById(R.id.imageViewMedicineConsumption);

            leftLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.layoutOnClick(v, getAdapterPosition());
                }
            });

            leftLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.layoutOnLongClick(v, getAdapterPosition());
                    return true;
                }
            });

            imageViewMedicineConsumption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.imageViewOnClick(v, getAdapterPosition());
                }
            });
        }
    }

    public MedicinePrescription getItem(int position) {
        return medicinePrescriptions.get(position);
    }

    public void setMedicinePrescriptions(ArrayList<MedicinePrescription> medicinePrescriptions) {
        this.medicinePrescriptions = medicinePrescriptions;
    }

    public interface MyAdapterListener {
        void layoutOnClick(View v, int position);
        boolean layoutOnLongClick(View v, int position);
        void imageViewOnClick(View v, int position);
    }
}