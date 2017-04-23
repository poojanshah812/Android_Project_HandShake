package fabssj4.nyu.handshake.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by FabSSJ4 on 3/24/2016.
 */
public class User implements Parcelable {

    private String contact;
    private String email;
    private String displayname;
    private String registrationtimestamp;
    private String registrationip;
    private URI photouri;

    protected User(Parcel in) {
        contact = in.readString();
        email = in.readString();
        displayname = in.readString();
        registrationtimestamp = in.readString();
        registrationip = in.readString();
        try {
            photouri = new URI(in.readString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public User(String contact, String email, String displayname, String registrationtimestamp, String registrationip, URI photouri){
        this.contact = contact;
        this.displayname = displayname;
        this.registrationtimestamp = registrationtimestamp;
        this.registrationip = registrationip;
        this.photouri = photouri;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getRegistrationtimestamp() {
        return registrationtimestamp;
    }

    public void setRegistrationtimestamp(String registrationtimestamp) {
        this.registrationtimestamp = registrationtimestamp;
    }

    public String getRegistrationip() {
        return registrationip;
    }

    public void setRegistrationip(String registrationip) {
        this.registrationip = registrationip;
    }

    public URI getPhotouri() {
        return photouri;
    }

    public void setPhotouri(URI photouri) {
        this.photouri = photouri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contact);
        dest.writeString(email);
        dest.writeString(displayname);
        dest.writeString(registrationtimestamp);
        dest.writeString(registrationip);
        dest.writeString(photouri.toString());
    }
}
