package irisia.module.impl.combat;

import irisia.event.bus.Listen;
import irisia.event.system.Event;
import irisia.event.system.impl.EventMotion;
import irisia.property.CategoryProperty;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import irisia.utils.MathUtil;
import irisia.utils.RotationHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Keyboard;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.utils.Stopwatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;


@ModuleAttributes(name = "Kill Aura", category = Module.Category.COMBAT, keyBind = Keyboard.KEY_R)
public final class KillAura extends Module {
    private float[] rotations;
    Stopwatch stopwatch = new Stopwatch();
    public Float previousYawAngle, previousPitchAngle, renderYaw, renderPitch;

    private final List<EntityLivingBase> possibleTargets = new ArrayList<>();
    @Getter public EntityLivingBase optimalTarget;
    public final CategoryProperty rotationsCategory = new CategoryProperty("Rotations...");
    public final EnumProperty rotationModeProperty = new EnumProperty( "Rotation Mode", () -> rotationsCategory.isExpanded(),"Vanilla", "Vanilla", "Snap", "None", "Watchdog");
    public final DoubleProperty rotationSpeedProperty = new DoubleProperty("Rotate Speed", () -> rotationsCategory.isExpanded(), 180, 10, 360, 1);
    public final CategoryProperty targetsCategory = new CategoryProperty("Targets...");
    public final EnumProperty targetFilteringModeProperty = new EnumProperty("Target Filtering",() -> targetsCategory.isExpanded(), "Priority", "Priority", "Dynamic", "Combined");
    public final CategoryProperty attackCategory = new CategoryProperty("Attack...");
    public final EnumProperty attackMethodProperty = new EnumProperty("Attack Method",() -> attackCategory.isExpanded(), "Packet", "Packet", "Click");
    public final BooleanProperty autoBlockProperty = new BooleanProperty( "Auto Block",() -> attackCategory.isExpanded(),false);
    public final EnumProperty autoBlockModeProperty = new EnumProperty("Auto Block Mode",() -> attackCategory.isExpanded() && autoBlockProperty.isEnabled(), "Fake", "Interact", "NCP", "Fake");
    public final CategoryProperty settingsCategory = new CategoryProperty("Settings...");
    public final BooleanProperty snapHeadProperty = new BooleanProperty("Snap Head",() -> settingsCategory.isExpanded(), true);
    public final DoubleProperty attackMinimalRangeProperty = new DoubleProperty( "Min Range",() -> settingsCategory.isExpanded(), 4.0, 0.5, 6.0, 0.05);
    public final DoubleProperty attackMaximalRangeProperty = new DoubleProperty("Max Range",() -> settingsCategory.isExpanded(), 4.2, 0.5, 6.0, 0.05);
    public final DoubleProperty minimalApsProperty = new DoubleProperty("Min APS",() -> settingsCategory.isExpanded(), 8.0, 0.5, 20.0, 0.1);
    public final DoubleProperty maximalApsProperty = new DoubleProperty("Max APS",() -> settingsCategory.isExpanded(), 12.0, 0.5, 20.0, 0.1);
    @Getter public final BooleanProperty keepSprintProperty = new BooleanProperty("Keep Sprint",() -> settingsCategory.isExpanded(), true);
    public final BooleanProperty noSwingProperty = new BooleanProperty("No Swing",() -> settingsCategory.isExpanded(), false);


    public static List<EntityLivingBase> getLivingEntities(final Predicate<EntityLivingBase> validator) {
        final List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
        for (final Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase e = (EntityLivingBase)entity;
                if (!validator.test(e)) {
                    continue;
                }
                entities.add(e);
            }
        }
        return entities;
    }
    void findTargets(){
        possibleTargets.addAll(getLivingEntities(this::validateTarget));
    }

    boolean validateTarget(final EntityLivingBase target){
        return !target.isDead && target.isEntityAlive() && mc.thePlayer.getDistanceToEntity(target) <= getRange() && target != mc.thePlayer  && !target.isInvisible();
    }

    public float getRange(){
        return MathUtil.getRandomInRange(attackMinimalRangeProperty.getValueFloat(), attackMaximalRangeProperty.getValueFloat());
    }

    float getAps(){
        return MathUtil.getRandomInRange(minimalApsProperty.getValueFloat(), maximalApsProperty.getValueFloat());
    }

    void prioritizeTargets(){
        if (possibleTargets.size() > 1) {
            possibleTargets.sort(Comparator.comparingDouble(o -> o.getDistanceToEntity(mc.thePlayer)));
        }
    }

    @Listen
    public void onMotion(final EventMotion e){
        if(e.getState() == Event.State.PRE) {
            setSuffix(targetFilteringModeProperty.getSelected());
            optimalTarget = null;
            findTargets();
            prioritizeTargets();

            assert optimalTarget == null;

            possibleTargets.forEach(t -> {
                if (mc.thePlayer.getDistanceToEntity(t) <= getRange() && validateTarget(t)) {
                    optimalTarget = t;
                }
            });

            if (optimalTarget != null) {
                if (autoBlockProperty.isEnabled()) {
                    System.out.println("pies");
                    if (mc.thePlayer.getHeldItem() != null) {
                        switch (autoBlockModeProperty.getSelected()) {
                            case "NCP": {
                                mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                                break;
                            }
                            case "Fake": {
                                mc.thePlayer.setFakeItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                                break;
                            }
                        }
                    }
                }

                rotations = RotationHelper.applyRotations(optimalTarget, rotationSpeedProperty.getValueFloat(), mc.thePlayer.prevRotationYaw, mc.thePlayer.prevRotationPitch, true);
                float yaw = rotations[0];
                float pitch = rotations[1];

                previousYawAngle = mc.thePlayer.rotationYaw;
                previousPitchAngle = mc.thePlayer.rotationPitch;

                this.renderYaw = yaw;
                this.renderPitch = pitch;
                switch (rotationModeProperty.getSelected()) {
                    case "Vanilla": {
                        e.setYaw(yaw);
                        e.setPitch(pitch);
                        RotationHelper.setAnglesClientside(yaw, pitch, rotationSpeedProperty.getValueFloat());
                        break;
                    }
                    case "Snap": {
                        if (mc.thePlayer.ticksExisted % 20 == 0) {
                            e.setYaw(yaw);
                            e.setPitch(pitch);
                            RotationHelper.setAnglesClientside(yaw, pitch, rotationSpeedProperty.getValueFloat());
                        }
                        break;
                    }
                    case "Watchdog": {
                        e.setYaw(yaw);
                        e.setPitch(pitch);
                        mc.thePlayer.rotationYaw = yaw;
                        mc.thePlayer.rotationPitch = pitch;
                        break;
                    }
                }
                if (this.stopwatch.hasTimeElapsed((long) (1000 / getAps()), true)) {
                    if (snapHeadProperty.isEnabled()) {
                        e.setYaw(yaw);
                        e.setPitch(pitch);
                        RotationHelper.setAnglesClientside(yaw, pitch, rotationSpeedProperty.getValueFloat());
                    }
                    if (!noSwingProperty.isEnabled()) mc.thePlayer.swingItem();
                    switch (attackMethodProperty.getSelected()){
                        case "Click":{
                            this.mc.playerController.attackEntity(this.mc.thePlayer, optimalTarget);
                            break;
                        }
                        case "Packet":{
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(optimalTarget, C02PacketUseEntity.Action.ATTACK));
                            break;
                        }
                    }
                }
            }else if(!mc.gameSettings.keyBindUseItem.isKeyDown())mc.thePlayer.stopUsingItem();
        }
    };
}