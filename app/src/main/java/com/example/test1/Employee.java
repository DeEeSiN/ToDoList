package com.example.test1;

public class Employee {
    private String login;
    private String password;
    private String value="";
    private String key;
    public Employee(){}
    public Employee(String login, String password, String value)
    {
        this.login = login;
        this.password = password;
        this.value = value;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

}
