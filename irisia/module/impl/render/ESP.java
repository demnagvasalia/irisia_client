package irisia.module.impl.render;

import irisia.Irisia;
import irisia.event.bus.Listen;
import irisia.event.system.impl.EventUI;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.module.impl.combat.KillAura;
import irisia.utils.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

@ModuleAttributes(name = "ESP", category = Module.Category.RENDER)
public class ESP extends Module {
    //todo: working esp
}
