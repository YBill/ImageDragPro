package com.bill.imagedragpro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * author : Bill
 * date : 2021/2/25
 * description :
 */
public class ImageFragment extends Fragment {

    private DragLayout dragLayout;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.iv_picture);
        if (getArguments() != null) {
            int resId = getArguments().getInt("imgRes");
            imageView.setImageResource(resId);
        }

        dragLayout = view.findViewById(R.id.drag_layout);
        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onDragFinished() {
                if (getActivity() != null) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onDrag(float change) {
                Log.e("Bill", "change:" + change);

                if (getActivity() instanceof ImageActivity) {
                    ((ImageActivity) getActivity()).parentView.setBackgroundColor(Utils.changeAlpha(0xff000000, 1 - change));
                }

                if (change > 0.25f) {
                    change = 0.25f;
                }

//                imageView.setScaleX(1 - change);
//                imageView.setScaleY(1 - change);
            }

        });
    }
}
