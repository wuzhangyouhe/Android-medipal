package sg.edu.nus.iss.se8.medipal.reminders;

public enum ReminderReferenceObjectType {
    Appointment("appointment"),
    AppointmentTask("appointment"),
    MedicinePrescriptionConsumption("prescription"),
    MedicinePrescriptionExpiry("prescription"),
    MedicinePrescriptionReplenish("prescription");

    String referenceTable;

    ReminderReferenceObjectType(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getReferenceTable() {
        return referenceTable;
    }
}
