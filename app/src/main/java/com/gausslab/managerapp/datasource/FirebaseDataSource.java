package com.gausslab.managerapp.datasource;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gausslab.managerapp.model.AccidentHistory;
import com.gausslab.managerapp.model.Notice;
import com.gausslab.managerapp.model.Result;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

enum KeyType {
    WORKSITE,
    NOTICE,
    ACCIDENT_HISTORY
}

public class FirebaseDataSource implements DataSource {
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void addWorksite(Worksite toAdd, CompletedCallback<Result<String>> callback) {
        getNewKey(KeyType.WORKSITE, new CompletedCallback<Result<String>>() {
            @Override
            public void onComplete(Result<String> result) {
                if (result instanceof Result.Success) {
                    String keyValue = ((Result.Success<String>) result).getData();
                    toAdd.setKeyValue(keyValue);
                    db.collection("worksite")
                            .document(keyValue)
                            .set(toAdd);
                    callback.onComplete(new Result.Success<String>("Success"));
                }
            }
        });
    }

    @Override
    public void addOrUpdateUser(User user, CompletedCallback<Result<String>> callback) {
        if (user.getPhoneNumber().isEmpty()) {
            db.collection("user")
                    .document("Guest_" + user.getUserName())
                    .set(user);
        } else {
            db.collection("user")
                    .document(user.getPhoneNumber())
                    .set(user);
        }
        callback.onComplete(new Result.Success<String>("Success"));
    }

    @Override
    public void addNotice(Notice notice, CompletedCallback<Result<String>> callback) {
        getNewKey(KeyType.NOTICE, new CompletedCallback<Result<String>>() {
            @Override
            public void onComplete(Result<String> result) {
                if (result instanceof Result.Success) {
                    String keyValue = ((Result.Success<String>) result).getData();
                    notice.setKeyValue(keyValue);
                    db.collection("notice")
                            .document(keyValue)
                            .set(notice);
                    callback.onComplete(new Result.Success<String>("Success"));
                }
            }
        });
    }

    @Override
    public void addAccidentHistory(AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback) {
        getNewKey(KeyType.ACCIDENT_HISTORY, new CompletedCallback<Result<String>>() {
            @Override
            public void onComplete(Result<String> result) {
                if (result instanceof Result.Success) {
                    String keyValue = ((Result.Success<String>) result).getData();
                    accidentHistory.setKeyValue(keyValue);
                    db.collection("accidenthistory")
                            .document(keyValue)
                            .set(accidentHistory);
                    callback.onComplete(new Result.Success<String>("Success"));
                }
            }
        });
    }

