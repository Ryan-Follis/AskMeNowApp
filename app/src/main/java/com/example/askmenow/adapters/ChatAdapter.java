package com.example.askmenow.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askmenow.databinding.ItemContainerReceivedAnswerBinding;
import com.example.askmenow.databinding.ItemContainerReceivedQuestionBinding;
import com.example.askmenow.databinding.ItemContainerSentAnswerBinding;
import com.example.askmenow.databinding.ItemContainerSentMessageBinding;
import com.example.askmenow.databinding.ItemContainerReceivedMessageBinding;
import com.example.askmenow.databinding.ItemContainerSentQuestionBinding;
import com.example.askmenow.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private final String senderID;

    public static final int VIEW_TYPE_SENT_MESSAGE = 1;
    public static final int VIEW_TYPE_RECEIVED_MESSAGE = 2;
    public static final int VIEW_TYPE_SENT_QUESTION = 3;
    public static final int VIEW_TYPE_RECEIVED_QUESTION = 4;
    public static final int VIEW_TYPE_SENT_ANSWER = 5;
    public static final int VIEW_TYPE_RECEIVED_ANSWER = 6;

    public void setReceiverProfileImage(Bitmap bitmap){
        receiverProfileImage = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderID){
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT_MESSAGE){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        else if(viewType == VIEW_TYPE_RECEIVED_MESSAGE){
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        else if(viewType == VIEW_TYPE_SENT_QUESTION){
            return new SentQuestion
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT_MESSAGE){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }
        else{
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position){
        if(chatMessages.get(position).senderID.equals(senderID)){
            return VIEW_TYPE_SENT_MESSAGE;
        }
        else{
            return VIEW_TYPE_RECEIVED_MESSAGE;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainterReceivedMessageBinding){
            super(itemContainterReceivedMessageBinding.getRoot());
            binding = itemContainterReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.profileImage.setImageBitmap(receiverProfileImage);
            }
        }
    }

    static class SentQuestionViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentQuestionBinding binding;

        SentQuestionViewHolder(ItemContainerSentQuestionBinding itemContainerSentQuestionBinding){
            super(itemContainerSentQuestionBinding.getRoot());
            binding = itemContainerSentQuestionBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedQuestionViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedQuestionBinding binding;

        ReceivedQuestionViewHolder(ItemContainerReceivedQuestionBinding itemContainterReceivedQuestionBinding){
            super(itemContainterReceivedQuestionBinding.getRoot());
            binding = itemContainterReceivedQuestionBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.profileImage.setImageBitmap(receiverProfileImage);
            }
        }
    }

    static class SentAnswerViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentAnswerBinding binding;

        SentAnswerViewHolder(ItemContainerSentAnswerBinding itemContainerSentAnswerBinding){
            super(itemContainerSentAnswerBinding.getRoot());
            binding = itemContainerSentAnswerBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedAnswerViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedAnswerBinding binding;

        ReceivedAnswerViewHolder(ItemContainerReceivedAnswerBinding itemContainterReceivedAnswerBinding){
            super(itemContainterReceivedAnswerBinding.getRoot());
            binding = itemContainterReceivedAnswerBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.profileImage.setImageBitmap(receiverProfileImage);
            }
        }
    }

}
