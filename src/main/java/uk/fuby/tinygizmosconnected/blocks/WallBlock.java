package uk.fuby.tinygizmosconnected.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import uk.fuby.tinygizmosconnected.enums.LightStates;

public class WallBlock extends Block {
	public static final EnumProperty<LightStates> LIGHT_STATE = EnumProperty.of("wall_light", LightStates.class);

	public WallBlock(Settings settings) {
		super(settings.luminance(state -> lightLevelFromState(state.get(LIGHT_STATE))));
		setDefaultState(getDefaultState().with(LIGHT_STATE, LightStates.NORMAL));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIGHT_STATE);
	}



	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return ActionResult.SUCCESS;
		if (hand.equals(Hand.OFF_HAND)) return ActionResult.SUCCESS;

		LightStates currentLightState = state.get(LIGHT_STATE);

		ItemStack itemInHand = player.getStackInHand(hand);
		Item itemUsed = itemInHand.getItem();
		if (itemUsed.equals(Items.GLOWSTONE_DUST) && currentLightState != LightStates.GLOWING) {
			if (currentLightState == LightStates.SHADED)
				returnItems(Items.BLACK_DYE, player, world, pos, hit.getSide());
			world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.GLOWING));
			if (!player.isCreative()) itemInHand.decrement(1);
			return ActionResult.CONSUME;
		}
		if (itemUsed.equals(Items.BLACK_DYE) && currentLightState != LightStates.SHADED) {
			if (currentLightState == LightStates.GLOWING)
				returnItems(Items.GLOWSTONE_DUST, player, world, pos, hit.getSide());
			world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.SHADED));
			if (!player.isCreative()) itemInHand.decrement(1);
			return ActionResult.CONSUME;
		}
		if (itemUsed.equals(Items.AIR) && player.isSneaking()) {
			if (currentLightState == LightStates.NORMAL) return ActionResult.FAIL;
			if (currentLightState == LightStates.GLOWING) {
				world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.NORMAL));
				return returnItems(Items.GLOWSTONE_DUST, player, world, pos, hit.getSide());
			}
			if (currentLightState == LightStates.SHADED) {
				world.setBlockState(pos, state.with(LIGHT_STATE, LightStates.NORMAL));
				return returnItems(Items.BLACK_DYE, player, world, pos, hit.getSide());
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	public static int lightLevelFromState(LightStates lightState) {
		if (lightState == LightStates.SHADED) return 0;
		if (lightState == LightStates.GLOWING) return 15;
		return 7;
	}

	private ActionResult returnItems(Item item, PlayerEntity player, World world, BlockPos pos, Direction direction) {
		if (player.isCreative()) return ActionResult.SUCCESS;
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
		return ActionResult.SUCCESS;
	}

}
