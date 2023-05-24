package uk.fuby.tinygizmosconnected.utils;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import uk.fuby.tinygizmosconnected.TinyGizmosConnected;
import uk.fuby.tinygizmosconnected.blocks.GizmoBlock;
import uk.fuby.tinygizmosconnected.blocks.WallBlock;

import java.util.List;

public class Register {


	public static final WallBlock WALL = new WallBlock(FabricBlockSettings
			.of(Material.METAL).hardness(1.0f).requiresTool());
	public static final GizmoBlock GIZMO = new GizmoBlock(FabricBlockSettings
			.of(Material.METAL).hardness(4.0f).requiresTool());

	public static final BlockItem GIZMO_ITEM = new BlockItem(GIZMO, new FabricItemSettings());
	public static final BlockItem WALL_ITEM = new BlockItem(WALL, new FabricItemSettings());


	public static void register(String modid) {

		Registry.register(Registry.BLOCK, new Identifier(modid, "gizmo"), GIZMO);
		Registry.register(Registry.BLOCK, new Identifier(modid, "wall"), WALL);

		Registry.register(Registry.ITEM, new Identifier(modid, "gizmo"), GIZMO_ITEM);
		Registry.register(Registry.ITEM, new Identifier(modid, "wall"), WALL_ITEM);


		List<ItemStack> itemStacks = List.of(
				new ItemStack(GIZMO_ITEM),
				new ItemStack(WALL_ITEM)
		);


		FabricItemGroupBuilder itemGroupBuilder = FabricItemGroupBuilder.create(
				new Identifier(TinyGizmosConnected.modid, "gizmo_group"))
				.icon(() -> new ItemStack(GIZMO));

		itemGroupBuilder.appendItems(itemStacks1 -> itemStacks1.addAll(itemStacks));

		itemGroupBuilder.build();
	}

}
