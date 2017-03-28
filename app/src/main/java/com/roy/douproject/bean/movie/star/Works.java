package com.roy.douproject.bean.movie.star;

import java.util.List;

public class Works {

    private List<String> roles;
    private Subject subject;

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

}