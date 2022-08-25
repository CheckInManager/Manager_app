package com.gausslab.managerapp.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentMapBinding;


public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private ImageView iv_map;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_notice;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        iv_map = binding.mapIvMap;
        bt_worksite = binding.addworkerBtWorksite;
        bt_addWorker = binding.addworkerBtAddWorker;
        bt_notice = binding.addworkerBtAddNotice;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Listener
        bt_worksite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MapFragment.this).navigate(R.id.action_mapFragment_to_todayWorkSiteFragment);
            }
        });

        bt_addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MapFragment.this).navigate(R.id.action_mapFragment_to_addWorkerFragment);
            }
        });

        bt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MapFragment.this).navigate(R.id.action_mapFragment_to_noticeFragment);
            }
        });
        //endregion
    }
}