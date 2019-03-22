package de.hsrm.lback.myapplication.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import de.hsrm.lback.myapplication.models.Location;

@Database(entities = {Location.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{
    public abstract LocationDao locationDao();
    private static volatile AppDatabase INSTANCE;

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
                database.execSQL(
                        "CREATE TABLE location_backup AS SELECT uid, name, logo, apiId, displayName FROM location;" +
                                "DROP TABLE location;" +
                                "ALTER TABLE location_backup RENAME TO location;"
                );


        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
