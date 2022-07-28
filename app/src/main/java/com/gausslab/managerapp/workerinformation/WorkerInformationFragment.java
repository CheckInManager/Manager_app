package com.gausslab.managerapp.workerinformation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentCheckinworkersbysiteBinding;
import com.gausslab.managerapp.databinding.FragmentWorkerinformationBinding;
import com.gausslab.managerapp.model.User;

import java.util.List;

public class WorkerInformationFragment extends Fragment {
    private FragmentWorkerinformationBinding binding;
    private WorkerInformationViewModel workerInformationViewModel;

    private ImageView iv_image;
    private TextView tv_nameLabel;
    private TextView tv_name;
    private TextView tv_phoneNumberLabel;
    private TextView tv_phoneNumber;
    private TextView tv_careerLabel;
    private TextView tv_career;
    private TextView tv_accidentHistoryLabel;
    private EditText et_accidentHistory;
    private TextView tv_memoLabel;
    private EditText et_memo;
    private Button bt_complete;

    private User currUser;

    public WorkerInformationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workerInformationViewModel = new ViewModelProvider(requireActivity()).get(WorkerInformationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkerinformationBinding.inflate(inflater, container, false);
        iv_image = binding.workinformationIvImage;
        tv_nameLabel = binding.workerinformationTvNameLabel;
        tv_name = binding.workerinformationEtName;
        tv_phoneNumberLabel = binding.workerinformationTvPhoneNumberLabel;
        tv_phoneNumber = binding.workerinformationEtPhoneNumber;
        tv_careerLabel = binding.workerinformationTvCareerLabel;
        tv_career = binding.workerinformationEtCareer;
        tv_accidentHistoryLabel = binding.workerinformationTvAccidentHistoryLabel;
        et_accidentHistory = binding.workerinformationEtAccidentHistory;
        tv_memoLabel = binding.workerinformationTvMemoLabel;
        et_memo = binding.workerinformationEtMemo;
        bt_complete = binding.workerinformationBtComplete;

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currUser = new User(currUser.getPhoneNumber(), currUser.getPassword(), currUser.getUserName(), currUser.getUserImage(), currUser.getCareer(), currUser.getWorksiteName(), et_accidentHistory.getText().toString(), et_memo.getText().toString());
                workerInformationViewModel.changeInformation(currUser);
                NavHostFragment.findNavController(WorkerInformationFragment.this).navigateUp();
            }
        });
    }

    private void init() {
        String phoneNumber = WorkerInformationFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        workerInformationViewModel.loadUserInformation(phoneNumber);
        workerInformationViewModel.userInformationLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    currUser = workerInformationViewModel.getUserInformation();

                    tv_name.setText(currUser.getUserName());
                    tv_phoneNumber.setText(currUser.getPhoneNumber());
                    tv_career.setText(currUser.getCareer());
                    et_accidentHistory.setText(currUser.getAccidentHistory());
                    et_memo.setText(currUser.getMemo());
                }
            }
        });
    }
}