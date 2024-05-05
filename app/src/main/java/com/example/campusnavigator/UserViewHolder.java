package com.example.campusnavigator;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusnavigator.models.User;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private TextView usernameTextView;
    private TextView emailTextView;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.UserName);
        emailTextView = itemView.findViewById(R.id.user_email);
    }

    public void bind(User user) {
        usernameTextView.setText(user.getName());
        Log.e("UserViewHolder", "Data received: " + user.getName() + ", " + user.getEmail());
        emailTextView.setText(user.getEmail());
    }
}
