package sg.edu.nus.iss.se8.medipal.dao;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.models.Appointment;
import sg.edu.nus.iss.se8.medipal.models.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CategoryDaoTest {

    @Before
    public void setUp() throws Exception {
        DBDAO.init(InstrumentationRegistry.getTargetContext());
    }

    @Test(expected = Exception.class)
    public void insertNewCategoryAndCheckTheDatabaseHasTheRecord() throws Exception {
        //create a new Category instance
        //save the instance - get a handle on the saved instance
        //get the record using the ID from the handle
        //compare the values
        //attempt retrieval of that same record from database, to test retrieval
        //cleanup - delete the record
        //confirm that the deletion happened

        Category category = new Category(null, "Incidental", "INC", Category.ReminderApplicableOption.Y, "For common ailments.");

        // CategoryDao dao = new CategoryDao(InstrumentationRegistry.getTargetContext());
        Category savedCategory = CategoryDao.save(category);

        assertEquals("Incidental", savedCategory.getName());
        assertEquals("INC", savedCategory.getCode());
        assertEquals("For common ailments.", savedCategory.getDescription());

        Category retrievedRecord = CategoryDao.get(savedCategory.getId());

        assertEquals("Incidental", retrievedRecord.getName());
        assertEquals("INC", retrievedRecord.getCode());
        assertEquals("For common ailments.", retrievedRecord.getDescription());

        int retrievedRecordId = retrievedRecord.getId();
        CategoryDao.delete(retrievedRecord.getId());
        retrievedRecord = CategoryDao.get(retrievedRecordId);
        assertNull(retrievedRecord);
    }


    @Test
    public void testCRUD() throws Exception {


        List<Category> start33 = CategoryDao.getAll();
        Category c  = new Category(null, "name", "code", Category.ReminderApplicableOption.Y, "desc");
        CategoryDao.save(c);
        Category c2 = new Category(c.getId(), "newly name", "ABC", Category.ReminderApplicableOption.Y, "desc");
        CategoryDao.update(c2);
        List<Category> end33 = CategoryDao.getAll();

    }

}