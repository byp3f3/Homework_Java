package com.example.authp1;

public class Service {
    private int serviceId;
    private String serviceName;
    private String category;

    public Service() {}

    public Service(int id, String name, String category) {
        this.serviceId = id;
        this.serviceName = name;
        this.category = category;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}