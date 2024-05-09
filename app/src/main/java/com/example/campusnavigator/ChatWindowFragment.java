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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");

        Bundle bundle = getArguments();
        if (bundle != null) {
            receiverId = bundle.getString("receiverId");
            receiverName = bundle.getString("receiverName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_window, container, false);

        msgRecyclerView = view.findViewById(R.id.msg_recyclerView);
        textMessage = view.findViewById(R.id.textmsg);
        sendButton = view.findViewById(R.id.sendbtn);
        nameTextView = view.findViewById(R.id.name);

        nameTextView.setText(receiverName);


        msgRecyclerView.setHasFixedSize(true);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String messageText = textMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {

            String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();


            Message message = new Message(null, senderId, receiverId, messageText, System.currentTimeMillis());


            messagesRef.push().setValue(message);

            textMessage.setText("");
        } else {
            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchNames() {
        Message message = new Message();


        message.fetchSenderName(new Message.OnNameFetchedListener() {
            @Override
            public void onNameFetched(String name) {
                message.setSenderName(name);
                adapter.notifyDataSetChanged();
            }
        });

        // Fetch receiver name asynchronously
        message.fetchReceiverName(new Message.OnNameFetchedListener() {
            @Override
            public void onNameFetched(String name) {
                message.setReceiverName(name);
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
