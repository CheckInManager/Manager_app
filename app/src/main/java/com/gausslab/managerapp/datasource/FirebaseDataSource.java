package com.gausslab.managerapp.datasource;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseDataSource implements DataSource {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    public void getTodayWorksite(String todayCal, DataSourceCallback<Result> callback) {
        db.collection("worksite")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<Worksite> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (int i = 0; i < snaps.size(); i++) {
                                String parsingStringStartDate = parsingDate(snaps.get(i).getString("startDate"));
                                String parsingStringLastDate = parsingDate(snaps.get(i).getString("lastDate"));
                                if((Integer.parseInt(parsingStringStartDate)<=Integer.parseInt(todayCal))&&(Integer.parseInt(parsingStringLastDate)>=Integer.parseInt(todayCal))){
                                    Worksite toAdd = new Worksite((snaps.get(i).getString("workName")), snaps.get(i).getString("startDate"), snaps.get(i).getString("lastDate"), snaps.get(i).getString("location"));
                                    toReturn.add(toAdd);
                                }
                            }
                            callback.onComplete(new Result.Success<List<Worksite>>(toReturn));
                        } else {
                            callback.onComplete(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }

    public String parsingDate(String date) {
        String[] splitDate = date.split("/");
        String strDate = String.join("", splitDate);
        return strDate;
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


    public void uploadFile(File toUpload, String destination, DataSourceCallback<Result<Uri>> callback) {
        Log.d("DEBUG:DataSource", "uploadFile: " + toUpload.getName() + " to " + destination);
        Uri localFile = Uri.fromFile(toUpload);
        StorageReference storageReference = firebaseStorage.getReference().child(destination);
        UploadTask uploadTask = storageReference.putFile(localFile);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri result = task.getResult();
                            callback.onComplete(new Result.Success<Uri>(result));
                        } else {

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onComplete(new Result.Error(e));
                Log.d("DEBUG", "DataSource: storeImage() failed!");
            }
        });
    }

    public void downloadFile(String downloadPath, File localFile, DataSourceCallback<Result> callback) {
        Log.d("DEBUG:DataSource", "downloadFile: " + downloadPath);
        StorageReference ref = firebaseStorage.getReference().child(downloadPath);
        ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onComplete(new Result.Success<File>(localFile));
                } else {
                    callback.onComplete(new Result.Error(task.getException()));
                }
            }
        });
    }

    public void getDocumentsFromCollection(String collectionName, DataSourceListenerCallback<Result> callback) {
        Log.d("DEBUG:DataSource", "getDocumentsFromCollection");
        db.collection(collectionName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error==null){
                    Map<String, Worksite> newMap = new HashMap<>();
                    List<Worksite> toReturn = new ArrayList<>();
                    List<DocumentSnapshot> snaps = value.getDocuments();
                    for(int i=0;i<snaps.size();i++){
                        Worksite toAdd = snaps.get(i).toObject(Worksite.class);
                        toReturn.add(toAdd);
                        newMap.put(toAdd.getWorkName(),toAdd);
                    }
                    callback.onUpdate(new Result.Success<Map<String, Worksite>>(newMap));
                }else{
                    callback.onUpdate(new Result.Error(new Exception("error")));
                }
            }
        });
    }

    @Override
    public void getUserByWorksite(String worksiteName, DataSourceCallback<Result> callback) {
        db.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<User> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (int i = 0; i < snaps.size(); i++) {
                                if(snaps.get(i).getString("worksiteName").equals(worksiteName)){
                                    User toAdd = new User(snaps.get(i).getString("phoneNumber"), snaps.get(i).getString("password"),snaps.get(i).getString("userName"),snaps.get(i).getString("userImage"),snaps.get(i).getString("userName"),snaps.get(i).getString("worksiteName"));
                                    toReturn.add(toAdd);
                                }
                            }
                            callback.onComplete(new Result.Success<List<User>>(toReturn));
                        } else {
                            callback.onComplete(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }
}
