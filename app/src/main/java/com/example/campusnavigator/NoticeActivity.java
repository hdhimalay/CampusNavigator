package com.example.campusnavigator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NoticeActivity extends AppCompatActivity {

    private EditText noticeHeadingEditText;
    private EditText noticeContentEditText;
    private Button uploadButton;
    private Button submitButton;
    private TextView fileAttachStatusTextView;
    private TextView file_attach_and_uploaded_statusTextView;
    private TextView notice_uploaded_status_serverTextView;
    private static final int PICK_IMAGE_REQUEST = 1;

    // Firebase
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri fileUri;
    private boolean fileUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("notices");
        storageReference = FirebaseStorage.getInstance().getReference();

        // Initialize Views
        noticeHeadingEditText = findViewById(R.id.notice_heading);
        noticeContentEditText = findViewById(R.id.notice_content);
        uploadButton = findViewById(R.id.upload_notice_button);
        submitButton = findViewById(R.id.submit_notice_button);
        fileAttachStatusTextView = findViewById(R.id.file_attach_status);
        file_attach_and_uploaded_statusTextView=findViewById(R.id.file_attach_and_uploaded_status);
        notice_uploaded_status_serverTextView=findViewById(R.id.notice_uploaded_status_server);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open file picker or camera/gallery
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // You can adjust the type as needed for your use case (e.g., "application/pdf" for PDF files)
                startActivityForResult(intent, PICK_IMAGE_REQUEST); // Use PICK_IMAGE_REQUEST as the request code
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heading = noticeHeadingEditText.getText().toString().trim();
                String content = noticeContentEditText.getText().toString().trim();
                String fileUrl = fileAttachStatusTextView.getText().toString().trim();

                if (!heading.isEmpty() && !content.isEmpty()) {
                    // Create a notice object with current date
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
                    String currentDate = sdf.format(new Date());
                    Notice notice = new Notice(heading, content, fileUrl, currentDate);

                    // Push the notice object to Firebase Realtime Database under "notices" node
                    String noticeId = databaseReference.push().getKey(); // Generating a unique key
                    databaseReference.child(noticeId).setValue(notice); // Setting value under generated key

                    notice_uploaded_status_serverTextView.setVisibility(View.VISIBLE);
                    file_attach_and_uploaded_statusTextView.setVisibility(View.INVISIBLE);
                    // Display a Toast message
                    Toast.makeText(NoticeActivity.this, "Notice is uploaded in the database", Toast.LENGTH_SHORT).show();

                    // Reset fields
                    noticeHeadingEditText.setText("");
                    noticeContentEditText.setText("");
                    fileAttachStatusTextView.setText("");
                    fileUploaded = false;
                } else {
                    Toast.makeText(NoticeActivity.this, "Please fill heading and content fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();

            // Upload the selected file to Firebase Storage
            uploadFileToStorage(fileUri);
        }
    }

    private void uploadFileToStorage(Uri fileUri) {
        // Create a StorageReference
        StorageReference fileRef = storageReference.child("uploads").child(UUID.randomUUID().toString());

        fileRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded file
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Update the file URL with the download URL
                                String fileUrl = uri.toString();

                                // Log the file URL
                                Log.d("NoticeActivity", "File URL: " + fileUrl);

                                // Set the file URL to TextView
                                fileAttachStatusTextView.setText(fileUrl);
                                fileUploaded = true;
                                notice_uploaded_status_serverTextView.setVisibility(View.INVISIBLE);
                                file_attach_and_uploaded_statusTextView.setVisibility(View.VISIBLE);


                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                        Toast.makeText(NoticeActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("NoticeActivity", "Upload failed: " + e.getMessage());
                    }
                });
    }
}
