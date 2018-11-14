package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.MedicalRecord;

public class MedicalRecordDao {

/*    public MedicalRecordDao(Context context) {
        super(context);
    }*/

    public static MedicalRecord save(MedicalRecord medicalRecord) throws MedipalException{
        if (medicalRecord.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            medicalRecord.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("name", medicalRecord.getNameOfAilment());
            values.put("start_date", medicalRecord.getStartDate().getTime());
            values.put("type", medicalRecord.getType().name());
            long id = DBDAO.getDatabase().insert("medical_records", null, values);
            medicalRecord.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return medicalRecord;
    }

    public static MedicalRecord get(Integer id) throws Exception {
        String sql = "SELECT id, name, start_date, type FROM medical_records WHERE id = " + id;
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            return (new MedicalRecord(
                    cursor.getInt(0),
                    cursor.getString(1),
                    new Date(cursor.getLong(2)),
                    MedicalRecord.Type.valueOf(cursor.getString(3))
            ));
        } else {
            throw new Exception("MedicalRecord with ID " + id + " not found.");
        }
    }

    public static List<MedicalRecord> getAll() {
        List<MedicalRecord> list = new ArrayList<MedicalRecord>();
        String sql = "SELECT id, name, start_date, type FROM medical_records";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new MedicalRecord(cursor.getInt(0), cursor.getString(1), new Date(cursor.getLong(2)), MedicalRecord.Type.valueOf(cursor.getString(3))));
        }
        cursor.close();
        return list;
    }

    public static MedicalRecord update(MedicalRecord medicalRecord) {
        ContentValues values = new ContentValues();
        values.put("name", medicalRecord.getNameOfAilment());
        values.put("start_date", medicalRecord.getStartDate().getTime());
        values.put("type", medicalRecord.getType().name());
        int count = DBDAO.getDatabase().update("medical_records", values, "id = ?", new String[]{medicalRecord.getId() + ""});
        return medicalRecord;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("medical_records", "id = ?", new String[]{id + ""});
    }
}
