package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.models.Medicine;

public class MedicineComparator implements Comparator<Medicine> {
    @Override
    public int compare(Medicine medicine1, Medicine medicine2) {
        return medicine1.getName().compareTo(medicine2.getName());
    }
}
