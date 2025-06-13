package com.pyj.mapp.service;

import com.pyj.mapp.dao.UserMapper;
import com.pyj.mapp.dto.UserDto;
import com.pyj.mapp.util.PbSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PbSecurityUtils pbSecurityUtils;

    @Override
    public void registerUser(UserDto userDto) {
        // 1. 추천인 코드 유효성 검증
        if (userDto.getReferredByCode() != null && !userDto.getReferredByCode().isBlank()) {
            UserDto referrer = userMapper.findByReferralCode(userDto.getReferredByCode());
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

        // 4. 기본 역할 부여 (USER)
        userDto.setRole("USER");

        // 5. DB에 저장
        userMapper.insertUser(userDto);
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
    public UserDto getUser(String id) {
        return userMapper.getUser(Map.of("id", id));
    }

}
