package sg.edu.nus.iss.se8.medipal.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.adapters.RemindersAdapter;
import sg.edu.nus.iss.se8.medipal.comparators.ReminderComparator;
import sg.edu.nus.iss.se8.medipal.dao.ReminderDao;
import sg.edu.nus.iss.se8.medipal.reminders.Reminder;

import static android.app.Activity.RESULT_OK;

public class RemindersFragment extends Fragment {
    public static final int REQUEST_CODE_EDIT_REMINDER = 1;

    public static final String EXTRA_REMINDER = "REMINDER";

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();
        populateListFromDatabase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_REMINDER) {
            if (resultCode == RESULT_OK) {
                populateListFromDatabase();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            populateListFromDatabase();
        }
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewReminders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RemindersAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void populateListFromDatabase() {
        List<Reminder> reminders = ReminderDao.getAll();

        Collections.sort(reminders, new ReminderComparator());

        ((RemindersAdapter) adapter).setReminders(reminders);
        adapter.notifyDataSetChanged();
    }
}
