package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;


public class PrescriptionConsumptionReportAdapter extends RecyclerView.Adapter<PrescriptionConsumptionReportAdapter.ViewHolder> {
    private List<Pair<String, String>> list;

    public PrescriptionConsumptionReportAdapter(List<Pair<String, String>> list) {
        this.list = list;
    }

    @Override
    public PrescriptionConsumptionReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_consumption, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Pair<String, String> pair = list.get(position);

        viewHolder.textViewConsumptionDosage.setText(pair.first);
        viewHolder.textViewConsumptionDateTime.setText(pair.second);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setConsumptions(List<Pair<String, String>> list) {
        this.list = list;
    }

    public Pair<String, String> getItem(int position) {
        return list.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewConsumptionDosage;
        private TextView textViewConsumptionDateTime;

        private ViewHolder(View view) {
            super(view);
            textViewConsumptionDosage = (TextView) view.findViewById(R.id.textViewConsumptionDosage);
            textViewConsumptionDateTime = (TextView) view.findViewById(R.id.textViewConsumptionDateTime);
        }
    }
}
