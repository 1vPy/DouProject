package com.roy.douproject.bean.other;

/**
 * Created by Administrator on 2017/3/16.
 */

public class User {
    private String username;
    private String password;

    public User(){

    }

    public User(String username,String password){
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
