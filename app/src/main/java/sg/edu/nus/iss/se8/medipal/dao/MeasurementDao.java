package sg.edu.nus.iss.se8.medipal.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.iss.se8.medipal.exceptions.MedipalException;
import sg.edu.nus.iss.se8.medipal.models.Measurement;
import sg.edu.nus.iss.se8.medipal.utils.DateUtils;

public class MeasurementDao {


    public static Measurement save(Measurement measurement) throws MedipalException {
        if (measurement.getId() != null) {
            throw new MedipalException("Attempting to save a persistent object, it may create another entry in database. Not allowed.", MedipalException.BAD_INPUT, MedipalException.Level.MINOR, null);
        }
        try {
            ContentValues values = new ContentValues();
            values.put("systolic", measurement.getSystolic());
            values.put("diastolic", measurement.getDiastolic());
            values.put("temperature", measurement.getTemperature());
            values.put("pulse", measurement.getPulse());
            values.put("weight", measurement.getWeight());
            values.put("measurement_date", measurement.getMeasurementDate().getTime());
            values.put("sugar", measurement.getSugar());
            values.put("cholesterol", measurement.getCholesterol());
            long id = DBDAO.getDatabase().insert("measurements", null, values);
            measurement.setId((int) id);
        } catch (SQLException se) {
            throw new MedipalException("", MedipalException.DB_ERROR, MedipalException.Level.SEVERE, se);
        } catch (Exception e) {
            throw new MedipalException("", MedipalException.ERROR, MedipalException.Level.SEVERE, e);
        }
        return measurement;
    }

    public static List<Measurement> getAll() {
        List<Measurement> list = new ArrayList<Measurement>();
        String sql = "SELECT id, systolic, diastolic, temperature, pulse, weight, measurement_date, sugar, cholesterol FROM measurements";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(new Measurement(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getDouble(3), cursor.getInt(4), cursor.getDouble(5), new Date(cursor.getLong(6)), cursor.getDouble(7), cursor.getDouble(8)));
        }
        return list;
    }

    public static List<Measurement> getAll(Date fromDate, Date toDate) {
        toDate = DateUtils.twentyFourHoursAfter(toDate);
        List<Measurement> list = new ArrayList<Measurement>();
        String sql = "SELECT id, systolic, diastolic, temperature, pulse, weight, measurement_date, sugar, cholesterol FROM measurements where measurement_date > ? and measurement_date < ?";
        Cursor cursor = DBDAO.getDatabase().rawQuery(sql, new String[]{fromDate.getTime() + "", toDate.getTime() + ""});
        while (cursor.moveToNext()) {
            list.add(new Measurement(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getDouble(3), cursor.getInt(4), cursor.getDouble(5), new Date(cursor.getLong(6)), cursor.getDouble(7), cursor.getDouble(8)));
        }
        return list;
    }

    public static Measurement update(Measurement measurement) {
        ContentValues values = new ContentValues();
        values.put("systolic", measurement.getSystolic());
        values.put("diastolic", measurement.getDiastolic());
        values.put("temperature", measurement.getTemperature());
        values.put("pulse", measurement.getPulse());
        values.put("weight", measurement.getWeight());
        values.put("measurement_date", measurement.getMeasurementDate().getTime());
        values.put("sugar", measurement.getSugar());
        values.put("cholesterol", measurement.getCholesterol());
        int count = DBDAO.getDatabase().update("measurements", values, "id = ?", new String[]{measurement.getId() + ""});
        return measurement;
    }

    public static void delete(Integer id) {
        DBDAO.getDatabase().delete("measurements", "id = ?", new String[]{id + ""});
    }
}
