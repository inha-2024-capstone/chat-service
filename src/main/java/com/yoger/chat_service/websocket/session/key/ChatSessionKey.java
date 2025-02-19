package com.yoger.chat_service.websocket.session.key;

import lombok.Getter;

@Getter
public class ChatSessionKey {
    private final String chatId;
    private final String userId;

    public ChatSessionKey(Long chatId, Long userId) {
        this.chatId = String.valueOf(chatId);
        this.userId = String.valueOf(userId);
    }

    public ChatSessionKey(String chatId, Long userId) {
        validateNum(chatId);
        this.chatId = chatId;
        this.userId = String.valueOf(userId);
    }

    public String getSessionKey() {
        return chatId + ":" + userId;
    }

    private void validateNum(String num) {
        try {
            Long.parseLong(num);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("num이 null입니다.");
        } catch (NullPointerException ex) {
            throw new RuntimeException("num: " + num + "은 숫자가 아닙니다.");
        }
    }
}

