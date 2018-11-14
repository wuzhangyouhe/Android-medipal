package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sg.edu.nus.iss.se8.medipal.fragments.tabs.AppointmentsFragment;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.EmergencyContactsFragment;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.MedicalRecordsFragment;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.MedicinePrescriptionsFragment;
import sg.edu.nus.iss.se8.medipal.fragments.tabs.RemindersFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numTabs;

    public PagerAdapter(FragmentManager fragmentManager, int numTabs) {
        super(fragmentManager);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MedicinePrescriptionsFragment();
            case 1:
                return new MedicalRecordsFragment();
            case 2:
                return new RemindersFragment();
            case 3:
                return new AppointmentsFragment();
            case 4:
                return new EmergencyContactsFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numTabs;
    }
}