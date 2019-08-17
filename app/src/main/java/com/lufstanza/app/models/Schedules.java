package com.lufstanza.app.models;


public class Schedules {
    private String departureAirport;
    private String departureTime;
    private String arrivalAirport;
    private String arrivalTime;
    private String MarketingCarrier;

    public Schedules() {

    }

    public Schedules(String departureAirport, String departureTime, String arrivalAirport, String arrivalalTime, String marketingCarrier) {
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalalTime;
        MarketingCarrier = marketingCarrier;
    }

    @Override
    public String toString() {
        return "Schedules{" +
                "departureAirport='" + departureAirport + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", MarketingCarrier='" + MarketingCarrier + '\'' +
                '}';
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getMarketingCarrier() {
        return MarketingCarrier;
    }

    public void setMarketingCarrier(String marketingCarrier) {
        MarketingCarrier = marketingCarrier;
    }
}