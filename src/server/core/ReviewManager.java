package server.core;

import server.database.Database;
import server.database.Review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewManager {
    private static ReviewManager instance;

    private static final Database database = Database.getInstance();
    private static final UserManager userManager = UserManager.getInstance();
    private static final SessionManager sessionManager = SessionManager.getInstance();
    private static final RankManager rankManager = RankManager.getInstance();

    public static ReviewManager getInstance() {
        if (instance == null)
            instance = new ReviewManager();
        return instance;
    }

    public String createReview(String token, String content) throws UserManager.NotLoggedInException, NotFoundException, UserManager.SessionExpiredException, ServerErrorException {
        if (!sessionManager.isUserOnline(token))
            throw new UserManager.NotLoggedInException();

        String username = userManager.getUser(token).getUsername();
        Review review = Review.fromJson(content);
        review.setDate(LocalDateTime.now());

        if (!review.getUser().equals(username))
            throw new ServerErrorException();

        if (review.getRate() < 0.f || review.getRate() > 5.f)
            throw new ServerErrorException();

        for (Float rate : review.getRatings().values())
            if (rate < 0.f || rate > 5.f)
                throw new ServerErrorException();

        List<Review> reviews = database.getReviews().computeIfAbsent(review.getHotel(), k -> new ArrayList<>());

        Optional<Review> opt = reviews.stream()
                .filter(r -> r.getUser().equals(username))
                .findFirst();

        if (opt.isPresent())
            opt.get().set(review);
        else
            reviews.add(review);

        rankManager.update();

        return review.toJson();
    }
}
