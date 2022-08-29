package com.gausslab.managerapp.addworker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gausslab.managerapp.App;
import com.gausslab.managerapp.FileService;
import com.gausslab.managerapp.R;
import com.gausslab.managerapp.databinding.FragmentAddworkerBinding;
import com.gausslab.managerapp.model.User;
import com.gausslab.managerapp.model.Worksite;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddWorkerFragment extends Fragment {
    private FragmentAddworkerBinding binding;
    private AddWorkerViewModel addWorkerViewModel;
    private ImageView iv_image;
    private EditText et_name;
    private EditText et_phoneNumber;
    private EditText et_memo;
    private Spinner sp_worksiteSpinner;
    private Button bt_add;
    private Button bt_takePicture;
    private Button bt_importPicture;
    private Uri selectedImage;
    private Button bt_worksite;
    private Button bt_addWorker;
    private Button bt_notice;
    private Button bt_map;

    private List<Worksite> openWorksiteList;
    private String todayCal;
    private User userToAdd;
    private File imageFile;
    private Bitmap changeBitmap;

    public AddWorkerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addWorkerViewModel = new ViewModelProvider(this).get(AddWorkerViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddworkerBinding.inflate(inflater, container, false);
        iv_image = binding.addworkerIvImage;
        et_name = binding.addworkerEtName;
        et_phoneNumber = binding.addworkerEtPhoneNumber;
        et_memo = binding.addworkerEtMemo;
        sp_worksiteSpinner = binding.addworkerSpWorksiteSpinner;
        bt_add = binding.addworkerBtAdd;
        bt_takePicture = binding.addworkerBtTakePicture;
        bt_importPicture = binding.addworkerBtImportPicture;
        bt_worksite = binding.addworkerBtWorksite;
        bt_addWorker = binding.addworkerBtAddWorker;
        bt_notice = binding.addworkerBtAddNotice;
        bt_map = binding.addworkerMap;

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addWorkerViewModel.loadPhoneNumberList();

        ActivityResultLauncher<Intent> launchCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            iv_image.setImageBitmap(bitmap);
                            addWorkerViewModel.saveBitmapToMediaStore(bitmap);
                            iv_image.invalidate();
                            changeBitmap = ((BitmapDrawable) iv_image.getDrawable()).getBitmap();
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> launchGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            selectedImage = result.getData().getData();
                            iv_image.setImageURI(selectedImage);
                            iv_image.invalidate();
                            changeBitmap = ((BitmapDrawable) iv_image.getDrawable()).getBitmap();
                        }
                    }
                });

        //region Observer
        addWorkerViewModel.openWorksiteListLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    List<String> toReturn = new ArrayList<>();
                    List<Worksite> openWorksiteList = addWorkerViewModel.getOpenWorksite();
                    for (int i = 0; i < openWorksiteList.size(); i++) {
                        toReturn.add(openWorksiteList.get(i).getWorksiteName());
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, toReturn);
                    sp_worksiteSpinner.setAdapter(arrayAdapter);
                }
            }
        });

