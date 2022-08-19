package com.gausslab.managerapp.accidenthistorydetail;

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
import androidx.navigation.fragment.NavHostFragment;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.databinding.FragmentAccidenthistorydetailBinding;
import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.workerinformation.WorkerInformationViewModel;

import java.util.Calendar;

public class AccidentHistoryDetailFragment extends Fragment {
    private FragmentAccidenthistorydetailBinding binding;
    private WorkerInformationViewModel workerInformationViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;
    private EditText et_description;
    private EditText et_place;
    private EditText et_date;
    private EditText et_time;
    private Button bt_ok;
    private String loadedKeyValue;

    public AccidentHistoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workerInformationViewModel = new ViewModelProvider(requireActivity()).get(WorkerInformationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccidenthistorydetailBinding.inflate(inflater, container, false);
        et_description = binding.accidenthistorydetailEtDescription;
        et_place = binding.accidenthistorydetailEtPlace;
        et_date = binding.accidenthistorydetailEtDate;
        et_time = binding.accidenthistorydetailEtTime;
        bt_ok = binding.accidenthistorydetailBtOk;
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccidentHistory mAccidentHistory = workerInformationViewModel.getCurrEditingAccidentHistory();
        et_description.setText(mAccidentHistory.getDescription());
        et_place.setText(mAccidentHistory.getPlace());
        et_date.setText(mAccidentHistory.getDate());
        et_time.setText(mAccidentHistory.getTime());

        //region Observer
        workerInformationViewModel.isAddAccidentHistorySuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if (!booleanEvent.isHandled()) {
                    boolean isAdded = booleanEvent.consumeData();
                    if (isAdded)
                        NavHostFragment.findNavController(AccidentHistoryDetailFragment.this).navigateUp();
                }
            }
        });
        //endregion

        //region Listener
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_description.getText().toString().length() < 1) {
                    Toast.makeText(requireContext(), "description is empty", Toast.LENGTH_SHORT).show();
                } else {
                    if(workerInformationViewModel.checkDate(et_date.getText().toString()) && workerInformationViewModel.checkTime(et_time.getText().toString())){
                        workerInformationViewModel.changeAccidentHistory(et_description.getText().toString(),
                                et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString());
                    }else{
                        Toast.makeText(requireContext(), "Date or Time format is Wrong", Toast.LENGTH_SHORT).show();
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

    private void init() {
        loadedKeyValue = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getKeyValue();
        workerInformationViewModel.setCurrEditingAccidentHistoryByKey(loadedKeyValue);
    }
}