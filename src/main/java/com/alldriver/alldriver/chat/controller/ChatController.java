package com.alldriver.alldriver.chat.controller;


import com.alldriver.alldriver.chat.service.ChatService;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "chat")
public class ChatController {



}
