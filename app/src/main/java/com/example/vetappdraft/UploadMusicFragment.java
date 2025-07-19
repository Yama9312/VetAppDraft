package com.example.vetappdraft;

import android.Manifest;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadMusicFragment extends Fragment {
    private ActivityResultLauncher<String[]> multipleAudioPickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private VetDatabase db;
    private ExecutorService executor;
    private ProgressBar progressBar;
    private int totalFilesToProcess = 0;
    private int processedFiles = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = VetDatabase.getInstance(requireContext());
        executor = Executors.newFixedThreadPool(4); // 4 threads for parallel processing

        // Initialize multiple file picker
        multipleAudioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                this::handleMultipleAudioSelection
        );

        // Initialize permission launcher
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
        progressBar = view.findViewById(R.id.upload_progress);

        btnSelectMusic.setOnClickListener(v -> checkAndRequestPermissions());
        btnContinue.setOnClickListener(v -> navigateToNextFragment());

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
            return true;
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

    private void safelyOpenFilePicker() {
        try {
            // Allow multiple audio formats
            multipleAudioPickerLauncher.launch(new String[]{"audio/*", "application/ogg"});
        } catch (ActivityNotFoundException e) {
            showFilePickerNotFoundError();
        }
    }

    private void handleMultipleAudioSelection(@Nullable List<Uri> uris) {
        if (uris == null || uris.isEmpty()) {
            showNoFileSelectedMessage();
            return;
        }

        totalFilesToProcess = uris.size();
        processedFiles = 0;
        progressBar.setMax(totalFilesToProcess);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            int successCount = 0;
            int failCount = 0;

            for (Uri uri : uris) {
                try {
                    MusicFile musicFile = createMusicFileFromUri(uri);
                    db.musicFileDAO().insert(musicFile);
                    successCount++;

                    requireActivity().runOnUiThread(() -> {
                        processedFiles++;
                        progressBar.setProgress(processedFiles);
                        showFileUploadProgress(musicFile.getFileName(), processedFiles, totalFilesToProcess);
                    });
                } catch (Exception e) {
                    failCount++;
                    Log.e("Upload", "Error processing file: " + uri, e);
                    requireActivity().runOnUiThread(() -> {
                        processedFiles++;
                        progressBar.setProgress(processedFiles);
                    });
                }
            }

            int finalSuccessCount = successCount;
            int finalFailCount = failCount;
            requireActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                showBatchCompletion(finalSuccessCount, finalFailCount);
            });
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

        musicFile.setFilePath(saveAudioToPermanentStorage(uri));
        musicFile.setDuration(extractAudioDuration(uri));
        musicFile.setUserId(getCurrentUserId());

        return musicFile;
    }

    private String saveAudioToPermanentStorage(Uri uri) throws IOException {
        File musicDir = new File(requireContext().getFilesDir(), "music");
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }

        String originalName = getOriginalFileName(uri);
        String fileName = originalName != null ?
                sanitizeFileName(originalName) :
                "audio_" + System.currentTimeMillis() + ".mp3";

        File outputFile = new File(musicDir, fileName);

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        }

        return outputFile.getAbsolutePath();
    }

    private String getOriginalFileName(Uri uri) {
        try (Cursor cursor = requireContext().getContentResolver()
                .query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                return cursor.getString(nameIndex);
            }
        }
        return null;
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
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
                Log.e("Upload", "Error releasing MediaMetadataRetriever", e);
            }
        }
    }

    private void showFileUploadProgress(String fileName, int current, int total) {
        Toast.makeText(requireContext(),
                "(" + current + "/" + total + ") Processing: " + fileName,
                Toast.LENGTH_SHORT).show();
    }

    private void showBatchCompletion(int successCount, int failCount) {
        String message = String.format(
                "Upload complete! %d successful, %d failed",
                successCount, failCount
        );
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
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

    private void showFilePickerNotFoundError() {
        Toast.makeText(requireContext(),
                "No compatible file picker app found", Toast.LENGTH_LONG).show();
    }

    private void showNoFileSelectedMessage() {
        Toast.makeText(requireContext(),
                "No audio files selected", Toast.LENGTH_SHORT).show();
    }

    private void navigateToNextFragment() {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, new UploadImageFragment());
        transaction.commit();
    }

    private int getCurrentUserId() {
        // Implement your actual user ID retrieval logic here
        return 1; // Example - replace with real implementation
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}

