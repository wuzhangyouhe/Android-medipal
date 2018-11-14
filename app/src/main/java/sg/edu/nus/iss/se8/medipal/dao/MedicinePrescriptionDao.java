package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Category;
import sg.edu.nus.iss.se8.medipal.models.Consumption;
import sg.edu.nus.iss.se8.medipal.models.Medicine;
import sg.edu.nus.iss.se8.medipal.models.MedicinePrescription;

public class MedicinePrescriptionDao {
    public static MedicinePrescription save(MedicinePrescription prescription) throws MedipalException {
        if (prescription.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            prescription.dataSanityCheck();
            ContentValues values = new ContentValues();
            values.put("medicine_id", prescription.getMedicine().getId());
            values.put("quantity", prescription.getQuantityIssued());
            values.put("dose", prescription.getDoseQuantity());
            values.put("dose_frequency", prescription.getDoseFrequency());
            values.put("issue_date", prescription.getIssueDate().getTime());
            values.put("expiry_date", prescription.getExpiryDate().getTime());
            values.put("reminder_on", prescription.isReminderOn());
            values.put("threshold_quantity", prescription.getThresholdQuantity());
            long id = DBDAO.getDatabase().insert("prescriptions", null, values);
            prescription.setId((int) id);
        } catch (MedipalException me) {
            throw me;
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return prescription;
    }

    public static MedicinePrescription get(Integer id) {
        String sql = "SELECT p.id, m.id, m.name, m.description, c.id," +
                " c.name, c.code, c.reminder_applicable, c.description, p.quantity, p.dose," +
                " p.dose_frequency, p.issue_date, p.expiry_date, p.reminder_on, p.threshold_quantity" +
                " FROM prescriptions p, medicines m, categories c where p.medicine_id = m.id and m.category_id=c.id and p.id = ?;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{"" + id});
        cursor.moveToFirst();
        MedicinePrescription medicinePrescription = new MedicinePrescription(cursor.getInt(0), new Medicine(cursor.getInt(1), cursor.getString(2), cursor.getString(3), new Category(cursor.getInt(4), cursor.getString(5), cursor.getString(6), Category.ReminderApplicableOption.valueOf(cursor.getString(7)), cursor.getString(8))), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), new Date(cursor.getLong(12)), new Date(cursor.getLong(13)), cursor.getInt(14) == 1, cursor.getInt(15));

        return medicinePrescription;
    }

    public static List<MedicinePrescription> getAll() {
        List<MedicinePrescription> list = new ArrayList<MedicinePrescription>();
        String sql = "SELECT p.id, m.id, m.name, m.description, c.id," +
                " c.name, c.code, c.reminder_applicable, c.description, p.quantity, p.dose," +
                " p.dose_frequency, p.issue_date, p.expiry_date, p.reminder_on, p.threshold_quantity" +
                " FROM prescriptions p, medicines m, categories c where p.medicine_id = m.id and m.category_id=c.id;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new MedicinePrescription(cursor.getInt(0), new Medicine(cursor.getInt(1), cursor.getString(2), cursor.getString(3), new Category(cursor.getInt(4), cursor.getString(5), cursor.getString(6), Category.ReminderApplicableOption.valueOf(cursor.getString(7)), cursor.getString(8))), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), new Date(cursor.getLong(12)), new Date(cursor.getLong(13)), cursor.getInt(14) == 1, cursor.getInt(15)));
        }
        return list;
    }

    public static List<MedicinePrescription> getAll(Integer medicineId) {
        List<MedicinePrescription> list = new ArrayList<MedicinePrescription>();
        String sql = "SELECT p.id, m.id, m.name, m.description, c.id," +
                " c.name, c.code, c.reminder_applicable, c.description, p.quantity, p.dose," +
                " p.dose_frequency, p.issue_date, p.expiry_date, p.reminder_on, p.threshold_quantity" +
                " FROM prescriptions p, medicines m, categories c where p.medicine_id = m.id and m.category_id=c.id and m.id=?;";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{medicineId + ""});
        while (cursor.moveToNext()) {
            list.add(new MedicinePrescription(cursor.getInt(0), new Medicine(cursor.getInt(1), cursor.getString(2), cursor.getString(3), new Category(cursor.getInt(4), cursor.getString(5), cursor.getString(6), Category.ReminderApplicableOption.valueOf(cursor.getString(7)), cursor.getString(8))), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11), new Date(cursor.getLong(12)), new Date(cursor.getLong(13)), cursor.getInt(14) == 1, cursor.getInt(15)));
        }
        return list;
    }

    public static MedicinePrescription update(MedicinePrescription prescription) {
        ContentValues values = new ContentValues();
        values.put("medicine_id", prescription.getMedicine().getId());
        values.put("quantity", prescription.getQuantityIssued());
        values.put("dose", prescription.getDoseQuantity());
        values.put("dose_frequency", prescription.getDoseFrequency());
        values.put("issue_date", prescription.getIssueDate().getTime());
        values.put("expiry_date", prescription.getExpiryDate().getTime());
        values.put("reminder_on", prescription.isReminderOn());
        values.put("threshold_quantity", prescription.getThresholdQuantity());
        int count = DBDAO.getDatabase().update("prescriptions", values, "id = ?", new String[]{prescription.getId() + ""});
        return prescription;
    }

    public static void delete(Integer id) {
        //Delete Consumptions as well
        for (Consumption c : ConsumptionDao.getAllByPrescription(new MedicinePrescription(id, null, null, null, null, null, null, null, null))) {
            ConsumptionDao.delete(c.getId());
        }
        DBDAO.getDatabase().delete("prescriptions", "id = ?", new String[]{id + ""});
    }
}
