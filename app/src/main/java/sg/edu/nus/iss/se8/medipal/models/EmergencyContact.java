package sg.edu.nus.iss.se8.medipal.models;

import java.io.Serializable;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;

public class EmergencyContact implements Serializable {
    private Integer id;
    private String name;
    private String phoneNumber;
    private Type type;
    private String description;
    private Integer priority;

    public enum Type {
        EN("Emergency Number"), GP("General Practitioner"), NOK("Next of Kin");
        String description;

        Type(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public EmergencyContact(Integer id, String name, String phoneNumber, Type type, String description, Integer priority) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.description = description;
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean dataSanityCheck() throws MedipalException {
        if (phoneNumber == null) {
            throw new MedipalException("Phone number is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        if (name == null || name.trim().equalsIgnoreCase("")) {
            throw new MedipalException("Name is mandatory", MedipalException.BAD_INPUT, MedipalException.Level.MAJOR, null);
        }
        return true;
    }

    @Override
    public String toString() {
        return "EmergencyContact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}
