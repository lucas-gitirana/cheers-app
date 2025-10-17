package com.example.cheers.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;

import com.example.cheers.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

    private PreviewView previewView;
    private Button buttonCapture;

    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    // Launcher para pedir permissão de câmera
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permissão concedida, podemos iniciar a câmera
                    startCamera();
                } else {
                    // Permissão negada. Informe o usuário e feche a activity.
                    Toast.makeText(this, "Permissão de câmera necessária.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        buttonCapture = findViewById(R.id.buttonCapture);

        // Cria uma thread dedicada para a câmera para não bloquear a UI
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Verifica e pede a permissão
        requestCameraPermission();

        // Configura o listener do botão de captura
        buttonCapture.setOnClickListener(v -> takePhoto());
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permissão já concedida
            startCamera();
        } else {
            // Pede a permissão
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        // Obtém a instância do provedor de câmera. Isso é assíncrono.
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // O provedor de câmera está disponível.
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // 1. Configurar o caso de uso: Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // 2. Configurar o caso de uso: ImageCapture
                imageCapture = new ImageCapture.Builder().build();

                // Seleciona a câmera traseira como padrão
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Desvincula qualquer caso de uso anterior antes de vincular os novos
                cameraProvider.unbindAll();

                // Vincula os casos de uso ao ciclo de vida da Activity
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (Exception e) {
                Log.e(TAG, "Falha ao iniciar a câmera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            return;
        }

        // Cria um nome de arquivo único baseado na data/hora
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis());

        // Define os metadados para salvar a imagem na galeria do dispositivo
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Cheers-App");
        }

        // Configura as opções de saída para salvar o arquivo
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions
                .Builder(getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
                .build();

        // Tira a foto
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                        String msg = "Foto salva com sucesso: " + output.getSavedUri();
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exc) {
                        Log.e(TAG, "Falha ao salvar a foto: ", exc);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libera a thread da câmera quando a activity for destruída
        cameraExecutor.shutdown();
    }
}