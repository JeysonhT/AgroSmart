package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.FragmentNewsInfoBinding;

public class NewsInfoFragment extends Fragment {

    private final String TAG = "NEWS_INFO_FRAGMENT";

    private FragmentNewsInfoBinding binding;

    public NewsInfoFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNewsInfoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle!=null){
            Bitmap image = ImageCacheManager.loadImageFromCache(getContext(),
                    bundle.getString("image"));
            String title = bundle.getString("title");
            String publicationDate = bundle.getString("publicationDate");
            String author = bundle.getString("author");
            String content = bundle.getString("content");

            try{
                binding.newImage.
                        setImageBitmap(image);
                binding.newTitle.setText(title);
                binding.newDate.setText(publicationDate);
                binding.newAuthor.setText(author);
                binding.newContent.setText(content);
            } catch(NullPointerException e){
                Log.println(Log.ERROR, TAG, "Error: " + e.getMessage());
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ImageCacheManager.cleanupCache(getContext());
    }
}