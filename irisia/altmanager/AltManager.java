package irisia.altmanager;

import com.google.gson.*;
import irisia.managers.Manager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AltManager extends Manager<Alt> {

    private List<Alt> accounts = new ArrayList<>();

    private final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    private File altsFile;

    private String alteningKey;

    private String lastAlteningAlt;

    private Alt lastAlt;

    public AltManager(File parent) {
        this.altsFile = new File(parent.toString() + File.separator + "alts.json");
        load();
    }

    public void save() {
        if (this.altsFile == null)
            return;
        try {
            if (!this.altsFile.exists())
                this.altsFile.createNewFile();
            PrintWriter printWriter = new PrintWriter(this.altsFile);
            printWriter.write(this.gson.toJson((JsonElement) toJson()));
            printWriter.close();
        } catch (IOException iOException) {
        }
    }

    public void load() {
        if (!this.altsFile.exists()) {
            save();
            return;
        }
        try {
            JsonObject json = (new JsonParser()).parse(new FileReader(this.altsFile)).getAsJsonObject();
            fromJson(json);
        } catch (IOException iOException) {
        }
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        getAccounts().forEach(account -> jsonArray.add((JsonElement) account.toJson()));
        if (this.lastAlt != null)
            jsonObject.add("lastalt", (JsonElement) this.lastAlt.toJson());
        jsonObject.add("accounts", (JsonElement) jsonArray);
        return jsonObject;
    }

    //   bo ti
    public void fromJson(JsonObject json) {

        if (json.has("lastalt")) {
            Alt account = new Alt();
            account.fromJson(json.get("lastalt").getAsJsonObject());
            this.lastAlt = account;
        }
        JsonArray jsonArray = json.get("accounts").getAsJsonArray();
        jsonArray.forEach(jsonElement -> {
            JsonObject jsonObject = (JsonObject) jsonElement;
            Alt account = new Alt();
            account.fromJson(jsonObject);
            getAccounts().add(account);
        });
    }

    public void remove(String username) {
        for (Alt account : getAccounts()) {
            if (account.getNickName().equalsIgnoreCase(username))
                getAccounts().remove(account);
        }
    }

    public Alt getAccountByEmail(String email) {
        for (Alt account : getAccounts()) {
            if (account.getEmail().equalsIgnoreCase(email))
                return account;
        }
        return null;
    }

    public String getLastAlteningAlt() {
        return this.lastAlteningAlt;
    }

    public void setLastAlteningAlt(String lastAlteningAlt) {
        this.lastAlteningAlt = lastAlteningAlt;
    }

    public String getAlteningKey() {
        return this.alteningKey;
    }

    public void setAlteningKey(String alteningKey) {
        this.alteningKey = alteningKey;
    }

    public Alt getLastAlt() {
        return this.lastAlt;
    }

    public void setLastAlt(Alt lastAlt) {
        this.lastAlt = lastAlt;
    }

    public List<Alt> getNotBannedAccounts() {
        List<Alt> accounts = new ArrayList<Alt>(this.accounts);
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).isBanned())
                accounts.remove(i);
        }
        return this.accounts;
    }

    public List<Alt> getAccounts() {
        return this.accounts;
    }
}