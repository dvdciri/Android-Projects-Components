package com.davidecirillo.multichoicesample.sampleImageGallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceToolbar;
import com.davidecirillo.multichoicesample.BaseActivity;
import com.davidecirillo.multichoicesample.R;

import butterknife.BindView;

public class GalleryActivity extends BaseActivity {

    @BindView(R.id.multiChoiceRecyclerView)
    public RecyclerView mMultiChoiceRecyclerView;

    @Override
    protected int setActivityIdentifier() {
        return R.layout.activity_sample_custom_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpMultiChoiceRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ((MultiChoiceAdapter) mMultiChoiceRecyclerView.getAdapter()).onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ((MultiChoiceAdapter) mMultiChoiceRecyclerView.getAdapter()).onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setUpMultiChoiceRecyclerView() {

        mMultiChoiceRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));

        MultiChoiceToolbar multiChoiceToolbar =
                new MultiChoiceToolbar.Builder(GalleryActivity.this, toolbar)
                        .setTitles(getString(toolbarTitle()), "")
                        .setMultiChoiceColours(R.color.colorPrimaryMulti, R.color.colorPrimaryDarkMulti)
                        .setDefaultIcon(R.drawable.ic_arrow_back_white_24dp, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        })
                        .build();

        SampleGalleryAdapter adapter = new SampleGalleryAdapter(GalleryActivity.this);
        adapter.setMultiChoiceToolbar(multiChoiceToolbar);

        mMultiChoiceRecyclerView.setAdapter(adapter);
    }

    @Override
    protected int toolbarTitle() {
        return R.string.image_gallery;
    }

    @Override
    protected boolean showBackHomeAsUpIndicator() {
        return true;
    }
}
