/*
    Privacy Friendly Pedometer is licensed under the GPLv3.
    Copyright (C) 2017  Tobias Neidig

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package waves.token.walkmiles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import waves.token.walkmiles.R;
import waves.token.walkmiles.SignUpActivity;
import waves.token.walkmiles.fragments.DailyReportFragment;
import waves.token.walkmiles.fragments.MainFragment;
import waves.token.walkmiles.fragments.MonthlyReportFragment;
import waves.token.walkmiles.fragments.WeeklyReportFragment;
import waves.token.walkmiles.utils.StepDetectionServiceHelper;
import waves.token.walkmiles.MaterialViewPager;
import waves.token.walkmiles.header.HeaderDesign;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends BaseActivity implements DailyReportFragment.OnFragmentInteractionListener, WeeklyReportFragment.OnFragmentInteractionListener, MonthlyReportFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("     WalkMiles");


        ButterKnife.bind(this);
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // init preferences
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

        // Load first view
        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new MainFragment(), "MainFragment");
        fragmentTransaction.commit();

        // Start step detection if enabled and not yet started
        StepDetectionServiceHelper.startAllIfEnabled(this);
        //Log.i(LOG_TAG, "MainActivity initialized");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 3) {
                    case 0:
                        return DailyReportFragment.newInstance();
                    case 1:
                        return WeeklyReportFragment.newInstance();
                    case 2:
                        return MonthlyReportFragment.newInstance();

                    default:
                        return DailyReportFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:

                        return "Day";
                    case 1:
                        return "Week";
                    case 2:
                        return "Month";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        String imageUri = "drawable://" + R.drawable.banner;
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,"");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,"");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.primary,"");
                }

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        final View logo = findViewById(R.id.logoContainer);
        //logo.setBackground(ContextCompat.getDrawable(this, R.drawable.banner));
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_about) {
            Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_cuenta){
            Intent intent = new Intent(getApplicationContext(),UpdateActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
