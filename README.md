# Popular Movie Stage 2

This is second project for the Udacity **Android Developer Fast Track** supported by Google. The project meet the requested specifications and it's implemented with high level libraries such as:
1. Piccaso, an easy and powerful library to manage images from the web.
2. Retrofit, a modern, fast and efficient REST client.
3. Butterknife to inject views.


#### Common Project Requirements

1. App is written solely in the Java Programming Language.
2. App conforms to common standards found in the [Android Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html).

#### User Interface - Layout
1. UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
2. Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
3. UI contains a screen for displaying the details for a selected movie.
4. Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
5. Movie Details layout contains a section for displaying trailer videos and user reviews.
   
#### User Interface - Function
1. When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
2. When a movie poster thumbnail is selected, the movie details screen is launched.
3. When a trailer is selected, app uses an Intent to launch the trailer.
4. In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

#### Network API Implementation
1. In a background thread, app queries the `/movie/popular` or `/movie/top_rated` API for the sort criteria specified in the settings menu.
2. App requests for related videos for a selected movie via the `/movie/{id}/videos` endpoint in a background thread and displays those details when the user selects a movie.
3. App requests for user reviews for a selected movie via the `/movie/{id}/reviews` endpoint in a background thread and displays those details when the user selects a movie.

#### Data Persistence
1. The titles and ids of the user's favorite movies are stored in a `ContentProvider` backed by a SQLite database. This `ContentProvider` is updated whenever the user favorites or unfavorites a movie.
2. When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the `ContentProvider`.


#### Suggestions to Make the Project Stand Out!
Extend the favorites ContentProvider to store the movie poster, synopsis, user rating, and release date, and display them even when offline.
Implement sharing functionality to allow the user to share the first trailer’s YouTube URL from the movie details screen.