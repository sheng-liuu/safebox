package com.securitish.safebox;

import com.securitish.safebox.Service.SafeBoxService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.securitish.safebox.ApplicationTestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc
public class SafeBoxBetaResourceTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected SafeBoxService safeBoxService;


    private static JSONArray jsonArray = new JSONArray();

    @BeforeAll
    public static void setup() throws Exception {
        // Mock Entities
        jsonArray.put("Safebox content 01");
        jsonArray.put("Safebox content 02");
        jsonArray.put("Safebox content 03");
    }

    // create safebox
    @Test
    void create_safe_box_return_200() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "Secure safebox 01");
        safeBoxJson.put("password", "extremelySecurePassword1!");

        var request = post(SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());
        //mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void create_safe_box_already_exists_return_409() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "safebox1");
        safeBoxJson.put("password", "extremelySecurePassword1!");

        var request = post(SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());
        mvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    void create_safe_box_with_password_unsafe_return_400() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "unsecure safebox");
        safeBoxJson.put("password", "password");

        var request = post(SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_safe_box_without_password_return_400() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "Secure safebox 01");

        var request = post(SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_safe_box_without_name_return_400() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("password", "extremelySecurePassword1!");

        var request = post(SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    // get content of safebox
    @Test
    @WithMockUser(username = "safebox1", password = "safebox1_123456")
    void get_content_200() throws Exception {
        var request = get(EXIST_SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void get_content_without_basic_auth_401() throws Exception {
        var request = get(EXIST_SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "safebox1", password = "safebox1_123456")
    void get_content_of_safebox_not_exists_404() throws Exception {
        var request = get(NOT_EXIST_SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    // add content
    @Test
    @WithMockUser(username = "safebox1", password = "safebox1_123456")
    void add_content_200() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("items", jsonArray);

        var request = put(EXIST_SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void add_content_without_basic_auth_401() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("items", jsonArray);

        var request = put(EXIST_SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "safebox1", password = "safebox1_123456")
    void add_content_to_safebox_not_exists_404() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("items", jsonArray);

        var request = put(NOT_EXIST_SAFEBOX_ENDPOINT_BETA)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
