package com.example.tcpsocketclient.View.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.tcpsocketclient.R;

public class ViewImageExtended extends AppCompatDialogFragment {

    //public Bitmap PICTURE_SELECTED;
    public Uri imageUri;
    public static ViewImageExtended newInstance(Bundle arguments) {
        Bundle args = arguments;
        ViewImageExtended fragment = new ViewImageExtended();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewImageExtended() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Creado1", "11");
        // Esta linea de código hace que tu DialogFragment sea Full screen
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar);
        Bundle arguments = getArguments();
        //PICTURE_SELECTED = arguments.getParcelable("PICTURE_SELECTED");
        imageUri = arguments.getParcelable("PICTURE_SELECTED");
        Log.v("Asignado", imageUri.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_view_image, container, false);
        Log.v("Creado2", "22");
        ImageView ivImage = (ImageView)view.findViewById(R.id.ivImage);

        //if(PICTURE_SELECTED != null)
        //    ivImage.setImageBitmap(PICTURE_SELECTED);

        if(imageUri != null) {
            Log.v("Extendido", imageUri.toString());
            ivImage.setImageURI(imageUri);
        }

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                // Aqui puedes capturar el OnBackPressed
                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}