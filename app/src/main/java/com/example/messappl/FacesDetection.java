package com.example.messappl;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messappl.Helper.GraphicOverlay;
import com.example.messappl.Helper.RectOverlay;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class FacesDetection extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    TextView text;
    Bitmap bitmap;
    Uri uri;
    private static final int REQUEST_WRITE_PERMISSION = 786;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
        }
    }

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faces_detection);
        cameraView = (CameraView) findViewById(R.id.camera_view);
        graphicOverlay = (GraphicOverlay) findViewById(R.id.graphics_overlay);
        text = (TextView) findViewById(R.id.text);
        requestPermission();
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
                start();
            }
        }.start();
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                //  cameraView.stop();
                runFaceDetector(bitmap);

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

    }

    private void runFaceDetector(final Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .build();
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                processFaceResult(firebaseVisionFaces);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FacesDetection.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void processFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {
        int count = 0;
        for (FirebaseVisionFace face : firebaseVisionFaces) {
            Rect bounds = face.getBoundingBox();
            RectOverlay rect = new RectOverlay(graphicOverlay, bounds);
            graphicOverlay.add(rect);
            count++;
        }

        uri = getImageUri(this, bitmap);

        FirebaseDatabase.getInstance().getReference().child("Messcounting").child("Count").removeValue();
        FirebaseDatabase.getInstance().getReference().child("Messcounting").child("Count").setValue(String.valueOf(count));

        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Image");
        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    FirebaseDatabase.getInstance().getReference().child("Messcounting").child("ImageUrl").setValue(downloadUri.toString());

                } else {
                    // Handle failures
                    // ...
                }
            }
        });



        if (count >= 30) {
            //   Toast.makeText(FacesDetection.this, String.format("Detected %d faces in vedio , Mess is crowded.", count), Toast.LENGTH_LONG).show();
            text.setText(String.format("Detected %d faces in video , Mess is crowded.", count));
        } else {
            // Toast.makeText(FacesDetection.this, String.format("Detected %d faces in vedio , Chairs are available.", count), Toast.LENGTH_LONG).show();
            text.setText(String.format("Detected %d faces in video , Chairs are available.", count));

        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

  /*  @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            if (cameraView.isStarted()) {
                cameraView.stop();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        cameraView.stop();
        timer.cancel();
        super.onBackPressed();
    }
}
