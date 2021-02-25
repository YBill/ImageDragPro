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

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = view.findViewById(R.id.iv_picture);
        if (getArguments() != null) {
            int resId = getArguments().getInt("imgRes");
            mImageView.setImageResource(resId);
        }

        DragViewLayout dragViewLayout = view.findViewById(R.id.drag_layout);
        dragViewLayout.setDragListener(new DragViewLayout.DragListener() {
            @Override
            public void onDragFinished() {
                // 还原
                View mainView = MainActivity.activity.getWindow().getDecorView();
                mainView.setScaleX(1f);
                mainView.setScaleY(1f);

                if (getActivity() != null) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onDrag(float change) {
                Log.e("Bill", "change:" + change);

                if (getActivity() instanceof ImageActivity) {
                    float alpha = Math.min(change * 2f, 1f);
                    ((ImageActivity) getActivity()).mParentView.setBackgroundColor(Utils.changeAlpha(0xff000000, 1 - alpha));
                }


                float mainScale = Math.min(change * 0.3f + 0.9f, 1f);
                View mainView = MainActivity.activity.getWindow().getDecorView();
                mainView.setScaleX(mainScale);
                mainView.setScaleY(mainScale);


                float scale = Math.min(change, 0.25f);
                mImageView.setScaleX(1 - scale);
                mImageView.setScaleY(1 - scale);

            }

        });
    }
}
