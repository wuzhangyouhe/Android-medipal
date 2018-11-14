package sg.edu.nus.iss.se8.medipal.seeders;

import sg.edu.nus.iss.se8.medipal.dao.EmergencyContactDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class EmergencyContactSeeder {
    private static Faker faker = new Faker();

    public static void run(int numRecords) {
        for (int i = 0; i < numRecords; i++) {
            try {
                EmergencyContactDao.save(getEmergencyContact());
            } catch (MedipalException e) {
                e.printStackTrace();
            }
        }
    }

    public static EmergencyContact getEmergencyContact() {
        return new EmergencyContact(null,
                faker.name(),
                faker.phoneNumber(),
                (EmergencyContact.Type) faker.randomElement(EmergencyContact.Type.values()),
                faker.words(50),
                (int) faker.numberBetween(1, 10)
        );
    }
}
