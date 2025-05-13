package irisia.module.impl.render;

import irisia.event.bus.Listen;
import irisia.event.bus.Listener;
import irisia.event.system.impl.Event3D;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import irisia.utils.ColorHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleAttributes(name = "China Hat", category = Module.Category.RENDER)
public final class ChinaHat extends Module {
    public DoubleProperty radiusProperty = new DoubleProperty("Radius", 0.5, 0.1, 1.0, 0.05);
    public DoubleProperty yPosProperty = new DoubleProperty("Y Pos", 0.1, -0.20, 1.0, 0.01);
    public DoubleProperty heightProperty = new DoubleProperty("Height", 0.3, 0.1, 1.0, 0.01);
    public DoubleProperty saturationProperty = new DoubleProperty( "Saturation",  0.9, 0.1, 1.0, 0.05);
    public DoubleProperty brightnessProperty = new DoubleProperty("Brightness", 1.0, 0.1, 1.0, 0.05);
    public BooleanProperty smoothingProperty = new BooleanProperty( "Smoothing", true);
    public DoubleProperty alphaProperty = new DoubleProperty( "Alpha", 120, 1, 255, 1);
    public BooleanProperty betterPhysicsProperty = new BooleanProperty("Physics", false);
    public EnumProperty colorModeProperty = new EnumProperty( "Color", "Rainbow", "Rainbow", "Astolfo");
    public DoubleProperty rainbowStartProperty = new DoubleProperty("Rainbow Start",  () -> colorModeProperty.getSelected() == "Rainbow", 0.5, 0.1, 1.0, 0.05);
    public DoubleProperty rainbowEndProperty = new DoubleProperty("Rainbow End", () -> colorModeProperty.getSelected() == "Rainbow", 1.0, 0.1, 1.0, 0.05);


    public double interpolate(double intended, double previous, double renderTicks) {
        return previous + (intended - previous) * renderTicks;
    }

    @Listen
    public void on3DEvent(Event3D e) {
        for (Entity entity : mc.theWorld.playerEntities) {
            if (entity != mc.thePlayer) return;
            if(mc.gameSettings.showDebugInfo != true && mc.gameSettings.thirdPersonView != 0) {
                drawHat(entity, this.radiusProperty.getValue(), e.getPartialTicks(), 1, 1, 1, 1);
            }
        }
    }

    public void drawHat(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final float yAdd, final int color) {
        double start = rainbowStartProperty.getValue();
        double end =  rainbowEndProperty.getValue();
        pre3D();
        GL11.glEnable(2929);
        GlStateManager.disableCull();
        GL11.glDisable(3553);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glRotatef((float) interpolate(mc.thePlayer.renderYaw, mc.thePlayer.lastRenderYaw, mc.timer.renderPartialTicks), 0, -1, 0);
        GL11.glTranslated(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().renderPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY + entity.height + yPosProperty.getValue() - (mc.thePlayer.isSneaking() ? 0.02 : 0), entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ);
        if(betterPhysicsProperty.isEnabled()) {
            GL11.glRotatef(mc.thePlayer.rotationPitchHead / 3.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslated(0.0, 0.0, mc.thePlayer.rotationPitchHead / 280.0f);
        }
        GL11.glRotatef((entity.ticksExisted + mc.timer.renderPartialTicks) * 8, 0f, 1f, 0f);

        int ez = ColorHelper.rainbowColor(2,saturationProperty.getValueFloat(),brightnessProperty.getValueFloat(),1);
        glColor(ColorHelper.getColor(new Color(ez).getRed(), new Color(ez).getGreen(), new Color(ez).getBlue(), alphaProperty.getValueInteger()));
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        if(this.smoothingProperty.isEnabled()){
            GL11.glShadeModel(7425);
        }
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex3d(0.0, heightProperty.getValue(), 0.0);
        for(double i = 0; i <= 360; i++){
            glColor(Color.getHSBColor((float) (i / 360f), (float) saturationProperty.getValue(),(float) brightnessProperty.getValue()), (float) alphaProperty.getValue());
            GL11.glVertex3d(radius * Math.cos(i * StrictMath.PI / 180.0), 0.0, radius * Math.sin(i * StrictMath.PI / 180.0));
        }
        GL11.glVertex3d(0.0, heightProperty.getValue(), 0.0);
        GL11.glEnd();
        GL11.glLineWidth(1.5F);
        GL11.glBegin(2);
        for(double i = 0; i < 360; ++i) {
            glColor(Color.getHSBColor((float) (i / 360), (float) saturationProperty.getValue(),(float) brightnessProperty.getValue()), (float) alphaProperty.getValue());
            GL11.glVertex3d(Math.cos(i * Math.PI / 180.0) * radius, 0.0, Math.sin(i * Math.PI / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GlStateManager.enableCull();
        GL11.glDisable(2929);
        post3D();
    }

    public void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT,GL11.GL_NICEST);
    }

    public void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }


    public static void glColor(final Color color, float alpha) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;

        GlStateManager.color(red, green, blue, alpha / 255F);
    }


    public static void glColor(int color) {
        // GlStateManager.color((float) (color >> 16 & 255) / 255F, (float) (color >> 8 & 255) / 255F, (float) (color & 255) / 255F, (float) (color >> 24 & 255) / 255F);
        GlStateManager.color((float) (color >> 16 & 255) / 255F, (float) (color >> 8 & 255) / 255F, (float) (color & 255) / 255F, 110 / 255F);
    }
}