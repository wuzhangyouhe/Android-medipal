package sg.edu.nus.iss.se8.medipal.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class MedicinePrescription implements Serializable {
    private Integer id;
    private Medicine medicine;
    private Integer quantityIssued;
    private Integer doseQuantity;
    private Integer doseFrequency;
    private Date issueDate;
    private Date expiryDate;
    private Boolean reminderOn;
    private Integer thresholdQuantity;
    private List<Consumption> consumptions;

    public MedicinePrescription(Integer id, Medicine medicine, Integer quantityIssued, Integer doseQuantity, Integer doseFrequency, Date issueDate, Date expiryDate, Boolean reminderOn, Integer thresholdQuantity) {
        this.id = id;
        this.medicine = medicine;
        this.quantityIssued = quantityIssued;
        this.doseQuantity = doseQuantity;
        this.doseFrequency = doseFrequency;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.reminderOn = reminderOn;
        this.thresholdQuantity = thresholdQuantity;
        this.consumptions = new ArrayList<>();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setConsumptions(List<Consumption> consumptions) {
        this.consumptions = consumptions;
    }

    public Integer getId() {
        return id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public Integer getQuantityIssued() {
        return quantityIssued;
    }

    public Integer getDoseQuantity() {
        return doseQuantity;
    }

    public Integer getDoseFrequency() {
        return doseFrequency;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public Boolean isReminderOn() {
        return reminderOn;
    }

    public Integer getThresholdQuantity() {
        return thresholdQuantity;
    }

    private List<Consumption> getConsumptions() {
        return consumptions;
    }

    public Date getDepletionDate() {
        Integer numDoses = quantityIssued / doseQuantity;
        Integer numDays = numDoses / doseFrequency;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, numDays);
        return calendar.getTime();
    }

    public Integer getCurrentQuantity() {
        Integer consumedQuantity = 0;

        for (Consumption consumption : consumptions) {
            consumedQuantity += consumption.getQuantity();
        }

        return getQuantityIssued() - consumedQuantity;
    }

    public Integer getRemainingNumDoses() {
        Integer consumedQuantity = 0;

        for (Consumption consumption : consumptions) {
            consumedQuantity += consumption.getQuantity();
        }

        return (getQuantityIssued() - consumedQuantity) / getDoseFrequency();
    }

    public Date getDateTimeOfNextDose() {
        long interval = (24 * 60 * 60 * 1000) / doseFrequency; //miliseconds before next consumption to be suggested
/*        if (expiryDate.getTime() < new Date().getTime()) {
            return null; //Medicine Expired
        }*/
        Date nextDateTime;
        if (consumptions.size() == 0) {
            nextDateTime = new Date(new Date().getTime() + interval); // Take medicine after interval, Make the second Consumption
        } else {
            nextDateTime = new Date(consumptions.get(consumptions.size() - 1).getConsumptionDate().getTime() + interval);
        }
        /*if (nextDateTime.getTime() < new Date().getTime()) {
            return new Date(); //Take medicine now, scheduled next medicine datetime has already passed
        }*/
        return nextDateTime;
    }

    void addConsumption(Consumption consumption) throws Exception {
        if (getCurrentQuantity() - consumption.getQuantity() < 0)
            throw new Exception("Insufficient medicine prescription quantity.");

        consumptions.add(consumption);
    }

    public Boolean dataSanityCheck() throws MedipalException {
        if (quantityIssued == null) {
            throw new MedipalException("Quantity issued is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (doseQuantity == null) {
            throw new MedipalException("Dosage quantity is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (doseFrequency == null) {
            throw new MedipalException("Dosage frequency is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (issueDate == null) {
            throw new MedipalException("Issued Date is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (expiryDate == null) {
            throw new MedipalException("Expiry Date is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (thresholdQuantity == null) {
            throw new MedipalException("Set threshold quantity is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }


    @Override
    public String toString() {
        return "MedicinePrescription{" +
                "id=" + id +
                ", medicine=" + medicine +
                ", quantityIssued=" + quantityIssued +
                ", doseQuantity=" + doseQuantity +
                ", doseFrequency=" + doseFrequency +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", reminderOn=" + reminderOn +
                ", thresholdQuantity=" + thresholdQuantity +
                ", consumptions=" + consumptions +
                '}';
    }
}
