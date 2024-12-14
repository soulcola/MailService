package ru.javaops.masterjava.model;

import com.google.common.base.Objects;

public class User {
    protected Integer id;
    protected String fullName;
    protected String email;
    protected UserFlag flag;

    public User(Integer id, String fullName, String email, UserFlag flag) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.flag = flag;
    }

    public User(String fullName, String email, UserFlag flag) {
        this(null, fullName, email, flag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserFlag getFlag() {
        return flag;
    }

    public void setFlag(UserFlag flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id) && Objects.equal(fullName, user.fullName) && Objects.equal(email, user.email) && flag == user.flag;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, fullName, email, flag);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", flag=" + flag +
                '}';
    }
}
