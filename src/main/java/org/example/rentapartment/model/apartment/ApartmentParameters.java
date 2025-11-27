package org.example.rentapartment.model.apartment;

public class ApartmentParameters {

    private Double totalArea;
    private Double livingArea;
    private Integer floor;
    private Integer totalFloors;
    private Integer roomCount;
    private Integer buildYear;

    public ApartmentParameters() {}

    public ApartmentParameters(Double totalArea, Double livingArea, Integer floor, Integer totalFloors, Integer roomCount, Integer buildYear) {
        this.totalArea = totalArea;
        this.livingArea = livingArea;
        this.floor = floor;
        this.totalFloors = totalFloors;
        this.roomCount = roomCount;
        this.buildYear = buildYear;
    }

    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public Double getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(Double livingArea) {
        this.livingArea = livingArea;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(Integer buildYear) {
        this.buildYear = buildYear;
    }

    @Override
    public String toString() {
        return "Area: " + totalArea + ", Rooms: " + roomCount;
    }
}
