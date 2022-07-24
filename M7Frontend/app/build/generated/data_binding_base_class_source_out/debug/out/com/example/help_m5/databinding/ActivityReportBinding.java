// Generated by view binder compiler. Do not edit!
package com.example.help_m5.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.help_m5.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityReportBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView RateFacilityDescription;

  @NonNull
  public final TextView RateFacilityTitle;

  @NonNull
  public final AppCompatButton cancelButtonReport;

  @NonNull
  public final CheckBox checkboxUser;

  @NonNull
  public final EditText editTextReport;

  @NonNull
  public final AppCompatButton submitButtonReport;

  private ActivityReportBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView RateFacilityDescription, @NonNull TextView RateFacilityTitle,
      @NonNull AppCompatButton cancelButtonReport, @NonNull CheckBox checkboxUser,
      @NonNull EditText editTextReport, @NonNull AppCompatButton submitButtonReport) {
    this.rootView = rootView;
    this.RateFacilityDescription = RateFacilityDescription;
    this.RateFacilityTitle = RateFacilityTitle;
    this.cancelButtonReport = cancelButtonReport;
    this.checkboxUser = checkboxUser;
    this.editTextReport = editTextReport;
    this.submitButtonReport = submitButtonReport;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityReportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityReportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_report, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityReportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.RateFacilityDescription;
      TextView RateFacilityDescription = ViewBindings.findChildViewById(rootView, id);
      if (RateFacilityDescription == null) {
        break missingId;
      }

      id = R.id.RateFacilityTitle;
      TextView RateFacilityTitle = ViewBindings.findChildViewById(rootView, id);
      if (RateFacilityTitle == null) {
        break missingId;
      }

      id = R.id.cancel_button_report;
      AppCompatButton cancelButtonReport = ViewBindings.findChildViewById(rootView, id);
      if (cancelButtonReport == null) {
        break missingId;
      }

      id = R.id.checkbox_user;
      CheckBox checkboxUser = ViewBindings.findChildViewById(rootView, id);
      if (checkboxUser == null) {
        break missingId;
      }

      id = R.id.editTextReport;
      EditText editTextReport = ViewBindings.findChildViewById(rootView, id);
      if (editTextReport == null) {
        break missingId;
      }

      id = R.id.submit_button_report;
      AppCompatButton submitButtonReport = ViewBindings.findChildViewById(rootView, id);
      if (submitButtonReport == null) {
        break missingId;
      }

      return new ActivityReportBinding((RelativeLayout) rootView, RateFacilityDescription,
          RateFacilityTitle, cancelButtonReport, checkboxUser, editTextReport, submitButtonReport);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
