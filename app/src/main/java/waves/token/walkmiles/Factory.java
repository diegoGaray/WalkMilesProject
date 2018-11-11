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
package waves.token.walkmiles;

import android.content.pm.PackageManager;

import waves.token.walkmiles.services.AbstractStepDetectorService;
import waves.token.walkmiles.services.AccelerometerStepDetectorService;
import waves.token.walkmiles.services.HardwareStepDetectorService;
import waves.token.walkmiles.utils.AndroidVersionHelper;

/**
 * Factory class
 *
 * @author Tobias Neidig
 * @version 20160610
 */

public class Factory {

    /**
     * Returns the class of the step detector service which should be used
     *
     * The used step detector service depends on different soft- and hardware preferences.
     * @param pm An instance of the android PackageManager
     * @return The class of step detector
     */
    public static Class<? extends AbstractStepDetectorService> getStepDetectorServiceClass(PackageManager pm){
        if(pm != null && AndroidVersionHelper.supportsStepDetector(pm)) {
            return HardwareStepDetectorService.class;
        }else{
            return AccelerometerStepDetectorService.class;
        }
    }
}
