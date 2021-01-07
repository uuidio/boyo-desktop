package com.hotron.c002fac.camera;


import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraWrapper {
    private static final String TAG = "CameraWrapper";

    private CameraWrapperCallback mCallback;

    private Camera mCamera;

    private int mCameraId;

    private int mCameraIndex;

    private ArrayList<FrameDimension> mFrameDims = new ArrayList<FrameDimension>();

    private FrameDimension mPrefFrameDim = new FrameDimension();

    public static CameraWrapper create(int paramInt) {
        return create(paramInt, new FrameDimension());
    }

    public static CameraWrapper create(int paramInt, FrameDimension paramFrameDimension) {
        CameraWrapper cameraWrapper = new CameraWrapper();
        return cameraWrapper.createCamera(paramInt, paramFrameDimension) ? cameraWrapper : null;
    }

    private boolean createCamera(int paramInt, FrameDimension paramFrameDimension) {
        this.mCameraId = paramInt;
        this.mPrefFrameDim = paramFrameDimension;
        try {
            this.mCamera = Camera.open(this.mCameraId);
            return (this.mCamera != null) ? initCamera() : false;
        } catch (Exception exception) {
            if (this.mCamera != null)
                destroy();
            Log.e("CameraWrapper", exception.getMessage());
            return false;
        }
    }

    private boolean initCamera() {
        Camera.Parameters parameters = this.mCamera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
//        Log.e("Supported preview sizes:", new Object[0]);
        boolean bool = true;
        for (Camera.Size size : list) {
//            Logger.d(String.format("Camera%d support size: %d x %d", new Object[] { Integer.valueOf(this.mCameraId), Integer.valueOf(size.width), Integer.valueOf(size.height) }));
            this.mFrameDims.add(new FrameDimension(size.width, size.height));
            boolean bool1 = bool;
            if (bool) {
                FrameDimension frameDimension = this.mPrefFrameDim;
                bool1 = bool;
                if (frameDimension != null) {
                    bool1 = bool;
                    if (frameDimension.mWidth == size.width) {
                        bool1 = bool;
                        if (this.mPrefFrameDim.mHeight == size.height)
                            bool1 = false;
                    }
                }
            }
            bool = bool1;
        }
        if (bool && !this.mFrameDims.isEmpty())
            this.mPrefFrameDim = this.mFrameDims.get(0);
        if (this.mPrefFrameDim.mWidth > 0 && this.mPrefFrameDim.mHeight > 0) {
            parameters.setPreviewSize(this.mPrefFrameDim.mWidth, this.mPrefFrameDim.mHeight);
            this.mCamera.setParameters(parameters);
            return true;
        }
        return false;
    }

    public void destroy() {
        if (this.mCamera == null)
            return;
        stopPreview();
        this.mCamera.setPreviewCallback(null);
        this.mCamera.release();
        this.mCamera = null;
    }

    public ArrayList<FrameDimension> getFrameDims() {
        return this.mFrameDims;
    }

    public FrameDimension getPrefFrameDim() {
        return this.mPrefFrameDim;
    }

    public void setCameraDisplayOrientation(int paramInt) {
        if (this.mCamera == null)
            return;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(this.mCameraIndex, cameraInfo);
        int i = 0;
        if (paramInt != 0) {
            if (paramInt != 1) {
                if (paramInt != 2) {
                    if (paramInt != 3) {
                        if (paramInt % 90 == 0)
                            i = (paramInt + 360) % 360;
                    } else {
                        i = 270;
                    }
                } else {
                    i = 180;
                }
            } else {
                i = 90;
            }
        } else {
            i = 0;
        }
        paramInt = cameraInfo.orientation;
        if (cameraInfo.facing == 1)
            paramInt = (360 - cameraInfo.orientation) % 360;
        this.mCamera.setDisplayOrientation((paramInt + 360 - i) % 360);
    }

    public void setPreviewCallback(CameraWrapperCallback paramCameraWrapperCallback) {
        Camera camera = this.mCamera;
        if (camera == null)
            return;
        if (paramCameraWrapperCallback == null) {
            this.mCallback = null;
            camera.setPreviewCallback(null);
            return;
        }
        this.mCallback = paramCameraWrapperCallback;
        (new Thread() {
            public void run() {
                super.run();
                CameraWrapper.this.mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    public void onPreviewFrame(byte[] param2ArrayOfbyte, Camera param2Camera) {
                        CameraWrapper.FrameDimension frameDimension = CameraWrapper.this.getPrefFrameDim();
                        CameraWrapper.this.mCallback.onPreviewFrame(param2ArrayOfbyte, frameDimension.mWidth, frameDimension.mHeight);
                    }
                });
                CameraWrapper.this.mCamera.setErrorCallback(new Camera.ErrorCallback() {
                    public void onError(int param2Int, Camera param2Camera) {
                        CameraWrapper.this.mCallback.onError();
                    }
                });
            }
        }).start();
    }

    public boolean setPreviewDisplay(SurfaceHolder paramSurfaceHolder) {
        Camera camera = this.mCamera;
        if (camera != null) {
            if (paramSurfaceHolder == null)
                return false;
            try {
                camera.setPreviewDisplay(paramSurfaceHolder);
                return false;
            } catch (IOException iOException) {
                iOException.printStackTrace();
//                Logger.e("CameraWrapper", new Object[] { "setPreviewDisplay failed!", iOException });
                return false;
            }
        }
        return false;
    }

    public void startPreview() {
        (new Thread() {
            public void run() {
                super.run();
                Log.e("CameraWrapper", "StartPreview");
                CameraWrapper.this.mCamera.startPreview();
            }
        }).start();
    }

    public void stopPreview() {
        Camera camera = this.mCamera;
        if (camera != null)
            camera.stopPreview();
    }

    public static interface CameraWrapperCallback {
        void onError();

        void onPreviewFrame(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
    }

    public static class FrameDimension {
        public int mHeight;

        public int mWidth;

        public FrameDimension() {
            this(0, 0);
        }

        public FrameDimension(int param1Int1, int param1Int2) {
            this.mWidth = param1Int1;
            this.mHeight = param1Int2;
        }

        public String toString() {
            return String.format("%dx%d", new Object[] { Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight) });
        }
    }
}