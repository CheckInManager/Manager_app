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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddWorkerFragment extends Fragment {
    private FragmentAddworkerBinding binding;
    private AddWorkerViewModel addWorkerViewModel;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView iv_image;
    private EditText et_name;
    private EditText et_phoneNumber;
    private EditText et_password;
    private EditText et_accidentHistory;
    private EditText et_memo;
    private Spinner sp_worksiteSpinner;
    private Button bt_add;
    private Button bt_takePicture;
    private Button bt_importPicture;
    private Uri selectedImage;

    private FileService fileService;
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
        et_password = binding.addworkerEtPassword;
        et_accidentHistory = binding.addworkerEtAccidentHistory;
        et_memo = binding.addworkerEtMemo;
        sp_worksiteSpinner = binding.addworkerSpWorksiteSpinner;
        bt_add = binding.addworkerBtAdd;
        bt_takePicture = binding.addworkerBtTakePicture;
        bt_importPicture = binding.addworkerBtImportPicture;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addWorkerViewModel.loadPhoneNumberList();
        addWorkerViewModel.loadWorksiteNameList();

        ActivityResultLauncher<Intent> launchCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            iv_image.setImageBitmap(bitmap);
                            addWorkerViewModel.saveBitmapToMediaStore(bitmap);
                            iv_image.invalidate();
                            changeBitmap = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> launchGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>()
                {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if(result.getResultCode() == Activity.RESULT_OK)
                        {
                            selectedImage = result.getData().getData();
                            iv_image.setImageURI(selectedImage);
                            iv_image.invalidate();
                            changeBitmap=((BitmapDrawable)iv_image.getDrawable()).getBitmap();
                        }
                    }
                });



        //region Observer
        addWorkerViewModel.isWorksiteNameList().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoaded) {
                if (isLoaded) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, addWorkerViewModel.getWorksiteNameList());
                    sp_worksiteSpinner.setAdapter(arrayAdapter);
                }
            }
        });
        //endregion

        //region Listener
        bt_takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                FileService fileService = App.getFileService();
                imageFile = fileService.createFile("", "temp.jpg");
                if(imageFile != null)
                {
                    Uri photoURI = FileProvider.getUriForFile(requireActivity(), App.getFileProvider(), imageFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                int permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
                if(permissionCheck== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CAMERA},0);
                }else{
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
                User userToAdd = new User(et_phoneNumber.getText().toString(),
                        et_password.getText().toString(), et_name.getText().toString(),
                        null, sp_worksiteSpinner.getSelectedItem().toString(), et_accidentHistory.getText().toString(), et_memo.getText().toString());
                if (addWorkerViewModel.checkPhoneNumber(et_phoneNumber.getText().toString())) {
                    addWorkerViewModel.addUser(userToAdd);
                    addWorkerViewModel.saveUserImage(userToAdd,changeBitmap);
                    Toast.makeText(requireContext(), R.string.toast_success, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(AddWorkerFragment.this).navigateUp();
                } else {
                    Toast.makeText(requireContext(), R.string.toast_changePhoneNumber, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion
    }
}