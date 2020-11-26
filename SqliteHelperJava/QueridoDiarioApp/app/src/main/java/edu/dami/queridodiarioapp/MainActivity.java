package edu.dami.queridodiarioapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.dami.queridodiarioapp.data.DiaryEntriesDao;
import edu.dami.queridodiarioapp.helpers.Truss;
import edu.dami.queridodiarioapp.models.DiaryEntry;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final int SAVE_REQUEST_CODE = 10;

    private TextView tvContent;
    private DiaryEntriesDao entriesDao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_delete) {
            deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SAVE_REQUEST_CODE) {
            onSaveEntry(resultCode);
        }
    }

    private void setup() {
        tvContent = findViewById(R.id.tv_content);
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSaveScreen();
            }
        });

        entriesDao = new DiaryEntriesDao(getApplicationContext());
        loadData();
    }
    
    private void loadData() {
        if(entriesDao == null) {
            Log.e(TAG, "null entriesDao");
            return;
        }

        final List<DiaryEntry> entries = entriesDao.getAll();
        showData(entries);
    }

    private void showData(List<DiaryEntry> entries) {
        if(entries == null || entries.isEmpty()) {
            tvContent.setText(R.string.no_data);
            return;
        }

        Truss contentBuilder = new Truss();
        for (DiaryEntry entry : entries) {
            contentBuilder
                    .pushSpan(new StyleSpan(Typeface.BOLD))
                    .append(entry.getTitle().toUpperCase() + "\n")
                    .popSpan()
                    .pushSpan(new StyleSpan(Typeface.ITALIC))
                    .append(entry.getCreatedAt() + "\n")
                    .popSpan()
                    .append(entry.getDescription() + "\n\n")
            ;
        }
        tvContent.setText(contentBuilder.build());
    }

    private void navigateToSaveScreen() {
        Intent intent = new Intent(getBaseContext(), SaveEntryActivity.class);
        startActivityForResult(intent, SAVE_REQUEST_CODE);
    }

    private void onSaveEntry(int resultCode) {
        if(resultCode == SaveEntryActivity.SAVE_RESULT_ERR_CODE) {
            showToast(R.string.error_saving_entry);
            return;
        }
        if(resultCode != SaveEntryActivity.SAVE_RESULT_OK_CODE) {
            Log.i(TAG, "Se ha retornado un codigo no soportado");
            return;
        }
        showToast(R.string.entry_saved);
        loadData();
    }

    private void deleteAll() {
        boolean isDeleted = entriesDao.deleteAll();
        if(isDeleted) {
            showToast(R.string.records_deleted);
            loadData();
        } else {
            showToast(R.string.records_deleted_error);
        }
    }

    private void showToast(@StringRes int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT)
                .show();
    }
}