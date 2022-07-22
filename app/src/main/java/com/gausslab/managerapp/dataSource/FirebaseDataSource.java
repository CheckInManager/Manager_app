package com.gausslab.managerapp.dataSource;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.Worksite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDataSource implements DataSource {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

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
        worksite.put("lastDate", toAdd.getLastDate());
        worksite.put("location", toAdd.getLocation());
        db.collection("worksite")
                .add(worksite);
        callback.onComplete(new Result.Success<String>("Success"));
    }


    public void uploadFile(File toUpload, String destination, DataSourceCallback<Result<Uri>> callback)
    {
        Log.d("DEBUG:DataSource", "uploadFile: " + toUpload.getName() + " to " + destination);
        Uri localFile = Uri.fromFile(toUpload);
        StorageReference storageReference = firebaseStorage.getReference().child(destination);
        UploadTask uploadTask = storageReference.putFile(localFile);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            Uri result = task.getResult();
                            callback.onComplete(new Result.Success<Uri>(result));
                        }
                        else
                        {

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                callback.onComplete(new Result.Error(e));
                Log.d("DEBUG", "DataSource: storeImage() failed!");
            }
        });
    }
}
