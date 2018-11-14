package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.models.Appointment;

public class AppointmentComparator implements Comparator<Appointment> {
    @Override
    public int compare(Appointment appointment1, Appointment appointment2) {
        return appointment1.getDateAndTime().compareTo(appointment2.getDateAndTime());
    }
}