    @Override
    public void getTodayWorksiteList(String todayCal, CompletedCallback<Result<List<Worksite>>> callback) {
        db.collection("worksite")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<Worksite> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (DocumentSnapshot snap : snaps) {
                                String parsedStringStartDate = parseDate(snap.getString("startDate"));
                                String parsedStringEndDate = parseDate(snap.getString("endDate"));
                                if ((Integer.parseInt(parsedStringStartDate) <= Integer.parseInt(todayCal)) && (Integer.parseInt(parsedStringEndDate) >= Integer.parseInt(todayCal))) {
                                    Worksite toAdd = new Worksite((snap.getString("worksiteName")), snap.getString("startDate"), snap.getString("endDate"), snap.getString("location"), snap.getString("keyValue"));
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
    public void getUserListByWorksite(String keyValue, ListenerCallback<Result<List<User>>> callback) {
        db.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<User> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (DocumentSnapshot snap : snaps) {
                                if (snap.getString("worksiteName").equals(keyValue)) {
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
    public void noPhoneNumberGetUser(String userName, CompletedCallback<Result<User>> callback) {
        db.collection("user")
                .whereEqualTo("userName", userName)
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
    public void getPhoneNumberList(CompletedCallback<Result<List<String>>> callback) {
        db.collection("user")
                .get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        List<String> toReturn = new ArrayList<>();
                        List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                        for (DocumentSnapshot snap : snaps) {
                            toReturn.add(snap.getString("phoneNumber"));
                        }
                        callback.onComplete(new Result.Success<List<String>>(toReturn));
                    }
                    callback.onComplete(new Result.Error(new Exception("error")));
                });
    }

    @Override
    public void getNoticeList(ListenerCallback<Result<List<Notice>>> callback) {
        db.collection("notice")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<Notice> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (DocumentSnapshot snap : snaps) {
                                Notice toAdd = snap.toObject(Notice.class);
                                toReturn.add(toAdd);
                            }
                            callback.onUpdate(new Result.Success<List<Notice>>(toReturn));
                        } else {
                            callback.onUpdate(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }

    @Override
    public void deleteNotice(String keyValue, CompletedCallback<Result<String>> callback) {
        db.collection("notice")
                .document(keyValue)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onComplete(new Result.Success<String>("Success"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(new Result.Error(new Exception("Failed")));
                    }
                });
    }

    @Override
    public void getNoticeDetailByName(String keyValue, CompletedCallback<Result<Notice>> callback) {
        db.collection("notice")
                .whereEqualTo("keyValue", keyValue)
                .get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> snaps = task.getResult().getDocuments();
                        callback.onComplete(new Result.Success<Notice>(snaps.get(0).toObject(Notice.class)));
                    } else {
                        callback.onComplete(new Result.Error(new Exception("error")));
                    }
                });
    }

    @Override
    public void getAccidentHistoryByUser(String phoneNumber, ListenerCallback<Result<List<AccidentHistory>>> callback) {
        db.collection("accidenthistory")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<AccidentHistory> toReturn = new ArrayList<>();
                            List<DocumentSnapshot> snaps = value.getDocuments();
                            for (DocumentSnapshot snap : snaps) {
                                AccidentHistory toAdd = snap.toObject(AccidentHistory.class);
                                if (toAdd.getUserPhoneNumber().equals(phoneNumber)) {
                                    toReturn.add(toAdd);
                                }
                            }
                            callback.onUpdate(new Result.Success<List<AccidentHistory>>(toReturn));
                        } else {
                            callback.onUpdate(new Result.Error(new Exception("error")));
                        }
                    }
                });
    }

    @Override
    public void getAccidentHistoryByKey(String key, CompletedCallback<Result<AccidentHistory>> callback) {
        db.collection("accidenthistory")
                .whereEqualTo("keyValue", key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AccidentHistory toReturn = task.getResult().getDocuments().get(0).toObject(AccidentHistory.class);
                            callback.onComplete(new Result.Success<AccidentHistory>(toReturn));
                        } else {
                            callback.onComplete(new Result.Error(task.getException()));
                        }
                    }
                });
    }

    @Override
    public void getWorksiteByKey(String key, CompletedCallback<Result<Worksite>> callback) {
        db.collection("worksite")
                .whereEqualTo("keyValue", key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Worksite toReturn = task.getResult().getDocuments().get(0).toObject(Worksite.class);
                            callback.onComplete(new Result.Success<Worksite>(toReturn));
                        } else {
                            callback.onComplete(new Result.Error(task.getException()));
                        }
                    }
                });
    }

    @Override
    public void deleteAccidentHistory(String keyValue, CompletedCallback<Result<String>> callback) {
        db.collection("accidenthistory")
                .document(keyValue)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onComplete(new Result.Success<String>("Success"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(new Result.Error(new Exception("Failed")));
                    }
                });
    }

    @Override
    public void changeAccidentHistory(AccidentHistory accidentHistory, CompletedCallback<Result<String>> callback) {
        db.collection("accidenthistory")
                .document(accidentHistory.getKeyValue())
                .set(accidentHistory);
        callback.onComplete(new Result.Success<String>("Success"));
    }

    @Override
    public void changeNotice(Notice notice, CompletedCallback<Result<String>> callback) {
        db.collection("notice")
                .document(notice.getKeyValue())
                .set(notice);
        callback.onComplete(new Result.Success<String>("Success"));
    }

    private void getNewKey(KeyType type, CompletedCallback<Result<String>> callback) {
        DocumentReference docRef = null;
        switch (type) {
            case WORKSITE:
                docRef = db.collection("worksiteKey").document("worksiteKey");
                break;
            case NOTICE:
                docRef = db.collection("noticeKey").document("noticeKey");
                break;
            case ACCIDENT_HISTORY:
                docRef = db.collection("accidenthistorykey").document("accidenthistorykey");
                break;
        }
        if (docRef != null) {
            DocumentReference finalDocRef = docRef;
            db.runTransaction(new Transaction.Function<String>() {
                @Nullable
                @Override
                public String apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    int currKey = transaction.get(finalDocRef).getDouble("key").intValue();
                    transaction.update(finalDocRef, "key", currKey + 1);
                    return "" + currKey;
                }
            }).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String key) {
                    callback.onComplete(new Result.Success<String>(key));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onComplete(new Result.Error(e));
                }
            });
        }
    }
}
