package sg.edu.nus.iss.se8.medipal.seeders;

import sg.edu.nus.iss.se8.medipal.dao.MeasurementDao;
import sg.edu.nus.iss.se8.medipal.dao.UsersDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Measurement;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class HealthMeasurementSeeder {
    private static Faker faker = new Faker();

    public static void run(int numRecords) {
        for (int i = 0; i < numRecords; i++) {
            Measurement measurement = new Measurement(
                    null,
                    (int) faker.numberBetween(120, 180),
                    (int) faker.numberBetween(90, 120),
                    (double) faker.numberBetween(35, 40),
                    (int) faker.numberBetween(50, 80),
                    (double) faker.numberBetween(60, 90),
                    faker.dateBeforeToday(),
                    (double) faker.numberBetween(1, 3),
                    (double) faker.numberBetween(20, 50)
            );


            String message;
            try {
                message = "";

                MeasurementDao.save(measurement);
            } catch (MedipalException me) {
                message = me.getMessage();
            }
        }
    }
}
