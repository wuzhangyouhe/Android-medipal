package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;

public class CategoryDao {

/*    public CategoryDao(Context context) {
        super(context);
    }*/

    public static Category save(Category category) throws MedipalException {
        if (category.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            category.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("name", category.getName());
            values.put("code", category.getCode());
            values.put("reminder_applicable", category.isReminderApplicable().name());
            values.put("description", category.getDescription());
            long id = DBDAO.getDatabase().insert("categories", null, values);
            category.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return category;
    }

    public static Category get(int id) throws Exception {
        String sql = "SELECT id, name, code, reminder_applicable, description FROM categories WHERE id = " + id;
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            return new Category(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Category.ReminderApplicableOption.valueOf(cursor.getString(3)),
                    cursor.getString(4)
            );
        } else {
            throw new Exception("Category with ID " + id + " not found.");
        }
    }

    public static List<Category> getAll() {
        List<Category> list = new ArrayList<Category>();
        String sql = "SELECT id, name, code, reminder_applicable, description FROM categories";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2), Category.ReminderApplicableOption.valueOf(cursor.getString(3)),cursor.getString(4)));
        }
        return list;
    }

    public static Category update(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("code", category.getCode());
        values.put("reminder_applicable", category.isReminderApplicable().name());
        values.put("description", category.getDescription());
        int count = DBDAO.getDatabase().update("categories", values, "id = ?", new String[]{category.getId() + ""});
        return category;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("categories", "id = ?", new String[]{id + ""});
    }
}
