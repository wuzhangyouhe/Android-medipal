package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.Medicine;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<Medicine> medicines;
    private MyAdapterListener onClickListener;

    public MedicineAdapter(List<Medicine> medicines, MyAdapterListener listener) {
        this.medicines = medicines;
        onClickListener = listener;
    }

    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Medicine medicine = medicines.get(position);

        viewHolder.textViewMedicineName.setText(medicine.getName().toString());
        viewHolder.textViewMedicineDescription.setText(medicine.getDescription());
        viewHolder.textViewCatCode.setText(medicine.getCategory().toString());
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public Medicine getItem(int position) {
        return medicines.get(position);
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private TextView textViewMedicineName;
        private TextView textViewMedicineDescription;
        private TextView textViewCatCode;

        private ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.leftlayout);
            textViewMedicineName = (TextView) view.findViewById(R.id.textViewMedicineName);
            textViewMedicineDescription = (TextView) view.findViewById(R.id.textViewMedicineDescription);
            textViewCatCode = (TextView) view.findViewById(R.id.textViewCatCode);

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
        }
    }

    public interface MyAdapterListener {

        void layoutOnClick(View v, int position);

        boolean layoutOnLongClick(View v, int position);
    }
}

