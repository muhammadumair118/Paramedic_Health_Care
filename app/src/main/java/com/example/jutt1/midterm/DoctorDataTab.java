package com.example.jutt1.midterm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DoctorDataTab extends AppCompatActivity {

    FirebaseAuth auth;

    ImageView imageView;
    TextView doctorName, doctorSpecialization;
    RatingBar ratingBar;
    Bundle bundle = new Bundle();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_data_tab);

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        PersonModel personModel = intent.getParcelableExtra("list");

        String Id = personModel.getId();

        String Image = personModel.getImageURL();
        String Name = personModel.getName();
        String Specialization = personModel.getSpecialization();
        String Rating_Bar = personModel.getRating_bar();

        imageView = findViewById(R.id.doctorImage);
        Picasso.with(this).load(Image).fit().into(imageView);
        doctorName = findViewById(R.id.doctorName);
        doctorName.setText(Name);
        doctorSpecialization = findViewById(R.id.doctorSpecialization);
        doctorSpecialization.setText(Specialization);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(Rating_Bar));

        bundle.putString("specialization", Specialization);
        bundle.putString("DoctorId", Id);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_data_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            if (auth.getCurrentUser() != null) {
                auth.signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("EMAIL");
                editor.remove("PASSWORD");
                editor.apply();
                editor.commit();
                startActivity(new Intent(this,Login.class));
                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragmentThree fragmentThree = new FragmentThree();
                    fragmentThree.setArguments(bundle);
                    return fragmentThree;
                case 1:
                    FragmentFour fragmentFour = new FragmentFour();
                    fragmentFour.setArguments(bundle);
                    return fragmentFour;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Symptoms";
                case 1:
                    return "Specialization";
                default:
                    return "";
            }
        }
    }
}
