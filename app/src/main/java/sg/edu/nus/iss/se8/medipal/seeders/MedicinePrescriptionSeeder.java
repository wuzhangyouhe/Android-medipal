package sg.edu.nus.iss.se8.medipal.seeders;

import android.content.Context;
import android.database.SQLException;

import java.util.Calendar;

import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class MedicinePrescriptionSeeder {
    private static Faker faker = new Faker();

    public static void run(Context context, int numRecords) throws MedipalException {
        for (int i = 0; i < numRecords; i++) {
            try {
                MedicinePrescriptionService medicinePrescriptionService = new MedicinePrescriptionService(context);
                medicinePrescriptionService.createMedicinePrescription(getMedicinePrescription());
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }
        }
    }

    public static MedicinePrescription getMedicinePrescription() {
        return new MedicinePrescription(
                null,
                (Medicine) faker.randomElement(MedicineDao.getAll().toArray()),
                (int) faker.numberBetween(10, 20),
                (int) faker.numberBetween(1, 2),
                (int) faker.numberBetween(1, 6),
                faker.dateBeforeToday(),
                faker.dateFromToday(Calendar.YEAR, 2),
                faker.randomBoolean(),
                3
        );

    }
}
