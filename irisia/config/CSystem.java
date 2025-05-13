package irisia.config;

import irisia.Irisia;
import irisia.module.Module;
import irisia.property.Property;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public enum CSystem {
    get;
    final File directory = new File("irisia");
    public final static String EXTENSION = ".iris";
    final String delimiter = ":";

    public final void saveConfig(final String fileName) throws FileNotFoundException {
        File targetFile = new File(directory + "\\" + fileName + EXTENSION);
        if(!directory.exists()) {
            directory.mkdir();
        }

        if(targetFile.exists() && targetFile.getName().equals("default.exi")){
            targetFile.delete();
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintWriter writer = new PrintWriter(targetFile);
        Irisia.getInstance.getInstanceCollection().getModuleManager().getModulesParsedForConfig(delimiter).forEach(m -> {
            writer.println(m);
        });
        Irisia.getInstance.getInstanceCollection().getModuleManager().getModules().forEach(m -> {
            Irisia.getInstance.getInstanceCollection().getModuleManager().getSettingsByModule(m).forEach(p -> {
                if(p instanceof DoubleProperty){
                    writer.println("PROPERTY" + delimiter + p.getNameWithParent() + delimiter + ((DoubleProperty) p).getValue());
                }
                if(p instanceof EnumProperty){
                    writer.println("PROPERTY" + delimiter + p.getNameWithParent() + delimiter + ((EnumProperty) p).getSelected());
                }
                if(p instanceof BooleanProperty){
                    writer.println("PROPERTY" + delimiter + p.getNameWithParent() + delimiter + ((BooleanProperty) p).isEnabled());
                }
            });
        });
        writer.println("creationdate:" + System.currentTimeMillis());
        writer.flush();
        writer.close();
    }

    public final List<Module> loadConfig(final String fileName) throws IOException {
        final List<String> lines = new ArrayList<>();
        File targetFile = new File(directory + "\\" + fileName + EXTENSION);

        BufferedReader reader = new BufferedReader(new FileReader(targetFile));
        String line = reader.readLine();
        while(line != null) {
            lines.add(line);
            line = reader.readLine();
        }
        for (final Module m : Irisia.getInstance.getInstanceCollection().getModuleManager().getModules()) {
            m.setToggled(false);
        }
        lines.forEach((s) -> {
            String[] args = s.split(delimiter);
            if(!s.contains("creationdate")) {
                switch (args[0]) {
                    case "MODULE": {
                        Module m = Irisia.getInstance.getInstanceCollection().getModuleManager().getModuleByName(args[1].toLowerCase());
                        if (m == null) break;
                        m.setKeyBind(Integer.parseInt(args[2]));
                        m.setToggled(Boolean.parseBoolean(args[3]));
                        break;
                    }
                    case "PROPERTY": {
                        Property property = Irisia.getInstance.getInstanceCollection().getModuleManager().getPropertyByNameWithParent(args[1].toLowerCase());
                        System.out.println(property);
                        if (property instanceof EnumProperty) {
                            ((EnumProperty) property).setSelected(args[2]);
                        }
                        if (property instanceof BooleanProperty) {
                            ((BooleanProperty) property).setEnabled(Boolean.parseBoolean(args[2]));
                        }
                        if (property instanceof DoubleProperty) {
                            ((DoubleProperty) property).setValue(Double.parseDouble(args[2]));
                        }
                        break;
                    }
                }

            }
        });
        return Irisia.getInstance.getInstanceCollection().getModuleManager().getModules();
    }

    public final String getConfigCreationDate(String configName) throws IOException {
        final List<String> lines = new ArrayList<>();
        String dt = null;
        final List<Module> mods = new ArrayList<>();
        File targetFile = new File(directory + "\\" + configName);
        final BufferedReader reader = new BufferedReader(new FileReader(targetFile));
        String line = reader.readLine();
        while(line != null) {
            lines.add(line);
            line = reader.readLine();
        }
        for (String s : lines) {
            String[] args = s.split(delimiter);
            if(s.contains("creation")){
                final long millis = Long.parseLong(args[1]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Date d = new Date(millis);
                dt = (sdf.format(d));
            }
        }
        return dt;
    }
}
