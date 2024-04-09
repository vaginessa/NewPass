package com.gero.newpass.view.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gero.newpass.R;
import com.gero.newpass.databinding.FragmentSettingsBinding;
import com.gero.newpass.encryption.EncryptionHelper;
import com.gero.newpass.view.activities.MainViewActivity;



public class SettingsFragment extends Fragment {
    private ImageButton buttonBack;
    private ImageView IVGithub, IVShare, IVContact, IVLanguage;
    private FragmentSettingsBinding binding;

    private SharedPreferences sharedPreferences;

    private Boolean isDarkModeSet;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(binding);

        Activity activity = this.getActivity();

        buttonBack.setOnClickListener(v -> {
            if (activity instanceof MainViewActivity) {
                ((MainViewActivity) activity).onBackPressed();
            }
        });

        IVGithub.setOnClickListener(v -> {
            String url = "https://github.com/6eero/NewPass";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        IVShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_text));
            startActivity(Intent.createChooser(shareIntent, "Share with..."));
        });

        IVContact.setOnClickListener(v -> {
            String url = "https://t.me/geroED";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //Dark mode toggle button listener
        binding.toggleDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !isDarkModeSet) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);
                editor.putBoolean(
                        "isDarkModeOn", true);
                editor.apply();
            } else if (!isChecked && isDarkModeSet) {
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                editor.putBoolean(
                        "isDarkModeOn", false);
                editor.apply();
            }
            if (activity instanceof MainViewActivity) {
                ((MainViewActivity) activity).updateNavigationBarColor(isChecked);
            }
        });

        IVLanguage.setOnClickListener(v -> {

            final String[] languages = getResources().getStringArray(R.array.language_options);

            String currentLanguage = sharedPreferences.getString("language", "");
            Log.i("234523", "current: " + currentLanguage);

            AlertDialog.Builder builder = new AlertDialog.Builder(this.requireContext());
            builder.setTitle("Select your language");
            builder.setSingleChoiceItems(languages, -1, (dialog, which) -> {
                String selectedLanguage = languages[which];

                Log.i("234523", "switching to " + selectedLanguage.toLowerCase().substring(0,2));
                editor.putString("language", selectedLanguage.toLowerCase().substring(0,2));
                editor.apply();
                dialog.dismiss();

                //TODO: refresh UI

            });
            builder.setNegativeButton(R.string.update_alertdialog_no, (dialogInterface, i) -> {

            });
            builder.create().show();
        });
    }

    private void initViews(FragmentSettingsBinding binding) {
        buttonBack = binding.backButton;
        IVGithub = binding.imageViewGithub;
        IVShare = binding.imageViewShare;
        IVContact = binding.imageViewContact;
        IVLanguage = binding.imageViewLanguage;

        //Dark mode toggle initialization, and language mode
        sharedPreferences = EncryptionHelper.getSharedPreferences(this.requireActivity().getApplicationContext());

        isDarkModeSet = sharedPreferences.getBoolean("isDarkModeOn", true);

        binding.toggleDarkMode.setChecked(isDarkModeSet);
    }
}