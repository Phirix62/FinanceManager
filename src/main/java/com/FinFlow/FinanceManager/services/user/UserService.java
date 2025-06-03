package com.FinFlow.FinanceManager.services.user;

import com.FinFlow.FinanceManager.dto.UserDTO;
import com.FinFlow.FinanceManager.entity.User;


public interface UserService {

    User registerUser(UserDTO userDTO);
    User loginUser(String username, String password);

}
