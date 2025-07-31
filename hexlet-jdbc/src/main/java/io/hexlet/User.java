package io.hexlet;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String phone;

    public User(String username, String phone) {
        this(null, username, phone);
    }

    public User(String username) {
        this(null, username, "");
    }

    public boolean isNew() {
        return this.id == null;
    }
}
