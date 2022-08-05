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
                            for (DocumentSnapshot snap:snaps) {
                                String parsedStringStartDate = parseDate(snap.getString("startDate"));
                                String parsedStringEndDate = parseDate(snap.getString("endDate"));
                                if ((Integer.parseInt(parsedStringStartDate) <= Integer.parseInt(todayCal)) && (Integer.parseInt(parsedStringEndDate) >= Integer.parseInt(todayCal))) {
                                    Worksite toAdd = new Worksite((snap.getString("worksiteName")), snap.getString("startDate"), snap.getString("endDate"), snap.getString("location"));
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

    public String parseDate(String date) {
        String[] splitDate = date.split("/");
        if (splitDate[1].length() < 2) {
            splitDate[1] = "0" + splitDate[1];
        }
        if (splitDate[2].length() < 2) {
            splitDate[2] = "0" + splitDate[2];
        }
        String strDate = String.join("", splitDate);
        return strDate;
    }


    @Override
    public void addWorksite(Worksite toAdd, CompletedCallback<Result<String>> callback) {
        db.collection("worksite")
                .add(toAdd);
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
    public void getUserListByWorksite(String worksiteName, ListenerCallback<Result<List<User>>> callback) {
        db.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<User> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (DocumentSnapshot snap:snaps) {
                                if (snap.getString("worksiteName").equals(worksiteName)) {
                                    User toAdd = snap.toObject(User.class);
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
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                            callback.onComplete(new Result.Success<User>(snaps.get(0).toObject(User.class)));
                        } else {
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
    public void addUser(User userToAdd, CompletedCallback<Result<String>> callback) {
        db.collection("user")
                .document(userToAdd.getPhoneNumber())
                .set(userToAdd);
        callback.onComplete(new Result.Success<String>("Success"));
    }

    @Override
    public void addGuestUser(User userToAdd, CompletedCallback<Result<String>> callback) {
        db.collection("user")
                .document("Guest_"+userToAdd.getUserName())
                .set(userToAdd);
        callback.onComplete(new Result.Success<String>("Success"));
    }

    @Override
    public void getPhoneNumberList(CompletedCallback<Result<List<String>>> callback) {
        db.collection("user")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> toReturn = new ArrayList<>();
                        List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                        for (DocumentSnapshot snap: snaps) {
                            toReturn.add(snap.getString("phoneNumber"));
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
                    if (task.isSuccessful()) {
                        List<String> toReturn = new ArrayList<>();
                        List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                        for (DocumentSnapshot snap:snaps) {
                            toReturn.add(snap.getString("worksiteName"));
                        }
                        callback.onComplete(new Result.Success<List<String>>(toReturn));
                    }
                    callback.onComplete(new Result.Error(new Exception("error")));
                });
    }
}
