package com.gausslab.managerapp.addaccidenthistoryform;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentAddaccidenthistoryformBinding;
import com.gausslab.managerapp.model.AccidentHistory;

public class AddAccidentHistoryFormFragment extends Fragment {
    private FragmentAddaccidenthistoryformBinding binding;
    private AddAccidentHistoryFormViewModel addAccidentHistoryFormViewModel;
    private EditText et_description;
    private EditText et_place;
    private EditText et_date;
    private EditText et_time;
    private Button bt_add;

    private String userPhoneNumber;
    private String userName;
    private AccidentHistory accidentHistory;

    public AddAccidentHistoryFormFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAccidentHistoryFormViewModel = new ViewModelProvider(this).get(AddAccidentHistoryFormViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddaccidenthistoryformBinding.inflate(inflater, container, false);
        et_description = binding.addaccidenthistoryEtDescription;
        et_place = binding.addaccidenthistoryEtPlace;
        et_date = binding.addaccidenthistoryEtDate;
        et_time = binding.addaccidenthistoryEtTime;
        bt_add = binding.addaccidenthistoryBtAdd;

        userPhoneNumber = AddAccidentHistoryFormFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        userName = AddAccidentHistoryFormFragmentArgs.fromBundle(getArguments()).getUserName();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addAccidentHistoryFormViewModel.isAddAccidentHistorySuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess)
                    NavHostFragment.findNavController(AddAccidentHistoryFormFragment.this).navigateUp();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_description.getText().toString().length() < 1) {
                    Toast.makeText(requireContext(), "description is empty", Toast.LENGTH_SHORT).show();
                } else {
                    if(userPhoneNumber.length()>1){
                        accidentHistory = new AccidentHistory(et_description.getText().toString(), et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userPhoneNumber);
                    }else{
                        accidentHistory = new AccidentHistory(et_description.getText().toString(), et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userName);
                    }
                    addAccidentHistoryFormViewModel.addAccidentHistory(accidentHistory);
                    bt_add.setEnabled(false);
                }
            }
        });
    }
}