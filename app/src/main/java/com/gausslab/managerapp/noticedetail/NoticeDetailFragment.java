package com.gausslab.managerapp.noticedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentNoticedetailBinding;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Worksite;

import java.util.ArrayList;
import java.util.List;

public class NoticeDetailFragment extends Fragment {
    private FragmentNoticedetailBinding binding;
    private NoticeDetailViewModel noticeDetailViewModel;

    private EditText et_noticeName;
    private EditText et_memo;
    private Spinner sp_worksiteName;
    private Button bt_ok;

    public NoticeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticeDetailViewModel = new ViewModelProvider(this).get(NoticeDetailViewModel.class);
        long loadedKeyValue = NoticeDetailFragmentArgs.fromBundle(getArguments()).getKeyValue();
        noticeDetailViewModel.setCurrNoticeId(loadedKeyValue); //Loading 한다
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoticedetailBinding.inflate(inflater, container, false);
        et_noticeName = binding.noticedetailEtNoticeName;
        et_memo = binding.noticedetailEtMemo;
        sp_worksiteName = binding.noticedetilaSpWorksiteName;
        bt_ok = binding.noticedetailBtOk;
        bt_ok.setEnabled(false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //region Observer
        noticeDetailViewModel.isDataLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    Notice currNotice = noticeDetailViewModel.getCurrNotice();
                    et_noticeName.setText(currNotice.getTitle());
                    et_memo.setText(currNotice.getContent());

                    setupAdapter();

                    bt_ok.setEnabled(true);
                }

            }
        });

        noticeDetailViewModel.isWorksiteUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated)
                    NavHostFragment.findNavController(NoticeDetailFragment.this).navigate(R.id.action_noticeDetailFragment_to_noticeFragment);
            }
        });
        //endregion

        //region Listener
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedIndex = sp_worksiteName.getSelectedItemPosition();
                Worksite selectedWorksite = noticeDetailViewModel.getWorksiteList().get(selectedIndex);
                noticeDetailViewModel.updateNotice(et_noticeName.getText().toString(), et_memo.getText().toString(), selectedWorksite);
            }
        });

        //endregion
    }

    private void setupAdapter() {
        Notice currNotice = noticeDetailViewModel.getCurrNotice();
        List<Worksite> worksiteList = noticeDetailViewModel.getWorksiteList();
        List<String> worksiteNameList = new ArrayList<>();
        for (Worksite worksite : worksiteList) {
            worksiteNameList.add(worksite.getWorksiteName());
        }
        sp_worksiteName.setAdapter(new ArrayAdapter<String>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, worksiteNameList));
        int initSelectionIndex = getIndex(sp_worksiteName, currNotice.getWorksite().getWorksiteName());
        sp_worksiteName.setSelection(initSelectionIndex);
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