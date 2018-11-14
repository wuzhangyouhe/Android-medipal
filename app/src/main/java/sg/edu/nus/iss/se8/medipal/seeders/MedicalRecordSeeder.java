package sg.edu.nus.iss.se8.medipal.seeders;

import android.database.SQLException;

import sg.edu.nus.iss.se8.medipal.dao.MedicalRecordDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class MedicalRecordSeeder {
    private static final String[] AILMENT_NAMES = new String[]{
            "Fever",
            "Flu",
            "Cough",
            "Runny Nose",
            "Eczema"
    };

    private static Faker faker = new Faker();

    public static void run(int numRecords) throws MedipalException{
        for (int i = 0; i < numRecords; i++) {
            MedicalRecord medicalRecord = new MedicalRecord(null,
                    (String) faker.randomElement(AILMENT_NAMES),
                    faker.dateBeforeToday(),
                    (MedicalRecord.Type) faker.randomElement(MedicalRecord.Type.values())
            );

            try {
                MedicalRecordDao.save(medicalRecord);
            } catch (MedipalException me) {
                throw me;
            }catch (SQLException se) {
                throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
            } catch (Exception e) {
                throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
            }
        }
    }
}
