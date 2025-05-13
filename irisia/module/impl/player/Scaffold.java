package irisia.module.impl.player;

import irisia.event.bus.Listen;
import irisia.event.system.Event;
import irisia.event.system.impl.EventMotion;
import irisia.event.system.impl.EventMove;
import irisia.event.system.impl.EventTick;
import irisia.module.Module;
import irisia.module.ModuleAttributes;
import irisia.property.impl.BooleanProperty;
import irisia.property.impl.DoubleProperty;
import irisia.property.impl.EnumProperty;
import irisia.utils.*;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@ModuleAttributes(name = "Scaffold", category = Module.Category.PLAYER, keyBind = Keyboard.KEY_G)
public final class Scaffold extends Module {
    public Set<Block> disallowedBlocks = new HashSet<>();
    private BlockInfo blockInfo;

    private final Stopwatch stopwatch = new Stopwatch();

    private float yaw, pitch;
    private float tickYaw, tickPitch;


    private Integer oldSlot;

    public EnumProperty blockPlaceModeProperty = new EnumProperty( "Place Mode", "Swing", "Swing", "Packet", "Watchdog");
    public EnumProperty rotationModeProperty = new EnumProperty( "Rotations", "Static", "Dynamic", "Static");
    public EnumProperty rotationBypassModeProperty = new EnumProperty("Rotation Bypass", () -> rotationModeProperty.is("Dynamic"),"NCP", "NCP", "Vanilla", "Vulcan");
    public BooleanProperty silentProperty = new BooleanProperty("Silent",  true);
    public EnumProperty towerModeProperty = new EnumProperty("Tower", "Vanilla", "Watchdog", "Vanilla");
    public DoubleProperty placeDelayProperty = new DoubleProperty("Place Delay (MS)",  50.0, 2.0, 400.0, 1.0);
    public DoubleProperty aimStepProperty = new DoubleProperty("Max Angle Step",  15.0, 1.0, 180.0, 1.0);
    public BooleanProperty expandProperty = new BooleanProperty( "Expand", false);
    public BooleanProperty sprintProperty = new BooleanProperty("Sprint",  false);

