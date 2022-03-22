package couch.camping.controller.member;

import couch.camping.common.BaseControllerTest;
import couch.camping.controller.member.dto.request.MemberSaveRequestDto;
import couch.camping.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//MockMvcRequestBuilders
//요청 데이터를 설정할 때 사용할 static 메서드
//MockMvcResultMatchers
//실행 결과를 검증할 때 사용할 static 메서드
//MockMvcResultHandlers
//실행 결과를 로그 등으로 출력할 때 사용할 static 메서드

@Slf4j
class MemberControllerTest extends BaseControllerTest {

    private static final String uid = "abcd";
    private static final String email = "abcd@daum.com";
    private static final String name = "성이름";
    private static final String nickname = "abcd";
    private static final String imgUrl = "https://www.balladang.com";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MemberService memberService;

    @Autowired
    private Filter springSecurityFilterChain;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    @DisplayName("로컬 회원 가입 테스트")
    void registerMemberTest() throws Exception {

        MemberSaveRequestDto localMember = MemberSaveRequestDto.builder()
                .uid(uid)
                .email(email)
                .name(name)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .build();


        ResultActions resultActions = mockMvc.perform(
                post("/members/local")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(localMember))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print());

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("uid").value(uid))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("nickname").value(nickname))
                .andExpect(jsonPath("imgUrl").value(imgUrl));
    }
    
    @Test
    @DisplayName("로그인 테스트")
    void memberLoginTest() throws Exception {
        //TODO 생성 메서드 개발
        memberService.register(uid, name, email, nickname, imgUrl);

        ResultActions resultActions = mockMvc.perform(
                get("/members/me")
                        .header("Authorization", "Bearer " + uid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("uid").value(uid))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("nickname").value(nickname))
                .andExpect(jsonPath("imgUrl").value(imgUrl));
    }
    
    @Test
    @DisplayName("닉네임 수정 테스트")
    void MemberEditNickname() throws Exception {
        //TODO 생성 메서드 개발
        memberService.register(uid, name, email, nickname, imgUrl);
        Map<String, String> map = new HashMap<>();
        map.put("nickname", "김상운");

        ResultActions resultActions = mockMvc.perform(
                patch("/members/me")
                        .header("Authorization", "Bearer " + uid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map))
        )
                .andDo(print());

        resultActions
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("회원 조회 테스트")
    void memberInfoTest() throws Exception {
        //TODO 생성 메서드 개발
        memberService.register(uid, name, email, nickname, imgUrl);

        ResultActions resultActions = mockMvc.perform(
                get("/members/me/info")
                        .header("Authorization", "Bearer " + uid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("uid").value(uid))
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("nickname").value(nickname))
                .andExpect(jsonPath("imgUrl").value(imgUrl));
    }


}