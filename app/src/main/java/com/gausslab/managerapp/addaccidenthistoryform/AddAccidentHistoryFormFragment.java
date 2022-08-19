package com.gausslab.managerapp.addaccidenthistoryform;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.databinding.FragmentAddaccidenthistoryformBinding;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.workerinformation.WorkerInformationViewModel;

import java.util.Calendar;

public class AddAccidentHistoryFormFragment extends Fragment {
    private FragmentAddaccidenthistoryformBinding binding;
    private WorkerInformationViewModel workerInformationViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;
    private NavController navController;

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
        navController = NavHostFragment.findNavController(AddAccidentHistoryFormFragment.this);
        workerInformationViewModel = new ViewModelProvider(requireActivity()).get(WorkerInformationViewModel.class);
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

        //region Observer
        workerInformationViewModel.isAddAccidentHistorySuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if (!booleanEvent.isHandled()) {
                    boolean isAdded = booleanEvent.consumeData();
                    if (isAdded)
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
                    if(workerInformationViewModel.checkDate(et_date.getText().toString()) &&workerInformationViewModel.checkTime(et_time.getText().toString())){
                        if (userPhoneNumber.length() > 1) {
                            accidentHistory = new AccidentHistory(et_description.getText().toString(), et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userPhoneNumber, null);
                        } else {
                            accidentHistory = new AccidentHistory(et_description.getText().toString(), et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userName, null);
                        }
                        workerInformationViewModel.addAccidentHistory(accidentHistory);
                        bt_add.setEnabled(false);
                    }else{
                        Toast.makeText(requireContext(), "Date or Time Format is Wrong", Toast.LENGTH_SHORT).show();
                        et_date.setText(null);
                        et_time.setText(null);
                    }
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
                String s = "" + hour + ":" + min;
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