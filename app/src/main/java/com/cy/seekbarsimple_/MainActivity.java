package com.cy.seekbarsimple_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cy.seekbarniubility.LogUtils;
import com.cy.seekbarniubility.SeekBarSimple;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBarSimple cySeekBar = findViewById(R.id.cySeekBar);
//        final SeekBarCache cacheSeekBar = findViewById(R.id.cacheSeekBar);

//        cacheSeekBar.addCacheRecord(1024, 10, 300)
//                .addCacheRecord(1024, 400, 600)
//                .addCacheRecord(1024, 700, 760)
//                .addCacheRecord(1024, 800, 890)
//                .addCacheRecord(1024, 903, 1000)
//                .invalidate();
//        cacheSeekBar.addCacheRecord( 10, 300)
//                .addCacheRecord( 400, 600)
//                .addCacheRecord( 700, 760)
//                .addCacheRecord( 800, 890)
//                .addCacheRecord( 903, 1000)
//                .refreshCacheProgress(1024);

        findViewById(R.id.btn_seekbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cySeekBar.setProgress(60);
                cySeekBar.setProgress_second(80);

//                cacheSeekBar.setProgress(40);
//                cacheSeekBar.setProgress_second(60);

            }
        });

        cySeekBar.setOnSeekBarChangeListener(new SeekBarSimple.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBarSimple seekBarSimple, int progress) {

            }

            @Override
            public void onStartTouch(SeekBarSimple seekBarSimple) {

            }

            @Override
            public void onStopTouch(SeekBarSimple seekBarSimple) {
                LogUtils.log("seekBarSimple.getProgress(",seekBarSimple.getProgress());
            }
        });

    }
}
