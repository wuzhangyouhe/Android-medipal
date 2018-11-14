package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.reminders.ReminderReferenceObjectType;

public class MedicineDao {

    public static Medicine save(Medicine medicine) throws MedipalException {
        if (medicine.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            medicine.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("name", medicine.getName());
            values.put("description", medicine.getDescription());
            values.put("category_id", medicine.getCategory().getId());
            long id = DBDAO.getDatabase().insert("medicines", null, values);
            medicine.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return medicine;
    }

    public static List<Medicine> getAll() {
        List<Medicine> list = new ArrayList<Medicine>();
        String sql = "SELECT m.id, m.name, m.description, c.id, c.name, c.code, c.reminder_applicable, c.description  FROM medicines m, categories c where m.category_id=c.id;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new Medicine(cursor.getInt(0), cursor.getString(1), cursor.getString(2), new Category(cursor.getInt(3), cursor.getString(4), cursor.getString(5), Category.ReminderApplicableOption.valueOf(cursor.getString(6)), cursor.getString(7))));
        }
        return list;
    }

    public static Medicine update(Medicine medicine) {
        ContentValues values = new ContentValues();
        values.put("name", medicine.getName());
        values.put("description", medicine.getDescription());
        values.put("category_id", medicine.getCategory().getId());
        int count = DBDAO.getDatabase().update("medicines", values, "id = ?", new String[]{medicine.getId() + ""});
        return medicine;
    }

    public static void delete(Integer id) throws MedipalException {
        if (MedicinePrescriptionDao.getAll(id).size() != 0) {
            throw new MedipalException("Medicine Prescription exists for this medicine. Please delete them before deleting the medicine.", MedipalException.DEPENDENT, MedipalException.Level.MAJOR, null);
        }
        DBDAO.getDatabase().delete("medicines", "id = ?", new String[]{id + ""});
    }
}
