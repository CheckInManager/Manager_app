package com.gausslab.managerapp.accidenthistorydetail;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentAccidenthistorydetailBinding;
import com.gausslab.managerapp.model.AccidentHistory;

import java.util.Calendar;

public class AccidentHistoryDetailFragment extends Fragment {
    private FragmentAccidenthistorydetailBinding binding;
    private AccidentHistoryDetailViewModel accidentHistoryDetailViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;
    private EditText et_description;
    private EditText et_place;
    private EditText et_date;
    private EditText et_time;
    private Button bt_ok;
    private String userPhoneNumber;

    private String loadDescription;
    private String loadPlace;
    private String loadDate;
    private String loadTime;
    private String loadKeyValue;


    public AccidentHistoryDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accidentHistoryDetailViewModel = new ViewModelProvider(requireActivity()).get(AccidentHistoryDetailViewModel.class);
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

        //region Observer
        accidentHistoryDetailViewModel.isDeletedSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessful) {
                if (isSuccessful) {
                    AccidentHistory accidentHistory = new AccidentHistory(et_description.getText().toString(),
                            et_place.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), userPhoneNumber,loadKeyValue);
                    accidentHistoryDetailViewModel.changeAccidentHistory(accidentHistory);
                    NavHostFragment.findNavController(AccidentHistoryDetailFragment.this).navigateUp();
                    accidentHistoryDetailViewModel.setDeletedSuccess();
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
                    AccidentHistory exAccidentHistory = new AccidentHistory(loadDescription, loadPlace, loadDate, loadTime, userPhoneNumber,loadKeyValue);
                    accidentHistoryDetailViewModel.deleteExAccidentHistory(exAccidentHistory);
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

    private void init() {
        loadDescription = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        loadPlace = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getPlace();
        loadDate = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getDate();
        loadTime = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getTime();
        userPhoneNumber = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getUserPhoneNumber();
        loadKeyValue = AccidentHistoryDetailFragmentArgs.fromBundle(getArguments()).getKeyValue();
        //accidentHistoryDetailViewModel.loadAccidentHistoryDetail(description,place,date,time);
        et_description.setText(loadDescription);
        et_place.setText(loadPlace);
        et_date.setText(loadDate);
        et_time.setText(loadTime);
    }
}