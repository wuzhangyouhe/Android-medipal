package sg.edu.nus.iss.se8.medipal.comparators;

import java.util.Comparator;

import sg.edu.nus.iss.se8.medipal.models.Consumption;

public class ConsumptionComparator implements Comparator<Consumption> {
    @Override
    public int compare(Consumption consumption1, Consumption consumption2) {
        return consumption2.getConsumptionDate().compareTo(consumption1.getConsumptionDate());
    }
}
