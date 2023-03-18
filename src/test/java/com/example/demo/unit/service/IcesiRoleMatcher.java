package com.example.demo.unit.service;

import java.util.Objects;

import org.mockito.ArgumentMatcher;

import com.example.demo.model.IcesiRole;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IcesiRoleMatcher implements ArgumentMatcher<IcesiRole>{
    
    private IcesiRole icesiRole2;

    @Override
    public boolean matches(IcesiRole icesiRole1) {
        return icesiRole1.getRoleId() != null &&
        Objects.equals(icesiRole1.getDescription(), icesiRole2.getDescription()) &&
        Objects.equals(icesiRole1.getName(), icesiRole2.getName());
    }
}
