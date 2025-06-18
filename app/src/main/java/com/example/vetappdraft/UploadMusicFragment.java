package com.example.vetappdraft;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.File;

public class UploadMusicFragment extends Fragment {
    private static final int PICK_AUDIO_REQUEST = 101;
    private VetDatabase db;

    public UploadMusicFragment () {
        // Required empty public constructor
    }

//    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        db = VetDatabase.getInstance(requireContext());
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_upload_music, container, false);
        Button btnSelectMusic = view.findViewById(R.id.btn_select_music);
        Button btnContinue = view.findViewById(R.id.btn_continue);

        db = VetDatabase.getInstance(requireContext());

        btnSelectMusic.setOnClickListener(v -> {
            if (checkPermissions()) {
                openFilePicker();
            } else {
                requestPermissions();
            }
        });

        btnContinue.setOnClickListener(v -> {
            // goes to the upload picture page
        });

        return view;
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO}, PICK_AUDIO_REQUEST);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_AUDIO_REQUEST);
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                processSelectedFile(uri);
            }
        }
    }

    private void processSelectedFile(Uri uri) {
        try {
            String filePath = getRealPathFromURI(uri);
            if (filePath != null) {
                File file = new File(filePath);
                MusicFile musicFile = new MusicFile();
                musicFile.setFileName(file.getName());
                musicFile.setFilePath(filePath);
                musicFile.setFileSize(file.length());
                // musicFile.setUserId(currentUserId);

                // Extract duration (example using MediaMetadataRetriever)
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(filePath);
                String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long duration = Long.parseLong(durationStr);
                musicFile.setDuration(duration);
                retriever.release();

                // Save to Room DB in background
                new Thread(() -> db.musicFileDAO().insert(musicFile)).start();

                Toast.makeText(requireContext(), "File uploaded successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error processing file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Audio.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath(); // Fallback
    }
}
