package com.practice.dev.baziershopcaranim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button buy;
    private BazierAnimView bazierAnimView;
    private ImageView car_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buy = (Button) findViewById(R.id.buy);
        car_iv = (ImageView) findViewById(R.id.car_iv);
        bazierAnimView = (BazierAnimView) findViewById(R.id.bezier_anim);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bazierAnimView.startCartAnim(buy, car_iv,R.layout.l_move);
            }
        });
    }
}
