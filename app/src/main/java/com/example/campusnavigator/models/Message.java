package com.example.campusnavigator.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String messageText;
    private long timestamp;
    private String senderName;
    private String receiverName;

    // Constructors

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String messageId, String senderId, String receiverId, String messageText, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getters and setters

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    // Method to fetch sender and receiver names asynchronously
    public void fetchNames(final OnNamesFetchedListener listener) {
        // Implement fetching sender and receiver names asynchronously
    }

    // Method to get message with sender and receiver names
    public String getMessage() {
        // Implement logic to get message with sender and receiver names
        return null;
    }

    // Listener interface for name fetching
    public interface OnNameFetchedListener {
        void onNameFetched(String name);
    }

    // Listener interface for names fetching completion
    public interface OnNamesFetchedListener {
        void onNamesFetched();
    }


    public void fetchSenderName(final OnNameFetchedListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(senderId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    if (listener != null) {
                        listener.onNameFetched(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    // Method to fetch receiver name asynchronously
    public void fetchReceiverName(final OnNameFetchedListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(receiverId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    if (listener != null) {
                        listener.onNameFetched(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
