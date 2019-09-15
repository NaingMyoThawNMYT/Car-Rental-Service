package com.yadanar.carrentalservice.model;

import java.io.Serializable;

public class RentedCar implements Serializable {
    private Customer customer;
    private Car car;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
