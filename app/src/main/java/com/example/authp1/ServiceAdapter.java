package com.example.authp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ServiceAdapter extends ArrayAdapter<Service> {

    private Context context;
    private List<Service> services;

    public ServiceAdapter(Context context, List<Service> services) {
        super(context, R.layout.service_item, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.service_item, parent, false);
        }

        Service service = services.get(position);

        TextView serviceName = convertView.findViewById(R.id.serviceName);
        TextView serviceCategory = convertView.findViewById(R.id.serviceCategory);

        serviceName.setText("Название: " + service.getServiceName());
        serviceCategory.setText("Категория: " + service.getCategory());

        return convertView;
    }
}