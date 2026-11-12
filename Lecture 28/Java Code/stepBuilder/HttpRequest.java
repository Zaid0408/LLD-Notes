package stepBuilder;

import java.util.*;

public class HttpRequest {
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String body;
    private int timeout; // in seconds

    // Private constructor - can only be accessed by the Builder
        // You want object creation to happen through your Builder. 
    // This gives the Builder control over how the object is constructed.
    private HttpRequest() {
        headers = new HashMap<>();
        queryParams = new HashMap<>();
        body = "";
    }

    // Method to execute the HTTP request
    public void execute() {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParams.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }

        System.out.println("Headers:");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println("  " + header.getKey() + ": " + header.getValue());
        }

        if (!body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }

    // Nested Step interfaces
    // Why so many interfaces?
    // Because you are implementing a Step Builder.
    // The idea is to enforce the order in which the user constructs the object.
    interface UrlStep {
        MethodStep withUrl(String url);
    }

    interface MethodStep {
        HeaderStep withMethod(String method);
    }

    interface HeaderStep {
        OptionalStep withHeader(String key, String value);
    }

    interface OptionalStep {
        OptionalStep withBody(String body);
        OptionalStep withTimeout(int timeout);
        HttpRequest build();
    }

    // Concrete step builder as static nested class
    // This is the actual Builder implementation. It implements all four interfaces and maintains a reference to the object being constructed.
    // Why static? Because it doesn't need to access any instance members of the outer class. It only needs to construct an instance of HttpRequest.
    static class HttpRequestStepBuilder implements UrlStep, MethodStep, HeaderStep, OptionalStep {
        private HttpRequest req;

        private HttpRequestStepBuilder() {
            req = new HttpRequest();
        }

        // UrlStep implementation
        /*
        What is happening?
        1. The URL is assigned to the actual HttpRequest object.
        2. this returns the current Builder object.
        3. But the return type is MethodStep, so the caller can now access only methods exposed by that interface.
        */
        public MethodStep withUrl(String url) {
            req.url = url;
            return this;
        }
        // The Builder is modifying the same HttpRequest object throughout the entire construction process.
        // It doesn't create a new HttpRequest at every step.

        // MethodStep implementation
        public HeaderStep withMethod(String method) {
            req.method = method;
            return this;
        }

        // HeaderStep implementation
        public OptionalStep withHeader(String key, String value) {
            req.headers.put(key, value);
            return this;
        }

        // OptionalStep implementation
        public OptionalStep withBody(String body) {
            req.body = body;
            return this;
        }

        public OptionalStep withTimeout(int timeout) {
            req.timeout = timeout;
            return this;
        }

        // Build method to create the immutable HttpRequest object
        // This is where the final object is returned.
        // Why have a build() method?
        // Because before returning the object, we can perform validations.
        public HttpRequest build() {
            if (req.url == null || req.url.isEmpty()) {
                throw new RuntimeException("URL cannot be empty");
            }
            return req;
        }

        // Static method to start the building process
        public static UrlStep getBuilder() {
            return new HttpRequestStepBuilder();
        }
    }
}
