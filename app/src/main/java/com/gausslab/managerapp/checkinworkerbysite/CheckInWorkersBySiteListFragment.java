package com.gausslab.managerapp.checkinworkerbysite;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.todayworksite.OnTodayWorksiteContextMenuInteractionListener;

import java.util.List;


public class CheckInWorkersBySiteListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private List<User> userList;
    private CheckInWorkersBySiteViewModel checkInWorkersBySiteViewModel;
    private CheckInWorkersBySiteRecyclerViewAdapter adapter;

    public CheckInWorkersBySiteListFragment() {
    }

    public CheckInWorkersBySiteListFragment(List<User> userList){this.userList = userList;}

    public static CheckInWorkersBySiteListFragment newInstance(int columnCount, List<User> userList) {
        CheckInWorkersBySiteListFragment fragment = new CheckInWorkersBySiteListFragment(userList);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkinworkersbysite_list, container, false);
        checkInWorkersBySiteViewModel = new ViewModelProvider(requireActivity()).get(CheckInWorkersBySiteViewModel.class);

        if(view instanceof RecyclerView){
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if(mColumnCount<=1){
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(context,mColumnCount));
            }
            adapter = new CheckInWorkersBySiteRecyclerViewAdapter(userList, new ViewModelProvider(requireActivity()).get(CheckInWorkersBySiteViewModel.class),new OnTodayWorksiteContextMenuInteractionListener<User>(){
                @Override
                public void onItemClick(User obj) {
                    String phoneNumber = obj.getPhoneNumber();
                    CheckInWorkersBySiteFragmentDirections.ActionCheckInWorkdersBySiteFragmentToUserInformationFragment action = CheckInWorkersBySiteFragmentDirections.actionCheckInWorkdersBySiteFragmentToUserInformationFragment();
                    action.setPhoneNumber(phoneNumber);
                    NavHostFragment.findNavController(CheckInWorkersBySiteListFragment.this).navigate(action);
                }

                @Override
                public void onContextReturnWorksite(User obj) {

                }
            });
            recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkInWorkersBySiteViewModel.userListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if(isLoaded){
                    adapter.setUserList(checkInWorkersBySiteViewModel.getUserByWorksite());
                }
            }
        });
    }
}