package com.project.project.globalnest.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.project.project.globalnest.data.Preferences;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 */

public final class CameraConfigurationManager {

	private static final String TAG = "CameraConfiguration";
	private static final int MIN_PREVIEW_PIXELS = 320 * 240; // small screen
	private static final int MAX_PREVIEW_PIXELS = 800 * 480; // large/HD screen

	private final Context context;
	private Point screenResolution;
	private Point cameraResolution;

	public CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(Camera camera) {
		Parameters parameters = camera.getParameters();

		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		// We're landscape-only, and have apparently seen issues with display
		// thinking it's portrait
		// when waking from sleep. If it's not landscape, assume it's mistaken
		// and reverse them:
		if (width < height) {

			Log.i(TAG,
					"Display reports portrait orientation; assuming this is incorrect");
			int temp = width;
			width = height;
			height = temp;
		}
		screenResolution = new Point(width, height);
		Log.i(TAG, "Screen resolution: " + screenResolution);
		cameraResolution = findBestPreviewSizeValue(parameters,
				screenResolution, false);
		Log.i(TAG, "Camera resolution: " + cameraResolution);
	}

	void setDesiredCameraParameters(Camera camera) {
		Parameters parameters = camera.getParameters();

		// /////seshu/////////////
		// parameters.set("orientation", "portrait");
		// parameters.set("rotation", 90);
		// /////seshu///////////

		if (parameters == null) {
			Log.w(TAG,
					"Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		initializeTorch(parameters, prefs);
		String focusMode = findSettableValue(
				parameters.getSupportedFocusModes(),
				Parameters.FOCUS_MODE_AUTO,
				Parameters.FOCUS_MODE_MACRO);
		if (focusMode != null) {
			parameters.setFocusMode(focusMode);
		}

		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		// parameters.set("orientation", "portrait");
		// parameters.set("rotation", 90);
		camera.setParameters(parameters);

		setDisplayOrientation(camera, 90);

		/*
		 * // Changing the orientation to Portrait if
		 * (Integer.parseInt(Build.VERSION.SDK) >= 8)
		 * camera.setDisplayOrientation(90);
		 *
		 * //setDisplayOrientation(camera, 90); else {
		 *
		 * }
		 */

	}

	// Changing the orientation to Portrait
	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
		}
	}

	public Point getCameraResolution() {
		return cameraResolution;
	}

	public Point getScreenResolution() {
		return screenResolution;
	}

	void setTorch(Camera camera, boolean newSetting) {
		Parameters parameters = camera.getParameters();
		doSetTorch(parameters, newSetting);
		camera.setParameters(parameters);
	}

	private static void initializeTorch(Parameters parameters,
			SharedPreferences prefs) {
		doSetTorch(parameters, Preferences.KEY_FRONT_LIGHT);
	}

	private static void doSetTorch(Parameters parameters,
			boolean newSetting) {
		String flashMode;
		if (newSetting) {
			flashMode = findSettableValue(parameters.getSupportedFlashModes(),
					Parameters.FLASH_MODE_TORCH,
					Parameters.FLASH_MODE_ON);
		} else {
			flashMode = findSettableValue(parameters.getSupportedFlashModes(),
					Parameters.FLASH_MODE_OFF);
		}
		if (flashMode != null) {
			parameters.setFlashMode(flashMode);
		}
	}

	private static Point findBestPreviewSizeValue(Parameters parameters,
			Point screenResolution, boolean portrait) {
		Point bestSize = null;
		int diff = Integer.MAX_VALUE;
		for (Camera.Size supportedPreviewSize : parameters
				.getSupportedPreviewSizes()) {
			int pixels = supportedPreviewSize.height
					* supportedPreviewSize.width;
			if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
				continue;
			}
			int supportedWidth = portrait ? supportedPreviewSize.height
					: supportedPreviewSize.width;
			int supportedHeight = portrait ? supportedPreviewSize.width
					: supportedPreviewSize.height;
			int newDiff = Math.abs(screenResolution.x * supportedHeight
					- supportedWidth * screenResolution.y);
			if (newDiff == 0) {
				bestSize = new Point(supportedWidth, supportedHeight);
				break;
			}
			if (newDiff < diff) {
				bestSize = new Point(supportedWidth, supportedHeight);
				diff = newDiff;
			}
		}
		if (bestSize == null) {
			Camera.Size defaultSize = parameters.getPreviewSize();
			bestSize = new Point(defaultSize.width, defaultSize.height);
		}
		return bestSize;
	}

	private static String findSettableValue(Collection<String> supportedValues,
			String... desiredValues) {
		Log.i(TAG, "Supported values: " + supportedValues);
		String result = null;
		if (supportedValues != null) {
			for (String desiredValue : desiredValues) {
				if (supportedValues.contains(desiredValue)) {
					result = desiredValue;
					break;
				}
			}
		}
		Log.i(TAG, "Settable value: " + result);
		return result;
	}

}
