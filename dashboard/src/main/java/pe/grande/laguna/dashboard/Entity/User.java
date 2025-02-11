package pe.grande.laguna.dashboard.Entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;

@Getter
@Setter
@Document(collection = "users")
public class User implements Serializable {

    @Id
    @NonNull
    private String id;

    @Field(value = "email")
    private String email;

    @Field(value = "password")
    private String password;

    @Field(value = "role")
    private String role; // ADMIN o USER

    @Field(value = "active")
    private boolean active = true;

    public User() {}

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
