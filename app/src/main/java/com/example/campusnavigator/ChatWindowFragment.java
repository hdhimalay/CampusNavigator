package com.example.campusnavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusnavigator.models.Message;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatWindowFragment extends Fragment {

    private RecyclerView msgRecyclerView;
    private EditText textMessage;
    private CardView sendButton;
    private TextView nameTextView;

    private DatabaseReference messagesRef;
    private MessageAdapter adapter;

    private String receiverId;
    private String receiverName;

    public ChatWindowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database reference for messages
        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");

        // Retrieve receiver's ID and name from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            receiverId = bundle.getString("receiverId");
            receiverName = bundle.getString("receiverName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_window, container, false);

        // Initialize views
        msgRecyclerView = view.findViewById(R.id.msg_recyclerView);
        textMessage = view.findViewById(R.id.textmsg);
        sendButton = view.findViewById(R.id.sendbtn);
        nameTextView = view.findViewById(R.id.name);

        // Set receiver's name
        nameTextView.setText(receiverName);

        // Set up RecyclerView
        msgRecyclerView.setHasFixedSize(true);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sending message
                sendMessage();
            }
        });

        // Set up FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, Message.class)
                        .build();

        adapter = new MessageAdapter(options, receiverId);

        // Set the adapter to the RecyclerView
        msgRecyclerView.setAdapter(adapter);

        return view;
    }

    private void sendMessage() {
        String messageText = textMessage.getText().toString().trim(); // Retrieve message text
        if (!messageText.isEmpty()) {
            // Get the current user ID (sender ID)
            String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Create a Message object
            Message message = new Message(null, senderId, receiverId, messageText, System.currentTimeMillis());

            // Push the message to the database
            messagesRef.push().setValue(message);

            textMessage.setText(""); // Clear the message text after sending
        } else {
            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchNames() {
        Message message = new Message(); // Create a new Message object

        // Fetch sender name asynchronously
        message.fetchSenderName(new Message.OnNameFetchedListener() {
            @Override
            public void onNameFetched(String name) {
                message.setSenderName(name); // Set sender name when fetched
                adapter.notifyDataSetChanged(); // Notify the adapter of data changes
            }
        });

        // Fetch receiver name asynchronously
        message.fetchReceiverName(new Message.OnNameFetchedListener() {
            @Override
            public void onNameFetched(String name) {
                message.setReceiverName(name); // Set receiver name when fetched
                adapter.notifyDataSetChanged(); // Notify the adapter of data changes
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Start listening for FirebaseRecyclerAdapter
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop listening for FirebaseRecyclerAdapter
        adapter.stopListening();
    }
}
