package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.models.Measurement;

public class HealthMeasurementComparator implements Comparator<Measurement> {
    @Override
    public int compare(Measurement measurement1, Measurement measurement2) {
        return measurement2.getMeasurementDate().compareTo(measurement1.getMeasurementDate());
    }
}
