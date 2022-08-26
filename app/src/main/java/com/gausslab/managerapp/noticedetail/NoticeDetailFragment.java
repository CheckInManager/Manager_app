package com.gausslab.managerapp.noticedetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gausslab.managerapp.Event;
import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentNoticedetailBinding;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Worksite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoticeDetailFragment extends Fragment {
    private FragmentNoticedetailBinding binding;
    private NoticeDetailViewModel noticeDetailViewModel;
    private EditText et_noticeName;
    private EditText et_memo;
    private Spinner sp_worksiteName;
    private Button bt_ok;

    private String todayCal;
    private ArrayAdapter arrayAdapter;
    private String loadedNoticeName;
    private String loadedMemo;
    private String loadedWorksiteName;
    private String loadedKeyValue;
    private String loadedTime;

    public NoticeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticeDetailViewModel = new ViewModelProvider(requireActivity()).get(NoticeDetailViewModel.class);

        loadedKeyValue = NoticeDetailFragmentArgs.fromBundle(getArguments()).getKeyValue();
        noticeDetailViewModel.loadNoticeDetail(loadedKeyValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoticedetailBinding.inflate(inflater, container, false);

        et_noticeName = binding.noticedetailEtNoticeName;
        et_memo = binding.noticedetailEtMemo;
        sp_worksiteName = binding.noticedetilaSpWorksiteName;
        bt_ok = binding.noticedetailBtOk;
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Observer
        noticeDetailViewModel.isNoticeDetailLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    Notice currNotice = noticeDetailViewModel.getNoticeDetail();
                    loadedNoticeName = currNotice.getNoticeName();
                    loadedMemo = currNotice.getMemo();
                    loadedWorksiteName = currNotice.getWorksiteName();
                    loadedTime = currNotice.getTime();
                    et_noticeName.setText(currNotice.getNoticeName());
                    et_memo.setText(currNotice.getMemo());
                    sp_worksiteName.setSelection(getIndex(sp_worksiteName, currNotice.getWorksiteName()));
                }
            }
        });

        noticeDetailViewModel.openWorksiteListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    List<String> toReturn = new ArrayList<>();
                    List<Worksite> openWorksiteList = noticeDetailViewModel.getOpenWorksite();
                    for (int i = 0; i < openWorksiteList.size(); i++) {
                        toReturn.add(openWorksiteList.get(i).getWorksiteName());
                    }
                    arrayAdapter = new ArrayAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, toReturn);
                    sp_worksiteName.setAdapter(arrayAdapter);
                }
            }
        });

        noticeDetailViewModel.isNoticeUpdateSuccessful().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if (!booleanEvent.isHandled()) {
                    boolean isSuccessful = booleanEvent.consumeData();
                    if (isSuccessful) {
                        NavHostFragment.findNavController(NoticeDetailFragment.this).navigate(R.id.action_noticeDetailFragment_to_noticeFragment);
                    }
                }
            }
        });

        noticeDetailViewModel.isChangedSpinnerString().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isChanged) {
                if(isChanged){
                    if (et_noticeName.getText().toString().length() < 1) {
                        Toast.makeText(requireContext(), R.string.toast_noticeNameEmpty, Toast.LENGTH_SHORT).show();
                    } else {
                        if ((loadedNoticeName + loadedMemo + loadedWorksiteName).equals(
                                (et_noticeName.getText().toString() + et_memo.getText().toString() + sp_worksiteName.getSelectedItem().toString()))) {
                            NavHostFragment.findNavController(NoticeDetailFragment.this).navigateUp();
                        } else {
                            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            long mNow = System.currentTimeMillis();
                            Date mDate = new Date(mNow);
                            Notice notice = new Notice(et_noticeName.getText().toString(), et_memo.getText().toString(),noticeDetailViewModel.getWorksiteKeyValue()
                                    , mFormat.format(mDate), loadedKeyValue, sp_worksiteName.getSelectedItem().toString());
                            noticeDetailViewModel.changeNotice(notice);
                        }
                        noticeDetailViewModel.changeState();
                    }
                }
            }
        });
        //endregion

        //region Listener
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDetailViewModel.changeSpinnerStringToKeyValue(sp_worksiteName.getSelectedItem().toString());
            }
        });

        et_noticeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                noticeDetailViewModel.updateNoticeNameText(s.toString());
            }
        });

        et_memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                noticeDetailViewModel.updateMemoText(s.toString());
            }
        });
        //endregion
    }

    private void init() {
        convertDateFormat();
        noticeDetailViewModel.loadOpenWorksite(todayCal);

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

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

}