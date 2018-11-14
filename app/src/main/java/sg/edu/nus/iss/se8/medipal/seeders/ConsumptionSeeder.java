package sg.edu.nus.iss.se8.medipal.seeders;

import android.database.SQLException;

import sg.edu.nus.iss.se8.medipal.dao.ConsumptionDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;
import sg.edu.nus.iss.se8.medipal.services.MedicinePrescriptionService;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class ConsumptionSeeder {
    private static Faker faker = new Faker();

    public static void run(int numRecords) throws MedipalException {
        for (int i = 0; i < numRecords; i++) {
            MedicinePrescription medicinePrescription = (MedicinePrescription) faker.randomElement(MedicinePrescriptionService.getAll().toArray());

            Consumption consumption = new Consumption(
                    null,
                    medicinePrescription,
                    (int) faker.numberBetween(1, medicinePrescription.getCurrentQuantity()),
                    faker.dateBetween(medicinePrescription.getIssueDate(), medicinePrescription.getExpiryDate())
            );

            try {
                ConsumptionDao.save(consumption);
            } catch (MedipalException me) {
                throw me;
            } catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }
        }
    }

    public static void run(int numRecords, MedicinePrescription medicinePrescription) throws MedipalException {
        for (int i = 0; i < numRecords; i++) {
            Consumption consumption = new Consumption(
                    null,
                    medicinePrescription,
                    (int) faker.numberBetween(1, medicinePrescription.getCurrentQuantity()),
                    faker.dateBetween(medicinePrescription.getIssueDate(), medicinePrescription.getExpiryDate())
            );

            try {
                ConsumptionDao.save(consumption);
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


