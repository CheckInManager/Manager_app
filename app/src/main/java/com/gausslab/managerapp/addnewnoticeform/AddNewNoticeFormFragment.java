package com.gausslab.managerapp.addnewnoticeform;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentAddnewnoticeformBinding;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Worksite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNewNoticeFormFragment extends Fragment {
    private FragmentAddnewnoticeformBinding binding;
    private AddNewNoticeFormViewModel addNewNoticeFormViewModel;
    private EditText et_noticeName;
    private EditText et_memo;
    private Spinner sp_worksiteName;
    private Button bt_add;

    private String todayCal;

    public AddNewNoticeFormFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewNoticeFormViewModel = new ViewModelProvider(this).get(AddNewNoticeFormViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddnewnoticeformBinding.inflate(inflater, container, false);
        et_noticeName = binding.addnewnoticeformEtNoticeName;
        et_memo = binding.addnewnoticeformEtMemo;
        sp_worksiteName = binding.addnewnoticeformSpWorksiteSpinner;
        bt_add = binding.addnewnoticeBtAdd;

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        addNewNoticeFormViewModel.isAddNoticeFormSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSaved) {
                if (isSaved) {
                    NavHostFragment.findNavController(AddNewNoticeFormFragment.this).navigateUp();
                }
            }
        });

        addNewNoticeFormViewModel.openWorksiteListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    List<String> toReturn = new ArrayList<>();
                    List<Worksite> openWorksiteList = addNewNoticeFormViewModel.getOpenWorksite();
                    for (int i = 0; i < openWorksiteList.size(); i++) {
                        toReturn.add(openWorksiteList.get(i).getWorksiteName());
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, toReturn);
                    sp_worksiteName.setAdapter(arrayAdapter);
                }
            }
        });

//        addNewNoticeFormViewModel.isChangedSpinnerString().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isChanged) {
//                if(isChanged){
//                    if (et_noticeName.getText().toString().length() < 1) {
//                        Toast.makeText(requireContext(), R.string.toast_noticeNameEmpty, Toast.LENGTH_SHORT).show();
//                    } else {
//                        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                        long mNow = System.currentTimeMillis();
//                        Date mDate = new Date(mNow);
//                        Notice notice = new Notice(et_noticeName.getText().toString(), et_memo.getText().toString(), addNewNoticeFormViewModel.getWorksiteKeyValue(),
//                                mFormat.format(mDate), null,sp_worksiteName.getSelectedItem().toString());
//                        addNewNoticeFormViewModel.addNotice(notice);
//                    }
//                }
//            }
//        });
        //endregion

        //region Listener
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Worksite selectedWorksite = addNewNoticeFormViewModel.getOpenWorksite().get(sp_worksiteName.getSelectedItemPosition());
                addNewNoticeFormViewModel.addNotice(new Notice(0, et_noticeName.getText().toString(), et_memo.getText().toString(), selectedWorksite, 0,selectedWorksite.getId()));
                bt_add.setEnabled(false);
            }
        });
        //endregion
    }

    private void init() {
        convertDateFormat();
        addNewNoticeFormViewModel.loadOpenWorksite(todayCal);
    }

    private void convertDateFormat() {
        Calendar cal = Calendar.getInstance();
        todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));

        String todayMonthCal = ((cal.get(Calendar.MONTH) + 1) + "");
        String todayDayCal = ((cal.get(Calendar.DATE)) + "");

        if (todayMonthCal.length() < 2 && todayDayCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        } else if (todayMonthCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));
        } else if (todayDayCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        }
    }
}