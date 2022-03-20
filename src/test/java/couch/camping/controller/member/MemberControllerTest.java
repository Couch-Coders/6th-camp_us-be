package couch.camping.controller.member;

import couch.camping.common.BaseControllerTest;
import couch.camping.controller.test.TestController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
class MemberControllerTest extends BaseControllerTest {

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(TestController.class)
                .build();
    }

    @Test
    public void registerMemberTest() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(content().string("okk"))// 여기부터 검증
                .andExpect(status().isOk());// 여기부터 검증
    }

}