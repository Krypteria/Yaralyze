package com.example.yaralyze01.ui.mainMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.staticAnalysis.StaticAnalysisActivity;

public class mainMenuFragment extends Fragment {

    private Button staticAnalysisButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        this.staticAnalysisButton = view.findViewById(R.id.staticAnalysisButton);
        this.staticAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StaticAnalysisActivity.class));
            }
        });

        return view;
    }
}