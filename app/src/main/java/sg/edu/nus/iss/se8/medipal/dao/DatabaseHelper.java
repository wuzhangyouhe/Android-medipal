package sg.edu.nus.iss.se8.medipal.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sg.edu.nus.iss.se8.medipal.seeders.CategorySeeder;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medipal_debug_3";
    private static final int DATABASE_VERSION = 45;

    private static final String[] TABLES = {"users", "emergency_contacts", "appointments", "appointment_tasks", "measurements", "medical_records", "categories", "medicines", "prescriptions", "consumptions", "reminders"};
    private static final String[] CREATE_TABLES = {
            "CREATE TABLE `users` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `birth_date` TEXT, `identity_number` TEXT, `address` TEXT,`postal_code` TEXT,`height` NUMERIC,`blood_type` TEXT )",
            "CREATE TABLE `emergency_contacts` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `phone_number` TEXT, `type` TEXT, `description` TEXT, `priority` INTEGER )",
            "CREATE TABLE `appointments` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `location` TEXT NOT NULL, `date_and_time` INTEGER NOT NULL, `reminder_on` INTEGER, `description` TEXT, `task_date_and_time` INTEGER, `task_description` TEXT  )",
            "CREATE TABLE `measurements` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `systolic` INTEGER, `diastolic` INTEGER, `temperature` NUMERIC, `pulse` INTEGER, `weight` NUMERIC, `measurement_date` INTEGER, `sugar` NUMERIC, `cholesterol` NUMERIC )",
            "CREATE TABLE `medical_records` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `start_date` INTEGER, `type` TEXT NOT NULL )",
            "CREATE TABLE `categories` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `code` TEXT NOT NULL, `reminder_applicable` TEXT, `description` TEXT )",
            "CREATE TABLE `medicines` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `description` TEXT, `category_id` INTEGER NOT NULL, FOREIGN KEY(`category_id`) REFERENCES `id`(`categories`) )",
            "CREATE TABLE `prescriptions` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `medicine_id` INTEGER NOT NULL, `quantity` INTEGER NOT NULL, `dose` INTEGER NOT NULL, `dose_frequency` INTEGER NOT NULL, `issue_date` INTEGER NOT NULL, `expiry_date` INTEGER NOT NULL,`reminder_on` INTEGER, `threshold_quantity` INTEGER NOT NULL, FOREIGN KEY(`medicine_id`) REFERENCES `id`(`medicines`) )",
            "CREATE TABLE `consumptions` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `prescription_id` INTEGER, `quantity` INTEGER, `consumption_date` INTEGER, FOREIGN KEY(`prescription_id`) REFERENCES `id`(`prescriptions`) )",
            "CREATE TABLE `reminders` ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `type` TEXT, `prescription_id` INTEGER, `appointment_id` INTEGER, `reminder_on` INTEGER,`reminder_date_and_time` INTEGER, `reminded` INTEGER, `description` TEXT, FOREIGN KEY(`prescription_id`) REFERENCES `id`(`prescriptions`),FOREIGN KEY(`appointment_id`) REFERENCES `id`(`appointments`) )"
    };

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < CREATE_TABLES.length; i++) {
            db.execSQL(CREATE_TABLES[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS user;");
        for (int i = 0; i < TABLES.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLES[i] + ";");
        }
        onCreate(db);
    }
}
