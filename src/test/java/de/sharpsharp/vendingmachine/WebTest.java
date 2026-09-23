package de.sharpsharp.vendingmachine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.Response;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/** Starts the real web server on a free port and talks HTTP to it: the contract the page relies on. */
public class WebTest {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final Javalin web = Main.web(new VendingMachine(LocalTime::now));

    @Test
    public void servesTheStartPage() {
        JavalinTest.test(web, (server, client) -> {
            Response response = client.get("/");

            assertThat(response.code(), is(200));
            assertThat(response.body().string(), containsString("<title>We eat yummy snacks :)</title>"));
        });
    }

    @Test
    public void reportsTheFreshMachine() {
        JavalinTest.test(web, (server, client) -> {
            JsonNode state = json(client.get("/api/state"));

            assertThat(state.get("credit").asInt(), is(0));
            assertThat(state.get("message").asText(), is("Bitte Münzen einwerfen"));
            assertThat(state.get("refused").asBoolean(), is(false));
            assertThat(state.get("slots").size(), is(4));
            assertThat(state.get("slots").get(0).get("position").asInt(), is(1));
            assertThat(state.get("slots").get(0).get("drink").asText(), is("COLA"));
            assertThat(state.get("slots").get(0).get("name").asText(), is("Cola"));
            assertThat(state.get("slots").get(0).has("price"), is(true));
            assertThat(state.get("slots").get(0).get("stock").asInt(), is(5));
            assertThat(state.get("outputTray").size(), is(0));
            assertThat(state.get("coinReturn").size(), is(0));
        });
    }

    @Test
    public void everyActionAnswersWithTheState() {
        JavalinTest.test(web, (server, client) -> {
            for (String action : new String[]{"/api/insert/200", "/api/select/COLA", "/api/cancel", "/api/take-drinks", "/api/take-coins"}) {
                JsonNode state = json(client.post(action));

                assertThat(action, state.has("credit"), is(true));
                assertThat(action, state.has("message"), is(true));
                assertThat(action, state.get("slots").size(), is(4));
            }
        });
    }

    @Test
    public void rejectsUnknownCoinsAndDrinksAsBadRequest() {
        JavalinTest.test(web, (server, client) -> {
            Response unknownCoin = client.post("/api/insert/abc");
            Response unknownDrink = client.post("/api/select/WATER");

            assertThat(unknownCoin.code(), is(400));
            assertThat(unknownCoin.body().string(), containsString("Unknown coin: abc"));
            assertThat(unknownDrink.code(), is(400));
        });
    }

    private static JsonNode json(Response response) throws Exception {
        assertThat(response.code(), is(200));
        return JSON.readTree(response.body().string());
    }
}
