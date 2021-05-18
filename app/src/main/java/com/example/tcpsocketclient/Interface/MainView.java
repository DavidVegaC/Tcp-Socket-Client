package com.example.tcpsocketclient.Interface;

import java.util.HashMap;

public interface MainView {
    void onActivityFragmentOk(HashMap<String, ?> map);
    void onActivityFragmentError(HashMap<String, ?> map);
}
