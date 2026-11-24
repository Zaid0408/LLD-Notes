// 1. Target interface expected by the application
interface ICelsiusTemperature {
    double getTemperatureInCelsius();
}

// 2. Adaptee: third-party class that provides Fahrenheit
class FahrenheitTemperatureProvider {
    private final double temperatureInFahrenheit;

    public FahrenheitTemperatureProvider(double temperatureInFahrenheit) {
        this.temperatureInFahrenheit = temperatureInFahrenheit;
    }

    public double getTemperatureInFahrenheit() {
        return temperatureInFahrenheit;
    }
}

// 3. Adapter: converts the adaptee's Fahrenheit value to Celsius
class FahrenheitToCelsiusAdapter implements ICelsiusTemperature {
    private final FahrenheitTemperatureProvider fahrenheitProvider;

    public FahrenheitToCelsiusAdapter(FahrenheitTemperatureProvider provider) {
        this.fahrenheitProvider = provider;
    }

    @Override
    public double getTemperatureInCelsius() {
        double fahrenheit = fahrenheitProvider.getTemperatureInFahrenheit();
        return (fahrenheit - 32) * 5 / 9;
    }
}

// 4. Client interacts only with the target interface
class TemperatureClient {
    public void displayTemperature(ICelsiusTemperature temperature) {
        System.out.printf("Temperature: %.1f C%n", temperature.getTemperatureInCelsius());
    }
}

public class AdapterPrac {

    public static void main(String[] args) {
        FahrenheitTemperatureProvider thirdPartyProvider =new FahrenheitTemperatureProvider(98.6);
        ICelsiusTemperature adapter = new FahrenheitToCelsiusAdapter(thirdPartyProvider);

        TemperatureClient client = new TemperatureClient();
        client.displayTemperature(adapter);
    }
}
