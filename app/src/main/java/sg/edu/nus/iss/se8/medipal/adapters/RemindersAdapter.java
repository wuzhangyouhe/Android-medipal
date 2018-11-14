package sg.edu.nus.iss.se8.medipal.adapters;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.reminders.EditReminderActivity;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.RemindersFragment;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ViewHolder> {
    private Fragment fragment;
    private List<Reminder> reminders;

    private String reminderType;
    private String reminderTitle;

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.UK);

    public RemindersAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Reminder reminder = reminders.get(holder.getAdapterPosition());

        getReminderTitleAndType(reminder);

        holder.textViewReminderTitle.setText(reminderTitle);
        holder.textViewWhenToRemind.setText(dateFormatter.format(reminder.getWhenToRemind()));
        holder.textViewReminderType.setText(reminderType);

        if (!reminder.isReminderOn()) {
            holder.imageViewReminderOn.setImageResource(R.drawable.ic_alarm_off);
        } else {
            holder.imageViewReminderOn.setImageResource(R.drawable.ic_alarm);
        }

        holder.leftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getActivity(), EditReminderActivity.class);
                intent.putExtra(RemindersFragment.EXTRA_REMINDER, reminder);
                fragment.startActivityForResult(intent, RemindersFragment.REQUEST_CODE_EDIT_REMINDER);
            }
        });
    }

    private void getReminderTitleAndType(Reminder reminder) {
        if(reminder.getReferenceObject() instanceof Appointment) {
            Appointment appointment = (Appointment) reminder.getReferenceObject();
            reminderTitle = String.valueOf(appointment.getLocation());

            if (reminder.getType().name().equals("Appointment")) {
                reminderType = "APPOINTMENT";
            } else {
                reminderType = "PRE TASK";
            }
        } else {
            MedicinePrescription medicinePrescription = ((MedicinePrescription) reminder.getReferenceObject());
            reminderTitle = String.valueOf(medicinePrescription.getMedicine().getName());

            if (reminder.getType().name().equals("MedicinePrescriptionConsumption")) {
                reminderType = "CONSUMPTION";
            } else if (reminder.getType().name().equals("MedicinePrescriptionReplenish")) {
                reminderType = "REPLENISH";
            } else {
                reminderType = "EXPIRY";
            }
        }
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private TextView textViewReminderTitle;
        private TextView textViewWhenToRemind;
        private TextView textViewReminderType;
        private ImageView imageViewReminderOn;

        private ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.leftLayout);
            textViewReminderTitle = (TextView) view.findViewById(R.id.textViewReminderTitle);
            textViewWhenToRemind = (TextView) view.findViewById(R.id.textViewWhenToRemind);
            textViewReminderType = (TextView) view.findViewById(R.id.textViewReminderType);
            imageViewReminderOn = (ImageView) view.findViewById(R.id.imageViewReminderOn);
        }
    }
}