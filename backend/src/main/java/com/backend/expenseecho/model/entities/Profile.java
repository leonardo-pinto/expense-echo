package com.backend.expenseecho.model.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private String avatar;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Profile() {
    }

    public Profile(String name, String avatar, User user) {
        this.user = user;
        this.name = name;
        this.avatar = avatar;
    }

    public Profile(String name, String avatar, User user, int id) {
        this.user = user;
        this.name = name;
        this.avatar = avatar;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
