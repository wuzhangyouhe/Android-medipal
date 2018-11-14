package sg.edu.nus.iss.se8.medipal.seeders;

import android.content.Context;
import android.database.SQLException;

import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.dao.ConsumptionDao;
import sg.edu.nus.iss.se8.medipal.dao.EmergencyContactDao;
import sg.edu.nus.iss.se8.medipal.dao.MeasurementDao;
import sg.edu.nus.iss.se8.medipal.dao.MedicalRecordDao;
import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.services.AppointmentService;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;

public class DatabaseSeeder {
    public static void seedMandatoryTablesIfEmpty() throws MedipalException {
        if (CategoryDao.getAll().size() == 0) {
            CategorySeeder.run();
        }
    }

    public static void seedEmptyTablesOnlyIfAllTablesAreEmpty(Context context) throws MedipalException {
        if (areAllTablesEmpty()) {
            try {
                AppointmentSeeder.run(context, 2);
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }

            MedicalRecordSeeder.run(3);
            EmergencyContactSeeder.run(1);
            MedicineSeeder.run();
            MedicinePrescriptionSeeder.run(context, 2);
            ConsumptionSeeder.run(20);
            HealthMeasurementSeeder.run(10);
        }
    }

    private static boolean areAllTablesEmpty() {
        boolean allTablesEmpty = true;

        if (MedicalRecordDao.getAll().size() > 0
                || EmergencyContactDao.getAll().size() > 0
                || AppointmentService.getAll().size() > 0
                || MedicineDao.getAll().size() > 0
                || MedicinePrescriptionService.getAll().size() > 0
                || ConsumptionDao.getAll().size() > 0
                || MeasurementDao.getAll().size() > 0) {
            allTablesEmpty = false;
        }

        return allTablesEmpty;
    }
}