//package com.example.vetappdraft;
//
//import static androidx.core.content.ContentProviderCompat.requireContext;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.media.MediaMetadataRetriever;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.provider.OpenableColumns;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class UploadMusicFragment extends Fragment {
//    // Contracts for modern API replacements
//    private ActivityResultLauncher<String[]> audioPickerLauncher;
//    private ActivityResultLauncher<String> permissionLauncher;
//    private VetDatabase db;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        db = VetDatabase.getInstance(requireContext());
//
//        // Initialize audio picker launcher (replaces startActivityForResult/onActivityResult)
//        audioPickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.OpenMultipleDocuments(),
//                this::handleMultipleAudioSelectionResult
//        );
//
//        // Initialize permission launcher (replaces requestPermissions)
//        permissionLauncher = registerForActivityResult(
//                new ActivityResultContracts.RequestPermission(),
//                this::handlePermissionResult
//        );
//    }
//
//    private void handleMultipleAudioSelectionResult(@Nullable List<Uri> uris) {
//        if (uris != null && !uris.isEmpty()) {
//            for (Uri uri : uris) {
//                processAudioFile(uri);
//            }
//        } else {
//            showNoFileSelectedMessage();
//        }
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_upload_music, container, false);
//        Button btnSelectMusic = view.findViewById(R.id.btn_select_music);
//        Button btnContinue = view.findViewById(R.id.btn_continue);
//
//        btnSelectMusic.setOnClickListener(v -> checkAndRequestPermissions());
//
//        btnContinue.setOnClickListener(v -> {
//            requireActivity().runOnUiThread(() -> {
//                FragmentTransaction transaction = requireActivity()
//                        .getSupportFragmentManager()
//                        .beginTransaction();
//                UploadImageFragment uIFragment = new UploadImageFragment();
//                transaction.replace(R.id.fragment_container, uIFragment);
//
//                transaction.commit();
//            });
//        });
//
//        return view;
//    }
//
//    private void checkAndRequestPermissions() {
//        if (hasRequiredPermissions()) {
//            safelyOpenFilePicker();
//        } else {
//            requestAppropriatePermission();
//        }
//    }
//
//    private boolean hasRequiredPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            return hasPermission(Manifest.permission.READ_MEDIA_AUDIO);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            return true; // No permission needed for Android 10+ with ACTION_GET_CONTENT
//        } else {
//            return hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//    }
//
//    private boolean hasPermission(String permission) {
//        return ContextCompat.checkSelfPermission(requireContext(), permission)
//                == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestAppropriatePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO);
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
//        } else {
//            safelyOpenFilePicker();
//        }
//    }
//
//    private void handlePermissionResult(boolean isGranted) {
//        if (isGranted) {
//            safelyOpenFilePicker();
//        } else {
//            showPermissionRationale();
//        }
//    }
//
//    private void showPermissionRationale() {
//        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
//            new AlertDialog.Builder(requireContext())
//                    .setTitle("Permission Needed")
//                    .setMessage("This permission is required to access your audio files")
//                    .setPositiveButton("Grant", (d, w) -> requestAppropriatePermission())
//                    .setNegativeButton("Cancel", null)
//                    .show();
//        } else {
//            showPermissionDeniedDialog();
//        }
//    }
//
//    private void showPermissionDeniedDialog() {
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Permission Permanently Denied")
//                .setMessage("Please enable audio access in app settings")
//                .setPositiveButton("Settings", (d, w) -> openAppSettings())
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
//
//    private void openAppSettings() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
//        startActivity(intent);
//    }
//
//    private void safelyOpenFilePicker() {
//        try {
//            audioPickerLauncher.launch(new String[]{"audio/*", "application/ogg"});
//        } catch (ActivityNotFoundException e) {
//            showFilePickerNotFoundError();
//        }
//    }
//
//    private void showFilePickerNotFoundError() {
//        Toast.makeText(requireContext(),
//                "No compatible file picker app found", Toast.LENGTH_LONG).show();
//    }
//
//    private void handleAudioSelectionResult(@Nullable Uri uri) {
//        if (uri != null) {
//            processAudioFile(uri);
//        } else {
//            showNoFileSelectedMessage();
//        }
//    }
//
//    private void processAudioFile(Uri uri) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(() -> {
//            try {
//                MusicFile musicFile = createMusicFileFromUri(uri);
//                db.musicFileDAO().insert(musicFile);
//                Log.d("Upload", "Inserted: " + musicFile.getFileName());
//                requireActivity().runOnUiThread(() ->
//                        showUploadSuccess(musicFile.getFileName()));
//            } catch (Exception e) {
//                Log.e("Upload", "Error inserting file", e);
//                requireActivity().runOnUiThread(() ->
//                        showUploadError(e.getMessage()));
//            }
//        });
//    }
//
//    private MusicFile createMusicFileFromUri(Uri uri) throws IOException {
//        MusicFile musicFile = new MusicFile();
//
//        try (Cursor cursor = requireContext().getContentResolver()
//                .query(uri, null, null, null, null)) {
//
//            if (cursor != null && cursor.moveToFirst()) {
//                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//
//                musicFile.setFileName(cursor.getString(nameIndex));
//                musicFile.setFileSize(cursor.getLong(sizeIndex));
//            }
//        }
//
//        musicFile.setFilePath(getRealPathFromUri(uri));
//        musicFile.setDuration(extractAudioDuration(uri));
//        musicFile.setUserId(getCurrentUserId());
//
//        return musicFile;
//    }
//
////    private String getRealPathFromUri(Uri uri) throws IOException {
////        File tempFile = File.createTempFile("audio_", ".tmp", requireContext().getCacheDir());
////
////        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri); FileOutputStream outputStream = new FileOutputStream(tempFile)) {
////
////            byte[] buffer = new byte[4 * 1024];
////            int read;
////
////            while ((read = inputStream.read(buffer)) != -1) {
////                outputStream.write(buffer, 0, read);
////            }
////
////            return tempFile.getAbsolutePath();
////        }
////    }
//
//    private String getRealPathFromUri(Uri uri) throws IOException {
//        File musicDir = new File(requireContext().getFilesDir(), "music");
//        if (!musicDir.exists()) {
//            musicDir.mkdirs();
//        }
//
//        String fileName = "audio_" + System.currentTimeMillis() + ".mp3";
//        File outputFile = new File(musicDir, fileName);
//
//        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
//             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
//            byte[] buffer = new byte[4 * 1024];
//            int read;
//            while ((read = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, read);
//            }
//        }
//
//        return outputFile.getAbsolutePath(); // Now points to a permanent file
//    }
//
//    private long extractAudioDuration(Uri uri) {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            retriever.setDataSource(requireContext(), uri);
//            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            return duration != null ? Long.parseLong(duration) : 0;
//        } catch (Exception e) {
//            return 0;
//        } finally {
//            try {
//                retriever.release();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private int getCurrentUserId() {
//        // Implement your user ID retrieval logic
//        return 1; // Example - replace with actual user ID
//    }
//
//    private void showUploadSuccess(String fileName) {
//        Toast.makeText(requireContext(),
//                "Uploaded: " + fileName, Toast.LENGTH_SHORT).show();
//    }
//
//    private void showUploadError(String error) {
//        Toast.makeText(requireContext(),
//                "Upload failed: " + error, Toast.LENGTH_LONG).show();
//    }
//
//    private void showNoFileSelectedMessage() {
//        Toast.makeText(requireContext(),
//                "No audio file selected", Toast.LENGTH_SHORT).show();
//    }
//}