    public Scaffold(){
        this.disallowedBlocks.addAll(Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence));
    }
    @Listen
    public void onMove(final EventMove e){
        if(blockPlaceModeProperty.is("Watchdog"))
            MovementUtil.getInstance.strafe(e,0.2);
    }
    @Listen
    public void onMotion(final EventMotion e){
        if(tickYaw != 0 && tickPitch != 0) {
            e.setYaw(tickYaw);
            e.setPitch(tickPitch);
            RotationHelper.setAnglesClientside(tickYaw , tickPitch, 1);
        }
    }
    @Listen
    public void onTick(final EventTick e){
        MovementUtil.getInstance.setSpeed(MovementUtil.getInstance.getBaseSpeed() - 0.05);
        if (true) {
            float[] rotations = new float[0];
            final BlockPos underneath = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ).offset(EnumFacing.DOWN);

            this.yaw = this.mc.thePlayer.rotationYaw;
            this.pitch = this.mc.thePlayer.rotationPitch;

            this.blockInfo = WorldUtil.getBlockUnder(mc.thePlayer.posY);
            if (this.blockInfo != null) {
                this.yaw = this.mc.thePlayer.rotationYaw;
                this.pitch = this.mc.thePlayer.rotationPitch;
                rotations = null;

                float optimalYaw = 0;

                switch (rotationModeProperty.getSelected()){
                    case "Static":{
                        rotations = getRotations(getVec3(blockInfo.getPos(), EnumFacing.DOWN));
                        tickYaw = MovementUtil.getInstance.getPlayerDirection() - 180;
                        tickPitch = mc.thePlayer.onGround ? 82 : 90;
                        break;
                    }
                    case "Dynamic":{
                        rotations = getRotations(getVec3(blockInfo.getPos(), blockInfo.getFacing()));
                        switch (rotationBypassModeProperty.getSelected()){
                            case "NCP":{
                                tickYaw = rotations[0];
                                tickPitch = rotations[1];
                                break;
                            }

                            case "Vulcan":{
                                break;
                            }
                        }
                        break;
                    }
                }



                if (this.mc.gameSettings.keyBindSneak.isKeyDown() && getBlock(underneath) != Blocks.air && this.towerModeProperty.is("Vanilla")) {
                    tickPitch = 90;
                    this.mc.thePlayer.motionY = 0.42;
                    this.mc.thePlayer.motionX *= 0.0;
                    this.mc.thePlayer.motionZ *= 0.0;
                }

                if (validateReplaceable(blockInfo)) {
                    boolean autosprint = false;
                    this.mc.thePlayer.setSprinting(this.sprintProperty.isEnabled() && MovementUtil.getInstance.isPlayerMoving() && autosprint);
                    if(sameY(getVec3(blockInfo.getPos(), EnumFacing.DOWN))) {
                        this.placeBlockAt(blockInfo);
                    }
                }
            }
        }
        if (e.getState() == Event.State.POST){
            tickPitch = pitch;
            tickYaw = yaw;
        }
    }

    public Block getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    private float[] getRotations(final Vec3 hitVec) {
        final EntityPlayerSP player = mc.thePlayer;

        final double radians = 180.0D / StrictMath.PI;

        final double xDif = hitVec.xCoord - player.posX;
        final double zDif = hitVec.zCoord - player.posZ;

        final double yDif = hitVec.yCoord - (player.posY + player.getEyeHeight());
        final double xzDist = StrictMath.sqrt(xDif * xDif + zDif * zDif);

        return new float[]{(float) ((StrictMath.atan2(zDif, xDif) * radians) - 90.0F), (float) (-(StrictMath.atan2(yDif, xzDist) * radians))
        };
    }

    private boolean sameY(final Vec3 hitVec) {
        final EntityPlayerSP player = mc.thePlayer;
        final double yDif = (hitVec.yCoord - 1) - player.posY;
        boolean sameY = yDif < -1.9f && yDif > -2.9f;
        System.out.println(yDif);

        return sameY;
    }

    int getBlockSlot() {
        for (int i = 0; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !this.disallowedBlocks.contains(((ItemBlock)this.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock())) {
                return i;
            }
        }
        return -1;
    }

    int getFreeSlot() {
        for (int i = 0; i < 9; ++i) {
            if (this.mc.thePlayer.inventory.getStackInSlot(i) != null && this.mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0) {
                return i;
            }
        }
        return this.mc.thePlayer.inventory.currentItem;
    }

    public BlockPos getExpandBlock(final float expandValue) {
        final double sine = -Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw)) * expandValue;
        final double cosine = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw)) * expandValue;
        return new BlockPos(this.mc.thePlayer.posX + sine, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + cosine);
    }

    public Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            y += MathUtil.getRandomInRange(0.49f, 0.5f);
        }
        else {
            x += MathUtil.getRandomInRange(0.3f, -0.3f);
            z += MathUtil.getRandomInRange(0.3f, -0.3f);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += MathUtil.getRandomInRange(0.3f, -0.3f);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += MathUtil.getRandomInRange(0.3f, -0.3f);
        }
        return new Vec3(x, y, z);
    }

    private int neededSlot() {
        for (int i = 0; i < 9; ++i) {
            if (this.mc.thePlayer.inventory.getStackInSlot(i) != null && this.mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0) {
                final Item item = this.mc.thePlayer.inventory.getStackInSlot(i).getItem();
                if (this.isValid(item)) {
                    return i;
                }
            }
        }
        return this.mc.thePlayer.inventory.currentItem;
    }

    boolean validateReplaceable(final BlockInfo data){
        final BlockPos pos = data.getPos().offset(data.getFacing());
        final World world = mc.theWorld;
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    private void placeBlockAt(final BlockInfo data) {
        this.oldSlot = this.mc.thePlayer.inventory.currentItem;
        if (this.mc.thePlayer.inventory.currentItem != this.neededSlot()) {
            this.mc.thePlayer.inventory.currentItem = this.neededSlot();
        }

        final ItemStack stack = this.mc.thePlayer.inventory.getStackInSlot(this.neededSlot());
        final Vec3 vector = this.getVec3(data.getPos(), data.getFacing());

        if (this.stopwatch.hasTimeElapsed((long)this.placeDelayProperty.getValue(), true) && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, stack, data.getPos(), data.getFacing(), vector)) {
            if (this.blockPlaceModeProperty.getSelected() == "Swing") {
                this.mc.thePlayer.swingItem();
            }
            else {
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
        }
        if (this.silentProperty.isEnabled()) {
            mc.thePlayer.sendQueue.addToSendQueueDirect(new C09PacketHeldItemChange(this.oldSlot));
        }
        else {
            this.mc.thePlayer.inventory.currentItem = this.oldSlot;
        }
    }

    private boolean hotBarContainBlock() {
        int i = 36;
        while (i < 45) {
            try {
                final ItemStack slotStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (slotStack == null || slotStack.getItem() == null || !(slotStack.getItem() instanceof ItemBlock) || !this.isValid(slotStack.getItem())) {
                    ++i;
                    continue;
                }
                return true;
            }
            catch (Exception ex) {
                continue;
            }
        }
        return false;
    }

    public boolean isAirBlock(final Block block) {
        return block.getMaterial().isReplaceable() && block.getBlockBoundsMaxY() <= 0.125;
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                blockCount += stack.stackSize;
            }
        }
        return blockCount;
    }

    private boolean isValid(final Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        }
        final ItemBlock itemBlock = (ItemBlock)item;
        final Block block = itemBlock.getBlock();
        return !this.disallowedBlocks.contains(block);
    }

    private int getBiggestBlockSlotHotbar() {
        int slot = -1;
        int size = 0;
        if (this.getBlockCount() == 0) {
            return -1;
        }
        for (int i = 36; i < 45; ++i) {
            final Slot s = this.mc.thePlayer.inventoryContainer.getSlot(i);
            if (s.getHasStack()) {
                final Item item = s.getStack().getItem();
                final ItemStack is = s.getStack();
                if (item instanceof ItemBlock && this.isValid(item) && is.stackSize > size) {
                    size = is.stackSize;
                    slot = i;
                }
            }
        }
        return slot;
    }

    private int getBlockCountHotBar() {
        int blockCount = 0;
        for (int i = 36; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    if (!this.disallowedBlocks.contains(((ItemBlock)item).getBlock())) {
                        blockCount += is.stackSize;
                    }
                }
            }
        }
        return blockCount;
    }

    public boolean checkBlockValidity(final Block blockIn) {
        return !this.disallowedBlocks.contains(blockIn);
    }
}
