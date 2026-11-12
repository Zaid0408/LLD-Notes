package stepBuilder;

public class Main {
    public static void main(String[] args) {
        HttpRequest stepRequest = HttpRequest.HttpRequestStepBuilder.getBuilder()
            .withUrl("https://api.example.com/products")
            .withMethod("POST")
            .withHeader("Content-Type", "application/json")
            .withBody("{\"product\": \"Laptop\", \"price\": 49999}")
            .withTimeout(45)
            .build();

        /*
        Notice that you cannot call: builder.withMethod("POST"); before providing the URL.
        Why?
        Because getBuilder() returns UrlStep, and UrlStep only exposes withUrl().
        After calling withUrl(), it returns MethodStep.

        This is enforced by Java's type system at compile time!That's the main advantage of Step Builder.
        */
        stepRequest.execute();
    }
}
