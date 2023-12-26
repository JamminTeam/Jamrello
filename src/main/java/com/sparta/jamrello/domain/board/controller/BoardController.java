package com.sparta.jamrello.domain.board.controller;

import com.sparta.jamrello.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
}
