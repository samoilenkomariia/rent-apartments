package model.apartment;

public class Address {
    private String country;
    private String region;
    private String city;
    private String district;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;

    public Address(String country, String region, String city, String district, String street, String buildingNumber) {
        this(country, region, city, district, street, buildingNumber, null);
    }

    public Address() {}

    public Address(String country, String region, String city, String district, String street, String buildingNumber, String apartmentNumber) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.district = district;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String toString() {
        String res = country + ", " + region + ", " + city + ", " + district + ", " + street + " " + buildingNumber;
        if (apartmentNumber != null) {
            res += ", " + apartmentNumber;
        }
        return res;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
