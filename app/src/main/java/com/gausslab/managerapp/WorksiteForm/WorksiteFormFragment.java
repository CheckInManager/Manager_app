package com.gausslab.managerapp.WorksiteForm;

import android.app.DatePickerDialog;
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

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.model.Worksite;

import java.util.Calendar;

public class WorksiteFormFragment extends Fragment {
    private WorksiteFormViewModel worksiteFormViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;

    private EditText et_workName;
    private EditText et_startDate;
    private EditText et_lastDate;
    private EditText et_location;
    private Button bt_add;


    public WorksiteFormFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        worksiteFormViewModel = new ViewModelProvider(this).get(WorksiteFormViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_worksiteform, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_workName = view.findViewById(R.id.worksiteform_et_workName);
        et_startDate = view.findViewById(R.id.worksiteform_et_startDate);
        et_lastDate = view.findViewById(R.id.worksiteform_et_lastDate);
        et_location = view.findViewById(R.id.worksiteform_et_location);
        bt_add = view.findViewById(R.id.worksitrform_bt_add);

        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String s = "" + year + "/" + (month + 1) + "/" + dayOfMonth;
                et_startDate.setText(s);
            }
        };

        DatePickerDialog.OnDateSetListener lastDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String s = "" + year + "/" + (month + 1) + "/" + dayOfMonth;
                et_lastDate.setText(s);
            }
        };

        dateCancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                et_startDate.clearFocus();
                et_lastDate.clearFocus();
            }
        };

        et_startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker(startDateSetListener);
                }
            }
        });

        et_lastDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker(lastDateListener);
                }
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Worksite worksite = new Worksite(et_workName.getText().toString(),et_startDate.getText().toString(),et_lastDate.getText().toString(),et_location.getText().toString());
                worksiteFormViewModel.addWorksite(worksite);
                worksiteFormViewModel.createQrForWorksite(worksite);
            }
        });

        worksiteFormViewModel.addWorksiteSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdditionalSuccessful) {
                if(isAdditionalSuccessful){
                    //여기가 아니라 QR화면을 먼저 가자
                    // NavHostFragment.findNavController(WorksiteFormFragment.this).navigate(R.id.action_worksiteFormFragment_to_todayWorkSiteFragment);
                    worksiteFormViewModel.setAddWorksiteSuccess(false);
                }
            }
        });
    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener listener){
        Calendar c =Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getContext(),listener,y,m,d);
        dpd.setOnCancelListener(dateCancelListener);
        dpd.show();
    }
}