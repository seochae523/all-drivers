package com.alldriver.alldriver.chat.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import com.alldriver.alldriver.chat.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="chat room")
public class ChatRoomController {



}
