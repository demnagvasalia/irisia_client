package irisia.altmanager;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alt {
    private @Getter @Setter String nickName;
    private @Getter @Setter String email;
    private @Getter @Setter String password;
    private @Getter @Setter boolean banned;

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", this.email);
        jsonObject.addProperty("password", this.password);
        jsonObject.addProperty("name", this.nickName);
        jsonObject.addProperty("banned", Boolean.valueOf(this.banned));
        return jsonObject;
    }

    public Alt(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.nickName = name;
    }

    public void fromJson(JsonObject json) {
        this.email = json.get("email").getAsString();
        this.password = json.get("password").getAsString();
        this.nickName = json.get("name").getAsString();
        this.banned = json.get("banned").getAsBoolean();
    }

}