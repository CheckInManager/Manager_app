package com.gausslab.managerapp.noticedetail;

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
import android.widget.EditText;
import android.widget.TextView;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentNoticedetailBinding;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.notice.NoticeFragment;

public class NoticeDetailFragment extends Fragment {
    private FragmentNoticedetailBinding binding;
    private NoticeDetailViewModel noticeDetailViewModel;
    private TextView tv_noticeName;
    private TextView tv_memo;
    private TextView tv_worksiteName;
    private Button bt_ok;


    public NoticeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticeDetailViewModel = new ViewModelProvider(requireActivity()).get(NoticeDetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoticedetailBinding.inflate(inflater, container, false);

        tv_noticeName = binding.noticedetailTvNoticeName;
        tv_memo = binding.noticedetailTvMemo;
        tv_worksiteName = binding.noticedetailTvWorksiteName;
        bt_ok = binding.noticedetailBtOk;
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Listener
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NoticeDetailFragment.this).navigate(R.id.action_noticeDetailFragment_to_noticeFragment);
            }
        });
        //endregion
    }

    private void init() {
        String noticeName = NoticeDetailFragmentArgs.fromBundle(getArguments()).getNoticeName();
        String worksiteName = NoticeDetailFragmentArgs.fromBundle(getArguments()).getWorksiteName();
        noticeDetailViewModel.loadNoticeDetail(noticeName, worksiteName);
        noticeDetailViewModel.isNoticeDetailLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    Notice currNotice = noticeDetailViewModel.getNoticeDetail();
                    tv_noticeName.setText(currNotice.getNoticeName());
                    tv_memo.setText(currNotice.getMemo());
                    tv_worksiteName.setText(currNotice.getWorksiteName());
                }
            }
        });
    }
}