package com.example.tallerjpa.unit.service.matcher;

import com.example.tallerjpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class UserMatcher implements ArgumentMatcher<IcesiUser> {

    private IcesiUser icesiUserLeft;

    @Override
    public boolean matches(IcesiUser UserRight) {
        return UserRight.getUserId() != null && UserRight.getIcesiRole() != null &&
                Objects.equals(UserRight.getFirstName(), icesiUserLeft.getFirstName()) &&
                Objects.equals(UserRight.getLastName(), icesiUserLeft.getLastName()) &&
                Objects.equals(UserRight.getPassword(), icesiUserLeft.getPassword()) &&
                Objects.equals(UserRight.getEmail(), icesiUserLeft.getEmail()) &&
                Objects.equals(UserRight.getPhoneNumber(), icesiUserLeft.getPhoneNumber());
    }

}
