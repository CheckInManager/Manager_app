package com.gausslab.managerapp.accidenthistorydetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentAccidenthistorydetailBinding;

public class AccidentHistoryDetailFragment extends Fragment {
    private FragmentAccidenthistorydetailBinding binding;
    private AccidentHistoryDetailViewModel accidentHistoryDetailViewModel;
    private EditText et_description;
    private EditText et_place;
    private EditText et_date;
    private EditText et_time;


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

        return binding.getRoot();
    }
}