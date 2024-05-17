package client.core;

import client.entities.Hotel;
import client.entities.Review;
import client.protocol.Request;
import client.protocol.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class HotelManager implements ResponseListener {
    private static HotelManager instance;

    private static final Client client = Client.getInstance();
    private static final RequestHandler requestHandler = RequestHandler.getInstance();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Response response;

    private HotelManager() {
    }

    public static HotelManager getInstance() {
        if (instance == null)
            instance = new HotelManager();
        return instance;
    }

    public List<Hotel> getHotels(String city, String name) {
        FutureTask<List<Hotel>> task = new FutureTask<>(() -> {
           String body = "hotel/" + city;

           if (name != null)
               body += "/" + name;

           requestHandler.send(Request.get(client.getToken(), body));

           synchronized (HotelManager.this) {
               HotelManager.this.wait();
           }

           if (response == null)
               return new ArrayList<>();
           if (response.getStatus() != Response.ResponseStatus.OK)
               return new ArrayList<>();
           if (!response.getBody().equals(body))
               return new ArrayList<>();

           return Hotel.parseList(response.getContent());
        });

        executor.execute(task);

        try {
            return task.get();
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    public boolean addReview(Review review) {
        FutureTask<Boolean> task = new FutureTask<>(() -> {
            String body = "reviews/" + review.getHotel();

            requestHandler.send(Request.push(client.getToken(), body, review.toJson()));

            synchronized (HotelManager.this) {
                HotelManager.this.wait();
            }

            if (response == null)
                return false;
            if (response.getStatus() != Response.ResponseStatus.OK)
                return false;

            return response.getBody().equals(body);
        });

        executor.execute(task);

        try {
            return task.get();
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public synchronized void onResponse(Response response) {
        this.response = response;
        this.notifyAll();
    }
}
