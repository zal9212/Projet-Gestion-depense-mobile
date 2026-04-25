package sn.esmt.financetrack.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    // @PrimaryKey dit : "C'est l'identifiant unique (auto-incrémenté 1, 2, 3...)"
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String login;
    private String password;
    private String photo;

    public User(int id, String login, String password, String photo) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
