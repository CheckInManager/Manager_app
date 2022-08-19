package com.gausslab.managerapp.workerinformation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.gausslab.managerapp.databinding.FragmentWorkerinformationBinding;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class WorkerInformationFragment extends Fragment {
    private FragmentWorkerinformationBinding binding;
    private WorkerInformationViewModel workerInformationViewModel;
    private AccidentHistoryRecyclerViewAdapter adapter;

    private ImageView iv_image;
    private TextView tv_name;
    private TextView tv_phoneNumber;
    private TextView tv_career;
    private RecyclerView rv_accidentHistoryList;
    private Button bt_accidentHistoryAdd;
    private EditText et_memo;
    private Button bt_complete;
    private List<AccidentHistory> accidentHistoryList;

    private User currUser;

    public WorkerInformationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workerInformationViewModel = new ViewModelProvider(requireActivity()).get(WorkerInformationViewModel.class);
        String phoneNumber = WorkerInformationFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        String userName = WorkerInformationFragmentArgs.fromBundle(getArguments()).getUserName();

        boolean userHasPhoneNumber = true;
        if (phoneNumber.isEmpty()) {
            phoneNumber = userName;
            userHasPhoneNumber = false;
        }

        workerInformationViewModel.loadAllUserInformation(phoneNumber, userHasPhoneNumber);

        adapter = new AccidentHistoryRecyclerViewAdapter(
                new ArrayList<>(),
                new OnAccidentHistoryInteractionListener() {
                    @Override
                    public void onClick(AccidentHistory obj) {
                        WorkerInformationFragmentDirections.ActionUserInformationFragmentToAccidentHistoryDetailFragment action = WorkerInformationFragmentDirections.actionUserInformationFragmentToAccidentHistoryDetailFragment();
                        action.setKeyValue(obj.getKeyValue());
                        NavHostFragment.findNavController(WorkerInformationFragment.this).navigate(action);
                    }

                    @Override
                    public void onDelete(AccidentHistory obj) {
                        workerInformationViewModel.deleteAccidentHistory(obj);
                    }
                });
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

        rv_accidentHistoryList.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        workerInformationViewModel.getAccidentHistoryList().observe(getViewLifecycleOwner(), new Observer<List<AccidentHistory>>() {
            @Override
            public void onChanged(List<AccidentHistory> accidentHistories) {
                adapter.setAccidentHistoryList(accidentHistories);
                accidentHistoryList = accidentHistories;

            }
        });

        workerInformationViewModel.isUserInformationLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    currUser = workerInformationViewModel.getCurrUser();
                    iv_image.setImageDrawable(workerInformationViewModel.getUserImage());
                    tv_name.setText(currUser.getUserName());
                    tv_phoneNumber.setText(currUser.getPhoneNumber());
                    tv_career.setText(currUser.getCareer());
                    et_memo.setText(currUser.getMemo());
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
                workerInformationViewModel.saveUser();
                NavHostFragment.findNavController(WorkerInformationFragment.this).navigateUp();
            }
        });

        et_memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                workerInformationViewModel.updateMemoText(s.toString());
            }
        });
        //endregion
    }
}