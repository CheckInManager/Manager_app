package com.gausslab.managerapp.workerinformation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentWorkerinformationBinding;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

public class WorkerInformationFragment extends Fragment {
    private FragmentWorkerinformationBinding binding;
    private WorkerInformationViewModel workerInformationViewModel;

    private ImageView iv_image;
    private TextView tv_name;
    private TextView tv_phoneNumber;
    private TextView tv_career;
    private RecyclerView rv_accidentHistoryList;
    private Button bt_accidentHistoryAdd;
    private EditText et_memo;
    private Button bt_complete;
    private AccidentHistoryRecyclerViewAdapter adapter;

    private User currUser;
    private String phoneNumber;
    private String userName;

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
        tv_name = binding.workerinformationEtName;
        tv_phoneNumber = binding.workerinformationEtPhoneNumber;
        tv_career = binding.workerinformationEtCareer;
        rv_accidentHistoryList = binding.workerinformationRvAccidentHisoryList;
        bt_accidentHistoryAdd = binding.workerinformationBtAccidentHistoryAdd;
        et_memo = binding.workerinformationEtMemo;
        bt_complete = binding.workerinformationBtComplete;

        rv_accidentHistoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_accidentHistoryList.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        workerInformationViewModel.isAccidentHistoryListLoaded(phoneNumber).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    adapter.setAccidentHistoryList(workerInformationViewModel.getAccidentHistoryList(phoneNumber));
                }
            }
        });
        //endregion

        //region Listener
        bt_accidentHistoryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkerInformationFragmentDirections.ActionUserInformationFragmentToAddAccidentHistoryFormFragment action = WorkerInformationFragmentDirections.actionUserInformationFragmentToAddAccidentHistoryFormFragment();
                action.setPhoneNumber(currUser.getPhoneNumber());
                action.setUserName(currUser.getUserName());
                NavHostFragment.findNavController(WorkerInformationFragment.this).navigate(action);
            }
        });

        bt_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workerInformationViewModel.getAccidentHistoryList(phoneNumber).toString().length() < 3) {
                    currUser = new User(currUser.getPhoneNumber(), currUser.getPassword(), currUser.getUserName(), currUser.getCareer(), currUser.getWorksiteName(),
                            "", et_memo.getText().toString());
                } else {
                    currUser = new User(currUser.getPhoneNumber(), currUser.getPassword(), currUser.getUserName(), currUser.getCareer(), currUser.getWorksiteName(),
                            workerInformationViewModel.getAccidentHistoryList(phoneNumber).toString(), et_memo.getText().toString());
                }
                if (currUser.getPhoneNumber().length() < 1) {
                    workerInformationViewModel.changeNoPhoneNumberUserInformation(currUser);
                } else {
                    workerInformationViewModel.changeInformation(currUser);
                }
                NavHostFragment.findNavController(WorkerInformationFragment.this).navigateUp();
            }
        });
        //endregion
    }

    private void init() {
        phoneNumber = WorkerInformationFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        userName = WorkerInformationFragmentArgs.fromBundle(getArguments()).getUserName();


        if (phoneNumber.length() > 1) {
            workerInformationViewModel.loadUserInformation(phoneNumber);
            workerInformationViewModel.loadUserImage(phoneNumber);
            workerInformationViewModel.loadAccidentHistoryListByUser(phoneNumber);

        } else {
            phoneNumber = userName;
            workerInformationViewModel.noPhoneNumberLoadUserInformation(userName);
            workerInformationViewModel.loadUserImage(userName);
            workerInformationViewModel.loadAccidentHistoryListByUser(userName);
        }
        workerInformationViewModel.userInformationLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    currUser = workerInformationViewModel.getUserInformation();
                    iv_image.setImageDrawable(workerInformationViewModel.getUserImage());
                    tv_name.setText(currUser.getUserName());
                    tv_phoneNumber.setText(currUser.getPhoneNumber());
                    tv_career.setText(currUser.getCareer());
                    et_memo.setText(currUser.getMemo());
                }
            }
        });
        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new AccidentHistoryRecyclerViewAdapter(workerInformationViewModel.getAccidentHistoryList(phoneNumber),
                workerInformationViewModel, new OnTodayWorksiteContextMenuInteractionListener<AccidentHistory>() {
            @Override
            public void onItemClick(AccidentHistory obj) {
                WorkerInformationFragmentDirections.ActionUserInformationFragmentToAccidentHistoryDetailFragment action = WorkerInformationFragmentDirections.actionUserInformationFragmentToAccidentHistoryDetailFragment();
                action.setDescription(obj.getDescription());
                action.setPlace(obj.getPlace());
                action.setDate(obj.getDate());
                action.setTime(obj.getTime());
                action.setUserPhoneNumber(phoneNumber);
                action.setKeyValue(obj.getKeyValue());
                NavHostFragment.findNavController(WorkerInformationFragment.this).navigate(action);
            }

            @Override
            public void onContextReturnWorksite(AccidentHistory obj) {

            }
        });
        rv_accidentHistoryList.setAdapter(adapter);
    }
}