package irisia.altmanager.gui;


import irisia.Irisia;
import irisia.altmanager.Alt;
import irisia.altmanager.gui.other.GuiAccountList;
import irisia.altmanager.gui.subguis.GuiAddAlt;
import irisia.altmanager.gui.subguis.GuiAltLogin;
import irisia.altmanager.microsoft.MicrosoftAuthResult;
import irisia.altmanager.microsoft.MicrosoftAuthenticationException;
import irisia.altmanager.microsoft.MicrosoftAuthenticator;
import irisia.altmanager.thread.AccountLoginThread;
import irisia.utils.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.Session;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class GuiAltManager extends GuiScreen {
    public static GuiAltManager INSTANCE;

    private GuiAccountList accountList;

    public Alt currentAccount;

    public AccountLoginThread loginThread;

    private String status = "Waiting for login...";

    public GuiAltManager() {
        INSTANCE = this;
    }

    public void initGui() {
        this.accountList = new GuiAccountList(this);
        this.accountList.registerScrollButtons(7, 8);
        this.accountList.elementClicked(-1, false, 0, 0);
        this.buttonList.add(new GuiButton(0, this.width / 2 + 158, this.height - 24, 100, 20, "Cancel"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height - 24, 100, 20, "Remove"));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Import"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 154, this.height - 24, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 258, this.height - 24, 100, 20, "Microsoft"));
        this.buttonList.add(new GuiButton(7, this.width / 2 + 54, this.height - 24, 100, 20, "Random Alt"));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 258, this.height - 48, 100, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(9, this.width / 2 + 158, this.height - 48, 100, 20, "Clear"));
    }
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        drawDefaultBackground();
        this.accountList.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        if (this.loginThread != null)
            status = this.loginThread.getStatus();
        drawCenteredString(this.mc.fontRendererObj, status, scaledResolution.getScaledWidth() / 2, 6, -3158065);
        drawCenteredString(this.mc.fontRendererObj, "Accounts: " + Irisia.getInstance.instanceCollection.getAltManager().getAccounts().size(), this.width / 2, 20, -1);

    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.accountList.handleMouseInput();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        List<Alt> registry;
        Random random;
        Alt randomAlt;
        JFrame frame;
        JFileChooser chooser;
        int returnVal;
        switch (button.id) {
            case 0:
                if (this.loginThread == null || !this.loginThread.getStatus().contains("Logging in"))
                    this.mc.displayGuiScreen((GuiScreen) new GuiMainMenu());
                break;
            case 1:
                if (this.accountList.selected == -1)
                    return;
                this.loginThread = new AccountLoginThread(this.accountList.getSelectedAccount().getEmail(), this.accountList.getSelectedAccount().getPassword());
                this.loginThread.start();
                break;
            case 2:
                this.accountList.removeSelected();
                this.accountList.selected = -1;
                break;
            case 3:
                if (this.loginThread != null)
                    this.loginThread = null;
                this.mc.displayGuiScreen((GuiScreen) new GuiAddAlt(this));
                break;
            case 4:
                if (this.loginThread != null)
                    this.loginThread = null;
                this.mc.displayGuiScreen((GuiScreen) new GuiAltLogin(this));
                break;
            case 7:
                if (Irisia.getInstance.instanceCollection.getAltManager().getAccounts().size() == 0)
                    return;

                registry = Irisia.getInstance.instanceCollection.getAltManager().getAccounts();
                random = new Random();
                randomAlt = registry.get(random.nextInt(Irisia.getInstance.instanceCollection.getAltManager().getAccounts().size()));
                if (randomAlt.isBanned())
                    return;
                this.currentAccount = randomAlt;
                login(randomAlt);
                break;
            case 6:
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                try {
                    //MicrosoftAuthResult result = authenticator.loginWithCredentials(this.username.getText(), this.password.getText());
                    MicrosoftAuthResult result = authenticator.loginWithWebview();
                    Minecraft.getMinecraft().session = new Session(result.getProfile().getName(),result.getProfile().getId(), result.getAccessToken(),"legacy");
                    status = StringUtil.simpleTranslateColors("&aLogged in &e") + result.getProfile().getName();
                } catch (MicrosoftAuthenticationException e) {
                    e.printStackTrace();
                }

                break;
            case 5:
                frame = new JFrame("Import alts");
                chooser = new JFileChooser();
                frame.add(chooser);
                frame.pack();
                returnVal = chooser.showOpenDialog(frame);
                if (returnVal == 0) {
                    frame.dispatchEvent(new WindowEvent(frame, 201));
                    try {
                        for (String line : Files.readAllLines(Paths.get(chooser.getSelectedFile().getPath(), new String[0]))) {
                            if (!line.contains(":"))
                                break;
                            String[] parts = line.split(":");
                            Alt account = new Alt(parts[0], parts[1], parts[0]);
                            Irisia.getInstance.instanceCollection.getAltManager().getAccounts().add(account);
                        }
                    } catch (MalformedInputException e) {
                        e.printStackTrace();
                        status = StringUtil.simpleTranslateColors( "&cThere has been an error importing the alts.");
                    }
                }
                Irisia.getInstance.instanceCollection.getAltManager().save();
                break;
            case 8:
                if (Irisia.getInstance.instanceCollection.getAltManager().getLastAlt() == null)
                    return;
                this.loginThread = new AccountLoginThread(Irisia.getInstance.instanceCollection.getAltManager().getLastAlt().getEmail(), Irisia.getInstance.instanceCollection.getAltManager().getLastAlt().getPassword());
                this.loginThread.start();
                break;
            case 9:
                if (Irisia.getInstance.instanceCollection.getAltManager().getAccounts().isEmpty())
                    return;
                Irisia.getInstance.instanceCollection.getAltManager().getAccounts().clear();
                break;
        }
    }

    public void login(Alt account) {
        this.loginThread = new AccountLoginThread(account.getEmail(), account.getPassword());
        this.loginThread.start();
    }

}