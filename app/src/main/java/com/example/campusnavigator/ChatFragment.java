package com.example.campusnavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusnavigator.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private FirebaseRecyclerAdapter<User, UserViewHolder> adapter;
    private DatabaseReference usersRef;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize RecyclerView
        userRecyclerView = view.findViewById(R.id.user_recyclerView);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up FirebaseRecyclerAdapter
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && !currentUser.getEmail().equals("admin@igdtuw.ac.in")) {
            // Query to fetch all users except admin
            Query query = usersRef.orderByChild("email");
            FirebaseRecyclerOptions<User> options =
                    new FirebaseRecyclerOptions.Builder<User>()
                            .setQuery(query, User.class)
                            .build();

            adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                    // Bind User object to ViewHolder
                    holder.bind(model);

                    // Set OnClickListener for user name card
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the clicked user's ID and name
                            String clickedUserId = getRef(position).getKey();
                            String clickedUserName = model.getName();

                            // Pass the clicked user's ID and name to the ChatWindowFragment
                            Bundle bundle = new Bundle();
                            bundle.putString("receiverId", clickedUserId);
                            bundle.putString("receiverName", clickedUserName);

                            ChatWindowFragment chatWindowFragment;
                            chatWindowFragment = new ChatWindowFragment();
                            chatWindowFragment.setArguments(bundle);

                            // Navigate to the ChatWindowFragment
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, chatWindowFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                }


                @NonNull
                @Override
                public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    // Inflate the layout for each item in the RecyclerView
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
                    return new UserViewHolder(view);
                }
            };

            // Set the adapter to the RecyclerView
            userRecyclerView.setAdapter(adapter);
        } else {
            // Show toast indicating that only non-admin users can access this feature
            Toast.makeText(getContext(), "Only non-admin users can access this feature", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
