package com.example.lab7;

public class MyCallLog
{
    String name, number, time, date, type;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MyCallLog() {
        this.name = name;
        this.number = number;
        this.time = time;
        this.date = date;
        this.type = type;
    }

    public String toString()
    {
        return name+ "\n"+
                number+"\n"+time+"\n"+date+"\n"+type;
    }
}
