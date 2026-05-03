package com.tp.gestiondepenses.ui.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentSettingsBinding;
import com.tp.gestiondepenses.notifications.NotificationScheduler;
import com.tp.gestiondepenses.utils.ExportUtils;
import com.tp.gestiondepenses.viewmodel.FinanceViewModel;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private FinanceViewModel viewModel;
    private SharedPreferences prefs;

    // Current chosen time for daily notification
    private int dailyHour   = 20;
    private int dailyMinute = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        prefs = requireContext().getSharedPreferences("FinanceTrackPrefs", Context.MODE_PRIVATE);

        // ----- Devise spinner -----
        String[] devises = {"FCFA (XOF)", "Euro (€)", "Dollar ($)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, devises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spDevise.setAdapter(adapter);

        // Load current prefs
        binding.etUsername.setText(prefs.getString("username", "Utilisateur"));
        String currentDevise = prefs.getString("devise", "FCFA");
        for (int i = 0; i < devises.length; i++) {
            if (devises[i].contains(currentDevise)) {
                binding.spDevise.setSelection(i);
                break;
            }
        }

        // ----- Dark mode -----
        binding.swDarkMode.setChecked(prefs.getBoolean("dark_mode", true));
        binding.swDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            int targetMode = isChecked
                    ? androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
                    : androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
            if (androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode() != targetMode) {
                com.tp.gestiondepenses.ui.activities.MainActivity.isChangingTheme = true;
                saveSettings();
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(targetMode);
            }
        });

        // ----- Notifications -----
        loadNotifPrefs();
        setupNotifListeners();

        // ----- Budget alerts -----
        binding.swBudgetAlerts.setChecked(prefs.getBoolean("budget_alerts", true));

        // ----- Profile Picture -----
        String profilePicUri = prefs.getString("profile_pic", null);
        if (profilePicUri != null) {
            binding.ivProfilePicSettings.setImageURI(android.net.Uri.parse(profilePicUri));
        }

        binding.btnChangePhoto.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // ----- Automatic Saving Listeners -----
        binding.etUsername.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) { saveSettings(); }
        });

        binding.spDevise.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { saveSettings(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        binding.spPinFrequency.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { saveSettings(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        binding.swBudgetAlerts.setOnCheckedChangeListener((v, isChecked) -> saveSettings());

        // ----- Navigation & buttons -----
        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(requireView()).navigateUp());
        binding.btnSave.setVisibility(View.GONE); // No longer needed as it saves automatically
        binding.btnReset.setOnClickListener(v -> showResetDialog());
        binding.btnExport.setOnClickListener(v -> handleExport());
    }

    private final androidx.activity.result.ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new androidx.activity.result.contract.ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            try {
                                // For persistence, we should ideally copy the file, 
                                // but for now we'll take persistable URI permission if possible or just save it
                                requireContext().getContentResolver().takePersistableUriPermission(uri, 
                                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (Exception e) {
                                // Ignore if not possible
                            }
                            prefs.edit().putString("profile_pic", uri.toString()).apply();
                            binding.ivProfilePicSettings.setImageURI(uri);
                        }
                    });

    // -------------------------------------------------------------------------
    // Notifications
    // -------------------------------------------------------------------------

    private void loadNotifPrefs() {
        boolean notifEnabled = prefs.getBoolean("notifications", false);
        dailyHour   = prefs.getInt("notif_daily_hour", 20);
        dailyMinute = prefs.getInt("notif_daily_minute", 0);

        binding.swNotifications.setChecked(notifEnabled);
        binding.llNotifConfig.setVisibility(notifEnabled ? View.VISIBLE : View.GONE);

        boolean dailyOn = prefs.getBoolean("notif_daily_enabled", false);
        binding.swDailyNotif.setChecked(dailyOn);
        binding.llDailyTime.setVisibility(dailyOn ? View.VISIBLE : View.GONE);
        updateTimeButton();

        // PIN Frequency
        String[] frequencies = {"À chaque lancement", "Une fois par jour", "Une fois par semaine", "Jamais"};
        android.widget.ArrayAdapter<String> freqAdapter = new android.widget.ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, frequencies);
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spPinFrequency.setAdapter(freqAdapter);
        
        String savedFreq = prefs.getString("pin_frequency", "À chaque lancement");
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i].equals(savedFreq)) {
                binding.spPinFrequency.setSelection(i);
                break;
            }
        }
    }

    private void setupNotifListeners() {
        // Master toggle → show/hide sub-options
        binding.swNotifications.setOnCheckedChangeListener((btn, isChecked) -> {
            binding.llNotifConfig.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            saveSettings();
        });

        // Daily toggle → show/hide time picker
        binding.swDailyNotif.setOnCheckedChangeListener((btn, isChecked) -> {
            binding.llDailyTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            saveSettings();
        });

        // Time picker button
        binding.btnPickTime.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(requireContext(),
                    (timePicker, hour, minute) -> {
                        dailyHour   = hour;
                        dailyMinute = minute;
                        updateTimeButton();
                        saveSettings();
                    },
                    dailyHour, dailyMinute, true /* 24h */);
            dialog.show();
        });
    }

    private void updateTimeButton() {
        binding.btnPickTime.setText(String.format(Locale.getDefault(), "%02d:%02d", dailyHour, dailyMinute));
    }

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    private void saveSettings() {
        String name   = binding.etUsername.getText().toString();
        String devise = binding.spDevise.getSelectedItem().toString();
        if (devise.contains("FCFA"))   devise = "FCFA";
        else if (devise.contains("Euro")) devise = "€";
        else if (devise.contains("Dollar")) devise = "$";

        boolean notifEnabled  = binding.swNotifications.isChecked();
        boolean dailyEnabled  = notifEnabled && binding.swDailyNotif.isChecked();
        boolean monthlyEnabled = notifEnabled && binding.swMonthlyNotif.isChecked();
        boolean yearlyEnabled  = notifEnabled && binding.swYearlyNotif.isChecked();

        prefs.edit()
                .putString("username", name)
                .putString("devise", devise)
                .putString("pin_frequency", binding.spPinFrequency.getSelectedItem().toString())
                .putBoolean("dark_mode", binding.swDarkMode.isChecked())
                .putBoolean("notifications", notifEnabled)
                .putBoolean("budget_alerts", binding.swBudgetAlerts.isChecked())
                .putBoolean("notif_daily_enabled", dailyEnabled)
                .putInt("notif_daily_hour", dailyHour)
                .putInt("notif_daily_minute", dailyMinute)
                .putBoolean("notif_monthly_enabled", monthlyEnabled)
                .putBoolean("notif_yearly_enabled", yearlyEnabled)
                .apply();

        // Request permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        // Apply scheduling
        NotificationScheduler.scheduleAll(requireContext(), prefs);

        Toast.makeText(getContext(), "Paramètres sauvegardés", Toast.LENGTH_SHORT).show();
    }

    // -------------------------------------------------------------------------
    // Export / Reset
    // -------------------------------------------------------------------------

    private void handleExport() {
        viewModel.getAllDepenses().observe(getViewLifecycleOwner(), depenses -> {
            if (depenses != null) {
                viewModel.getAllRevenus().observe(getViewLifecycleOwner(), revenus -> {
                    if (revenus != null) {
                        ExportUtils.exportTransactionsToCSV(requireContext(), depenses, revenus);
                        Toast.makeText(getContext(), "Exportation lancée...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showResetDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Réinitialiser les données")
                .setMessage("Voulez-vous supprimer toutes les transactions et budgets ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    viewModel.deleteAllData();
                    Toast.makeText(getContext(), "Données réinitialisées", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
