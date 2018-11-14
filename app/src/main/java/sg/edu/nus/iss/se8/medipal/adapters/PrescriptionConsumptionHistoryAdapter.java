package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.Consumption;


public class PrescriptionConsumptionHistoryAdapter extends RecyclerView.Adapter<PrescriptionConsumptionHistoryAdapter.ViewHolder> {
    private List<Consumption> consumptions;
    private MyAdapterListener onClickListener;

    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.UK);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.UK);

    public PrescriptionConsumptionHistoryAdapter(MyAdapterListener listener) {
        onClickListener = listener;
    }

    @Override
    public PrescriptionConsumptionHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_consumption, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Consumption consumption = consumptions.get(position);

        viewHolder.textViewConsumptionDosage.setText(consumption.getPrescription().getDoseQuantity() + " pills consumed");
        viewHolder.textViewConsumptionDateTime.setText("Consumed on " + dateFormatter.format(consumption.getConsumptionDate()) + " at " +  timeFormatter.format(consumption.getConsumptionDate()));
    }

    @Override
    public int getItemCount() {
        return consumptions.size();
    }

    public void setConsumptions(List<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

    public Consumption getItem(int position) { return consumptions.get(position); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout leftLayout;
        private TextView textViewConsumptionDosage;
        private TextView textViewConsumptionDateTime;

        private ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.leftLayout);
            textViewConsumptionDosage = (TextView) view.findViewById(R.id.textViewConsumptionDosage);
            textViewConsumptionDateTime = (TextView) view.findViewById(R.id.textViewConsumptionDateTime);

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