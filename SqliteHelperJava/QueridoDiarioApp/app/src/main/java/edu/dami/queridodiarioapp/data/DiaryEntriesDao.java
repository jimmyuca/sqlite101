package edu.dami.queridodiarioapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.dami.queridodiarioapp.helpers.DateHelper;
import edu.dami.queridodiarioapp.models.DiaryEntry;

public class DiaryEntriesDao {

    private static final String TAG = DiaryEntriesDao.class.getName();
    private final Context mContext;

    public DiaryEntriesDao(@NonNull Context context) {
        mContext = context;
    }

    public List<DiaryEntry> getAll() {
        //definimos los campos que consultaremos de la tabla en BD.
        String[] projection = {
                DiaryDbContract.Struct._ID,
                DiaryDbContract.Struct.COLUMN_TITLE,
                DiaryDbContract.Struct.COLUMN_DESCRIPTION,
                DiaryDbContract.Struct.COLUMN_CREATED_AT
        };

        //Ordenar por fecha de creacion DESC
        String sortOrder = DiaryDbContract.Struct.COLUMN_CREATED_AT + " DESC";

        //Crea o abre la conexion a BD y nos devuelve una instancia de ella.
        MainDbHelper mDbHelper = MainDbHelper.getInstance(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //De forma simple, un cursor representa cada fila de los resultados generados tras la consulta.
        Cursor rowCursor = db.query(
                DiaryDbContract.Struct.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List<DiaryEntry> entriesFromDb = new ArrayList<>();
        //en un ciclo recorremos cada fila de resultados usando el metodo moveToNext
        //el cual nos mueve a la siguiente fila por cada iteracion realizada.
        while (rowCursor.moveToNext()) {
            DiaryEntry entry = toEntryFromCursor(rowCursor);
            if(entry != null) {
                entriesFromDb.add(entry);
            }
        }

        rowCursor.close();
        return entriesFromDb;
    }

    @Nullable
    private DiaryEntry toEntryFromCursor(@NonNull Cursor cursor) {
        try {
            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DiaryDbContract.Struct._ID)
            );
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryDbContract.Struct.COLUMN_TITLE)
            );
            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryDbContract.Struct.COLUMN_DESCRIPTION)
            );
            long createdAt = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DiaryDbContract.Struct.COLUMN_CREATED_AT)
            );

            return new DiaryEntry(
                    id,
                    name,
                    description,
                    DateHelper.getDateFromMilli(createdAt)
                    );
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, String.format(
                    Locale.getDefault(),
                    "Ocurrió un error al obtener desde BD el registro. Mensaje: %s", ex.getMessage()
            ));
            return null;
        }
    }
}
