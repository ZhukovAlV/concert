package net.thumbtack.school.concert;

import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

import static net.thumbtack.school.concert.server.Server.isIsStarted;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestServer {

    private static File file = new File("savedDataFile.txt");

    // Пытаемся дважды запустить сервер и проверяем, что throws ServerException
    @Test(expected = ServerException.class)
    public void startServerException() throws ServerException {
        Server.startServer(null);
        Server.startServer(null);
    }

    // Пытаемся дважды запустить сервер и проверяем, что throws ServerException
    @Test(expected = ServerException.class)
    public void stopServerException() throws IOException, ServerException {
        Server.stopServer(file);
        Server.stopServer(file);
    }

    // Пытаемся запустить сервер и передаем NULL
    @Test
    public void startServerNullException() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Assert.assertTrue(isIsStarted());
    }

    // Пытаемся остановить сервер и передаем NULL в качества файла для сохранения и проверяем, что throws ServerException
    @Test(expected = ServerException.class)
    public void stopServerNullException() throws IOException, ServerException {
        if (!isIsStarted()) Server.startServer(file);
        Server.stopServer(null);
    }
}
