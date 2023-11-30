package com.cy.seekbarsimple_;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.cy.seekbarniubility.SeekBarGradient;
import com.cy.seekbarniubility.SeekBarSimple;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SeekBarSimple cySeekBar = findViewById(R.id.cySeekBar);

        SeekBar seekBar = findViewById(R.id.SeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.log("SeekBar onProgressChanged"+fromUser,progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtils.log("SeekBar onStartTrackingTouch",seekBar.getProgress());

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtils.log("SeekBar onStopTrackingTouch",seekBar.getProgress());
            }
        });
        seekBar.setProgress(60);

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
                LogUtils.log("onProgressChanged",progress);
            }

            @Override
            public void onStartTouch(SeekBarSimple seekBarSimple, int progress) {
                LogUtils.log("onStartTouch",progress);

            }

            @Override
            public void onStopTouch(SeekBarSimple seekBarSimple, int progress) {
                LogUtils.log("onStopTouch",progress);
            }
        });
        final SeekBarGradient seekBarGradient = findViewById(R.id.SeekBarGradient);

        findViewById(R.id.btn_change_gradient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarGradient.setLinearGradient(new int[]{0xffff0000, 0xffffff00, 0xff00ff00, 0xff00ffff, 0xff0000ff, 0xffff00ff},
                        new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1}, Shader.TileMode.CLAMP);
            }
        });

    }
}
