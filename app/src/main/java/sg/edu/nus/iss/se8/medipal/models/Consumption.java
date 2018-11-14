package sg.edu.nus.iss.se8.medipal.models;

import java.io.Serializable;
import java.util.Date;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class Consumption implements Serializable {
    private Integer id;
    private MedicinePrescription prescription;
    private Integer quantity;
    private Date consumptionDate;

    public Consumption(Integer id, MedicinePrescription prescription, Integer quantity, Date consumptionDate) {
        this.id = id;
        this.prescription = prescription;
        this.quantity = quantity;
        this.consumptionDate = consumptionDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MedicinePrescription getPrescription() {
        return prescription;
    }

    public void setPrescription(MedicinePrescription prescription) {
        this.prescription = prescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(Date consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public Boolean dataSanityCheck() throws MedipalException {
        if (quantity == null) {
            throw new MedipalException("Quantity is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (consumptionDate == null) {
            throw new MedipalException("Date is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Consumption{" +
                "id=" + id +
                ", prescription=" + prescription +
                ", quantity=" + quantity +
                ", consumptionDate=" + consumptionDate +
                '}';
    }
}
