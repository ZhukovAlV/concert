package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import net.thumbtack.school.concert.dao.DataDao;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.dto.response.GetRegistersUserDtoResponse;
import net.thumbtack.school.concert.dto.response.GetSongsDtoResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public void savedDataToFile(File file, String users, String songs, DataDao dataDao) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            JsonArray ja = new JsonArray();
            ja.add(users);
            ja.add(songs);
            bw.write(ja.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        dataDao.clear();
    }

    public void recoveryDataFromFile(File file, Gson gson, UserService userService, SongService songService, UserDao userDao, SongDao songDao) {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonReader reader = gson.newJsonReader(br);
            reader.beginArray();
            while (reader.hasNext()) {
                String str = gson.fromJson(reader, String.class);
                data.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        GetRegistersUserDtoResponse getRegistersUserDtoResponse = gson.fromJson(data.get(0),GetRegistersUserDtoResponse.class);
        GetSongsDtoResponse getSongsDtoResponse = gson.fromJson(data.get(1),GetSongsDtoResponse.class);
        userService.setUsers(getRegistersUserDtoResponse.getUserList(),userDao);
        songService.setSongs(getSongsDtoResponse.getSongsList(),songDao);
    }

}
