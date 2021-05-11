package com.twinkle.service.springboot.web;

import com.twinkle.service.springboot.config.auth.LoginUser;
import com.twinkle.service.springboot.config.auth.dto.SessionUser;
import com.twinkle.service.springboot.web.dto.PostsResponseDto;
import com.twinkle.service.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        if(user != null) {
            // 애트리뷰트 이름을 "userName"으로 등록하니 윈도우에서 %USERNAME% 환경변수를 등록하는 문제 발생
            // nickName으로 변경하니 정상적으로 해당 계정의 name이 등록
            model.addAttribute("nickName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
