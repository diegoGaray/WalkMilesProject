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
package com.mileston.walk.walkmilestonandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.answers.Answers;
import com.mileston.walk.walkmilestonandroid.LoginActivity;
import com.mileston.walk.walkmilestonandroid.R;

import io.fabric.sdk.android.Fabric;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Karola Marky
 * @version 20161105
 */

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Fabric.with(this, new Answers());
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
