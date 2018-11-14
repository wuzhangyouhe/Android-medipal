package sg.edu.nus.iss.se8.medipal.models;

import java.io.Serializable;
import java.util.Date;

public class AppointmentTask implements Serializable {
    private String description;
    private Date dateTime;

    public AppointmentTask(String description, Date dateTime) {
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
