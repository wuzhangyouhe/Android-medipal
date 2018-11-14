package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;

public class ConsumptionDao {
    public static Consumption save(Consumption consumption) throws MedipalException {
        if (consumption.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            consumption.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("prescription_id", consumption.getPrescription().getId());
            values.put("quantity", consumption.getQuantity());
            values.put("consumption_date", consumption.getConsumptionDate().getTime());
            long id = DBDAO.getDatabase().insert("consumptions", null, values);
            consumption.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return consumption;
    }

    public static List<Consumption> getAll() {
        List<Consumption> list = new ArrayList<Consumption>();
        String sql = "SELECT id, prescription_id, quantity, consumption_date FROM consumptions ORDER BY consumption_date;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MedicinePrescription mp = MedicinePrescriptionDao.get(cursor.getInt(1));
            list.add(new Consumption(cursor.getInt(0), mp, cursor.getInt(2), new Date(cursor.getLong(3))));
        }
        return list;
    }

    public static Consumption get(Integer id) {
        String sql = "SELECT id, prescription_id, quantity, consumption_date FROM consumptions where id = ? ORDER BY consumption_date;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{id + ""});
        cursor.moveToNext();
        MedicinePrescription mp = MedicinePrescriptionDao.get(cursor.getInt(1));
        return new Consumption(cursor.getInt(0), mp, cursor.getInt(2), new Date(cursor.getLong(3)));
    }

    public static List<Consumption> getAllByPrescription(MedicinePrescription prescription) {
        List<Consumption> list = new ArrayList<Consumption>();
        String sql = "SELECT id, quantity, consumption_date FROM consumptions where prescription_id=? order by consumption_date;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{"" + prescription.getId()});
        while (cursor.moveToNext()) {
            list.add(new Consumption(cursor.getInt(0), prescription, cursor.getInt(1), new Date(cursor.getLong(2))));
        }
        return list;
    }

    public static Consumption update(Consumption consumption) {
        ContentValues values = new ContentValues();
        values.put("prescription_id", consumption.getPrescription().getId());
        values.put("quantity", consumption.getQuantity());
        values.put("consumption_date", consumption.getConsumptionDate().getTime());
        int count = DBDAO.getDatabase().update("consumptions", values, "id = ?", new String[]{consumption.getId() + ""});
        return consumption;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("consumptions", "id = ?", new String[]{id + ""});
    }
}
