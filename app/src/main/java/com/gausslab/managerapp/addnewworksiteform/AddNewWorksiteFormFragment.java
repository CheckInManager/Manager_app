package com.gausslab.managerapp.addnewworksiteform;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.model.Worksite;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class AddNewWorksiteFormFragment extends Fragment {
    private AddNewWorksiteFormViewModel addNewWorksiteFormViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;

    private EditText et_workName;
    private EditText et_startDate;
    private EditText et_lastDate;
    private EditText et_location;
    private Button bt_add;

    private Worksite worksite;


    public AddNewWorksiteFormFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewWorksiteFormViewModel = new ViewModelProvider(this).get(AddNewWorksiteFormViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_addnewworksiteform, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_workName = view.findViewById(R.id.worksiteform_et_workName);
        et_startDate = view.findViewById(R.id.worksiteform_et_startDate);
        et_lastDate = view.findViewById(R.id.worksiteform_et_lastDate);
        et_location = view.findViewById(R.id.worksiteform_et_location);
        bt_add = view.findViewById(R.id.worksitrform_bt_add);


        //region observer
        addNewWorksiteFormViewModel.addWorksiteFormSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean finish) {
                if (finish) {
                    addNewWorksiteFormViewModel.createQrForWorksite(worksite);
                }
            }
        });

        addNewWorksiteFormViewModel.addWorksiteSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdditionSuccessful) {
                if (isAdditionSuccessful) {
                    AddNewWorksiteFormFragmentDirections.ActionWorksiteFormFragmentToQrEmailFragment action = AddNewWorksiteFormFragmentDirections.actionWorksiteFormFragmentToQrEmailFragment();
                    action.setWorksiteName(et_workName.getText().toString());
                    NavHostFragment.findNavController(AddNewWorksiteFormFragment.this).navigate(action);
                }
            }
        });

        addNewWorksiteFormViewModel.getAddNewWorksiteFormState().observe(getViewLifecycleOwner(), new Observer<AddNewWorksiteFormState>() {
            @Override
            public void onChanged(AddNewWorksiteFormState addNewWorksiteFormState) {
                if (addNewWorksiteFormState.getWorksiteNameErrorMessage() != null) {
                    et_workName.setError(addNewWorksiteFormState.getLastDateErrorMessage());
                }
                if (addNewWorksiteFormState.getStartDateErrorMessage() != null) {
                    et_startDate.setError(addNewWorksiteFormState.getStartDateErrorMessage());
                }
                if (addNewWorksiteFormState.getLastDateErrorMessage() != null) {
                    et_lastDate.setError(addNewWorksiteFormState.getLastDateErrorMessage());
                }
                if (addNewWorksiteFormState.getLocationErrorMessage() != null) {
                    et_location.setError(addNewWorksiteFormState.getLocationErrorMessage());
                }
                bt_add.setEnabled(addNewWorksiteFormState.isFieldsValid());
            }
        });
        //endregion

        //region Listener
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
                if (hasFocus) {
                    showDatePicker(startDateSetListener);
                }
            }
        });

        et_lastDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(lastDateListener);
                }
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addNewWorksiteFormViewModel.isDatesValid(et_startDate.getText().toString(), et_lastDate.getText().toString())) {
                    worksite = new Worksite(et_workName.getText().toString(), et_startDate.getText().toString(), et_lastDate.getText().toString(), et_location.getText().toString());
                    addNewWorksiteFormViewModel.addWorksite(worksite);
                    bt_add.setEnabled(false);
                } else {
                    Toast.makeText(requireContext(), "Change the date", Toast.LENGTH_SHORT).show();
                    et_startDate.setText(null);
                    et_lastDate.setText(null);
                }
            }
        });

        et_workName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addNewWorksiteFormViewModel.onWorksiteNameChanged(editable.toString());
            }
        });

        et_startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addNewWorksiteFormViewModel.onStartDateChanged(editable.toString());
            }
        });

        et_lastDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addNewWorksiteFormViewModel.onLastDateChanged(editable.toString());
            }
        });

        et_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addNewWorksiteFormViewModel.onLocationChanged(editable.toString());
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
}