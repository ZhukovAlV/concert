package net.thumbtack.school.concert.dto.response;

import net.thumbtack.school.concert.model.User;

import java.util.List;

public class GetRegistersUserDtoResponse {
    //vstrokan: нехорошо коллекции без дженерика с типом использовать
        //avzhukov: исправил
    private List<User> userList;

    public GetRegistersUserDtoResponse(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
