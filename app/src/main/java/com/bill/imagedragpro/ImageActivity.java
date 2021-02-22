package com.bill.imagedragpro;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

public class ImageActivity extends AppCompatActivity {

    public static final String TRANSIT_PIC = "picture";

    private DragLayout dragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        dragLayout = findViewById(R.id.drag_layout);
        dragLayout/*.bind(this)*/.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onDragFinished() {
                ImageActivity.this.onBackPressed();
            }

        });

        ImageView imageView = findViewById(R.id.picture);
        ViewCompat.setTransitionName(imageView, TRANSIT_PIC);

    }
}