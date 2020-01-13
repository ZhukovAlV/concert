package net.thumbtack.school.concert.dto.request;

import java.util.ArrayList;
import java.util.List;

public class AuthorsDtoRequest {

    List<String> name = new ArrayList<>();

    public List<String> getName() {
        return name;
    }

    public void setName(List name) {
        this.name = name;
    }
}
