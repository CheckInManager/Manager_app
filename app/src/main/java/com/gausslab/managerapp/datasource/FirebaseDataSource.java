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

public class FirebaseDataSource implements DataSource {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    public void getTodayWorksiteList(String todayCal, CompletedCallback<Result<List<Worksite>>> callback) {
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
                                if ((Integer.parseInt(parsingStringStartDate) <= Integer.parseInt(todayCal)) && (Integer.parseInt(parsingStringLastDate) >= Integer.parseInt(todayCal))) {
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
    public void addWorksite(Worksite toAdd, CompletedCallback<Result<String>> callback) {
        Map<String, Object> worksite = new HashMap<String, Object>();
        worksite.put("workName", toAdd.getWorkName());
        worksite.put("startDate", toAdd.getStartDate());
        worksite.put("lastDate", toAdd.getLastDate());
        worksite.put("location", toAdd.getLocation());
        db.collection("worksite")
                .add(worksite);
        callback.onComplete(new Result.Success<String>("Success"));
    }


    public void uploadFile(File toUpload, String destination, CompletedCallback<Result<Uri>> callback) {
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

    public void downloadFile(String downloadPath, File localFile, CompletedCallback<Result<File>> callback) {
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

    @Override
    public void getUsersByWorksite(String worksiteName, ListenerCallback<Result<List<User>>> callback) {
        db.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<User> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (int i = 0; i < snaps.size(); i++) {
                                if (snaps.get(i).getString("worksiteName").equals(worksiteName)) {
                                    User toAdd = new User(snaps.get(i).getString("phoneNumber"), snaps.get(i).getString("password"), snaps.get(i).getString("userName"), snaps.get(i).getString("userImage"), snaps.get(i).getString("career"), snaps.get(i).getString("worksiteName"), snaps.get(i).getString("accidentHistory"), snaps.get(i).getString("memo"));
                                    toReturn.add(toAdd);
                                }
                            }
                            callback.onUpdate(new Result.Success<List<User>>(toReturn));
                        } else {
                            callback.onUpdate(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }

    @Override
    public void getUserByPhoneNumber(String phoneNumber, CompletedCallback<Result<User>> callback) {
        db.collection("user")
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                            callback.onComplete(new Result.Success<User>(snaps.get(0).toObject(User.class)));
                        }
                        else
                        {
                            callback.onComplete(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }

    @Override
    public void changeUserInformation(User changeUserInformation, CompletedCallback<Result<String>> callback) {
        db.collection("user")
                .document(changeUserInformation.getPhoneNumber())
                .set(changeUserInformation);
        callback.onComplete(new Result.Success<String>("Success"));
    }

    @Override
    public void addUser(User addNewUser, CompletedCallback<Result<String>> callback) {
        Map<String, Object> newUser = new HashMap<String,Object>();
        newUser.put("accidentHistory",addNewUser.getAccidentHistory());
        newUser.put("memo",addNewUser.getMemo());
        newUser.put("password",addNewUser.getPassword());
        newUser.put("phoneNumber",addNewUser.getPhoneNumber());
        newUser.put("userImage",addNewUser.getUserImage());
        newUser.put("userName",addNewUser.getUserName());
        newUser.put("worksiteName",addNewUser.getWorksiteName());
        db.collection("user")
                .document(addNewUser.getPhoneNumber())
                .set(newUser);
        callback.onComplete(new Result.Success<String>("Success"));
    }

    @Override
    public void getPhoneNumberList(CompletedCallback<Result<List<String>>> callback) {
        db.collection("user")
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       List<String> toReturn = new ArrayList<>();
                       List<DocumentSnapshot> snaps= task.getResult().getDocuments();
                       for(int i=0;i<snaps.size();i++){
                           toReturn.add(snaps.get(i).getString("phoneNumber"));
                       }
                       callback.onComplete(new Result.Success<List<String>>(toReturn));
                   }
                   callback.onComplete(new Result.Error(new Exception("error")));
                });
    }

    @Override
    public void loadWorksiteNameList(CompletedCallback<Result<List<String>>> callback) {
        db.collection("worksite")
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       List<String> toReturn = new ArrayList<>();
                       List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                       for(int i=0;i<snaps.size();i++){
                           toReturn.add(snaps.get(i).getString("workName"));
                       }
                       callback.onComplete(new Result.Success<List<String>>(toReturn));
                   }
                   callback.onComplete(new Result.Error(new Exception("error")));
                });
    }
}
