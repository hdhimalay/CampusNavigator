package com.example.campusnavigator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusnavigator.models.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder> {
    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;
    private Context context;
    private String receiverId;




    public MessageAdapter(@NonNull FirebaseRecyclerOptions<Message> options, String receiverId) {
        super(options);
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == ITEM_SEND) {
            View sendView = inflater.inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(sendView);
        } else {
            View receiveView = inflater.inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(receiveView);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Message message) {
        if (holder instanceof SenderViewHolder) {
            if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    && message.getReceiverId().equals(receiverId)) {
                ((SenderViewHolder) holder).bind(message);
                Log.d("MessageAdapter", "onBindViewHolder for sender: " + message.getMessageText());
            } else {
                holder.itemView.setVisibility(View.GONE); // Hide messages not sent by the current user to the selected receiver
            }
        } else if (holder instanceof ReceiverViewHolder) {
            if (message.getSenderId().equals(receiverId)
                    && message.getReceiverId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                ((ReceiverViewHolder) holder).bind(message);
                Log.d("MessageAdapter", "onBindViewHolder for receiver: " + message.getMessageText());
            } else {
                holder.itemView.setVisibility(View.GONE); // Hide messages not received by the current user from the selected sender
            }
        }
    }



    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        if (message != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (currentUserId.equals(message.getSenderId())) {
                return ITEM_SEND;
            } else {
                return ITEM_RECEIVE;
            }
        }
        return super.getItemViewType(position);
    }


    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView firstLetterTxtView;
        TextView msgtxt;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            firstLetterTxtView = itemView.findViewById(R.id.sender_name_first_letter);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }

        public void bind(Message message) {
            if (message != null) {
                msgtxt.setText(message.getMessageText());
                Log.d("SenderViewHolder", "Sender Name: " + message.getSenderName());
                if (message.getSenderName() != null && !message.getSenderName().isEmpty()) {
                    firstLetterTxtView.setText(String.valueOf(message.getSenderName().charAt(0)));
                }
            }
        }

    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView firstLetterTxtView;
        TextView msgtxt;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            firstLetterTxtView = itemView.findViewById(R.id.receiver_name_first_letter);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }

        public void bind(Message message) {
            if (message != null) {
                msgtxt.setText(message.getMessageText());
                Log.d("ReceiverViewHolder", "Receiver Name: " + message.getReceiverName());
                if (message.getReceiverName() != null && !message.getReceiverName().isEmpty()) {
                    firstLetterTxtView.setText(String.valueOf(message.getReceiverName().charAt(0)));
                }
            }
        }

    }

}
