package sg.edu.nus.iss.se8.medipal.seeders;

import android.database.SQLException;

import sg.edu.nus.iss.se8.medipal.dao.CategoryDao;
import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;

public class CategorySeeder {
    public static void run() throws MedipalException{
        Category[] categories = new Category[]{
                new Category(null, "Supplement", "SUP", Category.ReminderApplicableOption.OY, "User may opt to set reminder for consumption of supplement."),
                new Category(null, "Chronic", "CHR", Category.ReminderApplicableOption.Y, "This is to categorise medication for long-term / life-time consumption for diseases, i.e. diabetes, hypertension, heart regulation, etc."),
                new Category(null, "Incidental", "INC", Category.ReminderApplicableOption.Y, "For common cold, flu or symptoms happen to be unplanned or subordinate conjunction with something and prescription from general practitioners."),
                new Category(null, "Complete Course", "COM", Category.ReminderApplicableOption.Y, "This may applies to medication like antibiotics for sinus infection, pneumonia, bronchitis, acne, strep throat, cellulitis, etc."),
                new Category(null, "Self Apply", "SEL",Category.ReminderApplicableOption.ON,  "To note down any self-prescribed or consume medication, i.e. applying band aids, balms, etc."),
        };

        for (Category category : categories) {
            try {
                CategoryDao.save(category);
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