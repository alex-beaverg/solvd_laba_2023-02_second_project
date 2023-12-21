package com.solvd.delivery_service.domain.structure;

import java.util.Objects;

public class Department {
    private Long id;
    private String title;

    public Department() {}

    public Department(String title) {
        this.title = title;
    }

    public Department(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Department: [Department id = " + id + ", Department title = " + title + "]";
    }
}
