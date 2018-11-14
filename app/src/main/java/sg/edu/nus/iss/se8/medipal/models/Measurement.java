package sg.edu.nus.iss.se8.medipal.models;

import java.util.Date;
import java.io.Serializable;

public class Measurement implements Serializable{
    private Integer id;
    private Integer systolic;
    private Integer diastolic;
    private Double temperature;
    private Integer pulse;
    private Double weight;
    private Date measurementDate;
    private Double sugar;
    private Double cholesterol;

    public enum Type {
        BP("systolic, diastolic"), TEMP("temperature"), PULSE("pulse"), WEIGHT("weight"), SUGAR("sugar"), CHOLESTEROL("cholesterol");
        String columns;

        Type(String columns) {
            this.columns = columns;
        }

        public String getColumns() {
            return columns;
        }
    }

    public Measurement() {
    }

    public Measurement(Integer id, Integer systolic, Integer diastolic, Double temperature, Integer pulse, Double weight, Date measurementDate, Double sugar, Double cholesterol) {
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.temperature = temperature;
        this.pulse = pulse;
        this.weight = weight;
        this.measurementDate = measurementDate;
        this.sugar = sugar;
        this.cholesterol = cholesterol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Date getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(Date measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }


}
