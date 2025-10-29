package com.ash.spring_boot_microservice.entity;

import java.util.List;

public class SoftwareEngineer {
    private Integer Id;
    private String name;
    private List<String> techStack;

    public SoftwareEngineer() {
    }

    public SoftwareEngineer(Integer id, String name, List<String> techStack) {
        Id = id;
        this.name = name;
        this.techStack = techStack;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTechStack() {
        return techStack;
    }

    public void setTechStack(List<String> techStack) {
        this.techStack = techStack;
    }
}
