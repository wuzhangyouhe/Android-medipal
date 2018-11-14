package sg.edu.nus.iss.se8.medipal.seeders;

import android.database.SQLException;

import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.dao.MedicineDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.utils.faker.Faker;

public class MedicineSeeder {
    private static Faker faker = new Faker();

    public static void run() throws MedipalException{
        Object[] categories = CategoryDao.getAll().toArray();
        Medicine[] medicines = new Medicine[]{
                new Medicine(null, "Hydrocodone", faker.words(3), (Category) faker.randomElement(categories)),
                new Medicine(null, "Azithromycin", faker.words(3), (Category) faker.randomElement(categories)),
                new Medicine(null, "Lisinopril", faker.words(3), (Category) faker.randomElement(categories)),
                new Medicine(null, "Hydrochlorothiazide", faker.words(3), (Category) faker.randomElement(categories)),
                new Medicine(null, "Amoxicillin", faker.words(3), (Category) faker.randomElement(categories)),
        };

        for (Medicine medicine : medicines) {
            try {
                MedicineDao.save(medicine);
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
