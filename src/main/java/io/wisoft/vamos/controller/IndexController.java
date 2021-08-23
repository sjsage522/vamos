package io.wisoft.vamos.controller;

import io.wisoft.vamos.config.auth.LoginUser;
import io.wisoft.vamos.config.auth.dto.SessionUser;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.service.BoardService;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("boards", boardService.findAll());
        if (user != null) {
            model.addAttribute("userName", user.getUsername());
        }

        return "index";
    }

    @GetMapping("/board/save")
    public String boardSave() {
        return "board-save";
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable Long id, Model model) {
        BoardUploadRequest dto = new BoardUploadRequest(boardService.findById(id));
        model.addAttribute("board", dto);
        return "board-update";
    }
}
