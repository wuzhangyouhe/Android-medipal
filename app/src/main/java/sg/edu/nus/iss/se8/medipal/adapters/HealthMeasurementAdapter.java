package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.Measurement;

public class HealthMeasurementAdapter extends RecyclerView.Adapter<HealthMeasurementAdapter.ViewHolder> {
    private List<Measurement> measurements;
    private MyAdapterListener onClickListener;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.UK);

    public HealthMeasurementAdapter(List<Measurement> measurements, MyAdapterListener listener) {
        this.measurements = measurements;
        onClickListener = listener;
    }

    @Override
    public HealthMeasurementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_healthmeasurement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Measurement measurement = measurements.get(position);
        viewHolder.textViewDate.setText(dateFormatter.format(measurement.getMeasurementDate()));
        if(measurement.getSystolic() > 0){
        viewHolder.textViewSystolic.setText("Systolic : " +  measurement.getSystolic().toString());
        viewHolder.textViewDiastolic.setText("Diatolic : " + measurement.getDiastolic().toString());

        }
        else{
            viewHolder.textViewSystolic.setText("Systolic : Not taken"  );
            viewHolder.textViewDiastolic.setText("Diatolic : Not taken");

        }
        if(measurement.getPulse() > 0) {
            viewHolder.textViewPulse.setText("Pulse : " + measurement.getPulse());

        }
        else {
            viewHolder.textViewPulse.setText("Pulse : Not taken");

        }
        if(measurement.getTemperature()> 0){
            viewHolder.textViewTemperature.setText("Temperature : " + measurement.getTemperature());

        }
        else {
            viewHolder.textViewTemperature.setText("Temperature : Not taken");
        }
        if(measurement.getWeight()> 0){
            viewHolder.textViewWeight.setText("Weight : " + measurement.getWeight());

        }
        else {
            viewHolder.textViewWeight.setText("Weight : Not taken");

        }
        if(measurement.getSugar()> 0){
            viewHolder.textViewGlucose.setText("Sugar : " + measurement.getSugar());

        }
        else {
            viewHolder.textViewGlucose.setText("Sugar : Not taken");

        }
        if(measurement.getCholesterol()> 0){
            viewHolder.textViewCholestrol.setText("Cholestrol : " + measurement.getCholesterol());

        }
        else {
            viewHolder.textViewCholestrol.setText("Cholestrol : Not taken");
        }
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }

    public Measurement getItem(int position) {
        return measurements.get(position);
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private TextView textViewDate;
        private TextView textViewSystolic;
        private TextView textViewDiastolic;
        private TextView textViewPulse;
        private TextView textViewTemperature;
        private TextView textViewWeight;
        private TextView textViewGlucose;
        private TextView textViewCholestrol;

        private ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.list_item_health_measurement);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            textViewSystolic = (TextView) view.findViewById(R.id.textViewSystolic);
            textViewDiastolic = (TextView) view.findViewById(R.id.textViewDiastolic);
            textViewPulse = (TextView) view.findViewById(R.id.textViewPulse);
            textViewTemperature = (TextView) view.findViewById(R.id.textViewTemperature);
            textViewWeight = (TextView) view.findViewById(R.id.textViewWeight);
            textViewGlucose = (TextView) view.findViewById(R.id.textViewGlucose);
            textViewCholestrol = (TextView) view.findViewById(R.id.textViewCholestrol);

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

