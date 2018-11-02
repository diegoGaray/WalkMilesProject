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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mileston.walk.walkmilestonandroid.Factory;
import com.mileston.walk.walkmilestonandroid.R;
import com.mileston.walk.walkmilestonandroid.models.WalkingMode;
import com.mileston.walk.walkmilestonandroid.persistence.StepCountPersistenceHelper;
import com.mileston.walk.walkmilestonandroid.persistence.WalkingModePersistenceHelper;
import com.mileston.walk.walkmilestonandroid.services.AbstractStepDetectorService;
import com.mileston.walk.walkmilestonandroid.utils.StepDetectionServiceHelper;
import com.mileston.walk.walkmilestonandroid.utils.UnitHelper;

import java.util.Calendar;

public class WalkingModeLearningActivity{
  

}
