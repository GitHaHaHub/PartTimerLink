package com.ptlink.ptlink_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInfo {
    private Long userId;
    private String username;
    private String email;
}
