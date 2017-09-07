package net.scero.test.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class User {
    private String user;
    private int userId;
    private String role;
}
