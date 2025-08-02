
package com.project.project.globalnest.result;

import android.app.Activity;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.project.project.R;


/**
 * Handles generic products which are not books.
 * 
 * 
 */
public final class ProductResultHandler extends ResultHandler {

    public ProductResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        super(activity, result, rawResult);
    }

    @Override
    public CharSequence getDisplayContents() {
        ProductParsedResult result = (ProductParsedResult) getResult();
        StringBuilder contents = new StringBuilder(100);
        ParsedResult.maybeAppend(result.getNormalizedProductID(), contents);
        contents.trimToSize();
        return contents.toString();
    }
    
    @Override
    public int getDisplayTitle() {
        return R.string.result_product;
    }
}
