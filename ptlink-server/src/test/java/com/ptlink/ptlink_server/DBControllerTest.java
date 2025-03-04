package com.ptlink.ptlink_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DBControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddUser() throws Exception {
        mockMvc.perform(post("/AddUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test_user\",\"email\":\"test@a.b\"}"))
            .andExpect(status().isOk());
    }

    @Test
    public void testFindUser() throws Exception {
        mockMvc.perform(get("/FindUser/test_user"))
        .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUser() throws Exception {
        mockMvc.perform(put("/UpdateUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test_user\",\"email\":\"test_update@a.b\"}"))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/DeleteUser").content("test_user"))
        .andExpect(status().isOk());
    }

}
