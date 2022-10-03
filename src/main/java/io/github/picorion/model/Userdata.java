package io.github.picorion.model;

import javafx.scene.image.Image;

/**
 * Object that contains the imported userdata
 */
public class Userdata {

    /**
     * Private constructor to prevent instantiation
     */
    private Userdata() {
        throw new IllegalStateException("Utility class");
    }

    private static final String UNKNOWN = "unknown";
    private static String street = UNKNOWN;
    private static String city = UNKNOWN;
    private static String postalCode = UNKNOWN;
    private static String state = UNKNOWN;
    private static String country = UNKNOWN;
    private static String facebookID = UNKNOWN;
    private static String realName = UNKNOWN;
    private static Image image = new Image("/images/imageNotFound.png");
    private static String username = UNKNOWN;
    private static String email = UNKNOWN;
    private static String birthdate = UNKNOWN;
    private static String gender = UNKNOWN;
    private static String mobileNumber = UNKNOWN;
    private static String creationTime = UNKNOWN;

    public static String getStreet() { return street; }
    public static void setStreet(String street) { Userdata.street = street; }

    public static String getCity() { return city; }
    public static void setCity(String city) { Userdata.city = city; }

    public static String getPostalCode() { return postalCode; }
    public static void setPostalCode(String postalCode) { Userdata.postalCode = postalCode; }

    public static String getState() { return state; }
    public static void setState(String state) { Userdata.state = state; }

    public static String getCountry() { return country; }
    public static void setCountry(String country) { Userdata.country = country; }

    public static String getFacebookID() { return facebookID; }
    public static void setFacebookID(String facebookID) { Userdata.facebookID = facebookID; }

    public static String getRealName() { return realName; }
    public static void setRealName(String realName) { Userdata.realName = realName; }

    public static Image getImage() { return image; }
    public static void setImage(Image image) { Userdata.image = image; }

    public static String getUsername() { return username; }
    public static void setUsername(String username) { Userdata.username = username; }

    public static String getEmail() { return email; }
    public static void setEmail(String email) { Userdata.email = email; }

    public static String getBirthdate() { return birthdate; }
    public static void setBirthdate(String birthdate) { Userdata.birthdate = birthdate; }

    public static String getGender() { return gender; }
    public static void setGender(String gender) { Userdata.gender = gender; }

    public static String getMobileNumber() { return mobileNumber; }
    public static void setMobileNumber(String mobileNumber) { Userdata.mobileNumber = mobileNumber; }

    public static String getCreationTime() { return creationTime; }
    public static void setCreationTime(String creationTime) { Userdata.creationTime = creationTime; }
    
}
