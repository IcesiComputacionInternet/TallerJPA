package com.edu.icesi.demojpa.Enum;

public enum RoleType {
    STUDENT("Student"),
    TEACHER("Teacher");

    private final String roleType;

    private RoleType(String roleType){
        this.roleType = roleType;
    }

    public String getRoleType() {
        return this.roleType;
    }
}
