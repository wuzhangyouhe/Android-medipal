package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.User;

public class UsersDao {

    public static User save(User user) throws Exception {
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("birth_date", user.getBirthDate().getTime());
        values.put("identity_number", user.getIdentityNumber());
        values.put("address", user.getAddress());
        values.put("postal_code", user.getPostalCode());
        values.put("height", user.getHeight());
        values.put("blood_type", user.getBloodType());
        long id = DBDAO.getDatabase().insert("users", null, values);
        user.setId((int) id);
        return user;
    }

    public static List<User> getAll() {
        List<User> list = new ArrayList<User>();
        String sql = "SELECT id, name, birth_date, identity_number, address, postal_code, height, blood_type FROM users";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new User(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getDouble(6), cursor.getString(7)));
        }
        return list;
    }

    public static User getUser() {
        User user = null;
        String sql = "SELECT id, name, birth_date, identity_number, address, postal_code, height, blood_type FROM users";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            user = new User(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getDouble(6), cursor.getString(7));
        }
        return user;
    }

    public static User update(User user) {
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("birth_date", user.getBirthDate().getTime());
        values.put("identity_number", user.getIdentityNumber());
        values.put("address", user.getAddress());
        values.put("postal_code", user.getPostalCode());
        values.put("height", user.getHeight());
        values.put("blood_type", user.getBloodType());
        int count = DBDAO.getDatabase().update("users", values, "id = ?", new String[]{user.getId() + ""});
        return user;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("users", "id = ?", new String[]{id + ""});
    }
}
