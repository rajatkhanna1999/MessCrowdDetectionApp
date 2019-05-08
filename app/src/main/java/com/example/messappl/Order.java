package com.example.messappl;

public class Order {
    String date;

    public Order() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Order(String date, String time, String info) {
        this.date = date;
        this.time = time;
        this.info = info;
    }

    String time;
    String info;
}
