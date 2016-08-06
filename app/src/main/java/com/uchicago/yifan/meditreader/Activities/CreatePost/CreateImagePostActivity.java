package com.uchicago.yifan.meditreader.Activities.CreatePost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.uchicago.yifan.meditreader.Model.Post;
import com.uchicago.yifan.meditreader.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateImagePostActivity extends CreatePostActivity {

    private StorageReference mStorageRef;
    private String imageUrl;

    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";

    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_image_post;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        ArrayList<Uri> image_urls = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        ImageView thumbnail = (ImageView)findViewById(R.id.media_image);
        imageUrl = image_urls.get(0).toString();
        Glide.with(this)
                .load(image_urls.get(0).toString())
                .fitCenter()
                .into(thumbnail);

        if (mFileUri == null){
            mFileUri = image_urls.get(0);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_FILE_URI, mFileUri);
        outState.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
    }

    @Override
    void writeNewPost(final String userId, final String username) {

        final StorageReference photoRef = mStorageRef.child("photos").child(mFileUri.getLastPathSegment());
        showProgressDialog();

        photoRef.putFile(mFileUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        hideProgressDialog();
                        createDatabasePost(userId, username);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDownloadUrl =  null;
                        hideProgressDialog();
                        Toast.makeText(CreateImagePostActivity.this, "Error: upload failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void createDatabasePost(String userId, String username){
        String key = mDatabase.child("posts").push().getKey();
        EditText ImageDescription = (EditText) findViewById(R.id.image_description);
        Post post = new Post(userId, "IMAGE", null, mDownloadUrl.toString(), username, ImageDescription.getText().toString());

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        finish();
    }
}
