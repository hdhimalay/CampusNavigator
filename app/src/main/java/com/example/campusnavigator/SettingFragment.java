package com.example.campusnavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    private TextView welcomeTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        welcomeTextView = rootView.findViewById(R.id.welcome_text_view);

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setWelcomeText(welcomeTextView);
        }

        return rootView;


    }
}
