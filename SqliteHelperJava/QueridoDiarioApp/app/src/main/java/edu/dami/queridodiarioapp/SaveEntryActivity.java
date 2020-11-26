package edu.dami.queridodiarioapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import edu.dami.queridodiarioapp.data.DiaryEntriesDao;
import edu.dami.queridodiarioapp.models.DiaryEntry;

public class SaveEntryActivity extends AppCompatActivity {

    public static final int SAVE_RESULT_OK_CODE = 200;
    public static final int SAVE_RESULT_ERR_CODE = 500;

    private static final String TAG = SaveEntryActivity.class.getName();

    private DiaryEntriesDao entriesDao;

    private TextInputLayout tilTitle, tilDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_entry);
        setup();
    }

    private void setup() {
        entriesDao = new DiaryEntriesDao(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEntry();
            }
        });

        tilTitle = findViewById(R.id.til_title);
        tilDescription = findViewById(R.id.til_content);
    }

    private void submitEntry() {
        String titleInput = extractText(tilTitle);
        String descInput = extractText(tilDescription);
        boolean isValid = validateFields(titleInput, descInput);
        if(!isValid) {
            Log.i(TAG, "Datos inválidos");
            return;
        }

        saveEntry(titleInput, descInput);
    }

    private void saveEntry(String title, String description) {
        DiaryEntry entryToSave = new DiaryEntry(title, description);
        final long newId = entriesDao.insert(entryToSave);
        final boolean isSaved = newId >= 0;
        navigateToPreviousScreen(isSaved);
    }

    private boolean validateFields(@Nullable String title, @Nullable String description) {
        tilTitle.setError(null);
        tilDescription.setError(null);

        if(TextUtils.isEmpty(title)) {
            tilTitle.setError("El título es obligatorio");
            return false;
        }
        if(TextUtils.isEmpty(description)) {
            tilDescription.setError("La descripción es obligatoria");
            return false;
        }
        return true;
    }

    @Nullable
    private String extractText(@Nullable TextInputLayout til) {
        if(til == null || til.getEditText() == null) return null;
        if(til.getEditText().getText() == null) return null;
        return til.getEditText().getText().toString();
    }

    private void navigateToPreviousScreen(boolean isSaved) {
        final int resultCode = isSaved ? SAVE_RESULT_OK_CODE : SAVE_RESULT_ERR_CODE;
        setResult(resultCode);
        finish();
    }
}