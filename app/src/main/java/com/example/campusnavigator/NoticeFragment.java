package com.example.campusnavigator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeFragment extends Fragment implements NoticeAdapter.OnNoticeClickListener {

    private RecyclerView noticeRecyclerView;
    private DatabaseReference noticesRef;
    private List<NoticeItem> noticeList;
    private NoticeAdapter adapter;
    private TextView welcomeTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notice, container, false);

        noticeRecyclerView = root.findViewById(R.id.notice_recyclerView);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Firebase database reference
        noticesRef = FirebaseDatabase.getInstance().getReference().child("notices");

        // Initialize notice list
        noticeList = new ArrayList<>();

        // Initialize adapter
        adapter = new NoticeAdapter(noticeList, this);
        noticeRecyclerView.setAdapter(adapter);

        // Fetch notice data from Firebase
        fetchNoticeData();

        welcomeTextView = root.findViewById(R.id.welcome_text_view);

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setWelcomeText(welcomeTextView);
        }

        return root;
    }

    private void fetchNoticeData() {
        noticesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NoticeItem notice = snapshot.getValue(NoticeItem.class);
                    if (notice != null) {
                        noticeList.add(notice);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("NoticeFragment", "Error fetching notices", databaseError.toException());
            }
        });
    }

    @Override
    public void onNoticeClick(NoticeItem notice) {
        String username = welcomeTextView.getText().toString();

        Intent intent = new Intent(getActivity(), DetailsNoticeActivity.class);
        intent.putExtra("heading", notice.getHeading());
        intent.putExtra("date", notice.getDate());
        intent.putExtra("content", notice.getContent());
        intent.putExtra("imageUri", notice.getFileUrl());
        intent.putExtra("username", username);
        startActivity(intent);
    }


}

