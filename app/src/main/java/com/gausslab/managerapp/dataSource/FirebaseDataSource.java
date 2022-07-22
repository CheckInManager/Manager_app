package com.gausslab.managerapp.dataSource;

import androidx.annotation.Nullable;

import com.gausslab.managerapp.R;
import com.gausslab.managerapp.Result;
import com.gausslab.managerapp.Worksite;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDataSource implements DataSource {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getTodayWorksite(DataSourceCallback<Result> callback) {
        db.collection("worksite")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error==null){
                            List<Worksite> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for(int i=0;i<snaps.size();i++){
                                Worksite toAdd = new Worksite((snaps.get(i).getString("workName")),snaps.get(i).getString("startDate"),snaps.get(i).getString("lastDate"),snaps.get(i).getString("location"));
                                toReturn.add(toAdd);
                            }
                            callback.onComplete(new Result.Success<List<Worksite>>(toReturn));
                        }else{
                            callback.onComplete(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }

    @Override
    public void addWorksite(Worksite toAdd, DataSourceCallback<Result> callback) {
        Map<String, Object> worksite = new HashMap<String, Object>();
        worksite.put("workName", toAdd.getWorkName());
        worksite.put("startDate", toAdd.getStartDate());
        worksite.put("lastDate",toAdd.getLastDate());
        worksite.put("location", toAdd.getLocation());
        db.collection("worksite")
                .add(worksite);
        callback.onComplete(new Result.Success<String>("Success"));
    }
}
