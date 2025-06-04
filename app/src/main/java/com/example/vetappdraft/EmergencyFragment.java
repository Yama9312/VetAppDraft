package com.example.vetappdraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * EmergencyFragment displays emergency contacts like 911 etc
 *
 * there's more to add to this fragments - mostly just textviews
 * @author ania
 */
public class EmergencyFragment extends Fragment {

  /**
   * Inflates the layout for the EmergencyFragment.
   *
   * @param inflater           LayoutInflater to inflate views in fragment
   * @param container          Parent view to attach fragment UI to
   * @param savedInstanceState Previous state, if any, of the fragment
   * @return                   The root view of the inflated layout
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_emergency, container, false);
  }

  /**
   * Called immediately after onCreateView has returned.
   *
   * @param view               The view returned by onCreateView
   * @param savedInstanceState Previous state, if any, of the fragment
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    VetDatabase db = VetDatabase.getInstance(requireContext());
    VetDAO dao = db.vetDAO();

    List<VetUser> contacts = dao.getAll();
    TextView emergencyTextView = view.findViewById(R.id.textViewFriend);

    if (contacts != null && !contacts.isEmpty()) {
      String emergencyNumber = contacts.get(0).getMcEContact();
      emergencyTextView.setText(emergencyNumber);
      emergencyTextView.setOnClickListener(v -> {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + emergencyNumber));
        startActivity(intent);
      });
    } else {
      emergencyTextView.setText("No emergency contact found");
      emergencyTextView.setOnClickListener(null);
    }
  }
}
