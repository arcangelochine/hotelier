package server.core;

import server.database.Database;
import server.database.Hotel;
import server.database.Review;
import server.util.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RankManager {
    private static RankManager instance;

    private static final Logger logger = Logger.getInstance();
    private static final Database database = Database.getInstance();

    public static RankManager getInstance() {
        if (instance == null)
            instance = new RankManager();
        return instance;
    }

    public void update() {
        database.getHotels().forEach((k ,v) -> v.sort(Comparator.comparing(RankManager::getRank).reversed()));
        logger.log("Rank manager updated.");
    }

    private static Float getRank(Hotel hotel) {
        List<Review> reviews = Database.getInstance().getReviews().get(hotel.getId());

        if (reviews == null || reviews.isEmpty())
            return 0f;

        int n = reviews.size();
        float sum = 0f;

        float globalScore = 0f;
        final HashMap<String, Float> localScores = new HashMap<>();

        for (Review review : reviews) {
            globalScore += review.getRate();
            review.getRatings().forEach((k, v) -> localScores.merge(k, v, Float::sum));

            float p, w;

            p = (float) (((float) 1 / 2) * (review.getRate() / 5 + review.getRatings().values().stream().mapToDouble(Float::doubleValue).average().orElse(0f) / 5));

            try {
                float expertise = (float) (UserManager.getInstance().getUser(review.getUser()).getBadge().ordinal() + 1) / 5;
                long months = ChronoUnit.MONTHS.between(review.getDate(), LocalDateTime.now());
                w = (float) (expertise * Math.pow(2, -months));
            } catch (Exception ignored) {
                w = 0;
            }

            sum += p * w;
        }

        final float finalGlobalScore = globalScore / n;
        localScores.replaceAll((k, v) -> v / n);

        Database.getInstance().getHotels().get(hotel.getCity())
                .stream()
                .filter(h -> h.getId().equals(hotel.getId()))
                .findFirst()
                .ifPresent(h -> {
                    h.setRate(finalGlobalScore);
                    h.setRatings(localScores);
                });

        return (sum / n);
    }
}
