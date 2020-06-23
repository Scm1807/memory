package headfirst.observer.weather;

import headfirst.observer.weather.display.displayimpl.CurrentConditionsDisplay;
import headfirst.observer.weather.display.displayimpl.ForecastDisplay;
import headfirst.observer.weather.display.displayimpl.HeatIndexDisplay;
import headfirst.observer.weather.display.displayimpl.StatisticsDisplay;
import headfirst.observer.weather.subject.WeatherData;

/**
 * 基础信息包括：计算酷热指数
 */
public class WeatherStationHeatIndex {

    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);
        HeatIndexDisplay heatIndexDisplay = new HeatIndexDisplay(weatherData);

        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(82, 70, 29.2f);
        weatherData.setMeasurements(78, 90, 29.2f);
    }
}
