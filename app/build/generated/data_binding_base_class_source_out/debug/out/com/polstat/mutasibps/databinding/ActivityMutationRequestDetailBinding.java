// Generated by view binder compiler. Do not edit!
package com.polstat.mutasibps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.polstat.mutasibps.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMutationRequestDetailBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final MaterialButton btnDelete;

  @NonNull
  public final LinearLayout buttonContainer;

  @NonNull
  public final CardView cardAjuanMutasi;

  @NonNull
  public final CardView cardIdentitas;

  @NonNull
  public final RelativeLayout headerContainer;

  @NonNull
  public final TextView headerText;

  @NonNull
  public final CardView statusCard;

  @NonNull
  public final LinearLayout statusOptionContainer;

  @NonNull
  public final TextView tvJabatanSekarang;

  @NonNull
  public final TextView tvJabatanTujuan;

  @NonNull
  public final TextView tvKabupatenTujuan;

  @NonNull
  public final TextView tvNama;

  @NonNull
  public final TextView tvNip;

  @NonNull
  public final TextView tvProvinsiTujuan;

  @NonNull
  public final TextView tvStatus;

  @NonNull
  public final TextView tvStatusLabel;

  @NonNull
  public final TextView tvTitleAjuan;

  @NonNull
  public final TextView tvTitleIdentitas;

  @NonNull
  public final TextView tvUnitKerjaSekarang;

  @NonNull
  public final TextView tvUnitKerjaTujuan;

  private ActivityMutationRequestDetailBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageButton backButton, @NonNull MaterialButton btnDelete,
      @NonNull LinearLayout buttonContainer, @NonNull CardView cardAjuanMutasi,
      @NonNull CardView cardIdentitas, @NonNull RelativeLayout headerContainer,
      @NonNull TextView headerText, @NonNull CardView statusCard,
      @NonNull LinearLayout statusOptionContainer, @NonNull TextView tvJabatanSekarang,
      @NonNull TextView tvJabatanTujuan, @NonNull TextView tvKabupatenTujuan,
      @NonNull TextView tvNama, @NonNull TextView tvNip, @NonNull TextView tvProvinsiTujuan,
      @NonNull TextView tvStatus, @NonNull TextView tvStatusLabel, @NonNull TextView tvTitleAjuan,
      @NonNull TextView tvTitleIdentitas, @NonNull TextView tvUnitKerjaSekarang,
      @NonNull TextView tvUnitKerjaTujuan) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.btnDelete = btnDelete;
    this.buttonContainer = buttonContainer;
    this.cardAjuanMutasi = cardAjuanMutasi;
    this.cardIdentitas = cardIdentitas;
    this.headerContainer = headerContainer;
    this.headerText = headerText;
    this.statusCard = statusCard;
    this.statusOptionContainer = statusOptionContainer;
    this.tvJabatanSekarang = tvJabatanSekarang;
    this.tvJabatanTujuan = tvJabatanTujuan;
    this.tvKabupatenTujuan = tvKabupatenTujuan;
    this.tvNama = tvNama;
    this.tvNip = tvNip;
    this.tvProvinsiTujuan = tvProvinsiTujuan;
    this.tvStatus = tvStatus;
    this.tvStatusLabel = tvStatusLabel;
    this.tvTitleAjuan = tvTitleAjuan;
    this.tvTitleIdentitas = tvTitleIdentitas;
    this.tvUnitKerjaSekarang = tvUnitKerjaSekarang;
    this.tvUnitKerjaTujuan = tvUnitKerjaTujuan;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMutationRequestDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMutationRequestDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_mutation_request_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMutationRequestDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.btnDelete;
      MaterialButton btnDelete = ViewBindings.findChildViewById(rootView, id);
      if (btnDelete == null) {
        break missingId;
      }

      id = R.id.buttonContainer;
      LinearLayout buttonContainer = ViewBindings.findChildViewById(rootView, id);
      if (buttonContainer == null) {
        break missingId;
      }

      id = R.id.cardAjuanMutasi;
      CardView cardAjuanMutasi = ViewBindings.findChildViewById(rootView, id);
      if (cardAjuanMutasi == null) {
        break missingId;
      }

      id = R.id.cardIdentitas;
      CardView cardIdentitas = ViewBindings.findChildViewById(rootView, id);
      if (cardIdentitas == null) {
        break missingId;
      }

      id = R.id.headerContainer;
      RelativeLayout headerContainer = ViewBindings.findChildViewById(rootView, id);
      if (headerContainer == null) {
        break missingId;
      }

      id = R.id.headerText;
      TextView headerText = ViewBindings.findChildViewById(rootView, id);
      if (headerText == null) {
        break missingId;
      }

      id = R.id.statusCard;
      CardView statusCard = ViewBindings.findChildViewById(rootView, id);
      if (statusCard == null) {
        break missingId;
      }

      id = R.id.statusOptionContainer;
      LinearLayout statusOptionContainer = ViewBindings.findChildViewById(rootView, id);
      if (statusOptionContainer == null) {
        break missingId;
      }

      id = R.id.tvJabatanSekarang;
      TextView tvJabatanSekarang = ViewBindings.findChildViewById(rootView, id);
      if (tvJabatanSekarang == null) {
        break missingId;
      }

      id = R.id.tvJabatanTujuan;
      TextView tvJabatanTujuan = ViewBindings.findChildViewById(rootView, id);
      if (tvJabatanTujuan == null) {
        break missingId;
      }

      id = R.id.tvKabupatenTujuan;
      TextView tvKabupatenTujuan = ViewBindings.findChildViewById(rootView, id);
      if (tvKabupatenTujuan == null) {
        break missingId;
      }

      id = R.id.tvNama;
      TextView tvNama = ViewBindings.findChildViewById(rootView, id);
      if (tvNama == null) {
        break missingId;
      }

      id = R.id.tvNip;
      TextView tvNip = ViewBindings.findChildViewById(rootView, id);
      if (tvNip == null) {
        break missingId;
      }

      id = R.id.tvProvinsiTujuan;
      TextView tvProvinsiTujuan = ViewBindings.findChildViewById(rootView, id);
      if (tvProvinsiTujuan == null) {
        break missingId;
      }

      id = R.id.tvStatus;
      TextView tvStatus = ViewBindings.findChildViewById(rootView, id);
      if (tvStatus == null) {
        break missingId;
      }

      id = R.id.tvStatusLabel;
      TextView tvStatusLabel = ViewBindings.findChildViewById(rootView, id);
      if (tvStatusLabel == null) {
        break missingId;
      }

      id = R.id.tvTitleAjuan;
      TextView tvTitleAjuan = ViewBindings.findChildViewById(rootView, id);
      if (tvTitleAjuan == null) {
        break missingId;
      }

      id = R.id.tvTitleIdentitas;
      TextView tvTitleIdentitas = ViewBindings.findChildViewById(rootView, id);
      if (tvTitleIdentitas == null) {
        break missingId;
      }

      id = R.id.tvUnitKerjaSekarang;
      TextView tvUnitKerjaSekarang = ViewBindings.findChildViewById(rootView, id);
      if (tvUnitKerjaSekarang == null) {
        break missingId;
      }

      id = R.id.tvUnitKerjaTujuan;
      TextView tvUnitKerjaTujuan = ViewBindings.findChildViewById(rootView, id);
      if (tvUnitKerjaTujuan == null) {
        break missingId;
      }

      return new ActivityMutationRequestDetailBinding((ConstraintLayout) rootView, backButton,
          btnDelete, buttonContainer, cardAjuanMutasi, cardIdentitas, headerContainer, headerText,
          statusCard, statusOptionContainer, tvJabatanSekarang, tvJabatanTujuan, tvKabupatenTujuan,
          tvNama, tvNip, tvProvinsiTujuan, tvStatus, tvStatusLabel, tvTitleAjuan, tvTitleIdentitas,
          tvUnitKerjaSekarang, tvUnitKerjaTujuan);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
