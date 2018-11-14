package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

public class EmergencyContactDao {

    public static EmergencyContact save(EmergencyContact emergencyContact) throws MedipalException {
        if (emergencyContact.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            emergencyContact.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("name", emergencyContact.getName());
            values.put("phone_number", emergencyContact.getPhoneNumber());
            values.put("type", emergencyContact.getType().name());
            values.put("description", emergencyContact.getDescription());
            values.put("priority", emergencyContact.getPriority());
            Log.d("EC Save", emergencyContact.getName() + " " + emergencyContact.getPhoneNumber());
            long id = DBDAO.getDatabase().insert("emergency_contacts", null, values);
            emergencyContact.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return emergencyContact;
    }

    public static EmergencyContact update(EmergencyContact emergencyContact) {
        ContentValues values = new ContentValues();
        values.put("name", emergencyContact.getName());
        values.put("phone_number", emergencyContact.getPhoneNumber());
        values.put("type", emergencyContact.getType().name());
        values.put("description", emergencyContact.getDescription());
        values.put("priority", emergencyContact.getPriority());
        Log.d("EC Save", emergencyContact.getName() + " " + emergencyContact.getPhoneNumber());
        int count = DBDAO.getDatabase().update("emergency_contacts", values, "id = ?", new String[]{emergencyContact.getId() + ""});
        return emergencyContact;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("emergency_contacts", "id = ?", new String[]{id + ""});
    }

    public static List<EmergencyContact> getAll() {
        List<EmergencyContact> list = new ArrayList<EmergencyContact>();
        String sql = "SELECT id, name, phone_number, type, description, priority FROM emergency_contacts order by priority";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new EmergencyContact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), EmergencyContact.Type.valueOf(cursor.getString(3)), cursor.getString(4), cursor.getInt(5)));
        }
        return list;
    }

    public static void rePrioritize(List<EmergencyContact> list) {
        for (int i = 1; i <= list.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("priority", i);
            int count = DBDAO.getDatabase().update("emergency_contacts", values, "id = ?", new String[]{list.get(i).getId() + ""});
        }
    }
}