//        addWorkerViewModel.isChangedSpinnerString().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isChanged) {
//                if (isChanged) {
//                    userToAdd = new User(et_phoneNumber.getText().toString(),
//                            "", et_name.getText().toString(),
//                            "", addWorkerViewModel.getWorksiteKeyValue(), false, et_memo.getText().toString(), true);
//                    if (et_name.getText().toString().length() < 1) {
//                        Toast.makeText(requireContext(), R.string.toast_nameEmpty, Toast.LENGTH_SHORT).show();
//                    }
//                    if (iv_image.getDrawable() == null) {
//                        Toast.makeText(requireContext(), R.string.toast_imageEmpty, Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (et_phoneNumber.getText().toString().length() < 1) {
//                            noPhoneNumNewUser();
//                        } else if (addWorkerViewModel.checkPhoneNumber(et_phoneNumber.getText().toString())) {
//                            phoneNumNewUser();
//                        } else {
//                            Toast.makeText(requireContext(), R.string.toast_changePhoneNumber, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//        });
        //endregion

        //region Listener
        bt_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                FileService fileService = App.getFileService();
                imageFile = fileService.createFile("", "temp.jpg");
                if (imageFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(requireActivity(), App.getFileProvider(), imageFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                int permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    launchCamera.launch(takePictureIntent);
                }
            }
        });

        bt_importPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                loadImageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                launchGallery.launch(loadImageIntent);
            }
        });


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedIndex = sp_worksiteSpinner.getSelectedItemPosition();
                Worksite selectedWorksite = addWorkerViewModel.getOpenWorksite().get(selectedIndex);
                userToAdd = new User(et_phoneNumber.getText().toString(),
                        "", et_name.getText().toString(),
                        "", String.valueOf(selectedWorksite.getId()), false, et_memo.getText().toString(), true);
                if (et_name.getText().toString().length() < 1) {
                    Toast.makeText(requireContext(), R.string.toast_nameEmpty, Toast.LENGTH_SHORT).show();
                }
                if (iv_image.getDrawable() == null) {
                    Toast.makeText(requireContext(), R.string.toast_imageEmpty, Toast.LENGTH_SHORT).show();
                } else {
                    if (et_phoneNumber.getText().toString().length() < 1) {
                        noPhoneNumNewUser();
                    } else if (addWorkerViewModel.checkPhoneNumber(et_phoneNumber.getText().toString())) {
                        phoneNumNewUser();
                    } else {
                        Toast.makeText(requireContext(), R.string.toast_changePhoneNumber, Toast.LENGTH_SHORT).show();
                    }
                }
//                addWorkerViewModel.changeSpinnerStringToKeyValue(sp_worksiteSpinner.getSelectedItem().toString());

            }
        });

        bt_worksite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddWorkerFragment.this).navigate(R.id.action_addWorkerFragment_to_todayWorkSiteFragment);
            }
        });

        bt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddWorkerFragment.this).navigate(R.id.action_addWorkerFragment_to_noticeFragment);
            }
        });

        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddWorkerFragment.this).navigate(R.id.action_addWorkerFragment_to_mapFragment);
            }
        });
        //endregion
    }

    private void init() {
        convertDateFormat();
        addWorkerViewModel.loadOpenWorksite(todayCal);
        openWorksiteList = addWorkerViewModel.getOpenWorksite();
    }

    private void convertDateFormat() {
        Calendar cal = Calendar.getInstance();
        todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));

        String todayMonthCal = ((cal.get(Calendar.MONTH) + 1) + "");
        String todayDayCal = ((cal.get(Calendar.DATE)) + "");

        if (todayMonthCal.length() < 2 && todayDayCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        } else if (todayMonthCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "0" + (cal.get(Calendar.MONTH) + 1) + "" + (cal.get(Calendar.DATE)));
        } else if (todayDayCal.length() < 2) {
            todayCal = ((cal.get(Calendar.YEAR)) + "" + (cal.get(Calendar.MONTH) + 1) + "0" + (cal.get(Calendar.DATE)));
        }
    }

    private void noPhoneNumNewUser() {
        addWorkerViewModel.addGuestUser(userToAdd);
        if (changeBitmap != null) {
            addWorkerViewModel.saveNoPhoneNumberUserImage(userToAdd, changeBitmap);
            Toast.makeText(requireContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(AddWorkerFragment.this).navigateUp();
        }
    }

    private void phoneNumNewUser() {
        userToAdd.setPassword("0000");
        addWorkerViewModel.addUser(userToAdd);
        if (changeBitmap != null) {
            addWorkerViewModel.saveUserImage(userToAdd, changeBitmap);
            Toast.makeText(requireContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(AddWorkerFragment.this).navigateUp();
        }
    }
}