package com.gausslab.managerapp.addaccidenthistoryform;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gausslab.managerapp.databinding.FragmentAddaccidenthistoryformBinding;
import com.gausslab.managerapp.model.AccidentHistory;

import java.util.Calendar;

public class AddAccidentHistoryFormFragment extends Fragment {
    private FragmentAddaccidenthistoryformBinding binding;
    private AddAccidentHistoryFormViewModel addAccidentHistoryFormViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;
    private EditText et_description;
    private EditText et_place;
    private EditText et_date;
    private EditText et_time;
    private Button bt_add;

    private String userPhoneNumber;
    private String userName;
    private AccidentHistory accidentHistory;
    private String accidentHistoryKey;

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

        addAccidentHistoryFormViewModel.loadAccidentHistoryKey();

        //region Observer
        addAccidentHistoryFormViewModel.isAccidentHistoryKeyLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if(isLoaded){
                    accidentHistoryKey= String.valueOf(addAccidentHistoryFormViewModel.getAccidentHistoryKey());
                    Log.d("asdfadsfasdfasdfasdf", "onChanged: "+accidentHistoryKey);
                }
            }
        });

        addAccidentHistoryFormViewModel.isAddAccidentHistorySuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess){
                    NavHostFragment.findNavController(AddAccidentHistoryFormFragment.this).navigateUp();
                }
            }
        });
        //endregion

        //region Listener
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_description.getText().toString().length() < 1) {
                    Toast.makeText(requireContext(), "description is empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (userPhoneNumber.length() > 1) {
                        accidentHistory = new AccidentHistory(et_description.getText().toString(), et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userPhoneNumber,accidentHistoryKey);
                    } else {
                        accidentHistory = new AccidentHistory(et_description.getText().toString(), et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userName,accidentHistoryKey);
                    }
                    addAccidentHistoryFormViewModel.addAccidentHistory(accidentHistory);
                    bt_add.setEnabled(false);
                }
            }
        });

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String s = "" + year + "." + (month + 1) + "." + dayOfMonth;
                et_date.setText(s);
            }
        };

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String s = "h" + hour + "m" + min;
                et_time.setText(s);
            }
        };

        dateCancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                et_date.clearFocus();
                et_time.clearFocus();
            }
        };

        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(dateSetListener);
                }
            }
        });

        et_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker(timeSetListener);
                }
            }
        });
        //endregion
    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener listener) {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getContext(), listener, y, m, d);
        dpd.setOnCancelListener(dateCancelListener);
        dpd.show();
    }

    private void showTimePicker(TimePickerDialog.OnTimeSetListener listener) {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, listener, h, m, false);
        tpd.setOnCancelListener(dateCancelListener);
        tpd.show();
    }
}