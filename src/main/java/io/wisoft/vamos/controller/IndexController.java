package io.wisoft.vamos.controller;

import io.wisoft.vamos.config.auth.LoginUser;
import io.wisoft.vamos.config.auth.dto.SessionUser;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.board.BoardResponse;
import io.wisoft.vamos.dto.board.BoardUploadRequest;
import io.wisoft.vamos.dto.user.UserResponse;
import io.wisoft.vamos.service.BoardService;
import io.wisoft.vamos.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        List<UserResponse> userResponses = userService.findAll()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        model.addAttribute("users", userResponses);

        List<BoardResponse> boardResponses = boardService.findAll()
                .stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
        model.addAttribute("boards", boardResponses);

        if (user != null) {
            model.addAttribute("userName", user.getUsername());
            User loginUser = userService.findByEmail(user.getEmail());
            UserLocation location = loginUser.getLocation();
            if (location != null) {
                model.addAttribute("location", location);
            }
        }

        return "index";
    }

    @GetMapping("/board/save")
    public String boardSave() {
        return "board-save";
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable Long id, Model model) {
        BoardResponse dto = new BoardResponse(boardService.findById(id));
        model.addAttribute("board", dto);
        return "board-update";
    }

    @GetMapping("/user/{email}/location/update/")
    public String locationUpdate(@PathVariable String email, Model model) {
        UserLocation location = userService.findByEmail(email).getLocation();
        model.addAttribute("location", location);
        return "location-update";
    }
}
