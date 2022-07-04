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
import org.springframework.test.web.servlet.MvcResult;

import static com.securitish.safebox.ApplicationConstants.AUTHORIZATION_HEADER;
import static com.securitish.safebox.ApplicationTestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@AutoConfigureMockMvc
public class SafeBoxV1ResourceTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected SafeBoxService safeBoxService;

    private static String expiredAuthorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWZlYm94MSIsImlhdCI6MTY1NjgwMjgxNSwiZXhwIjoxNjU2ODAyOTk1fQ.At4Lo69IBNkd9UTDXMC72XQ3n4KmRcf1xl1diluibFM";
    private static JSONArray jsonArray = new JSONArray();

    @BeforeAll
    public static void setup() throws Exception {
        // Mock Entities
        jsonArray.put("Safebox content 01");
        jsonArray.put("Safebox content 02");
        jsonArray.put("Safebox content 03");
    }
    // tests of api version 1
    // create safebox
    @Test
    void create_safe_box_v1_return_200() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "Secure safebox 01");
        safeBoxJson.put("password", "extremelySecurePassword1!");

        var request = post(SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void create_safe_box_v1_already_exists_return_409() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "safebox1");
        safeBoxJson.put("password", "extremelySecurePassword1!");

        var request = post(SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    void create_safe_box_v1_without_password_return_400() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "Secure safebox 01");

        var request = post(SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_safe_box_v1_without_name_return_400() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("password", "extremelySecurePassword1!");

        var request = post(SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_safe_box_v1_with_unsafe_password_return_400() throws Exception {
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("name", "unsecure safebox");
        safeBoxJson.put("password", "unsafePassword");

        var request = post(SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    // open safebox
    @Test
    @WithMockUser(username = "safebox1", password = "safebox1_123456")
    void open_safe_box_v1_return_200() throws Exception {

        var request = get(OPEN_EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "safebox1", password = "safebox1_123456")
    void open_safe_box_v1_not_exists_return_404() throws Exception {

        var request = get(OPEN_NOT_EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void open_safe_box_v1_without_basic_auth_return_401() throws Exception {
        var request = get(OPEN_EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void open_safe_box_v1_locked_return_423() throws Exception {
        var request = get(OPEN_EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request);
        mvc.perform(request);
        mvc.perform(request);
        mvc.perform(request).andExpect(status().isLocked());
    }

    // get token
    String get_token(String endpoint) throws Exception {
        var tokenRequest = get(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(tokenRequest).andReturn();
        if(result.getResponse().getStatus() == 200) {
            String content = result.getResponse().getContentAsString();
            return content.split(":")[1].replace("\"", "").replace("}", "");
        }
        return null;
    }

    // get content
    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void get_content_v1_return_200() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);

        var request = get(EXIST_SAFEBOX_ENDPOINT_V1_2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void get_content_v1_without_token_return_401() throws Exception {
        var request = get(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get_content_v1_with_expired_token_return_401() throws Exception {
        var request = get(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, "Bearer " + expiredAuthorization);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void get_content_v1_token_not_match_return_401() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);

        var request = get(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void get_content_v1_of_safebox_not_exist_return_404() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);

        var request = get(NOT_EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void get_content_v1_safebox_locked_return_200() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);

        var request = get(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isLocked());
    }

    // add content
    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void add_content_v1_return_200() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("items", jsonArray);

        var request = put(EXIST_SAFEBOX_ENDPOINT_V1_2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString())
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void add_content_v1_to_safebox_not_exists_return_404() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("items", jsonArray);

        var request = put(NOT_EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString())
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void add_content_v1_without_token_return_401() throws Exception {
        var safeBoxJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        safeBoxJson.put("items", jsonArray);

        var request = put(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString());

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void add_content_v1_without_param_body_return_400() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);

        var request = put(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "safebox2", password = "safebox2_123456")
    void add_content_v1_safebox_locked_return_423() throws Exception {
        String authorization = get_token(OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2);
        var safeBoxJson = new JSONObject();
        safeBoxJson.put("items", jsonArray);

        var request = put(EXIST_SAFEBOX_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(safeBoxJson.toString())
                .header(AUTHORIZATION_HEADER, "Bearer " + authorization);

        mvc.perform(request)
                .andExpect(status().isLocked());
    }
}
