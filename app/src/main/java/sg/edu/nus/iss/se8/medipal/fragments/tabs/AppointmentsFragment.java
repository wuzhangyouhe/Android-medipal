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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.appointments.AddAppointmentActivity;
import sg.edu.nus.iss.se8.medipal.activities.appointments.EditAppointmentActivity;
import sg.edu.nus.iss.se8.medipal.adapters.AppointmentsAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.AppointmentComparator;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;
import sg.edu.nus.iss.se8.medipal.services.AppointmentService;
import sg.edu.nus.iss.se8.medipal.services.ReminderService;

public class AppointmentsFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Appointment> appointments;
    public static final String APPOINTMENT = "APPOINTMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFab();
        initRecyclerView();
        populateListFromDatabase();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fabAddRecord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddAppointmentActivity.class));
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewAppointment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AppointmentsAdapter(appointments, new AppointmentsAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View v, int position) {
                Appointment appointment = ((AppointmentsAdapter) recyclerView.getAdapter()).getItem(position);

                Intent goToEditActivity = new Intent(getActivity(), EditAppointmentActivity.class);
                goToEditActivity.putExtra(APPOINTMENT, appointment);
                startActivity(goToEditActivity);
            }

            @Override
            public boolean layoutOnLongClick(View v, int position) {
                final Appointment appointment = ((AppointmentsAdapter) recyclerView.getAdapter()).getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this appointment?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        AppointmentService appointmentService = new AppointmentService(AppointmentsFragment.this.getActivity());
                        appointmentService.deleteAppointment(appointment.getId());

                        for (Appointment appointment1 : AppointmentService.getAll()) {
                            Log.d("deleted appointment", "onClick: " + appointment1);
                        }

                        for (Reminder reminder : ReminderService.getAll()) {
                            Log.d("deleted appointment", "onClick: " + reminder);
                        }

                        adapter.notifyDataSetChanged();
                        populateListFromDatabase();

                        Snackbar.make(recyclerView, "Appointment deleted", Snackbar.LENGTH_LONG)
                                .setAction("DISMISS", null)
                                .show();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();

                return true;
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public void populateListFromDatabase() {
        appointments = AppointmentService.getAll();

        Collections.sort(appointments, new AppointmentComparator());

        ((AppointmentsAdapter) adapter).setAppointments(appointments);

        adapter.notifyDataSetChanged();
    }
}