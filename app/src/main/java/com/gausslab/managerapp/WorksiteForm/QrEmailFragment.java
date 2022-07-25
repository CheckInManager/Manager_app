package com.gausslab.managerapp.WorksiteForm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentQremailBinding;

public class QrEmailFragment extends Fragment {

    private FragmentQremailBinding binding;

    private ImageView iv_qr;
    private Button bt_sendEmail;
    private Button bt_home;


    public QrEmailFragment() {
    }


    public static QrEmailFragment newInstance(String param1, String param2) {
        QrEmailFragment fragment = new QrEmailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQremailBinding.inflate(inflater, container, false);
        iv_qr = binding.qremailIvQr;
        bt_sendEmail = binding.qreamilBtSendEmail;
        bt_home = binding.qremailBtHome;
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(QrEmailFragment.this).navigate(R.id.action_qrEmailFragment_to_todayWorkSiteFragment);
            }
        });
    }
}