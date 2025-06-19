package com.pyj.mapp.service;

import com.pyj.mapp.dao.ReferralMapper;
import com.pyj.mapp.dao.UserMapper;
import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.util.PbSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ReferralMapper referralMapper;
    private final PbSecurityUtils pbSecurityUtils;
    private final ReferralService referralService;

    @Override
    public void registerUser(UserDto userDto) {
        // 1. 추천인 코드 유효성 검증 및 referrer 조회
        UserDto referrer = null;
        if (userDto.getReferredByCode() != null && !userDto.getReferredByCode().isBlank()) {
            referrer = userMapper.findByReferralCode(userDto.getReferredByCode());
            if (referrer == null) {
                throw new IllegalArgumentException("유효하지 않은 추천인 코드입니다.");
            }
        }

        // 2. 나의 추천 코드 생성 (8자리 랜덤 문자열)
        String myReferralCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        userDto.setReferralCode(myReferralCode);

        // 3. 비밀번호 암호화
        String encryptedPw = pbSecurityUtils.getSHA256(userDto.getPw());
        userDto.setPw(encryptedPw);

        // 4. 기본 권한 부여 (USER)
        userDto.setRole("USER");

        // 5. 기본 GRADE 및 MAPP_POINT 설정
        userDto.setGrade("M1");
        userDto.setMappPoint(0);

        // 5. 유저 저장
        userMapper.insertUser(userDto);  // userDto.userNo가 자동 생성되었다고 가정

        // 7. 추천 관계 저장
        if (referrer != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("referrerNo", referrer.getUserNo());
            param.put("referredNo", userDto.getUserNo());
            referralMapper.insertReferral(param);

            // 8. 추천한 referrer의 등급 자동 업데이트
            referralService.updateUserGrade(referrer.getUserNo());
        }
    }

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        String pw = pbSecurityUtils.getSHA256(request.getParameter("pw"));

        Map<String, Object> map = Map.of("id", id, "pw", pw);

        UserDto user = userMapper.getUser(map);

        if (user != null) {
            request.getSession().setAttribute("user", user);
        } else {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('일치하는 회원 정보가 없습니다.')");
            out.println("location.href='" + request.getContextPath() + "/main'");
            out.println("</script>");
            out.flush();
            out.close();
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/user/login.form");
    }

    @Override
    public UserDto findById(String id) {
        return userMapper.findById(id);
    }

    @Override
    public UserDto getUser(String id) {
        return userMapper.getUser(Map.of("id", id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public UserDto getUserByUserNo(int userNo) {
        return userMapper.findUserByUserNo(userNo);
    }

}
