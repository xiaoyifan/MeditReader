package com.uchicago.yifan.meditreader.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.ogaclejapan.arclayout.ArcLayout;
import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateImagePostActivity;
import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateLinkPostActivity;
import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateQuotePostActivity;
import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateTextPostActivity;
import com.uchicago.yifan.meditreader.Model.User;
import com.uchicago.yifan.meditreader.R;
import com.uchicago.yifan.meditreader.Utitilies.AnimatorUtils;
import com.uchicago.yifan.meditreader.Utitilies.ClipRevealFrame;
import com.uchicago.yifan.meditreader.fragment.MyPostsFragment;
import com.uchicago.yifan.meditreader.fragment.TrendingPostsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private static final String KEY_DEMO = "demo";
    Toast toast = null;

    private static final String NotDoing = "NotTaking";
    private static final String PicturePost = "TakingPicture";
    private static final String UploadingAvatar = "UploadingAvatar";

    public String imagePickerStatus = NotDoing;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    View rootLayout;
    ClipRevealFrame menuLayout;
    ArcLayout arcLayout;
    View centerItem;

    private IProfile profile;

    private AccountHeader headerResult = null;
    private Drawer result = null;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootLayout = findViewById(R.id.root_layout);
        menuLayout = (ClipRevealFrame) findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        centerItem = findViewById(R.id.center_item);

        centerItem.setOnClickListener(this);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }

        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = (v.getLeft() + v.getRight()) / 2;
                int y = (v.getTop() + v.getBottom()) / 2;
                float radiusOfFab = 1f * v.getWidth() / 2f;
                float radiusFromFabToRoot = (float) Math.hypot(
                        Math.max(x, rootLayout.getWidth() - x),
                        Math.max(y, rootLayout.getHeight() - y));

                if (v.isSelected()) {
                    hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
                } else {
                    showMenu(x, y, radiusOfFab, radiusFromFabToRoot);
                }
                v.setSelected(!v.isSelected());

                FloatingActionButton floatingActionButton = (FloatingActionButton)v;

                if (v.isSelected()){
                    v.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.tumblr_primary)));
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.cancel_white));
                }
                else{
                    v.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.colorAccent)));
                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_image_edit));
                }
            }
        });


        this.profile = new ProfileDrawerItem().withName("New user").withEmail("newuser@gmail.com").withIcon(R.drawable.profile).withIdentifier(100);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        showUpdateImageDialog();
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_bookmark).withIcon(FontAwesome.Icon.faw_bookmark).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(3),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(4),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn).withIdentifier(5))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//
                        switch ((int) drawerItem.getIdentifier())
                        {
                            case 2:
                                startActivity(new Intent(getBaseContext(), BookmarkActivity.class));
                                break;
                            case 3:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getBaseContext(), SignInActivity.class));
                                finish();
                                break;
                            case 4:
                                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/xiaoyifan/MeditReader"));
                                startActivity(intent);
                                break;
                            case 5:
                                showDialog();
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // set the selection to the item with the identifier 5
        if (savedInstanceState == null) {
            result.setSelection(5, false);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getBaseContext(), SignInActivity.class));
            finish();
            return;
        }

        updateDrawerProfile(profile);

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] mFragments = new Fragment[]{
                    new TrendingPostsFragment(),
                    new MyPostsFragment(),
            };

            private final String[] mFragmentNames = new String[]{
                    "Trending", "My Posts"
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        if (intent.getData() != null){
                getImages(PicturePost);
        }



    }

    private void showDialog(){
        new SweetAlertDialog(this)
                .setTitleText("Contact me")
                .setContentText("This is an app I built to learn Android & Firebase, and it still has a large space to be promoted with better experience and advanced functions. so shoot me your advice! My Email: xiaoyifanno1@gmail.com")
                .show();
    }

    private void showUpdateImageDialog(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Wanna update profile avtar?")
                .setConfirmText("Sure")
                .setCancelText("Cancel")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        getImages(UploadingAvatar);
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void updateDrawerProfile(final IProfile profile){
        final String userId = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null){
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(MainActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            profile.withName(user.username);
                            profile.withEmail(user.email);

                            if (user.avatarUri != null){
                                profile.withIcon(user.avatarUri);
                            }

                            headerResult.updateProfile(profile);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton vv = (FloatingActionButton) findViewById(R.id.fab_new_post);
        vv.setSelected(false);

        if (vv.isSelected()){
            vv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.tumblr_primary)));
            vv.setImageDrawable(getResources().getDrawable(R.drawable.cancel_white));
        }
        else{
            vv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.colorAccent)));
            vv.setImageDrawable(getResources().getDrawable(R.drawable.ic_image_edit));
        }
        menuLayout.setVisibility(View.INVISIBLE);
    }

    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);

        animList.add(revealAnim);
        animList.add(createShowItemAnimator(centerItem));

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    private void hideMenu(int cx, int cy, float startRadius, float endRadius) {
        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        animList.add(createHideItemAnimator(centerItem));

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });

        animList.add(revealAnim);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {
        float dx = centerItem.getX() - item.getX();
        float dy = centerItem.getY() - item.getY();

        item.setScaleX(0f);
        item.setScaleY(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        final float dx = centerItem.getX() - item.getX();
        final float dy = centerItem.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(1f, 0f),
                AnimatorUtils.scaleY(1f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        anim.setDuration(50);
        return anim;
    }

    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,
                                          float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }

    @Override
    public void onClick(View v) {

        if (v instanceof ImageButton) {
         switch ((String)v.getTag())
         {
             case "photoTag":
                getImages(PicturePost);
                 break;
             case "quoteTag":
                 startActivity(new Intent(this, CreateQuotePostActivity.class));
                 break;
             case "linkTag":
                 startActivity(new Intent(this, CreateLinkPostActivity.class));
                 break;
             case "textTag":
                 startActivity(new Intent(this, CreateTextPostActivity.class));
                 break;
             default:
                 break;
         }
        }

    }

    public void getImages(String code) {

        imagePickerStatus = code;

        String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};

        int permsRequestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){

            case 200:
                Config config = new Config();
                config.setSelectionMin(1);
                config.setSelectionLimit(1);
                ImagePickerActivity.setConfig(config);
                Intent intent  = new Intent(this, ImagePickerActivity.class);
                startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

            ArrayList<Uri>  image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

            if (imagePickerStatus == PicturePost){
                Intent imageIntent = new Intent(this, CreateImagePostActivity.class);
                imageIntent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
                startActivity(imageIntent);
            }
            else if (imagePickerStatus == UploadingAvatar){

                Uri mFileUri = Uri.fromFile(new File(image_uris.get(0).toString()));
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("photos").child(mFileUri.getLastPathSegment());

                photoRef.putFile(mFileUri)
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                                hideProgressDialog();
                                updateUserProfile(mDownloadUrl);
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Uri mDownloadUrl =  null;
                                hideProgressDialog();
                                Toast.makeText(MainActivity.this, "Error: upload failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            imagePickerStatus = NotDoing;
        }
    }

    public void updateUserProfile(Uri avatarUri){

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + getUid() + "/avatarUri", avatarUri.toString());

        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);

        updateDrawerProfile(this.profile);
    }

}
