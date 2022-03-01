package ai.digital.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStreamReader;

import ai.digital.sample.databinding.SplashFragmentBinding;

public class SplashFragment extends Fragment {

    private SplashFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SplashFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSecretMessage(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setSecretMessage(@NonNull View view) {
        try (InputStreamReader reader = new InputStreamReader(view.getContext().getAssets().open("message.txt"))) {
            final int BUFFER_SIZE = 512;
            char[] bytes = new char[BUFFER_SIZE];
            reader.read(bytes, 0, BUFFER_SIZE);
            binding.textView.setText(new String(bytes));
        } catch (IOException e) { }
    }
}