package com.gausslab.managerapp.addworker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentAddworkerBinding;
import com.gausslab.managerapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddWorkerFragment extends Fragment {
    private FragmentAddworkerBinding binding;
    private AddWorkerViewModel addWorkerViewModel;

    private ImageView iv_image;
    private EditText et_name;
    private EditText et_phoneNumber;
    private EditText et_password;
    private EditText et_accidentHistory;
    private EditText et_memo;
    private Spinner sp_worksiteSpinner;
    private Button bt_add;

    public AddWorkerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addWorkerViewModel = new ViewModelProvider(this).get(AddWorkerViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddworkerBinding.inflate(inflater, container, false);
        iv_image = binding.addworkerIvImage;
        et_name = binding.addworkerEtName;
        et_phoneNumber = binding.addworkerEtPhoneNumber;
        et_password = binding.addworkerEtPassword;
        et_accidentHistory = binding.addworkerEtAccidentHistory;
        et_memo = binding.addworkerEtMemo;
        sp_worksiteSpinner = binding.addworkerSpWorksiteSpinner;
        bt_add = binding.addworkerBtAdd;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addWorkerViewModel.loadPhoneNumberList();
        addWorkerViewModel.loadWorksiteNameList();

        addWorkerViewModel.isWorksiteNameList().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if(isLoaded){
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,addWorkerViewModel.getWorksiteNameList());
                    sp_worksiteSpinner.setAdapter(arrayAdapter);
                }
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User addNewUser = new User(et_phoneNumber.getText().toString(),
                        et_password.getText().toString(),et_name.getText().toString(), iv_image.toString(),
                        null,sp_worksiteSpinner.getSelectedItem().toString(),et_accidentHistory.getText().toString(), et_memo.getText().toString());
                if(addWorkerViewModel.checkPhoneNumber(et_phoneNumber.getText().toString())){
                    addWorkerViewModel.addUser(addNewUser);
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(AddWorkerFragment.this).navigateUp();
                }else{
                    Toast.makeText(requireContext(), "Please change phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}