package sg.edu.nus.iss.se8.medipal.reminders;

import java.io.Serializable;
import java.util.Date;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;

public class Reminder implements Serializable {
    private Integer id;
    private Object referenceObject = null;
    private ReminderReferenceObjectType type;
    private Boolean reminderOn = true;
    private Date whenToRemind = null;
    private Boolean reminded = false;
    private String description = null;

    public Reminder(Integer id, Object referenceObject, ReminderReferenceObjectType type, Boolean reminderOn, Date whenToRemind, Boolean reminded, String description) {
        this.id = id;
        this.referenceObject = referenceObject;
        this.type = type;
        this.reminderOn = reminderOn;
        this.whenToRemind = whenToRemind;
        this.reminded = reminded;
        this.description = description;
    }

    public Integer getReferenceObjectId() {
        return referenceObject == null ? null : (referenceObject instanceof Appointment) ? ((Appointment) referenceObject).getId() : ((MedicinePrescription) referenceObject).getId();
    }

    public void turnOn() {
        if (reminderOn) throw new IllegalStateException("Reminder is already on.");

        reminderOn = true;
    }

    public void turnOff() {
        if (!reminderOn) throw new IllegalStateException("Reminder is already off.");

        reminderOn = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getReferenceObject() {
        return referenceObject;
    }

    public void setReferenceObject(Object referenceObject) {
        this.referenceObject = referenceObject;
    }

    public ReminderReferenceObjectType getType() {
        return type;
    }

    public void setType(ReminderReferenceObjectType type) {
        this.type = type;
    }

    public Boolean isReminderOn() {
        return reminderOn;
    }

    public void setReminderOn(Boolean reminderOn) {
        this.reminderOn = reminderOn;
    }

    public Date getWhenToRemind() {
        return whenToRemind;
    }

    public void setWhenToRemind(Date whenToRemind) {
        this.whenToRemind = whenToRemind;
    }

    public Boolean hasReminded() {
        return reminded;
    }

    public void setReminded(Boolean reminded) {
        this.reminded = reminded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", referenceObject=" + referenceObject +
                ", reminderOn=" + reminderOn +
                ", whenToRemind=" + whenToRemind +
                ", reminded=" + reminded +
                ", description='" + description + '\'' +
                '}';
    }
}
