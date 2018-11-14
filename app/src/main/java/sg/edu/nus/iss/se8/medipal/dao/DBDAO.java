package sg.edu.nus.iss.se8.medipal.dao;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBDAO {
    protected static SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;

    public static void init(Context context){
        new DBDAO(context);
    };

    private DBDAO(Context context) {
        this.mContext = context;
        dbHelper = DatabaseHelper.getHelper(mContext);
        open();
    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DatabaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
        Log.d("",database.toString());
    }

    public static SQLiteDatabase getDatabase(){return database;}

    public void close() {
        dbHelper.close();
        database = null;
    }
}
