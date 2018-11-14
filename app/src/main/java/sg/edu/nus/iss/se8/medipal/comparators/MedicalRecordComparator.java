package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;

public class MedicalRecordComparator implements Comparator<MedicalRecord> {
    @Override
    public int compare(MedicalRecord medicalRecord1, MedicalRecord medicalRecord2) {
        return medicalRecord2.getStartDate().compareTo(medicalRecord1.getStartDate());
    }
}
