package com.app.estockcard.view.admin.settings;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.view.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @BindView(R.id.contact_us)
    AppCompatTextView contactUsField;

    @BindView(R.id.user_guide)
    AppCompatTextView userGuideField;

    @BindView(R.id.terms_and_condition)
    AppCompatTextView termsAndConditionField;

    @BindView(R.id.policy_privacy)
    AppCompatTextView policyPrivacyField;

    @BindView(R.id.send_feedback)
    AppCompatTextView sendFeedBackField;

    private BaseActivity parent;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_settings, container, false);
        parent = (BaseActivity) getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Drawable drawable = ContextCompat.getDrawable(Objects.requireNonNull(getActivity()),R.drawable.ic_arrow_right);
        parent.setDrawablePreLollipop(contactUsField,null,null,drawable,null);
        parent.setDrawablePreLollipop(userGuideField,null,null,drawable,null);
        parent.setDrawablePreLollipop(termsAndConditionField,null,null,drawable,null);
        parent.setDrawablePreLollipop(policyPrivacyField,null,null,drawable,null);
        parent.setDrawablePreLollipop(sendFeedBackField,null,null,drawable,null);
    }

    @OnClick({R.id.edit_profile,R.id.store_settings,R.id.reset_data,R.id.printer_layout,R.id.struct_layout,R.id.backup_data
            , R.id.contact_us, R.id.user_guide, R.id.terms_and_condition, R.id.policy_privacy, R.id.send_feedback,
            R.id.logout})
    void onClick(View view){
        Intent intent;
        if (view.getId() == R.id.edit_profile ) {
            intent = new Intent(getActivity(),ProfileOwnerActivity.class);
            startActivityForResult(intent,101);
        }else  if (view.getId() == R.id.store_settings ) {
            intent = new Intent(getActivity(), StoreSettingsActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.reset_data ) {
            intent = new Intent(getActivity(),ResetDataActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.printer_layout ) {
            intent = new Intent(getActivity(), PrinterSettingsActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.struct_layout ) {
            intent = new Intent(getActivity(), StructSettingsActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.backup_data ) {
            intent = new Intent(getActivity(),BackupDataActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.contact_us ) {
            intent = new Intent(Intent.ACTION_SEND);
            String[] recipients={getString(R.string.appstr_service_mail)};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.setPackage(getString(R.string.appstr_mail_client_gm));
            intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.appstr_mail_subject_title));
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.appstr_mail_content_hint));
            intent.setType(getString(R.string.appstr_text_html));
            startActivityForResult(Intent.createChooser(intent, "Send mail"),101);
        }else  if (view.getId() == R.id.user_guide ) {
            intent = new Intent(getActivity(),UserGuideActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.terms_and_condition ) {
            intent = new Intent(getActivity(),TermsConditionActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.policy_privacy ) {
            intent = new Intent(getActivity(),PrivacyPolicyActivity.class);
            startActivity(intent);
        }else  if (view.getId() == R.id.send_feedback ) {
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
            }
        } else if (view.getId() == R.id.logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            DBManager dbManager = ((BaseActivity) getActivity()).dbManager;
            ((BaseActivity) getActivity()).dbManager.deleteUser(dbManager.getUser(), new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    SysLog.getInstance().sendLog(getClass().getSimpleName(), " success remove ");
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

                @Override
                public void onError(String error) {

                }
            });

        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onSettingsFragmentInteraction();
    }
}
