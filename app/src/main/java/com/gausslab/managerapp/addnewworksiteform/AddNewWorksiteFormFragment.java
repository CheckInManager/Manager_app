package com.gausslab.managerapp.addnewworksiteform;

import android.app.AlertDialog;
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
import com.gausslab.managerapp.databinding.FragmentAddnewworksiteformBinding;
import com.gausslab.managerapp.model.Worksite;
import com.gausslab.managerapp.util.DateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewWorksiteFormFragment extends Fragment {
    private FragmentAddnewworksiteformBinding binding;
    private AddNewWorksiteFormViewModel addNewWorksiteFormViewModel;
    private DialogInterface.OnCancelListener dateCancelListener;
    private EditText et_worksiteName;
    private EditText et_startDate;
    private EditText et_endDate;
    private EditText et_location;
    private Button bt_add;

    private Worksite worksite;
    private Long longStartDate;
    private Long longEndDate;

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
        binding = FragmentAddnewworksiteformBinding.inflate(inflater, container, false);
        et_worksiteName = binding.worksiteformEtWorksiteName;
        et_startDate = binding.worksiteformEtStartDate;
        et_endDate = binding.worksiteformEtEndDate;
        et_location = binding.worksiteformEtLocation;
        bt_add = binding.worksitrformBtAdd;

        addNewWorksiteFormViewModel.loadAllWorksite();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                    action.setKeyValue(worksite.getId());
                    NavHostFragment.findNavController(AddNewWorksiteFormFragment.this).navigate(action);
                }
            }
        });

        addNewWorksiteFormViewModel.getAddNewWorksiteFormState().observe(getViewLifecycleOwner(), new Observer<AddNewWorksiteFormState>() {
            @Override
            public void onChanged(AddNewWorksiteFormState addNewWorksiteFormState) {
                if (addNewWorksiteFormState.getWorksiteNameErrorMessage() != null) {
                    et_worksiteName.setError(addNewWorksiteFormState.getEndDateErrorMessage());
                }
                if (addNewWorksiteFormState.getStartDateErrorMessage() != null) {
                    et_startDate.setError(addNewWorksiteFormState.getStartDateErrorMessage());
                }
                if (addNewWorksiteFormState.getEndDateErrorMessage() != null) {
                    et_endDate.setError(addNewWorksiteFormState.getEndDateErrorMessage());
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
                String strMonth;
                if (String.valueOf(month + 1).length() < 2) {
                    strMonth = "0" + (month + 1);
                } else {
                    strMonth = "" + (month + 1);
                }
                String s = "" + year + "" + strMonth + "" + dayOfMonth;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                ArrayList<String> result = new ArrayList<>();
                try {
                    Date date = format.parse(s);
                    longStartDate = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                et_startDate.setText(s);
            }
        };

        DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String strMonth;
                if (String.valueOf(month + 1).length() < 2) {
                    strMonth = "0" + (month + 1);
                } else {
                    strMonth = "" + (month + 1);
                }
                String s = "" + year + "" + strMonth + "" + dayOfMonth;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                ArrayList<String> result = new ArrayList<>();
                try {
                    Date date = format.parse(s);
                    longEndDate = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                et_endDate.setText(s);
            }
        };

        dateCancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                et_startDate.clearFocus();
                et_endDate.clearFocus();
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

        et_endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(endDateListener);
                }
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addNewWorksiteFormViewModel.checkDate(et_startDate.getText().toString()) && addNewWorksiteFormViewModel.checkDate(et_endDate.getText().toString())) {
                    if (addNewWorksiteFormViewModel.isDatesValid(et_startDate.getText().toString(), et_endDate.getText().toString())) {
                        worksite = new Worksite(et_worksiteName.getText().toString(), longStartDate, longEndDate, et_location.getText().toString(), 0);
                        if (addNewWorksiteFormViewModel.checkSameWorksiteName(et_worksiteName.getText().toString())) {
                            addNewWorksiteFormViewModel.addWorksite(worksite);
                            bt_add.setEnabled(false);
                        } else {
                            showDialog();
                        }
                    } else {
                        Toast.makeText(requireContext(), R.string.toast_dateWrong, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.toast_dateWrong, Toast.LENGTH_SHORT).show();
                }

            }
        });

        et_worksiteName.addTextChangedListener(new TextWatcher() {
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

        et_endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addNewWorksiteFormViewModel.onEndDateChanged(editable.toString());
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.dialog_title_caution).setMessage(R.string.dialog_message_choose);

        builder.setPositiveButton(R.string.dialog_positive_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addNewWorksiteFormViewModel.addWorksite(worksite);
                bt_add.setEnabled(false);
            }
        });

        builder.setNegativeButton(R.string.dialog_negative_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                et_worksiteName.setText(null);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
