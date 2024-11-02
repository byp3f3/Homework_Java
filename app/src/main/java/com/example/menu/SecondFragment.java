package com.example.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment {

    private TextView counterTextView;
    private int counter = 0;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondfragment, container, false);

        counterTextView = view.findViewById(R.id.counterTextView);
        Button showPopupMenuButton = view.findViewById(R.id.showPopupMenuButton);

        showPopupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        return view;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.increment) {
                    counter++;
                    updateCounter();
                    return true;
                }
                else if(item.getItemId() == R.id.decrement) {
                    counter--;
                    updateCounter();
                    return true;
                }
                else return false;
            }
        });

        popupMenu.show();
    }

    private void updateCounter() {
        counterTextView.setText("Счетчик: " + counter);
    }
}

