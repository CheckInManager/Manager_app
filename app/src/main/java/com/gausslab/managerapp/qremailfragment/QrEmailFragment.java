package com.gausslab.managerapp.qremailfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentQremailBinding;
import com.gausslab.managerapp.repository.WorksiteRepository;

import java.io.File;

public class QrEmailFragment extends Fragment {
    private FragmentQremailBinding binding;
    private QrEmailViewModel qrEmailViewModel;
    private WorksiteRepository worksiteRepository = WorksiteRepository.getInstance();

    private ImageView iv_qr;
    private Button bt_sendEmail;
    private Button bt_home;

    public QrEmailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrEmailViewModel = new ViewModelProvider(this).get(QrEmailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQremailBinding.inflate(inflater, container, false);
        iv_qr = binding.qremailIvQr;
        bt_sendEmail = binding.qreamilBtSendEmail;
        bt_home = binding.qremailBtHome;

        qrEmailViewModel.setWorksite(QrEmailFragmentArgs.fromBundle(getArguments()).getWorksiteName(),
                QrEmailFragmentArgs.fromBundle(getArguments()).getWorksiteName() + QrEmailFragmentArgs.fromBundle(getArguments()).getWorksiteLocation() + QrEmailFragmentArgs.fromBundle(getArguments()).getWorksiteStartDate());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        qrEmailViewModel.isQrImageLoaded().observe(getViewLifecycleOwner(), isQrLoaded -> {
            if (isQrLoaded)
                iv_qr.setImageDrawable(qrEmailViewModel.getQrImage());
        });
        //endregion

        //region Listener
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(QrEmailFragment.this).navigate(R.id.action_qrEmailFragment_to_todayWorkSiteFragment);
            }
        });

        bt_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mail_intent = new Intent(Intent.ACTION_SEND);
                mail_intent.setType("*/*");
                Uri uri = FileProvider.getUriForFile(requireActivity(), App.getFileProvider(), worksiteRepository.localFile);
                mail_intent.putExtra(Intent.EXTRA_STREAM, uri);
                mail_intent.putExtra(Intent.EXTRA_SUBJECT, "emailTest"); //이메일 제목
                mail_intent.putExtra(Intent.EXTRA_TEXT, "test"); //메일 내용
                startActivity(mail_intent);
            }
        });
        //endregion
    }
}