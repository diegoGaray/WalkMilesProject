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

import java.util.Map;


public class ActivityDayChart {
    private String title;
    private Map<String, ActivityChartDataSet> steps;
    private Map<String, ActivityChartDataSet> distance;
    private Map<String, ActivityChartDataSet> walkMiles;
    private DataType displayedDataType;
    private int goal;
    public ActivityDayChart(Map<String, ActivityChartDataSet> steps, Map<String, ActivityChartDataSet> distance, Map<String, ActivityChartDataSet> walkMiles, String title) {
        this.steps = steps;
        this.title = title;
        this.distance = distance;
        this.walkMiles = walkMiles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, ActivityChartDataSet> getSteps() {
        return steps;
    }

    public void setSteps(Map<String, ActivityChartDataSet> steps) {
        this.steps = steps;
    }

    public Map<String, ActivityChartDataSet> getDistance() {
        return distance;
    }

    public void setDistance(Map<String, ActivityChartDataSet> distance) {
        this.distance = distance;
    }

    public Map<String, ActivityChartDataSet> getwalkMiles() {
        return walkMiles;
    }

    public void setwalkMiles(Map<String, ActivityChartDataSet> walkMiles) {
        this.walkMiles = walkMiles;
    }

    public DataType getDisplayedDataType() {
        return displayedDataType;
    }

    public void setDisplayedDataType(DataType displayedDataType) {
        this.displayedDataType = displayedDataType;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public enum DataType {
        STEPS, DISTANCE, WALKMILES
    }
}