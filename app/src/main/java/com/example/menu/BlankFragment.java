package com.example.menu;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.menu.R;

public class BlankFragment extends Fragment {

    TextView editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        editText = view.findViewById(R.id.editText);
        registerForContextMenu(editText);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_text && editText.getText() != "Мандарины") {
            editText.setText("Мандарины");
            return true;
        }
        else if(item.getItemId() == R.id.change_text && editText.getText() == "Мандарины"){
            editText.setText("Тыкните сюда чтобы изменить текст");
            return true;
        }
        return super.onContextItemSelected(item);
    }
}