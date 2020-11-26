package edu.dami.queridodiarioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import edu.dami.queridodiarioapp.data.DiaryEntriesDao;
import edu.dami.queridodiarioapp.helpers.Truss;
import edu.dami.queridodiarioapp.models.DiaryEntry;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private TextView tvContent;
    private DiaryEntriesDao entriesDao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }
    
    private void setup() {
        tvContent = findViewById(R.id.tv_content);
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
                    .append(entry.getTitle() + "\n")
                    .popSpan()
                    .pushSpan(new StyleSpan(Typeface.ITALIC))
                    .append(entry.getCreatedAt() + "\n")
                    .popSpan()
                    .append(entry.getDescription() + "\n")
            ;
        }
        tvContent.setText(contentBuilder.build());
    }
}