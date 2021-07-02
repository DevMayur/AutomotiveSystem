package com.mayur.document_vault;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mayur.R;
import com.mayur.utils.ProgressUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DocumentUploaderActivity extends AppCompatActivity {

    private EditText et_doc_title;
    private Button bt_upload;
    public static final int MY_CAMERA_PERMISSION_CODE = 103;
    public static final int CAMERA_REQUEST = 104;
    private UploadToStorageInterface uploadToStorageInterface;
    String mobile_number = "9975888110";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_uploader);

        et_doc_title = findViewById(R.id.et_doc_title);
        bt_upload = findViewById(R.id.bt_upload);

        if (getIntent().getStringExtra("mobile_number") != null) {
            mobile_number = getIntent().getStringExtra("mobile_number");
        }
        uploadToStorageInterface = new UploadToStorageInterface() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onSuccess(Uri downloadUri) {
                addImageToDocumentsDirectory(downloadUri.toString(), DocumentUploaderActivity.this);
            }

            @Override
            public void onFailure() {

            }
        };

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_doc_title.getText())) {
                    selectImage();
                } else {
                    Toast.makeText(DocumentUploaderActivity.this, "please write document name !" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addImageToDocumentsDirectory(String img_url, Context context) {
        Map<String, Object> params = new HashMap<>();
        params.put("title",et_doc_title.getText().toString());
        params.put("img_url", img_url);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection(mobile_number + "_documents").add(params).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        if (options[item].equals("Take Photo")) {
                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAMERA_REQUEST);
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , CAMERA_REQUEST);

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                }

            }
        });
        builder.show();
    }

    public Context requireContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap bitmap = null;
            if(data.getData()==null){
                bitmap = (Bitmap)data.getExtras().get("data");
            }else{
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null) {
                if (getImageUri(requireContext(),bitmap) != null) {
                    uploadImage(getImageUri(requireContext(),bitmap), uploadToStorageInterface,"Documents", requireContext());
                } else {
                    Toast.makeText(requireContext(), "Bitmap Null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "null bitmap", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadImage(Uri file, UploadToStorageInterface uploadToStorageInterface, String field, Context context) {
        final ProgressUtils progress = ProgressUtils.getInstance(context);
        progress.showProgress("Please wait", "Uploading Image");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        String filename = String.valueOf(Calendar.getInstance().getTimeInMillis());
        StorageReference feedReference = storageReference.child(field+"/"+filename+".jpg");

        uploadToStorageInterface.onStart();

        UploadTask uploadTask = feedReference.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            uploadToStorageInterface.onStart();
            if (!task.isSuccessful()) {
                uploadToStorageInterface.onFailure();
                progress.hideProgress();
            }
            return feedReference.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                progress.hideProgress();
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    uploadToStorageInterface.onSuccess(downloadUri);
                } else {
                    uploadToStorageInterface.onFailure();
                }
            }
        });
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(new Date().getTime()), null);
        if (path == null) {
            try {
                return saveBitmap(requireContext(),inImage,Bitmap.CompressFormat.JPEG,"image/*",String.valueOf(new Date().getTime()),"blogImages");
            } catch (IOException e) {
                Toast.makeText(requireContext(), "exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return null;
            }
        } else {
            return Uri.parse(path);
        }
    }

    @NonNull
    private Uri saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                           @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                           @NonNull final String displayName, @Nullable final String subFolder) throws IOException
    {
        String relativeLocation = Environment.DIRECTORY_PICTURES;

        if (!TextUtils.isEmpty(subFolder))
        {
            relativeLocation += File.separator + subFolder;
        }

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try
        {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, contentValues);

            if (uri == null)
            {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);

            if (stream == null)
            {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false)
            {
                throw new IOException("Failed to save bitmap.");
            }

            return uri;
        }
        catch (IOException e)
        {
            if (uri != null)
            {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
    }
}