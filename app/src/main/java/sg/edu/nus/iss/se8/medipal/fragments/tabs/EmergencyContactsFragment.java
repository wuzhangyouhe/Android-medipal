package sg.edu.nus.iss.se8.medipal.fragments.tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.activities.MainActivity;
import sg.edu.nus.iss.se8.medipal.activities.emergencycontacts.AddEmergencyContactActivity;
import sg.edu.nus.iss.se8.medipal.activities.emergencycontacts.EditEmergencyContactActivity;
import sg.edu.nus.iss.se8.medipal.adapters.EmergencyContactsAdapter;
import sg.edu.nus.iss.se8.medipal.dao.EmergencyContactDao;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

public class EmergencyContactsFragment extends Fragment {
    public static final String EMERGENCY_CONTACT = "EMERGENCY_CONTACT";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<EmergencyContact> emergencyContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_emergency_contacts, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.findItem(R.id.action_sort).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
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
                startActivity(new Intent(view.getContext(), AddEmergencyContactActivity.class));
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewEmergencyContacts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EmergencyContactsAdapter(emergencyContacts, new EmergencyContactsAdapter.MyAdapterListener() {
            @Override
            public void layoutOnClick(View v, int position) {
                EmergencyContact emergencyContact = ((EmergencyContactsAdapter) recyclerView.getAdapter()).getItem(position);

                Intent goToEditActivity = new Intent(v.getContext(), EditEmergencyContactActivity.class);
                goToEditActivity.putExtra(EMERGENCY_CONTACT, emergencyContact);
                v.getContext().startActivity(goToEditActivity);
            }

            @Override
            public boolean layoutOnLongClick(View v, int position) {
                final EmergencyContact emergencyContact = ((EmergencyContactsAdapter) recyclerView.getAdapter()).getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this emergency contact?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        EmergencyContactDao.delete(emergencyContact.getId());
                        adapter.notifyDataSetChanged();
                        populateListFromDatabase();

                        Snackbar.make(recyclerView, "Emergency contact deleted", Snackbar.LENGTH_LONG)
                                .setAction("DISMISS", null)
                                .show();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();

                return true;
            }

            @Override
            public void callButtonOnClick(View v, int position) {
                Intent goToDialer = new Intent(Intent.ACTION_DIAL);
                goToDialer.setData(Uri.parse("tel:" + emergencyContacts.get(position).getPhoneNumber()));

                String chooserTitle = "Make a call via";
                Intent appChooser = Intent.createChooser(goToDialer, chooserTitle);

                if (goToDialer.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(appChooser);
                }
            }

            @Override
            public void messageButtonOnClick(View v, int position) {
                Intent goToMessenger = new Intent(Intent.ACTION_SENDTO);
                goToMessenger.setData(Uri.parse("smsto:" + emergencyContacts.get(position).getPhoneNumber()));
                goToMessenger.putExtra("sms_body", emergencyContacts.get(position).getDescription());

                String chooserTitle = "Send a message via";
                Intent appChooser = Intent.createChooser(goToMessenger, chooserTitle);

                if (goToMessenger.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(appChooser);
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public void populateListFromDatabase() {
        emergencyContacts = new ArrayList<>();

        emergencyContacts.addAll(EmergencyContactDao.getAll());

        ((EmergencyContactsAdapter) adapter).setEmergencyContacts(emergencyContacts);

        adapter.notifyDataSetChanged();
    }
}
