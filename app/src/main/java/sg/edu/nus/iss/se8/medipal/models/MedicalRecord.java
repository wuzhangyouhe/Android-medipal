package sg.edu.nus.iss.se8.medipal.models;

import java.util.Date;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class MedicalRecord {
    private Integer id;
    private String nameOfAilment;
    private Date startDate;
    private Type type;



    public enum Type {
        A("Allergy"), C("Condition");
        String description;

        Type(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public MedicalRecord(Integer id, String nameOfAilment, Date startDate, Type type) {
        this.id = id;
        this.nameOfAilment = nameOfAilment;
        this.startDate = startDate;
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getNameOfAilment() {
        return nameOfAilment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", nameOfAilment='" + nameOfAilment + '\'' +
                ", startDate=" + startDate +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicalRecord that = (MedicalRecord) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getNameOfAilment() != null ? !getNameOfAilment().equals(that.getNameOfAilment()) : that.getNameOfAilment() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
            return false;
        return getType() == that.getType();

    }

    public void setNameOfAilment(String nameOfAilment) {
        this.nameOfAilment = nameOfAilment;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public Boolean dataSanityCheck() throws MedipalException {
        if (nameOfAilment == null || nameOfAilment.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Name of ailment is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (startDate == null) {
            throw new MedipalException("Date is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }



}
