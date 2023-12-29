package com.backend.expenseecho.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "is_default")
    private Boolean isDefault;

    public Category() {};

    // when a category is created without a user
    // it is default (accessible for all users)
    public Category(String name) {
        this.name = name;
        this.isDefault = true;
    }

    // When a user is set to the category, it means that the category
    // is not default
    public Category(String name, User user) {
        this.name = name;
        this.user = user;
        this.isDefault = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        setDefault(false);
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
