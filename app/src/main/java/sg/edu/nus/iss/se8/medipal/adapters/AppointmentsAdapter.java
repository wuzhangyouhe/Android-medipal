package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {
    private List<Appointment> appointments;
    public MyAdapterListener onClickListener;

    public AppointmentsAdapter(List<Appointment> appointments, MyAdapterListener listener) {
        this.appointments = appointments;
        onClickListener = listener;
    }

    @Override
    public AppointmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Appointment appointment = appointments.get(position);

        viewHolder.textViewLocation.setText(appointment.getLocation());
        viewHolder.textViewDate.setText(DateUtils.toFriendlyDateTimeString(appointment.getDateAndTime()));
        viewHolder.textViewDescription.setText(appointment.getDescription());

        if (appointment.isReminderOn()) {
            viewHolder.imageViewAppointmentReminder.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageViewAppointmentReminder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public Appointment getItem(int position) {
        return appointments.get(position);
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private TextView textViewLocation;
        private TextView textViewDate;
        private TextView textViewDescription;
        private ImageView imageViewAppointmentReminder;


        private ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.leftLayout);
            textViewLocation = (TextView) view.findViewById(R.id.location);
            textViewDate = (TextView) view.findViewById(R.id.date);
            textViewDescription = (TextView) view.findViewById(R.id.description);
            imageViewAppointmentReminder = (ImageView) view.findViewById(R.id.imageViewAppointmentReminder);

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