package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.User;

public interface AdminService {
    void createAdmin();

    boolean isAdmin(String email);

    User getAdmin();
}
