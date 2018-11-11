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
package waves.token.walkmiles.models;

import waves.token.walkmiles.R;
import waves.token.walkmiles.utils.UnitHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class StepCount {

    // from https://github.com/bagilevi/android-pedometer/blob/master/src/name/bagi/levente/pedometer/walkMilesNotifier.java
    private static double METRIC_RUNNING_FACTOR = 1.02784823;
    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double METRIC_AVG_FACTOR = (METRIC_RUNNING_FACTOR + METRIC_WALKING_FACTOR) / 2;


    private int stepCount;
    private long startTime;
    private long endTime;
    private double walkmiles;
    private WalkingMode walkingMode;

    public int getStepCount() {


        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public WalkingMode getWalkingMode() {
        return walkingMode;
    }

    public void setWalkingMode(WalkingMode walkingMode) {
        this.walkingMode = walkingMode;
    }

    /**
     * Gets the distance walked in this interval.
     *
     * @return The distance in meters
     */
    public double getDistance(){
        if(getWalkingMode() != null) {
            return getStepCount() * getWalkingMode().getStepLength();
        }else{
            return 0;
        }
    }

    public double getWalkmiles() {
        return walkmiles;
    }

    public void setWalkmiles(double walkmiles) {
        this.walkmiles = walkmiles;
    }

    /**
     * Gets the walkMiles
     * @return the walkMiles in cal
     */
    /*public double getCalories(Context context){
        // inspired by https://github.com/bagilevi/android-pedometer/blob/master/src/name/bagi/levente/pedometer/walkMilesNotifier.java
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        float bodyWeight = Float.parseFloat(sharedPref.getString(context.getString(R.string.pref_weight),context.getString(R.string.pref_default_weight)));
        return bodyWeight * METRIC_AVG_FACTOR * UnitHelper.metersToKilometers(getDistance());
    }*/



    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
        return "StepCount{" + format.format(new Date(startTime)) +
                " - " + format.format(new Date(endTime)) +
                ": " + stepCount + " @ " + ((walkingMode == null) ? -1 : walkingMode.getId())+
                '}';
    }
}
