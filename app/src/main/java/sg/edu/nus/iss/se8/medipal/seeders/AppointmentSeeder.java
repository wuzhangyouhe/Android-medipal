package sg.edu.nus.iss.se8.medipal.seeders;

import android.content.Context;
import android.database.SQLException;

import java.util.Calendar;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.services.AppointmentService;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class AppointmentSeeder {
    private static final String[] POLYCLINIC_NAMES = new String[]{
            "Ang Mo Kio Polyclinic (NHG)",
            "Bedok Polyclinic (Singhealth)",
            "Bukit Batok Polyclinic (NHG)",
            "Bukit Merah Polyclinic (Singhealth)",
            "Choa Chu Kang Polyclinic (NHG)",
            "Clementi Polyclinic (NHG)",
            "Geylang Polyclinic (Singhealth)",
            "Hougang Polyclinic (NHG)",
            "Jurong Polyclinic (NHG)",
            "Marine Parade Polyclinic (Singhealth)",
            "Outram Polyclinic (Singhealth)",
            "Pasir Ris Polyclinic (Singhealth)",
            "Queenstown Polyclinic (Singhealth)",
            "Sengkang Polyclinic (Singhealth)",
            "Tampines Polyclinic (Singhealth)",
            "Toa Payoh Polyclinic (NHG)",
            "Woodlands Polyclinic (NHG)",
            "Yishun Polyclinic (NHG)",
    };

    private static Faker faker = new Faker();

    public static void run(Context context, int numRecords) throws MedipalException {
        for (int i = 0; i < numRecords; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.SECOND, 30 * (i + 1));

            Appointment appointment = new Appointment(null,
                    (String) faker.randomElement(POLYCLINIC_NAMES),
                    calendar.getTime(),
                    true,
                    faker.words(10),
                    null
            );

            try {
                AppointmentService appointmentService = new AppointmentService(context);
                appointmentService.createAppointment(appointment);
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }
        }
    }
}
