package com.palebluedot.potion.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.palebluedot.potion.R;
import com.palebluedot.potion.search.SearchActivity;
import com.palebluedot.potion.search.SearchMaterialActivity;
import com.palebluedot.potion.customized.AddCustomizedActivity;
import com.palebluedot.potion.customized.CustomizedFragment;
import com.palebluedot.potion.db.DbHelper;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.palebluedot.potion.mypotion.MyPotionsFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

//TODO: 약 추가한 후 새로고침 하도록 만들기
public class MainActivity extends AppCompatActivity{
    public static final int REQUEST_SEARCH = 1;
    private MaterialViewPager mViewPager;
    private DbHelper dbHelper;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = DbHelper.getInstance(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*CircularFloatingActionMenu*/
        // Set up the white button on the lower right corner
        // more or less with default parameter

        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_filter_alt_24));
        final FloatingActionButton rightLowerButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_search));    //search
        rlIcon2.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));     //add my own
        rlIcon3.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_view));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.baseline_add_white_36));

        SubActionButton searchSubButton = rLSubBuilder.setContentView(rlIcon1).build();
        SubActionButton addSubButton = rLSubBuilder.setContentView(rlIcon2).build();
        SubActionButton searchMaterialSubButton = rLSubBuilder.setContentView(rlIcon3).build();


        /*set onClickListener on sub buttons*/
        searchSubButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivityForResult(intent, REQUEST_SEARCH);
        });

        addSubButton.setOnClickListener(v -> {
            //TODO: 내 전용 포션 추가하는 엑티비티로 이동
            Intent intent = new Intent(getApplicationContext(), AddCustomizedActivity.class);
            startActivity(intent);
        });

        searchMaterialSubButton.setOnClickListener(v -> {
            //TODO: 성분 검색하는 액티비티로 이동
            Intent intent = new Intent(getApplicationContext(), SearchMaterialActivity.class);
            startActivity(intent);
        });

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons

        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(addSubButton)
                .addSubActionView(searchSubButton)
                .addSubActionView(searchMaterialSubButton)
                .attachTo(rightLowerButton)
                .build();

        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });


        /*ViewPager*/
        mViewPager = findViewById(R.id.materialViewPager);
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 1:
                        return CustomizedFragment.newInstance(1);
                    default:
                        return MyPotionsFragment.newInstance(1);
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "My Potions";
                    case 1:
                        return "Customized Potions";
                }
                return "";
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_SEARCH && resultCode == RESULT_OK && data != null){
        }
    }
}
