package model.apartment;

public class ApartmentSearchDTO {
    private String country;
    private String region;
    private String city;

    private Integer floor;
    private Integer roomCount;
    private Double totalArea;

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public Double getTotalArea() {
        return totalArea;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }
}
