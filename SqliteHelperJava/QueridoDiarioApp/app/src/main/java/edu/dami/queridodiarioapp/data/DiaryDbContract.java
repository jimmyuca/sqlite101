package edu.dami.queridodiarioapp.data;

import android.provider.BaseColumns;

public class DiaryDbContract {
    //declaramos el constructor como privado para evitar que esta clase sea instanciada
    //ya que su objetivo es simplemente la definición de metadatos de BD.
    private DiaryDbContract() {}

    //implementar BaseColumns permite implementar facilmente un campo por defecto que funciona
    //como llave primaria (_ID)
    public static class Struct implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CREATED_AT = "created_at";
    }

    public static class Queries {
        //Tipos de datos: https://www.sqlite.org/datatype3.html
        public static final String CREATE =
                "CREATE TABLE " + Struct.TABLE_NAME +
                        " (" + Struct._ID + " INTEGER PRIMARY KEY, " +
                        Struct.COLUMN_TITLE + " TEXT, " +
                        Struct.COLUMN_DESCRIPTION + " TEXT, " +
                        Struct.COLUMN_CREATED_AT + " LONG " +
                        " )";

        public static final String DELETE =
                "DROP TABLE IF EXISTS " + Struct.TABLE_NAME;
    }
}
