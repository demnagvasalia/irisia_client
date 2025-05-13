package irisia.managers;

import irisia.module.impl.combat.AntiBot;
import irisia.module.impl.combat.KillAura;
import irisia.module.impl.combat.Velocity;
import irisia.module.impl.movement.Flight;
import irisia.module.impl.movement.Speed;
import irisia.module.impl.player.Animations;
import irisia.module.impl.player.ChestStealer;
import irisia.module.impl.player.Scaffold;
import irisia.module.impl.player.Sprint;
import irisia.module.impl.render.*;
import irisia.property.Property;
import lombok.Getter;
import irisia.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    @Getter private List<Module> modules = new ArrayList<>();

    public ModuleManager(){
        modules.add(new HUD());
        modules.add(new Flight());
        modules.add(new KillAura());
        modules.add(new ClickGUI());
        modules.add(new Scaffold());
        modules.add(new Velocity());
        modules.add(new ChestStealer());
        modules.add(new Sprint());
        modules.add(new Chams());
        modules.add(new ESP());
        modules.add(new Speed());
        modules.add(new Animations());
        modules.add(new ViewModel());
        modules.add(new ChinaHat());
        modules.add(new AntiBot());

        for(Module m : getModules()){
            m.reflectProperties();
            for(Property p : m.getProperties()){
                p.setParentModule(m);
            }
        }
    }
    public List<String> getModulesParsedForConfig(final String delimiter){
        final List<String> l = new ArrayList<>();
        getModules().forEach((m -> {
            l.add("MODULE" + delimiter + m.getName() + delimiter + m.getKeyBind() + delimiter + m.isToggled());
        }));
        return l;
    }
    public List<Module> getModulesByCategory(String category){
        List<Module> moduleList = new ArrayList<>();
        for(Module module : modules){
            if(module.getCategory().name().toLowerCase().contains(category.toLowerCase())){
                moduleList.add(module);
            }
        }
        return moduleList;
    }
    public Property getPropertyByNameFromModule(String moduleName, String propertyName) {
        Module module = getModuleByName(moduleName);
        if (module != null) {
            return module.getPropertyByName(propertyName);
        }
        return null;
    }
    public List<Property> getProperties() {
        List<Property> options = new ArrayList<>();
        for(Module module : getModules()) {
            for(Property option : module.getProperties()) {
                options.add(option);
            }
        }
        return options;
    }
    public Property getPropertyByName(String name) {
        for(Property o : getProperties()) {
            if(o.name.equalsIgnoreCase(name)) {
                return o;
            }
        }
        return null;
    }
    public Property getPropertyByNameWithParent(String name) {
        for(Property o : getProperties()) {
            if(o.getNameWithParent().equalsIgnoreCase(name)) {
                return o;
            }
        }
        return null;
    }
    public List<Property> getSettingsByModule(Module m) {
        List<Property> list = new ArrayList<>();
        list.addAll(m.getProperties());
        return list;
    }
    public List<Module> getModulesByCat(Module.Category category){
        List<Module> moduleList = new ArrayList<>();
        for(Module module : modules){
            if(module.getCategory() == category){
                moduleList.add(module);
            }
        }
        return moduleList;
    }
    public List<Module> getEnabledModules(){
        List<Module> moduleList = new ArrayList<>();
        for(Module module : modules){
            if(module.isToggled()){
                moduleList.add(module);
            }
        }
        return moduleList;
    }
    public Module getModuleByName(String name) {

        return getModules().stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public List<Module> getModulesByHalfOfCategory(String category, int half){
        List<Module> moduleList = new ArrayList<>();
        if(half == 1) {
            int index = 0;
            for (Module m : getModulesByCategory(category)) {
                if (index <= getModulesByCategory(category).size() / 2){
                    moduleList.add(m);
                }
                index++;
            }
        }
        if(half == 2) {
            int index = 0;
            for (Module m : getModulesByCategory(category)) {
                if (index > getModulesByCategory(category).size() / 2){
                    moduleList.add(m);
                }
                index++;
            }
        }
        return moduleList;
    }
}
