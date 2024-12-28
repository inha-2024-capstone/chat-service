package com.yoger.chat_service.websocket.session;

import java.util.Arrays;
import lombok.Getter;

@Getter
public class StompSessionKey {
    private final String sessionKey;

    public StompSessionKey(String chatId, String userId) {
        validateNumber(chatId);
        validateNumber(userId);
        this.sessionKey = chatId + ":" + userId;
    }

    public StompSessionKey(String key) {
        validate(key);
        this.sessionKey = key;
    }

    private void validate(String key) {
        String[] split = key.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("[StompSessionKey] 세션 키의 형식이 알맞지 않습니다.");
        }

        Arrays.stream(split).forEach(this::validateNumber);
    }

    private void validateNumber(String num) {
        try {
            Integer.parseInt(num);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException("[StompSessionKey] id가 null입니다.");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("[StompSessionKey] id가 숫자가 아닙니다.");
        }
    }
}
