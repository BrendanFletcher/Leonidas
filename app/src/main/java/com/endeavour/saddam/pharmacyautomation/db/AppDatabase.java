package com.endeavour.saddam.pharmacyautomation.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;

@Database(entities = {MedicineDTO.class, SoldDTO.class}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

//    ALTER TABLE TABLENAME
//    ADD TEST1 varchar(100) NULL,
//    TEST2 varchar(100) NULL,
//    TEST3 varchar(100) NULL

    public static final String DATABASE_NAME = "pharmacy-db";
    private static AppDatabase sInstance;

    public abstract MedicineDAO medicineDAO();

//    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
////            database.execSQL("ALTER TABLE " + DATABASE_NAME + " "
////                    + " ADD COLUMN quantity INTEGER");
//            database.execSQL("ALTER TABLE pharmacy-db "
//                    +"ADD COLUMN quantity INTEGER;");
////            database.execSQL("ALTER TABLE pharmacy-db "
////                    +"ADD COLUMN medicine_name string");
//        }
//    };
//    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
////            database.execSQL("ALTER TABLE " + DATABASE_NAME + " "
////                    + " ADD COLUMN quantity INTEGER");
//
//            database.execSQL("ALTER TABLE pharmacy-db "
//                    +"ADD COLUMN medicine_name string;");
//        }
//    };

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return sInstance;
    }
}