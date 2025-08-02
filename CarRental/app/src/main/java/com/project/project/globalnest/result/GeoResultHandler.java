
package com.project.project.globalnest.result;


import android.app.Activity;

import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.project.project.R;


/**
 * Handles geographic coordinates (typically encoded as geo: URLs).
 * 
 *
 */
public final class GeoResultHandler extends ResultHandler {

    public GeoResultHandler(Activity activity, ParsedResult result) {
        super(activity, result);
    }

    @Override
    public CharSequence getDisplayContents() {
        GeoParsedResult result = (GeoParsedResult) getResult();
        StringBuilder contents = new StringBuilder(100);
        ParsedResult.maybeAppend(result.getGeoURI(), contents);
        ParsedResult.maybeAppend(String.valueOf(result.getLatitude()), contents);
        ParsedResult.maybeAppend(String.valueOf(result.getLongitude()), contents);
        contents.trimToSize();
        return contents.toString();
    }

    @Override
    public int getDisplayTitle() {
        return R.string.result_geo;
    }
}
