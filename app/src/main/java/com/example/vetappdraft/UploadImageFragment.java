package com.example.vetappdraft;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.vetappdraft.R;
import com.example.vetappdraft.VetDatabase;
import com.example.vetappdraft.ImageFile;
import java.io.File;

public class UploadImageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 102;
    private VetDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);
        Button btnSelectImage = view.findViewById(R.id.btn_select_image);
        Button btnContinue = view.findViewById(R.id.btnContinue);
        db = VetDatabase.getInstance(requireContext());

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnContinue.setOnClickListener(v -> {
            requireActivity().runOnUiThread(() -> {
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, DynamicPageFragment.newInstance(0));

                transaction.commit();
            });
        });
        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) processSelectedImage(uri);
        }
    }

    private void processSelectedImage(Uri uri) {
        try {
            String filePath = getRealPathFromURI(uri);
            if (filePath != null) {
                ImageView ivPreview = requireView().findViewById(R.id.iv_preview);
                ivPreview.setImageURI(uri);
                ivPreview.setVisibility(View.VISIBLE);

                File file = new File(filePath);
                ImageFile imageFile = new ImageFile();
                imageFile.setFileName(file.getName());
                imageFile.setFilePath(filePath);
                imageFile.setFileSize(file.length());
                //imageFile.setUserId(getCurrentUserId());  // Implement this method

                // Optional: Extract image dimensions
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                imageFile.setWidth(options.outWidth);
                imageFile.setHeight(options.outHeight);

                new Thread(() -> db.imageFileDAO().insert(imageFile)).start();
                Toast.makeText(requireContext(), "Image uploaded!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }
}
