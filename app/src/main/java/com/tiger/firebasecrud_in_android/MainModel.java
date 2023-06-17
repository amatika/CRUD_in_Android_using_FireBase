package com.tiger.firebasecrud_in_android;

public class MainModel
{
    String name,email,course,turl;

    public MainModel()
    {

    }

    public MainModel(String name, String email, String course, String turl)
    {
        this.name = name;
        this.email = email;
        this.course = course;
        this.turl = turl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }
}
