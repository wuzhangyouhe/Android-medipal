package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;

public class MedicinePrescriptionComparator implements Comparator<MedicinePrescription> {
    @Override
    public int compare(MedicinePrescription medicinePrescription1, MedicinePrescription medicinePrescription2) {
        return medicinePrescription2.getId().compareTo(medicinePrescription1.getId());
    }
}
