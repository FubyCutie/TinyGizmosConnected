package uk.fuby.tinygizmosconnected.blocks;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import uk.fuby.tinygizmosconnected.enums.LightStates;

import java.awt.*;

public class WallBlock extends Block {
	public static final EnumProperty<LightStates> LIGHT_STATE = EnumProperty.of("wall_light", LightStates.class);

	public static final EnumProperty<DyeColor> WALL_COLOR = EnumProperty.of("wall_color", DyeColor.class);

	public WallBlock(Settings settings) {
		super(settings.luminance(state -> lightLevelFromState(state.get(LIGHT_STATE))));
		setDefaultState(getDefaultState().with(LIGHT_STATE, LightStates.NORMAL).with(WALL_COLOR, DyeColor.WHITE));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIGHT_STATE, WALL_COLOR);
	}



	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return super.onUse(state, world, pos, player, hand, hit);
		if (hand.equals(Hand.OFF_HAND)) return ActionResult.SUCCESS;

		LightStates currentLightState = state.get(LIGHT_STATE);

		ItemStack itemInHand = player.getStackInHand(hand);
		Item itemUsed = itemInHand.getItem();

		if (itemUsed.equals(Items.AIR) && player.isSneaking()) {
			if (currentLightState == LightStates.NORMAL) return ActionResult.FAIL;
			if (currentLightState == LightStates.GLOWING) {
				world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.NORMAL));
				returnItems(Items.GLOWSTONE_DUST, player, world, pos, hit.getSide());
				return ActionResult.SUCCESS;
			}
			if (currentLightState == LightStates.SHADED) {
				DyeItem item = DyeItem.byColor(state.get(WALL_COLOR));
				world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.NORMAL).with(WALL_COLOR, DyeColor.WHITE));
				returnItems(item, player, world, pos, hit.getSide());
				return ActionResult.SUCCESS;
			}
		}

		if (itemUsed.equals(Items.GLOWSTONE_DUST) && currentLightState == LightStates.NORMAL) {
			world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.GLOWING));
			if (!player.isCreative()) itemInHand.decrement(1);
			return ActionResult.SUCCESS;
		}

		if (!itemInHand.isIn(ConventionalItemTags.DYES))
			return super.onUse(state, world, pos, player, hand, hit);

		if (currentLightState != LightStates.NORMAL) return ActionResult.FAIL;


		DyeColor color = ((DyeItem) itemUsed).getColor();
		world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.SHADED).with(WALL_COLOR, color));
		if (!player.isCreative()) itemInHand.decrement(1);
		return ActionResult.SUCCESS;

	}

	public static int lightLevelFromState(LightStates lightState) {
		if (lightState == LightStates.SHADED) return 0;
		if (lightState == LightStates.GLOWING) return 15;
		return 7;
	}

	private void returnItems(Item item, PlayerEntity player, World world, BlockPos pos, Direction direction) {
		if (player.isCreative()) return;
		Vec3f vector = direction.getUnitVector();
		double velX = vector.getX() * 0.2;
		double velY = vector.getY() * 0.2;
		double velZ = vector.getZ() * 0.2;
		Vec3f vectorPos = new Vec3f(
				pos.getX() + 0.5f,
				pos.getY() + 0.5f,
				pos.getZ() + 0.5f
		);
		vector.scale(0.8f);
		vectorPos.add(vector);
		double spawnX = vectorPos.getX();
		double spawnY = vectorPos.getY();
		double spawnZ = vectorPos.getZ();
		world.spawnEntity(
				new ItemEntity(world, spawnX, spawnY, spawnZ, new ItemStack(item), velX, velY, velZ)
		);
	}

}
