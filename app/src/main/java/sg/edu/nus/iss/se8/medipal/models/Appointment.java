package sg.edu.nus.iss.se8.medipal.models;

import java.io.Serializable;
import java.util.Date;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class Appointment implements Serializable {
    private Integer id;
    private String location;
    private Date dateAndTime;
    private boolean reminderOn;
    private String description;
    private AppointmentTask appointmentTask;

    public Appointment(Integer id, String location, Date dateAndTime, boolean reminderOn, String description) {
        this.id = id;
        this.location = location;
        this.dateAndTime = dateAndTime;
        this.reminderOn = reminderOn;
        this.description = description;
    }

    public Appointment(Integer id, String location, Date dateAndTime, boolean reminderOn, String description, AppointmentTask appointmentTask) {
        this.id = id;
        this.location = location;
        this.dateAndTime = dateAndTime;
        this.reminderOn = reminderOn;
        this.description = description;
        this.appointmentTask = appointmentTask;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public boolean isReminderOn() {
        return reminderOn;
    }

    public void setReminderOn(boolean reminderOn) {
        this.reminderOn = reminderOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppointmentTask getAppointmentTask() {
        return appointmentTask;
    }

    public void setAppointmentTask(AppointmentTask appointmentTask) {
        this.appointmentTask = appointmentTask;
    }

    public Boolean dataSanityCheck() throws MedipalException {
        if (location == null || location.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Location of appointment is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (dateAndTime == null) {
            throw new MedipalException("Date and time of appointment is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (description == null || description.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Description of appointment is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", reminderOn=" + reminderOn +
                ", description='" + description + '\'' +
                '}';
    }
}
