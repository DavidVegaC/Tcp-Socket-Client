package com.example.tcpsocketclient.Base;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class PresenterFragment {

    protected PresenterFragment(){

    }

    @CallSuper
    public void onCreate(@Nullable final Bundle savedInstanceState){

    }

    @CallSuper
    public void onCreateView(@NonNull final Bundle outState){

    }

}
