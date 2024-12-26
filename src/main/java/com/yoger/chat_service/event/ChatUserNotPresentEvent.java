package com.yoger.chat_service.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserNotPresentEvent {

    @NotNull
    private String id;

    @NotNull
    private Long userId;

    @NotBlank
    private String message;
}
