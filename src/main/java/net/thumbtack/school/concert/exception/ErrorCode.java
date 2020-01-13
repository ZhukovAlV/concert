package net.thumbtack.school.concert.exception;

public enum ErrorCode {
    USER_NOT_EXIST("{\"error\" : \"Пользователь с таким логином не зарегистрирован.\"}"),
    USER_LOGIN_FAILED("{\"error\" : \"Неверный логин или пароль.\"}"),
    USER_LOGIN_EMPTY("{\"error\" : \"Не заполнен логин.\"}"),
    USER_LOGIN_SHORT("{\"error\" : \"Логин очень короткий.\"}"),
    USER_PASSWORD_EMPTY("{\"error\" : \"Не заполнен пароль.\"}"),
    USER_TOKEN_NOT_VALID("{\"error\" : \"Ошибка авторизации.\"}"),
    USER_COMMUNITY_NOT_EXIST("{\"error\" : \"Пользователь сообщества не заведен в системе.\"}"),
    USER_REGISTER_DUPLICATE("{\"error\" : \"Пользователь с таким именем уже существует.\"}"),
    USER_FIRST_NAME_IS_EMPTY("{\"error\" : \"Не заполнено имя пользователя.\"}"),
    USER_LAST_NAME_IS_EMPTY("{\"error\" : \"Не заполнена фамилия пользователя.\"}"),
    SERVER_IS_START("{\"error\" : \"Сервер уже запущен.\"}"),
    SERVER_NOT_START("{\"error\" : \"Сервер не запущен.\"}"),
    SERVER_FILE_NOT_EXIST("{\"error\" : \"Файл для сохранения не найден.\"}"),
    SONG_TITLE_IS_EMPTY("{\"error\" : \"Не заполнено название песни.\"}"),
    SONG_COMPOSER_IS_EMPTY("{\"error\" : \"Не заполнено поле: композитор(ы).\"}"),
    SONG_AUTHOR_IS_EMPTY("{\"error\" : \"Не заполнено поле: автор.\"}"),
    SONG_SINGER_IS_EMPTY("{\"error\" : \"Не заполнено поле: исполнитель песни.\"}"),
    SONG_DURATION_IS_EMPTY("{\"error\" : \"Не заполнено поле: продожительность песни.\"}"),
    SONG_RATING_ONLY_READ("{\"error\" : \"Поле рейтинга менять запрещено.\"}"),
    SONG_RATING_EXIST("{\"error\" : \"Данной песне уже была выставлена оценка.\"}"),
    SONG_RATING_OUT("{\"error\" : \"Рейтинг выходит за возможные границы от 1 до 5.\"}"),
    SONG_IS_EXIST("{\"error\" : \"Данная песня уже есть в списке.\"}");

    private String message;

    ErrorCode(String message){
        this.message = message;
    }

    public String getErrorString(){
        return message;
    }
}
