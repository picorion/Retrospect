package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Data;
import model.Userdata;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerYourData implements Initializable {

    @FXML
    ImageView Image;
    @FXML
    Label Username;
    @FXML
    Label RealName;
    @FXML
    Label Birthdate;
    @FXML
    Label Gender;
    @FXML
    Label CreationTime;
    @FXML
    Label Street;
    @FXML
    Label City;
    @FXML
    Label PostalCode;
    @FXML
    Label State;
    @FXML
    Label Country;
    @FXML
    Label FacebookID;
    @FXML
    Label Email;
    @FXML
    Label MobileNumber;

    /**
     * Calls importUserdata if there could be a change in the userdata class
     *
     * @param location .
     * @param resources .
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Data.userdataChangeEvent.addListener(change -> {
            importUserdata();
            Data.userdataChangeEvent.setValue(false);
            //Don't know why this works or is needed but the event listener stops working without this line
            Data.userdataChangeEvent.getValue();
        });
    }

    /**
     * Gets the userdata from the userdata class and updates the labels and image with it
     */
    private void importUserdata() {
        Image.setImage(Userdata.getImage());
        Username.setText(Userdata.getUsername());
        RealName.setText(Userdata.getRealName());
        Birthdate.setText(Userdata.getBirthdate());
        Gender.setText(Userdata.getGender());
        CreationTime.setText(Userdata.getCreationTime());
        Street.setText(Userdata.getStreet());
        City.setText(Userdata.getCity());
        PostalCode.setText(Userdata.getPostalCode());
        State.setText(Userdata.getState());
        Country.setText(Userdata.getCountry());
        FacebookID.setText(Userdata.getFacebookID());
        Email.setText(Userdata.getEmail());
        MobileNumber.setText(Userdata.getMobileNumber());
    }

}
