package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.reminders.Reminder;

public class ReminderComparator implements Comparator<Reminder> {
    @Override
    public int compare(Reminder reminder1, Reminder reminder2) {
        return reminder1.getWhenToRemind().compareTo(reminder2.getWhenToRemind());
    }
}
