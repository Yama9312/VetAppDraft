package com.example.vetappdraft;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadMusicFragment extends Fragment {
    // Contracts for modern API replacements
    private ActivityResultLauncher<String> audioPickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private VetDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = VetDatabase.getInstance(requireContext());

        // Initialize audio picker launcher (replaces startActivityForResult/onActivityResult)
        audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                this::handleAudioSelectionResult
        );

        // Initialize permission launcher (replaces requestPermissions)
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                this::handlePermissionResult
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_music, container, false);
        Button btnSelectMusic = view.findViewById(R.id.btn_select_music);
        Button btnContinue = view.findViewById(R.id.btn_continue);

        btnSelectMusic.setOnClickListener(v -> checkAndRequestPermissions());

        btnContinue.setOnClickListener(v -> {
            requireActivity().runOnUiThread(() -> {
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();
                UploadImageFragment uIFragment = new UploadImageFragment();
                transaction.replace(R.id.fragment_container, uIFragment);

                transaction.commit();
            });
        });

        return view;
    }

    private void checkAndRequestPermissions() {
        if (hasRequiredPermissions()) {
            safelyOpenFilePicker();
        } else {
            requestAppropriatePermission();
        }
    }

    private boolean hasRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return hasPermission(Manifest.permission.READ_MEDIA_AUDIO);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true; // No permission needed for Android 10+ with ACTION_GET_CONTENT
        } else {
            return hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(requireContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestAppropriatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            safelyOpenFilePicker();
        }
    }

    private void handlePermissionResult(boolean isGranted) {
        if (isGranted) {
            safelyOpenFilePicker();
        } else {
            showPermissionRationale();
        }
    }

    private void showPermissionRationale() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Permission Needed")
                    .setMessage("This permission is required to access your audio files")
                    .setPositiveButton("Grant", (d, w) -> requestAppropriatePermission())
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            showPermissionDeniedDialog();
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Permission Permanently Denied")
                .setMessage("Please enable audio access in app settings")
                .setPositiveButton("Settings", (d, w) -> openAppSettings())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        startActivity(intent);
    }

    private void safelyOpenFilePicker() {
        try {
            audioPickerLauncher.launch("audio/*");
        } catch (ActivityNotFoundException e) {
            showFilePickerNotFoundError();
        }
    }

    private void showFilePickerNotFoundError() {
        Toast.makeText(requireContext(),
                "No compatible file picker app found", Toast.LENGTH_LONG).show();
    }

    private void handleAudioSelectionResult(@Nullable Uri uri) {
        if (uri != null) {
            processAudioFile(uri);
        } else {
            showNoFileSelectedMessage();
        }
    }

    private void processAudioFile(Uri uri) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                MusicFile musicFile = createMusicFileFromUri(uri);
                db.musicFileDAO().insert(musicFile);
                requireActivity().runOnUiThread(() ->
                        showUploadSuccess(musicFile.getFileName()));
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        showUploadError(e.getMessage()));
            }
        });
    }

    private MusicFile createMusicFileFromUri(Uri uri) throws IOException {
        MusicFile musicFile = new MusicFile();

        try (Cursor cursor = requireContext().getContentResolver()
                .query(uri, null, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                musicFile.setFileName(cursor.getString(nameIndex));
                musicFile.setFileSize(cursor.getLong(sizeIndex));
            }
        }

        musicFile.setFilePath(getRealPathFromUri(uri));
        musicFile.setDuration(extractAudioDuration(uri));
        musicFile.setUserId(getCurrentUserId());

        return musicFile;
    }

    private String getRealPathFromUri(Uri uri) throws IOException {
        File tempFile = File.createTempFile("audio_", ".tmp", requireContext().getCacheDir());

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri); FileOutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[4 * 1024];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            return tempFile.getAbsolutePath();
        }
    }

    private long extractAudioDuration(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(requireContext(), uri);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return duration != null ? Long.parseLong(duration) : 0;
        } catch (Exception e) {
            return 0;
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int getCurrentUserId() {
        // Implement your user ID retrieval logic
        return 1; // Example - replace with actual user ID
    }

    private void showUploadSuccess(String fileName) {
        Toast.makeText(requireContext(),
                "Uploaded: " + fileName, Toast.LENGTH_SHORT).show();
    }

    private void showUploadError(String error) {
        Toast.makeText(requireContext(),
                "Upload failed: " + error, Toast.LENGTH_LONG).show();
    }

    private void showNoFileSelectedMessage() {
        Toast.makeText(requireContext(),
                "No audio file selected", Toast.LENGTH_SHORT).show();
    }
}
