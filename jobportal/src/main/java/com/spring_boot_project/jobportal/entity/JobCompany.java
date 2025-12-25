package com.spring_boot_project.jobportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JobCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String namy;
    private String logo;

    public JobCompany() {
    }

    public JobCompany(Integer id, String namy, String logo) {
        Id = id;
        this.namy = namy;
        this.logo = logo;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNamy() {
        return namy;
    }

    public void setNamy(String namy) {
        this.namy = namy;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "JobCompany{" +
                "Id=" + Id +
                ", namy='" + namy + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
