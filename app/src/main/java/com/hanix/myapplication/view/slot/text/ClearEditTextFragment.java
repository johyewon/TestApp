package com.hanix.myapplication.view.slot.text;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.widget.clearEditText.ClearEditText;

public class ClearEditTextFragment extends Fragment {

    View rootView;
    ClearEditText clearEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clear_edit_text, container, false);

        clearEditText = rootView.findViewById(R.id.editText);
        // TextChangeListener 꼭 추가 해줘야 함 (RuntimeError 방지)
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }

}