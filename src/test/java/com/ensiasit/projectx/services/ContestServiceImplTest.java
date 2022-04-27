package com.ensiasit.projectx.services;

import com.ensiasit.projectx.mappers.ContestMapper;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.utils.Helpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ContestServiceImplTest {
    @Mock
    private ContestRepository contestRepository;

    @Mock
    private UserContestRoleRepository userContestRoleRepository;

    @Mock
    private AdminService adminService;

    @Mock
    private ContestMapper contestMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Helpers helpers;

    @InjectMocks
    private ContestServiceImpl contestService;

    @Test
    void test() {
        assertThat(1).isEqualTo(1);
    }
}