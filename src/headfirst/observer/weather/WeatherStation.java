package headfirst.observer.weather;

import headfirst.observer.weather.display.displayimpl.CurrentConditionsDisplay;
import headfirst.observer.weather.display.displayimpl.ForecastDisplay;
import headfirst.observer.weather.display.displayimpl.StatisticsDisplay;
import headfirst.observer.weather.subject.WeatherData;

/**
 * 基础信息
 */
public class WeatherStation {

    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);

        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(82, 70, 29.2f);
        weatherData.setMeasurements(78, 90, 29.2f);
    }
}
