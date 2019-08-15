package com.example.autoirrigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabLayoutFragment extends Fragment {
    public static String TABLAYOUT_FRAGMENT = "tab_fragment";
    private TextView txt;
    private int type;

    public static TabLayoutFragment newInstance(int type){
        TabLayoutFragment fragment = new TabLayoutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            type = (int) getArguments().getSerializable(TABLAYOUT_FRAGMENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_layout, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view){
        txt = (TextView) view.findViewById(R.id.tab_txt);

        switch(type){
            case 2:
                txt.setText("功能页 2");
                break;
            case 3:
                txt.setText("功能页 3");
                break;
            case 4:
                txt.setText("功能页 4");
                break;
        }
    }
}
