package com.tp.gestiondepenses.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentAuthBinding;
import com.tp.gestiondepenses.utils.SecurityUtils;

public class AuthFragment extends Fragment {

    private FragmentAuthBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("FinanceTrackPrefs", Context.MODE_PRIVATE);
        String savedPin = prefs.getString("user_pin", null);
        String savedName = prefs.getString("username", null);
        String freq = prefs.getString("pin_frequency", "À chaque lancement");
        long lastLogin = prefs.getLong("last_pin_timestamp", 0);

        // Check if we can skip the PIN
        if (savedPin != null) {
            long diff = System.currentTimeMillis() - lastLogin;
            boolean skip = false;
            
            // Grace period of 30 seconds for all frequencies
            if (diff < 30 * 1000) {
                skip = true;
            } else {
                if (freq.equals("Une fois par jour") && diff < 24L * 3600 * 1000) skip = true;
                else if (freq.equals("Une fois par semaine") && diff < 7L * 24 * 3600 * 1000) skip = true;
                else if (freq.equals("Jamais")) skip = true;
            }

            if (skip) {
                Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
                return;
            }
        }

        // UI Adjustments based on state
        if (savedPin != null) {
            // LOGIN MODE
            binding.tvWelcomeBack.setText("Bon retour !");
            binding.tvAuthDesc.setText("Entrez votre code PIN pour continuer");
            binding.tilUsername.setVisibility(View.GONE);
            binding.btnLogin.setText("Se connecter");
        } else {
            // REGISTRATION MODE
            binding.tvWelcomeBack.setText("Bienvenue !");
            binding.tvAuthDesc.setText("Créez votre profil pour commencer");
            binding.tilUsername.setVisibility(View.VISIBLE);
            binding.btnLogin.setText("Créer mon compte");
        }

        binding.btnLogin.setOnClickListener(v -> {
            String pin = binding.etPin.getText().toString().trim();
            
            if (pin.length() < 4) {
                binding.tilPin.setError("Le PIN doit faire 4 chiffres");
                return;
            }

            if (savedPin == null) {
                // Registering
                String name = binding.etUsername.getText().toString().trim();
                if (name.isEmpty()) {
                    binding.tilUsername.setError("Veuillez entrer votre nom");
                    return;
                }
                prefs.edit()
                        .putString("username", name)
                        .putString("user_pin", SecurityUtils.hashPin(pin))
                        .putLong("last_pin_timestamp", System.currentTimeMillis())
                        .apply();
                
                Toast.makeText(getContext(), "Compte créé, bienvenue " + name + " !", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
            } else {
                // Logging in
                if (SecurityUtils.hashPin(pin).equals(savedPin) || pin.equals(savedPin)) { // pin.equals is for backward compatibility with unhashed pins
                    // Si le PIN correspond (ou s'il est au format non haché de l'ancienne version, on le met à jour en haché)
                    if (pin.equals(savedPin)) {
                        prefs.edit().putString("user_pin", SecurityUtils.hashPin(pin)).apply();
                    }
                    prefs.edit().putLong("last_pin_timestamp", System.currentTimeMillis()).apply();
                    Toast.makeText(getContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
                } else {
                    binding.tilPin.setError("Code PIN incorrect");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
